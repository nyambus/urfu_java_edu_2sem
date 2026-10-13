# ЛР № 1 — чистый Spring MVC

Вариант A; номер в списке 10, остаток 10 % 4 = 2: `GET /uppercase?text=…`.
JDK 21, Maven 3.9.x/3.10.x, Spring Framework 7.0.9, Servlet API 6.1 (provided), внешний Apache Tomcat 11.0.26. Spring Boot не используется. Сервер по умолчанию слушает 8080.

Из этого каталога выполните `mvn -q clean package`: получится `target/spring-mvc-lab.war`. Скопируйте WAR в каталог `webapps` вашего Tomcat (PowerShell: `Copy-Item target/spring-mvc-lab.war "$env:CATALINA_HOME\webapps\spring-mvc-lab.war" -Force`; Unix: `cp target/spring-mvc-lab.war "$CATALINA_HOME/webapps/"`). Запустите сервер `& "$env:CATALINA_HOME\bin\catalina.bat" run` в PowerShell или `bash "$CATALINA_HOME/bin/catalina.sh" run` в Unix. Приложение доступно по контекстному пути `/spring-mvc-lab`. PowerShell: используйте `curl.exe` вместо `curl`.

| Проверка (curl -i URL) | Ожидаемый код и тело |
| --- | --- |
| `http://localhost:8080/spring-mvc-lab/hello` | 200, `Hello, World!` |
| `http://localhost:8080/spring-mvc-lab/hello?name=Student` | 200, `Hello, Student!` |
| `http://localhost:8080/spring-mvc-lab/hello?name=%D0%98%D0%BB%D1%8C%D1%8F` | 200, `Hello, Илья!`, UTF-8 |
| `http://localhost:8080/spring-mvc-lab/about` | 200, `Козионов Илья Алексеевич, РИЗ-150110у` |
| `http://localhost:8080/spring-mvc-lab/time` | 200, текущее время ISO 8601 с часовым смещением |
| `http://localhost:8080/spring-mvc-lab/uppercase?text=hello` | 200, `HELLO` |
| `http://localhost:8080/spring-mvc-lab/uppercase` | 400, тело ошибки контейнера зависит от настроек |
| `http://localhost:8080/spring-mvc-lab/unknown` | 404, тело ошибки контейнера зависит от настроек |

Повторяйте сборку и развертывание WAR после каждого изменения исходников.
