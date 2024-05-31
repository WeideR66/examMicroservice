# Домашняя работа - Микросервис
### Микросервис позволяет получать вопросы и ответы на них по разным дисциплинам (на данный момент по математике и истории), а также добавлять новые.

Микросервис состоит из 4 частей:
- MathService
- HistoryService
- ExamService
- Notificator
- EurekaServer

MathService и HistoryService хранят, выдают и добавляют вопросы в свои базы данных.
ExamService по REST общается с MathService И HistoryService, представляет собой общую точку, через которую можно получить или добавить вопросы.
EurekaServer позволяет управлять экземплярами MathService и HistoryService и хранить информацию об их состоянии.
Notificator логирует все операции с вопросами (выдача, добавление), он является Kafka consumer и принимает сообщения от MathService и HistoryService (они producers).

Инструкция по запуску:
- Запустить docker-compose файл для установки БД и Kafka.
- По очереди запустить сервисы: EurekaServer, MathService, HistoryService, ExamService, Notificator.

[Ссылка на видео с демострацией работы](https://disk.yandex.ru/i/QuLN3ESi0wfGRg)