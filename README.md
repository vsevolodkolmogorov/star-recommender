# Рекомендательная система для банка «Стар»

## Описание проекта

Проект представляет собой сервис для рекомендательной системы банка «Стар», цель которого — улучшить маркетинг и помочь сотрудникам предлагать персонализированные банковские продукты клиентам.

Рекомендательная система будет показывать пользователям персонализированные предложения. Этот микросервис ориентирован на одну задачу — предоставление рекомендаций для клиентов банка, основываясь на их действиях и интересах.

## Задача

Создать сервис, который будет рекомендовать пользователям новые банковские продукты. Сервис должен быть интегрирован с базой данных, содержащей информацию о пользователях банка, продуктах и операциях с ними.

### Данные:
1. Пользователи банка.
2. Продукты банка.
3. Операции пользователей по продуктам банка.

Для хранения данных используется встроенная файловая база данных **H2**.

## Архитектура

Сервис должен быть построен на **SpringBoot**, с использованием REST API для взаимодействия с внешними системами. Веб-сервис предоставляет следующий интерфейс:

### API

**GET** `/recommendation/{user_id}`

#### Response:

- **200 OK**

```json
{
    "user_id": <user_id>,
    "recommendations": [
        {
            "name": "<имя продукта>",
            "id": <id продукта>,
            "text": "текстовое описание продукта"
        },
        ...
    ]
}

```
В случае отсутствия рекомендаций для пользователя, поле "recommendations" будет пустым, но структура объекта останется неизменной.

### Развитие проекта

Текущая версия — это первая итерация разработки сервиса. В будущем система будет расширяться с добавлением новых возможностей и улучшений.
