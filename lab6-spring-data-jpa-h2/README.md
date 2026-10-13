# ЛР №6 — Spring Data JPA и H2

Самостоятельный Maven-проект: Java 17, Spring Boot 3.1.5, Spring Web, Spring Data JPA, H2, Lombok и Jakarta Validation. Порт `8080`. Сущности `STUDENTS` (студенты) и `SUBJECTS` (учебные дисциплины) имеют отдельные контроллеры, DAO и сервисы.

## Сборка и запуск

Работайте из каталога `lab6-spring-data-jpa-h2`:

```sh
mkdir -p testdb
mvn clean package
java -jar target/lab6-spring-data-jpa-h2-1.0.0.jar
```

Можно запустить через `mvn spring-boot:run`. Для Windows PowerShell вместо `mkdir -p testdb` используйте `New-Item -ItemType Directory -Force testdb` (или создайте каталог в проводнике). Файлы `testdb/students.mv.db` и `testdb/students.lock.db` — данные и временная блокировка H2; их не добавляют в Git.

Путь к БД задаётся `spring.datasource.url` в `src/main/resources/application.properties` (по умолчанию `jdbc:h2:file:./testdb/students;AUTO_SERVER=TRUE`, относительно рабочего каталога процесса). Для другой БД задайте переменную окружения `LAB6_DB_URL` с полным JDBC URL, например `jdbc:h2:file:/absolute/path/to/testdb/students;AUTO_SERVER=TRUE`. Не указывайте суффикс `.mv.db` в URL. Логин `sa`, пароль пустой. `AUTO_SERVER=TRUE` позволяет нескольким процессам с совместимой версией H2 одновременно использовать одну файловую БД; при подключении DBeaver выберите драйвер **H2 Embedded 2.x**, тот же путь к файлу и параметр `AUTO_SERVER=TRUE`. Если версии драйвера отличаются или соединение не открывается, остановите приложение перед подключением DBeaver и закройте соединение DBeaver перед повторным запуском. Для одного и того же файла используйте одинаковый абсолютный путь, когда рабочие каталоги процессов отличаются.

При запуске Spring выполняет `src/main/resources/schema.sql` (`CREATE TABLE IF NOT EXISTS` для обеих таблиц), затем Hibernate **проверяет** схему (`ddl-auto=validate`), а не создаёт таблицы автоматически. Для ручного создания в DBeaver создайте H2 Embedded 2.x подключение к файлу `testdb/students` (либо разрешите DBeaver создать его), откройте SQL-редактор и выполните содержимое `schema.sql`. Скрипт можно выполнять повторно; `STUDENTS` содержит `id BIGINT` с автоинкрементом, `name VARCHAR(15)`, `surname VARCHAR(25)`, `faculty VARCHAR(20)`, `age INTEGER`; `SUBJECTS` — `id`, `name VARCHAR(100)`, `teacher VARCHAR(100)`, `hours INTEGER`. Поля, кроме идентификатора, обязательны. Не держите DBeaver подключённым к файлу с несовместимым режимом/версией H2 во время запуска приложения.

## Проверка HTTP

Команды ниже выполняются при запущенном сервисе. `-i` показывает статус и заголовки. Значение `id=1` предполагает пустую БД: при повторных запусках подставьте идентификатор из ответа `POST`.

```sh
curl -i http://localhost:8080/api/students
# 200 OK, тело [] для пустой таблицы
curl -i -H 'Content-Type: application/json' -d '{"name":"Иван","surname":"Иванов","faculty":"ИТ","age":25}' http://localhost:8080/api/students
# 201 Created, Location: /api/students/1, тело {"id":1,"name":"Иван","surname":"Иванов","faculty":"ИТ","age":25}
curl -i http://localhost:8080/api/students/1
# 200 OK, тело созданного студента
curl -i -X PUT -H 'Content-Type: application/json' -d '{"id":1,"name":"Иван","surname":"Иванов","faculty":"ФТИ","age":26}' http://localhost:8080/api/students
# 200 OK, тело с faculty="ФТИ", age=26
curl -i -X DELETE http://localhost:8080/api/students/1
# 200 OK, {"message":"Студент удалён: 1"}
curl -i http://localhost:8080/api/students/1
# 404 Not Found, {"error":"Студент не найден: 1"}
curl -i -X PUT -H 'Content-Type: application/json' -d '{"id":1,"name":"Иван","surname":"Иванов","faculty":"ИТ","age":25}' http://localhost:8080/api/students
# 404 Not Found, строка не создаётся при обновлении отсутствующего id
curl -i -X DELETE http://localhost:8080/api/students/1
# 404 Not Found, строка не создаётся при удалении отсутствующего id
```

Для дисциплин доступны те же операции со своим маршрутом `/api/subjects` и полями `name`, `teacher`, `hours`:

```sh
curl -i http://localhost:8080/api/subjects
# 200 OK, [] для пустой таблицы
curl -i -H 'Content-Type: application/json' -d '{"name":"Базы данных","teacher":"Петров","hours":72}' http://localhost:8080/api/subjects
# 201 Created, Location: /api/subjects/1, {"id":1,"name":"Базы данных","teacher":"Петров","hours":72}
curl -i http://localhost:8080/api/subjects/1
# 200 OK, тело дисциплины
curl -i -X PUT -H 'Content-Type: application/json' -d '{"id":1,"name":"СУБД","teacher":"Петров","hours":80}' http://localhost:8080/api/subjects
# 200 OK, {"id":1,"name":"СУБД","teacher":"Петров","hours":80}
curl -i -X DELETE http://localhost:8080/api/subjects/1
# 200 OK, {"message":"Дисциплина удалена: 1"}
curl -i http://localhost:8080/api/subjects/1
# 404 Not Found, {"error":"Дисциплина не найдена: 1"}
curl -i -X PUT -H 'Content-Type: application/json' -d '{"id":1,"name":"СУБД","teacher":"Петров","hours":80}' http://localhost:8080/api/subjects
# 404 Not Found, отсутствие записи не превращается в создание
curl -i -X DELETE http://localhost:8080/api/subjects/1
# 404 Not Found, удаление отсутствующей записи не меняет БД
```

`POST` не принимает заданный `id`; для `PUT` существующий `id` обязателен. В обоих случаях нарушение даёт `400 Bad Request` с `{"error":"..."}` без записи в БД. Отсутствующие обязательные поля, значения вне границ (возраст 1–150, часы ≥ 1, длина строк согласно схеме), некорректный JSON или нечисловой id также дают `400` с объяснением в `error`. Для удаления возвращается явное сообщение об успехе; для чтения/создания/изменения — соответствующая запись, для отсутствующего id — `404`.

**Допущения:** в методичке дополнительная таблица названа «Учебные дисциплины», но её поля и маршруты не заданы: выбраны `SUBJECTS(id, name, teacher, hours)` и `/api/subjects` по аналогии со студентами. Методичка показывает шаблон `api/students{id}` без `/` в одной строке; используется однозначный `DELETE /api/students/{id}`, как в таблице тестирования и в требованиях. Возраст/часы и текстовые поля валидируются на входе, чтобы некорректные данные не попадали в БД.
