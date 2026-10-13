# ЛР 4 — Сервис 2 (8084)

Независимый Maven-проект: Java 17, Spring Boot 2.7.16, Spring Web, Validation (`javax.validation`) и Lombok. Это копия модели REST-сервиса из ЛР 2–3 с переименованной точкой входа `ru.education.lab4.Service2Application` и портом 8084; она **не пересылает** запросы обратно в сервис 1.

## Сборка и запуск

Из каталога `service2` выполните `mvn clean package`, затем `java -jar target/lab4-service2-1.0.0.jar` (или `mvn spring-boot:run`). В другом терминале из `../service1` запустите сервис 1 (порт 8082). Проекты собираются и запускаются по отдельности, общий родительский Maven-проект не нужен.

## Проверка

Предпочтительный сценарий — выполнить `POST http://localhost:8082/api/request` из README сервиса 1. Сервис 2 получает модифицированный `Request`, пишет все поля в INFO и пишет время в миллисекундах от получения его сервисом 1; для этого сервис 1 отправляет заголовок `X-Service1-Received-At` с Unix epoch в миллисекундах. Поле `systemTime` входящего JSON остаётся неизменным; в ответе оно означает время формирования ответа и задаётся в UTC. Прямой вызов без заголовка тоже допустим (но измерения времени тогда нет):

```bash
curl -i -X POST http://localhost:8084/api/request -H 'Content-Type: application/json' -d '{"uid":"10","operationUid":"op-10","systemName":"ERP","systemTime":"2023-10-07T07:47:56.503Z","source":"service1:mobile","communicationId":17}'
```

Ожидается `HTTP 200`, тело вида `{"uid":"10","operationUid":"op-10","systemTime":"2026-10-13T10:00:00.000Z","code":"success","errorCode":"","errorMessage":""}`; реальное время меняется. Прямой вызов с `uid=123` даёт `HTTP 400` и тело с `code=failed`, `errorCode=UnsupportedCodeException`, `errorMessage="Идентификатор uid=123 не поддерживается"`. Пустой `uid`, отсутствующие `operationUid` или `systemTime`, `communicationId` вне диапазона 1–100000, неизвестный `systemName` или неверный JSON дают `HTTP 400`, `errorCode=ValidationException`, `errorMessage="Ошибка валидации запроса"`; ошибки BindingResult логируются на уровне ERROR. Если тело JSON не читается, ответ не содержит исходных идентификаторов (`uid`/`operationUid` равны null). Сообщения об изменении ответа пишутся на уровне INFO.

Допущения: `POST /api/request` выбран из-за того, что URL в документе ЛР 2 указан лишь на скриншоте; версия Spring Boot 2.7.16 взята из настроек проекта ЛР 2 (несмотря на упоминание Boot 3 в шапке). Время клиента «Send» сервером недоступно: измеряется время от входа запроса в контроллер сервиса 1 до входа в контроллер сервиса 2.
