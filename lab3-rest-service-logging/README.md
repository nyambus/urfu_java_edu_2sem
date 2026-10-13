# ЛР №3 — REST-сервис, перечисления и журналирование

Независимая доработка ЛР №2: Java 17, Spring Boot 2.7.16, Spring Web, Validation, Lombok; порт **8082**. ЛР №2 и №3 не запускаются одновременно на одном порту. `@Slf4j` и `logging.level.ru.education.lab3=INFO` выводят в консоль получение запроса, формирование и изменения ответа; ошибки проверки `BindingResult` и исключения — на уровне ERROR. Класс `DateTimeUtil` каждый раз возвращает отдельный `SimpleDateFormat` (UTC).

```bash
mvn clean package
java -jar target/lab3-rest-service-logging-1.0.0.jar
```

`POST /api/request`, заголовок `Content-Type: application/json`:

```bash
curl -i -X POST http://localhost:8082/api/request -H 'Content-Type: application/json' -d '{"uid":"10","operationUid":"operation-10","systemName":"ERP","systemTime":"2026-10-13T12:00:00Z","source":"web","communicationId":100000}'
```

HTTP **200**: `{"uid":"10","operationUid":"operation-10","systemTime":"2026-10-13T12:00:01.000Z","code":"success","errorCode":"","errorMessage":""}` (время примерное). Допустимые `systemName`: **ERP** (Enterprise Resource Planning), **CRM** (Customer Relationship Management), **WMS** (Warehouse Management System); поле необязательно. `code`, `errorCode`, `errorMessage` — перечисления, сериализуемые в привычные строковые значения. Две реализации `ModifyResponseService` изменяют ответ на успешном и ошибочном пути соответственно.

Обязательны непустые `uid` и `operationUid` (каждый максимум 32 символа), `systemTime`; целый `communicationId` — от 1 до 100000 включительно. Прочие поля необязательны. Для проверки нескольких ошибок сразу:

```bash
curl -i -X POST http://localhost:8082/api/request -H 'Content-Type: application/json' -d '{"uid":"","operationUid":"","communicationId":0}'
```

HTTP **400**, тело: `{"uid":"","operationUid":"","systemTime":"...","code":"failed","errorCode":"ValidationException","errorMessage":"Ошибка валидации запроса","details":"communicationId: must be greater than or equal to 1; operationUid: must not be blank; systemTime: must not be blank; uid: must not be blank"}`. `details` содержит ошибки полей, а текст зависит от локали валидатора. Проверяйте также отдельно пустые/отсутствующие `uid`, `operationUid`, `systemTime`, длину 33 символа, пропущенный `communicationId` и значение 100001. Значение `systemName: "INVALID"`, неверный JSON или неверный тип поля возвращают **400**, с сообщением о допустимых типах.

```bash
curl -i -X POST http://localhost:8082/api/request -H 'Content-Type: application/json' -d '{"uid":"123","operationUid":"op-123","systemName":"CRM","systemTime":"2026-10-13T12:00:00Z","communicationId":5}'
```

HTTP **422**: `{"uid":"123","operationUid":"op-123","systemTime":"...","code":"failed","errorCode":"UnsupportedCodeException","errorMessage":"Идентификатор uid=123 не поддерживается"}`. Неожиданная ошибка сервера дает HTTP 500 и `UnknownException` без выдачи внутренних деталей. Проверка полей предшествует проверке специального uid.

Допущения: путь `/api/request` взят из требования к проекту, поскольку URL на рисунке задания неразличим. Версия из настроек ЛР №2 (**2.7.16**) имеет приоритет над упоминанием Boot 3 в ее шапке, поэтому используются `javax.validation`. Для неподдерживаемого uid выбран HTTP 422.
