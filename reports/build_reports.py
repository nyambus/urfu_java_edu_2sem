"""Шесть отдельных отчётов на стилях оригинала; рисунки требуют реальных кадров студента."""
from pathlib import Path
from docx_builder import Report, BODY_SIZE, FONT, ST_BODY

ROOT = Path(__file__).resolve().parent.parent
HERE = Path(__file__).resolve().parent
REPO = "https://github.com/nyambus/urfu_java_edu_2sem"

LABS = {
    1: {
        "title": "Веб-приложение на Spring Framework без Spring Boot",
        "goal": "Создать минимальное веб-приложение на Spring MVC, собрать его в WAR и развернуть на отдельно установленном Apache Tomcat. После работы вы сможете объяснить, какие действия Spring Boot автоматизирует в следующей лабораторной.",
        "software": "JDK 21; Maven 3.9.x; Tomcat 11.0.26; браузер или curl; IntelliJ IDEA CE.",
        "versions": "Spring Framework 7.0.9; Jakarta Servlet API 6.1; Java 21; Tomcat 11.0.26.",
        "folder": "lab1-spring-mvc",
        "sections": [
            ("Подготовка и настройка", "Собран Maven-проект без Spring Boot с упаковкой WAR. WebConfig включает Spring MVC и сканирование компонентов; WebApplicationInitializer регистрирует DispatcherServlet на / без web.xml и без корневого контекста.", ["pom.xml", "src/main/java/ru/education/springmvc/WebConfig.java", "src/main/java/ru/education/springmvc/WebApplicationInitializer.java"]),
            ("Реализация обработчиков", "HelloController отвечает на GET /hello; TaskController реализует /about, /time и вариант 10 mod 4 = 2: /uppercase?text. Отсутствующий обязательный text приводит к HTTP 400.", ["src/main/java/ru/education/springmvc/HelloController.java", "src/main/java/ru/education/springmvc/TaskController.java"]),
            ("Проверка WAR", "Выполнить mvn -q clean package, скопировать target/spring-mvc-lab.war в webapps Tomcat 11 и запустить catalina. Проверить URL относительно /spring-mvc-lab; фактические результаты и логи фиксировать только после собственного запуска.", ["README.md"]),
        ],
        "requests": [
            ("GET", "/spring-mvc-lab/hello", "—", "200", "Hello, World!"),
            ("GET", "/spring-mvc-lab/hello?name=Student", "—", "200", "Hello, Student!"),
            ("GET", "/spring-mvc-lab/about", "—", "200", "Козионов Илья Алексеевич, РИЗ-150110у"),
            ("GET", "/spring-mvc-lab/time", "—", "200", "текущее время ISO 8601 с часовым смещением"),
            ("GET", "/spring-mvc-lab/uppercase?text=hello", "—", "200", "HELLO"),
            ("GET", "/spring-mvc-lab/uppercase", "—", "400", "тело ошибки Tomcat зависит от конфигурации"),
            ("GET", "/spring-mvc-lab/unknown", "—", "404", "тело ошибки Tomcat зависит от конфигурации"),
        ],
        "figures": [
            ("Сборка WAR и развёртывание в Tomcat", "mvn -q clean package; запустить Tomcat с развёрнутым spring-mvc-lab.war", "WAR создан, в журнале Tomcat видно успешное развёртывание"),
            ("Ответы /hello и /about", "curl -i http://localhost:8080/spring-mvc-lab/hello; curl -i http://localhost:8080/spring-mvc-lab/about", "200, Hello, World!; 200, Козионов Илья Алексеевич, РИЗ-150110у"),
            ("Вариант /uppercase и ошибки параметра и пути", "curl -i 'http://localhost:8080/spring-mvc-lab/uppercase?text=hello'; curl -i http://localhost:8080/spring-mvc-lab/uppercase; curl -i http://localhost:8080/spring-mvc-lab/unknown", "200 HELLO; 400; 404"),
        ],
        "answers": [
            (2, "DispatcherServlet — центральный сервлет Spring MVC: получает запрос от Tomcat и выбирает метод контроллера через HandlerMapping. WebApplicationInitializer сопоставляет его с /, а @GetMapping в HelloController выбирает /hello. Затем Spring формирует HTTP-ответ из возвращённой строки @RestController."),
            (4, "Tomcat читает сервис-провайдеры ServletContainerInitializer из библиотек WAR по механизму Servlet SPI. SpringServletContainerInitializer обнаруживает реализации WebApplicationInitializer и вызывает их при развёртывании. Поэтому файл web.xml не требуется; именно WebApplicationInitializer возвращает WebConfig и путь /."),
            (6, "Servlet API нужен для компиляции WebApplicationInitializer, но реализация API уже поставляется Tomcat 11. Область Maven provided исключает этот API из библиотек WAR и предотвращает конфликт двух копий при развёртывании. Spring MVC остаётся обычной зависимостью приложения."),
            (8, "Spring Boot автоматически конфигурирует контекст приложения, DispatcherServlet и связанные компоненты MVC по зависимостям, вместо ручных WebConfig и WebApplicationInitializer. В этой работе внешний Tomcat запускается отдельно и принимает WAR из webapps. В типовом Boot-приложении встроенный Tomcat включён в запускаемый JAR и стартует вместе с main; HTTP-обработка Spring MVC остаётся той же."),
        ],
        "conclusion": "Создано приложение Spring MVC без Spring Boot с ручной регистрацией DispatcherServlet. Реализованы /hello, /about, /time и вариант /uppercase. Сборка создаёт WAR для внешнего Tomcat; проверки 400 и 404 отделяют обязательные параметры от неизвестных адресов. Скриншоты и фактическое время должны быть добавлены после запуска на рабочем месте.",
    },
    2: {
        "title": "Разработка простого REST сервиса",
        "goal": "Разработать простой REST сервис",
        "software": "IntelliJ IDEA CE, Java 17, браузер, Postman или curl.",
        "versions": "Spring Boot 2.7.16; Java 17; порт 8082. В шапке задания указано Spring Boot 3, но в настройках проекта — 2.7.16; выбран вариант из настроек.",
        "folder": "lab2-rest-service",
        "sections": [
            ("Описание сообщений", "Входной Request содержит uid, operationUid, systemName, systemTime, source, communicationId, templateId, productCode, smsCode. Response содержит uid, operationUid, systemTime, code, errorCode, errorMessage; успешный результат и ошибки представлены отдельно.", []),
            ("REST-сервис и валидация", "Контроллер принимает POST /api/request на порту 8082. Проверяются uid и operationUid (непустые, не длиннее 32), systemTime, а также communicationId от 1 до 100000. Исключение ValidationFailedException оформляет ошибку валидации.", []),
            ("Реализация дополнительного функционала", "Проверить каждое обязательное поле по отдельности, затем сочетание ошибок. Для uid=123 выбрасывается UnsupportedCodeException и возвращается соответствующий errorCode и понятное errorMessage. Снимок коллекции запросов Postman должен быть сделан самостоятельно.", []),
        ],
        "requests": [
            ("POST", "/api/request", '{"uid":"1","operationUid":"op1","systemTime":"2026-10-13T12:00:00Z","communicationId":1}', "200", 'code=success, uid=1, operationUid=op1'),
            ("POST", "/api/request", '{"uid":"","operationUid":"op1","systemTime":"2026-10-13T12:00:00Z","communicationId":1}', "400", "errorCode=ValidationException"),
            ("POST", "/api/request", '{"uid":"1","operationUid":"op1","systemTime":"2026-10-13T12:00:00Z","communicationId":100001}', "400", "errorCode=ValidationException"),
            ("POST", "/api/request", '{"uid":"123","operationUid":"op1","systemTime":"2026-10-13T12:00:00Z","communicationId":1}', "422", "errorCode=UnsupportedCodeException"),
        ],
        "figures": [
            ("Успешный POST-запрос сервиса", "curl -i -H 'Content-Type: application/json' -d '{\"uid\":\"1\",\"operationUid\":\"op1\",\"systemTime\":\"2026-10-13T12:00:00Z\",\"communicationId\":1}' http://localhost:8082/api/request", "200; code=success"),
            ("Отдельные и совместные ошибки валидации", "Отправить запросы для пустого uid, operationUid, systemTime; communicationId=0 и 100001; затем совместить ошибки", "400; errorCode=ValidationException; перечисление нарушений"),
            ("Исключение UnsupportedCodeException", "Отправить корректный JSON с uid=123", "ответ с errorCode=UnsupportedCodeException и пояснением"),
            ("Коллекция проверок в Postman", "Сохранить предыдущие запросы в коллекции ЛР №2 и открыть её", "на кадре видны все кейсы"),
        ],
        "answers": [
            (2, "POST-запрос передаёт данные в теле: например JSON-объект Request в этом проекте. В HTTP-теле также возможны XML, текст, бинарный поток и multipart-формы; тип задаётся Content-Type. Параметры пути и заголовки могут передаваться одновременно с телом."),
            (4, "Пользовательское исключение выделяет конкретную причину отказа от остальных ошибок. UnsupportedCodeException соответствует uid=123 и преобразуется в errorCode того же имени, что позволяет клиенту реагировать на неё явно. ValidationFailedException отдельно обозначает ошибки обязательных полей."),
            (6, "@RestController регистрирует класс обработчиком HTTP-запросов и включает сериализацию возвращаемых объектов в тело ответа. Это эквивалент сочетания @Controller и @ResponseBody. В MyController метод POST /api/request принимает JSON Request и отдаёт Response вместо имени HTML-шаблона."),
        ],
        "conclusion": "Разработан REST-сервис для приёма и ответа в JSON. Валидация ограничивает обязательные поля и диапазон communicationId. Дополнительный сценарий с uid=123 получает собственный код ошибки. Результаты запросов в таблице обозначены как ожидаемые до ручной фиксации HTTP-ответов.",
    },
    3: {
        "title": "Доработка простого REST сервиса",
        "goal": "Доработать простой REST сервис",
        "software": "IntelliJ IDEA CE, Java 17, браузер, Postman или curl.",
        "versions": "Spring Boot 2.7.16; Java 17; SLF4J через Lombok; порт 8082.",
        "folder": "lab3-rest-service-logging",
        "sections": [
            ("Доработка response", "DateTimeUtil.getCustomFormat() создаёт формат системного времени. ModifyResponseService имеет две реализации; ErrorCodes и ErrorMessages определяют типизированные результаты. Systems ограничивает допустимые systemName значениями ERP, CRM и WMS.", []),
            ("Настройка логов", "Добавлены @Slf4j и logging.level в application.properties. INFO записывает этапы модификации request/response, ERROR записывает исключения и сообщения BindingResult. Request.toString() делает содержимое входных данных читаемым в журнале.", []),
            ("Дополнительный функционал", "Проверить успешный запрос, неверное enum-значение, несколько ошибок валидации и uid=123. Логи снимайте только с реально работающего экземпляра приложения.", []),
        ],
        "requests": [
            ("POST", "/api/request", '{"uid":"1","operationUid":"op1","systemName":"ERP","systemTime":"2026-10-13T12:00:00Z","communicationId":1}', "200", 'code="success" (значение enum Codes), errorCode=""'),
            ("POST", "/api/request", '{"uid":"123","operationUid":"op1","systemName":"CRM","systemTime":"2026-10-13T12:00:00Z","communicationId":1}', "422", "код неподдерживаемого uid"),
            ("POST", "/api/request", '{"uid":"1","operationUid":"op1","systemName":"INVALID","systemTime":"2026-10-13T12:00:00Z","communicationId":1}', "400", "ошибка десериализации Systems"),
        ],
        "figures": [
            ("Успешный ответ с перечислениями", "POST http://localhost:8082/api/request с systemName=ERP и остальными обязательными полями", "код 200 и JSON-ответ с перечислениями"),
            ("INFO и ERROR в журнале", "Отправить корректный запрос, затем пустой uid и uid=123", "в журнале видны этапы INFO и ошибки ERROR"),
            ("Проверка недопустимой системы", "POST /api/request c systemName=INVALID", "400 для недопустимого значения перечисления"),
        ],
        "answers": [
            (2, "CRM (Customer Relationship Management) — система управления взаимодействиями с клиентами. Она хранит контакты, историю сделок и обращения; примеры — Salesforce и Битрикс24. В Request значение CRM — один из трёх допустимых вариантов Systems, а не произвольная строка."),
            (4, "Spring Boot использует фасад SLF4J, за которым по умолчанию работает Logback. Возможны реализации Log4j 2 или адаптеры для java.util.logging при соответствующей настройке зависимостей. Lombok @Slf4j генерирует поле логгера SLF4J; logging.level регулирует вывод сообщений без изменения Java-кода."),
            (6, "@Qualifier выбирает конкретный bean при нескольких кандидатах одного интерфейса. У ModifyResponseService две реализации, поэтому внедрение без различения кандидатов неоднозначно. Значение @Qualifier должно соответствовать имени выбранного bean, что позволяет явно назначить этап обработки ответа."),
        ],
        "conclusion": "Модель ответа получила типизированные коды и сообщения, а имя системы — ограниченный набор Systems. Время выделено в DateTimeUtil. Логирование INFO отражает изменения сообщения, ERROR — ошибки валидации и исключения. Для доказательства работы журнала нужны снимки собственного запуска.",
    },
    4: {
        "title": "Доработка простого REST сервиса: два сервиса",
        "goal": "Доработать простой REST сервис",
        "software": "IntelliJ IDEA CE, Java 17, браузер, Postman или curl.",
        "versions": "Spring Boot 2.7.16; Java 17; сервис 1 — 8082, сервис 2 — 8084.",
        "folder": "lab4-two-services",
        "sections": [
            ("Создание второго сервиса", "Service2Application запускается на порту 8084 и принимает пересланный request. Второй сервис протоколирует полученный объект и момент получения.", []),
            ("Пересылка сообщения", "Сервис 1 принимает POST /api/request на 8082, вызывает ModifyRequestService, затем RestTemplate отправляет request в сервис 2. Исходный момент получения передаётся вместе с HTTP-запросом; разницу между ним и моментом приёма вычисляет и логирует сервис 2.", []),
            ("Реализация дополнительного функционала", "Отдельная реализация ModifyRequestService изменяет source. Проверка: оба приложения запущены одновременно, в логах первого видно изменение, второго — новое source и прошедшее время. Если сервис 2 недоступен, успех не должен выдаваться как результат пересылки.", []),
        ],
        "requests": [
            ("POST", "/api/request (8082)", '{"uid":"1","operationUid":"op1","systemName":"ERP","systemTime":"2026-10-13T12:00:00Z","communicationId":1,"source":"postman"}', "200", "успех после ответа сервиса 2; в логах 8084 изменённый source и длительность"),
            ("POST", "/api/request (8082, 8084 остановлен)", "тот же JSON", "502", "code=failed; errorCode=DownstreamUnavailable; пересылка не подтверждена"),
        ],
        "figures": [
            ("Одновременный запуск сервисов", "запустить оба JAR; просмотреть консоли портов 8082 и 8084", "оба приложения слушают свои порты"),
            ("Ответ и журнал сервиса 1", "POST http://localhost:8082/api/request с source=postman", "JSON-ответ и запись модифицированного request"),
            ("Модифицированный request и длительность в сервисе 2", "после предыдущего POST открыть журнал сервиса 2", "изменённый source и измеренная разница времени"),
        ],
        "answers": [
            (2, "@SpringBootApplication объединяет @SpringBootConfiguration, @EnableAutoConfiguration и @ComponentScan. Поэтому главный класс запускает конфигурацию и обнаруживает контроллеры и сервисы своего пакета. В ЛР4 отдельные главные классы создают независимые процессы на разных портах."),
            (4, "Lombok @NoArgsConstructor генерирует конструктор без параметров. Он полезен для десериализации Request из JSON, когда объект создаётся до заполнения свойств. Это сокращает шаблонный код, но не отменяет проверку обязательных полей после десериализации."),
            (6, "Lombok @Data генерирует геттеры, сеттеры, equals, hashCode и toString для класса данных. У Request геттеры и сеттеры нужны для JSON-сериализации и чтения полей модификатором. Для JPA-сущностей и чувствительных полей @Data следует использовать осторожно из-за поведения equals и toString."),
        ],
        "conclusion": "Запланированная схема разделяет приём и обработку на два HTTP-сервиса. Изменение source происходит до пересылки, а второй сервис вычисляет длительность по переданному моменту получения. Отдельные порты позволяют запускать оба сервиса одновременно. Для подтверждения результата требуются реальные журналы обоих процессов.",
    },
    6: {
        "title": "Разработка REST-сервиса с базой данных H2",
        "goal": "разработать REST full service",
        "software": "IntelliJ IDEA CE, Postman или curl, H2 Database, DBeaver, Java 17.",
        "versions": "Spring Boot 3.1.5; Java 17; Spring Data JPA, H2, Lombok; порт 8080. В теории задания упомянут Spring Boot 2, в конфигурации проекта — 3.1.5.",
        "folder": "lab6-spring-data-jpa-h2",
        "sections": [
            ("Подключение H2 и создание таблиц", "Путь к файлу H2 настраивается через application.properties. В DBeaver открыть эту же БД после остановки приложения и выполнить SQL-скрипт создания STUDENTS и учебных дисциплин; *.mv.db не включаются в репозиторий.", []),
            ("CRUD студентов", "Сущность Student, слой DAO, сервис и контроллер разделяют хранение, операции и HTTP-маршруты. GET /api/students и GET /api/students/{id} возвращают данные; POST, PUT и DELETE меняют состояние и сообщают о результате.", []),
            ("Дополнительный функционал: дисциплины", "Созданы вторая таблица и независимые слои entity/dao/service/controller для учебных дисциплин. Проверить добавление, чтение, изменение, удаление и попытку обращения к отсутствующей записи.", []),
        ],
        "requests": [
            ("POST", "/api/students", '{"name":"Иван","surname":"Иванов","faculty":"ИТ","age":25}', "201", "JSON нового студента и заголовок Location"),
            ("GET", "/api/students", "—", "200", "JSON-массив студентов"),
            ("PUT", "/api/students", '{"id":1,"name":"Иван","surname":"Иванов","faculty":"РТФ","age":26}', "200 при существующем id", "JSON обновлённого студента"),
            ("DELETE", "/api/students/1", "—", "200 при существующем id", '{"message":"Студент удалён: 1"}'),
            ("POST", "/api/subjects", '{"name":"Программирование","teacher":"Архипов Н. А.","hours":72}', "201", "JSON новой дисциплины и заголовок Location"),
            ("GET", "/api/subjects", "—", "200", "JSON-массив дисциплин"),
        ],
        "figures": [
            ("Таблицы STUDENTS и учебных дисциплин в DBeaver", "остановить сервер, открыть testdb/students в DBeaver, выполнить schema.sql", "видны две таблицы; реальное содержимое зависит от внесённых записей"),
            ("Создание и чтение студента", "POST /api/students c JSON из таблицы; GET /api/students", "результат создания и студент в списке"),
            ("Обновление, удаление, проверка отсутствующего id", "PUT /api/students; DELETE /api/students/{id}; GET /api/students/{id}", "изменение, подтверждение удаления, ответ об отсутствии записи"),
            ("CRUD учебной дисциплины", "POST /api/subjects; GET /api/subjects; PUT /api/subjects; DELETE /api/subjects/{id}", "подтверждения операций и обновлённые данные"),
        ],
        "answers": [
            (2, "API-First определяет контракт интерфейса до реализации, например OpenAPI-схему с маршрутами и ошибками. Code-First сначала реализует контроллеры, а затем извлекает или описывает контракт по коду. Для этого проекта маршруты /api/students и /api/subjects заданы в контроллерах; при совместной работе с клиентами заранее согласованный контракт уменьшил бы расхождения."),
            (4, "Синхронная интеграция через REST или SOAP выполняет запрос и ожидает ответ в рамках вызова. Асинхронная интеграция через очередь или брокер событий развязывает время работы отправителя и получателя. Пакетный обмен выгружает файлы или наборы записей по расписанию; общая БД позволяет прямой доступ, но сильнее связывает схемы и права систем."),
            (6, "gRPC вызывает удалённые методы по контракту Protocol Buffers и обычно использует HTTP/2. REST в этой работе обращается к ресурсам через стандартные HTTP-методы и передаёт JSON; клиент может тестировать его через curl. gRPC удобен для строго типизированного обмена и потоков, но требует сгенерированных клиентских классов или специального инструмента проверки."),
        ],
        "conclusion": "Слои controller, service, dao и entity реализуют операции с данными H2. CRUD студентов и учебных дисциплин покрывает две независимые таблицы. Ответы об отсутствии записи позволяют отличать неуспех от успешной операции. Доказательство сохранения данных требует ручной проверки API и БД.",
    },
    7: {
        "title": "Веб-приложение с формами и базой данных",
        "goal": "разработать веб-приложения с двумя веб-формами и базой данных",
        "software": "IntelliJ IDEA CE, Java 17, H2 Database, DBeaver, браузер.",
        "versions": "Spring Boot 3.1.12; Java 17; JPA, H2, Thymeleaf, Actuator. В задании указана несуществующая в Maven Central 3.1.15 (разрешена другая актуальная версия); выбрана последняя 3.1.x — 3.1.12.",
        "folder": "lab7-thymeleaf-forms",
        "sections": [
            ("База и серверная часть", "Student отображается на STUDENTS через JPA; StudentRepository и StudentController обеспечивают сохранение, обновление, выборку и удаление. Файловая H2 сконфигурирована в application.properties; скрипт таблиц пригоден для DBeaver.", []),
            ("Формы студентов", "Thymeleaf-шаблон add-student-form отправляет данные на /saveStudent; /list показывает сохранённых студентов, ссылки на изменение и действие удаления. Bootstrap стилизует форму и таблицу.", []),
            ("Дополнительный функционал: учебная группа", "Учебная группа имеет собственную сущность, репозиторий, контроллер, форму ввода и список с обновлением и удалением. Проверить создание и редактирование групп тем же способом, что и студентов.", []),
            ("Actuator", "Actuator публикует служебные HTTP-эндпоинты: список доступных методов, health для состояния, info для метаданных, beans для обзора компонентов. Для отчёта требуется 3–5 настоящих скриншотов вызовов; здесь оставлены четыре подписанных места.", []),
        ],
        "requests": [
            ("GET", "/add-student-form", "—", "200", "HTML-форма студента"),
            ("POST", "/saveStudent", "form: name=Иван&surname=Иванов&faculty=ИТ&age=25", "302", "переход на /list; студент сохранён"),
            ("GET", "/list", "—", "200", "HTML-таблица со студентом"),
            ("GET", "/actuator", "—", "200", "ссылки на опубликованные эндпоинты"),
            ("GET", "/actuator/health", "—", "200", 'JSON с полем status: UP при исправной БД'),
            ("GET", "/actuator/info", "—", "200", "JSON метаданных или пустой объект"),
            ("GET", "/actuator/beans", "—", "200", "JSON с компонентами контекста"),
        ],
        "figures": [
            ("Форма и сохранение студента", "открыть /add-student-form, заполнить поля и сохранить", "переход в список со студентом"),
            ("Обновление и удаление студента", "из /list изменить студента и удалить его", "обновлённая строка и отсутствие строки после удаления"),
            ("Форма и список учебных групп", "открыть форму группы, добавить и затем изменить группу", "запись присутствует и меняется в списке"),
            ("Список эндпоинтов Actuator", "GET http://localhost:8080/actuator", "JSON со ссылками"),
            ("Проверка состояния Actuator", "GET http://localhost:8080/actuator/health", "status=UP при исправной БД"),
            ("Метаданные Actuator", "GET http://localhost:8080/actuator/info", "JSON метаданных или {}"),
            ("Список beans Actuator", "GET http://localhost:8080/actuator/beans", "JSON с beans"),
        ],
        "answers": [],
        "conclusion": "Созданы две формы ввода и два списка на Thymeleaf с хранением в H2. Изменение и удаление обеих сущностей доступны из браузера. Actuator показывает состояние приложения и зарегистрированные компоненты. Четыре подписанных места для кадров Actuator требуют снимков собственного экземпляра.",
    },
}


def relevant_files(folder):
    """Ссылки только на существующие исходники и настройки, не на target и БД."""
    root = ROOT / folder
    suffixes = {".java", ".html", ".properties", ".sql"}
    return [p.relative_to(ROOT).as_posix() for p in sorted(root.rglob("*"))
            if p.is_file() and (p.suffix in suffixes or p.name in {"pom.xml", "README.md"})
            and "target" not in p.parts and "testdb" not in p.parts]


SECTION_FILES = {
    2: {
        1: ["src/main/java/ru/education/lab2/model/Request.java", "src/main/java/ru/education/lab2/model/Response.java"],
        2: ["src/main/java/ru/education/lab2/controller/MyController.java", "src/main/java/ru/education/lab2/service/ValidationService.java", "src/main/resources/application.properties"],
        3: ["src/main/java/ru/education/lab2/exception/UnsupportedCodeException.java", "src/main/java/ru/education/lab2/controller/RequestExceptionHandler.java"],
    },
    3: {
        1: ["src/main/java/ru/education/lab3/util/DateTimeUtil.java", "src/main/java/ru/education/lab3/model/Response.java", "src/main/java/ru/education/lab3/service/ModifyResponseService.java"],
        2: ["src/main/java/ru/education/lab3/controller/MyController.java", "src/main/resources/application.properties"],
        3: ["src/main/java/ru/education/lab3/model/Systems.java", "src/main/java/ru/education/lab3/model/Request.java", "src/main/java/ru/education/lab3/service/FailureModifyResponseService.java"],
    },
    4: {
        1: ["service2/src/main/java/ru/education/lab4/Service2Application.java", "service2/src/main/java/ru/education/lab4/controller/MyController.java", "service2/src/main/resources/application.properties"],
        2: ["service1/src/main/java/ru/education/lab4/controller/MyController.java", "service1/src/main/java/ru/education/lab4/service/ModifyRequestService.java", "service1/src/main/resources/application.properties"],
        3: ["service1/README.md", "service2/README.md"],
    },
    6: {
        1: ["src/main/resources/schema.sql", "src/main/resources/application.properties"],
        2: ["src/main/java/ru/education/lab6/entity/Student.java", "src/main/java/ru/education/lab6/dao/StudentDAO.java", "src/main/java/ru/education/lab6/service/StudentService.java", "src/main/java/ru/education/lab6/controller/StudentController.java"],
        3: ["src/main/java/ru/education/lab6/entity/Subject.java", "src/main/java/ru/education/lab6/dao/SubjectDAO.java", "src/main/java/ru/education/lab6/service/SubjectService.java", "src/main/java/ru/education/lab6/controller/SubjectController.java"],
    },
    7: {
        1: ["src/main/resources/schema.sql", "src/main/resources/application.properties", "src/main/java/ru/education/lab7/repository/StudentRepository.java"],
        2: ["src/main/java/ru/education/lab7/controller/StudentController.java", "src/main/resources/templates/add-student-form.html", "src/main/resources/templates/list-students.html"],
        3: ["src/main/java/ru/education/lab7/controller/EducationalGroupController.java", "src/main/java/ru/education/lab7/entity/EducationalGroup.java", "src/main/resources/templates/add-group-form.html", "src/main/resources/templates/list-groups.html"],
        4: ["src/main/resources/application.properties", "README.md"],
    },
}


OBSERVED = {
    1: "Фактический запуск на Tomcat 11.0.26: GET /hello — 200, тело Hello, World!; /hello?name=Илья — 200, Hello, Илья! (UTF-8); /about — 200, Козионов Илья Алексеевич, РИЗ-150110у; /uppercase?text=привет — 200, ПРИВЕТ; /uppercase без text — 400; /unknown — 404. /time вернул 2026-10-13T19:15:23.399395800+05:00 (при следующем запросе время изменится).",
    2: "Фактический запуск: POST /api/request с uid=10 и валидным JSON вернул 200, code=success; uid=123 — 422, errorCode=UnsupportedCodeException; пустой uid — 400, errorCode=ValidationException; несколько нарушений одновременно — 400 с перечислением полей. HTTP-проверки сделаны без Postman.",
    3: "Фактический запуск: systemName=ERP дал 200 и JSON с code=success; systemName=BAD — 400, errorCode=ValidationException; пустой uid — 400; uid=123 — 422. В журнале INFO записаны этапы модификации response, ERROR — BindingResult и исключение uid=123.",
    4: "Фактический запуск обоих сервисов: POST /api/request на 8082 вернул 200 и code=success; сервис 2 записал source=service1:postman и измеренное время 161 мс. После остановки сервиса 2 аналогичный запрос вернул 502 с errorCode=DownstreamUnavailable.",
    6: "Фактический запуск: POST /api/students и /api/subjects — 201 с id=1; PUT для обеих таблиц — 200; GET после обновления показал новые значения; DELETE — 200; GET удалённого id — 404; невалидный POST — 400. ID и содержание БД в новом запуске могут отличаться.",
    7: "Фактический запуск в браузере: студент и учебная группа созданы, обновлены и удалены через HTML-формы; POST формы дал перенаправление 302. Отдельно созданный студент был доступен в /list после перезапуска (H2), затем удалён. /actuator, /actuator/health, /actuator/info, /actuator/beans вернули 200; health содержал status=UP; beans — один контекст.",
}


def request_table(report, rows):
    table = report.doc.add_table(rows=1, cols=5)
    table.autofit = True
    for position, row in enumerate([("Метод", "URL", "Тело запроса", "Код ответа", "Тело ответа"), *rows]):
        cells = table.rows[0].cells if position == 0 else table.add_row().cells
        for cell, text in zip(cells, row):
            paragraph = cell.paragraphs[0]
            paragraph.style = ST_BODY
            paragraph.paragraph_format.line_spacing = 1.5
            run = paragraph.add_run(text)
            run.font.name = FONT
            run.font.size = BODY_SIZE
            run.bold = position == 0


def build(number, spec):
    report = Report(number, spec["title"])
    report.section("ЦЕЛЬ РАБОТЫ")
    report.body(spec["goal"])
    report.body("Необходимое ПО: " + spec["software"])
    report.body("Использованные версии: " + spec["versions"])
    report.link(REPO)
    report.body("Номер в списке группы — 10 (чётный). Значения и ответы в таблицах ниже ожидаемые; подлинные результаты фиксируются при запуске. Скриншоты не создавались автоматически.")

    files = relevant_files(spec["folder"])
    for index, (heading, description, preferred) in enumerate(spec["sections"], 1):
        report.section(f"ЗАДАНИЕ №{index}")
        report.body(heading + ". " + description)
        names = preferred or SECTION_FILES.get(number, {}).get(index, [])
        selected = [f"{spec['folder']}/{name}" for name in names]
        if not selected:
            selected = files[(index - 1) * 3:index * 3]
        for filename in selected:
            if (ROOT / filename).is_file():
                report.body("Исходный файл: " + filename)
                report.link(REPO + "/blob/main/" + filename)
        examples = {
            1: "src/main/java/ru/education/springmvc/TaskController.java",
            2: "src/main/java/ru/education/lab2/controller/MyController.java",
            3: "src/main/java/ru/education/lab3/controller/MyController.java",
            4: "service1/src/main/java/ru/education/lab4/controller/MyController.java",
            6: "src/main/java/ru/education/lab6/controller/StudentController.java",
            7: "src/main/java/ru/education/lab7/controller/StudentController.java",
        }
        if index == 2:
            example = ROOT / spec["folder"] / examples[number]
            report.body("Листинг контроллера показывает входной маршрут и обработку HTTP-запроса.")
            report.link(REPO + "/blob/main/" + example.relative_to(ROOT).as_posix())
            report.code(example.read_text(encoding="utf-8"), "Листинг 1 — " + example.name)
        if number == 7 and index in (2, 3):
            for caption, command, expected in (spec["figures"][:2] if index == 2 else spec["figures"][2:3]):
                report.body("Команда/действие для рисунка: " + command + ". Ожидается: " + expected + ".")
                report.image(None, caption)
        if index == len(spec["sections"]):
            report.body("Таблица проверок (ожидаемые результаты; адрес localhost соответствует указанному в README порту):")
            request_table(report, spec["requests"])
            report.body("Для POST JSON укажите Content-Type: application/json; коды и данные снимайте из реального HTTP-ответа. Для динамических полей (время, id, длительность) приведены только условия проверки.")
            report.body("Наблюдения при проверке в этой среде: " + OBSERVED[number])
    if number == 7:
        active_figures = spec["figures"][3:]
    else:
        active_figures = spec["figures"]
    for caption, command, expected in active_figures:
        report.body("Команда/действие для рисунка: " + command + ". Ожидается: " + expected + ".")
        report.image(None, caption)
    if number != 7:
        report.section(f"ЗАДАНИЕ №{len(spec['sections']) + 1}")
        report.body("Вопросы по чётному номеру в списке (10).")
        for question, answer in spec["answers"]:
            report.body(f"Вопрос {question} (чётный). {answer}")
    report.section("ВЫВОД")
    report.body(spec["conclusion"])
    output = HERE / f"Отчет_о_лабораторной_работе_{number}_Козионов.docx"
    report.save(output)
    return output


def figures_manifest():
    lines = ["# План реальных скриншотов", "", "Заполнители в DOCX не являются доказательством запуска. Перед сдачей выполните указанные действия, вставьте собственные кадры вместо заполнителей и исправьте ожидаемые ответы на фактические. PowerShell: замените `curl` на `curl.exe`; POST JSON передавайте с заголовком `Content-Type: application/json`.", ""]
    for number, spec in LABS.items():
        lines += [f"## ЛР №{number}", ""]
        figures = spec["figures"]
        for index, (caption, command, result) in enumerate(figures, 1):
            lines.append(f"{index}. **Рисунок {index} — {caption}.** Выполнить: `{command}`. Ожидается: {result}.")
        lines.append("")
    (HERE / "figures.md").write_text("\n".join(lines), encoding="utf-8")


if __name__ == "__main__":
    figures_manifest()
    for n, config in LABS.items():
        print(build(n, config))
