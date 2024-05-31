# History Service

### Сервис истории
Выдает по запросам вопросы по истории.
Общается с сервисом-экзаминатором по REST.
Отправляет события (выдача вопросов, добавление вопросов) по Kafka.
Имеет свою базу данных для хранения вопросов.

Используемые технологии: Spring, Spring Web, Spring Data JPA, Kafka, PostgreSQL, Liquibase, Eureka Discovery Client.