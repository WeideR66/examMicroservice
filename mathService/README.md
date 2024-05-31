# Math Service

### Сервис математики
Выдает по запросам вопросы по математике.
Общается с сервисом-экзаминатором по REST.
Отправляет события (выдача вопросов, добавление вопросов) по Kafka.
Имеет свою базу данных для хранения вопросов.

Используемые технологии: Spring, Spring Web, Spring Data JPA, Kafka, PostgreSQL, Liquibase, Eureka Discovery Client.