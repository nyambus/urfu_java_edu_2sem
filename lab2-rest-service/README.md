# ЛР №2 — REST-сервис с валидацией

Самостоятельный проект на Java 17 и Spring Boot 2.7.16 (Spring Web, Validation, Lombok). Порт **8082**. Зависимости загружаются Maven; запускать ЛР №2 и №3 одновременно без изменения порта нельзя.

```bash
mvn clean package
java -jar target/lab2-rest-service-1.0.0.jar
```

Endpoint: **POST /api/request**, заголовок `Content-Type: application/json`. Успешный запрос:

```bash
curl -i -X POST http://localhost:8082/api/request -H 'Content-Type: application/json' -d '{"uid":"10","operationUid":"operation-10","systemName":"ERP","systemTime":"2026-10-13T12:00:00Z","source":"web","communicationId":1,"templateId":2,"productCode":3,"smsCode":4}'
```

HTTP **200**; пример тела (`systemTime` генерируется во время обработки):
```json
{"uid":"10","operationUid":"operation-10","systemTime":"2026-10-13T12:00:01Z","code":"success","errorCode":"","errorMessage":""}
```

Обязательны непустые `uid`, `operationUid` (не более 32 символов каждый), `systemTime`; `communicationId` — целое число в диапазоне 1–100000. `systemName`, `source`, `templateId`, `productCode`, `smsCode` необязательны. Проверить каждое нарушение по отдельности и совместно:

```bash
curl -i -X POST http://localhost:8082/api/request -H 'Content-Type: application/json' -d '{"uid":"","operationUid":"","communicationId":100001}'
```

HTTP **400**: `{"uid":"","operationUid":"","systemTime":"...","code":"failed","errorCode":"ValidationException","errorMessage":"communicationId: must be less than or equal to 100000; operationUid: must not be blank; systemTime: must not be blank; uid: must not be blank"}` (текст валидатора зависит от локали). Пропущенный `communicationId`, ноль, отрицательное значение и число выше 100000 также дают 400. Для `uid` и `operationUid` отдельно проверьте пустое значение, пропуск и 33 символа; для `systemTime` — пропуск или пустую строку. Не-JSON и неверные типы полей дают 400 с понятным сообщением.

```bash
curl -i -X POST http://localhost:8082/api/request -H 'Content-Type: application/json' -d '{"uid":"123","operationUid":"op-123","systemTime":"2026-10-13T12:00:00Z","communicationId":5}'
```

HTTP **422**: `{"uid":"123","operationUid":"op-123","systemTime":"...","code":"failed","errorCode":"UnsupportedCodeException","errorMessage":"Идентификатор uid=123 не поддерживается"}`. Ошибка валидации имеет приоритет над исключением `UnsupportedCodeException`. Непредвиденная серверная ошибка возвращает 500 и `UnknownException` без внутренних деталей.

Допущения: путь запроса на исходном рисунке неразличим — принят `/api/request`; шапка задания упоминает Boot 3, но настройки проекта явно требуют **2.7.16** (поэтому используются `javax.validation`). Для случая `uid=123` выбран HTTP 422, так как валидный по схеме идентификатор не поддерживается бизнес-правилом.
