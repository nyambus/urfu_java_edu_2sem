# ЛР 4 — Сервис 1 (8082)

Самостоятельный Maven-проект: Java 17, Spring Boot 2.7.16, Spring Web, Validation (`javax.validation`) и Lombok. Точка входа — `ru.education.lab4.Service1Application`.

## Сборка и запуск

Из каталога `service1` выполните `mvn clean package`, затем `java -jar target/lab4-service1-1.0.0.jar` (либо `mvn spring-boot:run`). Перед отправкой запроса отдельно соберите и запустите `../service2` на порту 8084. Этот сервис запускается на порту 8082; адрес второго задаётся свойством `service2.url` (по умолчанию `http://localhost:8084/api/request`). Для одновременного запуска нужны **два терминала**. Сервис 2 следует запустить первым.

## Запросы (примеры для Bash)

```bash
curl -i -X POST http://localhost:8082/api/request -H 'Content-Type: application/json' -d '{"uid":"10","operationUid":"op-10","systemName":"CRM","systemTime":"2023-10-07T07:47:56.503Z","source":" mobile ","communicationId":17,"templateId":1,"productCode":2,"smsCode":3}'
```

Ответ `HTTP 200`; пример тела: `{"uid":"10","operationUid":"op-10","systemTime":"2026-10-13T10:00:00.000Z","code":"success","errorCode":"","errorMessage":""}` (фактическое `systemTime` — текущее UTC). В логе сервиса 1 сначала исходный запрос `source= mobile `, затем модифицированный `source=service1:mobile`; в логе сервиса 2 — модифицированный запрос и разница времени в миллисекундах. Две реализации `ModifyRequestService` по порядку обрезают пробелы источника и добавляют префикс `service1`. `systemTime` запроса не переписывается. Исходный момент получения в сервисе 1 пересылается отдельным заголовком `X-Service1-Received-At` (миллисекунды Unix epoch); сервис 2 измеряет время до приёма запроса. Поскольку оба приложения работают на одном компьютере, их системные часы совпадают.

```bash
curl -i -X POST http://localhost:8082/api/request -H 'Content-Type: application/json' -d '{"uid":"123","operationUid":"op-10","systemTime":"2023-10-07T07:47:56.503Z","communicationId":17}'
curl -i -X POST http://localhost:8082/api/request -H 'Content-Type: application/json' -d '{"uid":"10","operationUid":"op-10","systemTime":"2023-10-07T07:47:56.503Z","communicationId":100001}'
```

Первый запрос даёт `HTTP 400`, `code=failed`, `errorCode=UnsupportedCodeException`, `errorMessage="Идентификатор uid=123 не поддерживается"`. Второй даёт `HTTP 400`, `code=failed`, `errorCode=ValidationException`, `errorMessage="Ошибка валидации запроса"`; детали поля выводятся в лог. Обязательны `uid` (до 32 символов), `operationUid` (до 32), `systemTime` и `communicationId` (1–100000). Необязательные `systemName`: `ERP`, `CRM`, `WMS`; `source`, `templateId`, `productCode`, `smsCode`. Некорректный JSON/значение enum также даёт 400 и ответ с `ValidationException` (без uid, если тело не удалось прочитать).

Если сервис 2 остановлен, запрос с корректными данными получает `HTTP 502`, `code=failed`, `errorCode=DownstreamUnavailable`, `errorMessage="Сервис 2 недоступен или отклонил запрос"`; соединение/ответ ограничены 3 секундами. При не-2xx от сервиса 2 сервис 1 также отвечает 502. Обе ошибки логируются на уровне ERROR.

Допущения: endpoint `POST /api/request` принят потому, что URL оригинального задания виден только на иллюстрации; в шапке ЛР 2 упоминается Spring Boot 3, но настройки проекта требуют 2.7.16 — использована версия из настроек. Задание измеряет время от нажатия Send: заголовок фиксирует ближайший серверу наблюдаемый момент (приём сервисом 1), а не момент на клиенте.
