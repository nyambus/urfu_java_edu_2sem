# Лабораторная работа № 7 — формы студентов и учебных групп

Самостоятельное веб-приложение на Java 17, Maven, Spring Web, Spring Data JPA, H2, Thymeleaf, Spring Boot Actuator, Lombok и Bean Validation. Стили страниц — Bootstrap 5.3.3 (CDN; для отображения стилей нужен доступ к интернету). Порт по умолчанию: **8080**.

## Принятое допущение о версии

В тексте задания указана Spring Boot **3.1.15** «или другая актуальная». Этого выпуска нет в [метаданных Maven Central для spring-boot-starter-parent](https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-starter-parent/maven-metadata.xml); [запрос артефакта 3.1.15](https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-starter-parent/3.1.15/spring-boot-starter-parent-3.1.15.pom) возвращает HTTP 404. Последний доступный выпуск ветки 3.1.x — **3.1.12**, он указан в `pom.xml` вместо несуществующего 3.1.15. Для дополнительной сущности, поля которой не заданы в методичке, выбраны номер группы, факультет и курс (1–6).

## Сборка и запуск

Из каталога `lab7-thymeleaf-forms`:

```powershell
mvn clean package
mvn spring-boot:run
# Или после сборки: java -jar target/lab7-thymeleaf-forms-1.0.0.jar
```

По умолчанию файл H2 `lab7db.mv.db` создаётся в текущем каталоге запуска. Чтобы выбрать другой путь **без расширения** `.mv.db`, задайте переменную окружения перед запуском (пример PowerShell):

```powershell
$env:DB_PATH = 'C:/homework/prog/lab7-thymeleaf-forms/mydata/lab7db'
mvn spring-boot:run
```

Каталог для произвольного пути создайте заранее. В `src/main/resources/application.properties` указан `jdbc:h2:file:${DB_PATH:./lab7db};AUTO_SERVER=TRUE`; можно менять значение по умолчанию непосредственно там. СУБД файловая, данные сохраняются между перезапусками. Скрипт `src/main/resources/schema.sql` создаёт таблицы `STUDENTS` и `EDUCATIONAL_GROUPS` при первом и повторных запусках (`CREATE TABLE IF NOT EXISTS`), JPA проверяет их схему (`ddl-auto=validate`), не удаляя данные.

### DBeaver и SQL-скрипт

Создайте H2-подключение к файлу через URL `jdbc:h2:file:C:/homework/prog/lab7-thymeleaf-forms/lab7db;AUTO_SERVER=TRUE` (замените на **абсолютный** путь к вашему `lab7db` без расширения). Пользователь `sa`, пароль пустой. Используйте H2-драйвер той же версии, что Maven-зависимость приложения; `AUTO_SERVER=TRUE` позволяет подключить DBeaver одновременно с запущенным приложением. Откройте SQL-редактор DBeaver, вставьте и выполните содержимое `src/main/resources/schema.sql` (можно до первого запуска, достаточно одного раза); затем обновите список таблиц и выполните:

```sql
SELECT * FROM STUDENTS;
SELECT * FROM EDUCATIONAL_GROUPS;
```

Структура `STUDENTS`: `id BIGINT` (автоинкремент), `name VARCHAR(15)`, `surname VARCHAR(25)`, `faculty VARCHAR(20)`, `age INTEGER`. Структура `EDUCATIONAL_GROUPS`: `id BIGINT` (автоинкремент), `group_number VARCHAR(20)`, `faculty VARCHAR(20)`, `course INTEGER`. Файл БД и SQL-следы работы не следует коммитить.

## Формы и запросы

Перейдите в браузере на `http://localhost:8080/list` (студенты) или `http://localhost:8080/groups/list` (группы). Кнопки «Добавить», «Изменить», «Удалить» работают для обеих сущностей; удаление отправляет **POST**, а не опасную изменяющую состояние GET-ссылку. Поля формы проверяются на сервере, ошибки отображаются рядом с полями. При сохранении/удалении происходит перенаправление на список. Пустой список тоже возвращает HTTP 200.

Следующие `curl.exe`-команды (PowerShell) демонстрируют оба независимых сценария; коды и фрагменты ниже **ожидаемые**. При просмотре заголовков используйте `-i`, а для обработки перенаправления можно добавить `-L`. Для новых файлов БД первый вставленный объект каждой таблицы имеет id=1, для существующих используйте id со страницы списка.

```powershell
curl.exe -i http://localhost:8080/add-student-form
# 200 OK; HTML с формой action="/saveStudent"
curl.exe -i -X POST http://localhost:8080/saveStudent -d "name=Ivan&surname=Ivanov&faculty=RTF&age=20"
# 302 Found; Location: http://localhost:8080/list
curl.exe -i http://localhost:8080/list
# 200 OK; HTML содержит Ivanov, RTF, ссылки изменения и POST-форму удаления
curl.exe -i "http://localhost:8080/showUpdateForm?studentId=1"
# 200 OK; HTML формы с ранее введёнными значениями
curl.exe -i -X POST http://localhost:8080/saveStudent -d "id=1&name=Ivan&surname=Petrov&faculty=RTF&age=21"
# 302 Found; после GET /list отображается Petrov и возраст 21
curl.exe -i -X POST http://localhost:8080/deleteStudent -d "studentId=1"
# 302 Found; после GET /list строка удалённого студента отсутствует

curl.exe -i http://localhost:8080/groups/add-form
# 200 OK; HTML с формой action="/groups/save"
curl.exe -i -X POST http://localhost:8080/groups/save -d "groupNumber=RI-101&faculty=RTF&course=1"
# 302 Found; Location: http://localhost:8080/groups/list
curl.exe -i http://localhost:8080/groups/list
# 200 OK; HTML содержит RI-101
curl.exe -i "http://localhost:8080/groups/update-form?groupId=1"
# 200 OK; HTML формы содержит RI-101
curl.exe -i -X POST http://localhost:8080/groups/save -d "id=1&groupNumber=RI-102&faculty=RTF&course=2"
# 302 Found; после GET /groups/list отображается RI-102 и курс 2
curl.exe -i -X POST http://localhost:8080/groups/delete -d "groupId=1"
# 302 Found; после GET /groups/list строка удалённой группы отсутствует
```

Если id не существует, страницы редактирования и команды удаления возвращают HTTP 404. При ошибках проверки (например, пустое имя или возраст 0) `POST /saveStudent` повторно показывает форму с ошибкой, HTTP 200; для курса группы действует диапазон 1–6. Полный маршрут студентов: `GET /`, `/list`, `/add-student-form`, `/showUpdateForm?studentId=...`; `POST /saveStudent`, `/deleteStudent`. Группы: `GET /groups/list`, `/groups/add-form`, `/groups/update-form?groupId=...`; `POST /groups/save`, `/groups/delete`.

## Actuator

В `application.properties` явно опубликованы `health`, `info`, `beans`; доступ к `/actuator` (список ссылок) также включён. Ожидаемые ответы:

```powershell
curl.exe -i http://localhost:8080/actuator
# 200 OK; JSON _links с health, info и beans
curl.exe -i http://localhost:8080/actuator/health
# 200 OK; {"status":"UP"} для работоспособного приложения
curl.exe -i http://localhost:8080/actuator/info
# 200 OK; JSON с app.name="Lab 7 Student and Educational Group Forms"
curl.exe -i http://localhost:8080/actuator/beans
# 200 OK; JSON contexts с зарегистрированными бинами
```
