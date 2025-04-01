# 📝 Task Management System – Backend API

**Task Management System** — это RESTful API, позволяющее создавать, редактировать, удалять и просматривать задачи с гибкой системой ролей и авторизацией через JWT.

---

## 🚀 Возможности

- 🔐 Аутентификация и авторизация пользователей по **email** и **паролю**
- 🛡️ Защита всех endpoint'ов с помощью **JWT токена**
- 👤 Роли: `ADMIN` и `USER`
- 📋 Работа с задачами:
    - Заголовок, описание
    - Статус: `PENDING`, `IN_PROGRESS`, `COMPLETED`
    - Приоритет: `HIGH`, `MEDIUM`, `LOW`
    - Автор и исполнитель
    - Комментарии от исполнителей и администраторов
- ⚙️ ADMIN может:
    - Создавать, редактировать и удалять задачи
    - Менять статус и приоритет
    - Назначать исполнителей
    - Оставлять комментарии
- 👥 USER может:
    - Видеть задачи, в которых он является исполнителем
    - Изменять статус
    - Оставлять комментарии
- 🔍 Фильтрация и пагинация задач по статусу и приоритету
- 🧾 Получение всех комментариев к задаче
- ⚠️ Глобальный обработчик исключений
- 🧪 Покрытие **юнит-тестами** с использованием JUnit 5 и Mockito
- 📖 Интерактивная Swagger-документация

---

## 🛠️ Стек технологий

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JUnit 5 + Mockito
- Swagger / OpenAPI

---

## 🧑‍💻 Локальный запуск проекта

### 1. 📥 Клонирование репозитория

```bash
git clone https://github.com/DenisKalimulin/TaskManagementSystem.git
cd TaskManagementSystem
```
### 2. 🛢️ Подключение PostgreSQL
Убедитесь, что PostgreSQL установлен и запущен локально.
Создайте базу данных:
```bash
CREATE DATABASE task_management_system_db;
```
Используйте пользователя postgres и пароль postgres или настройте своё.

### 3. 📝 Настройка application.yml

Перейдите в src/main/resources/application.yml и убедитесь, что настройки БД указаны так:
```bash
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/task_management_system_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
```
### 4. 🧬 Инициализация базы данных
Приложение при запуске выполнит SQL-скрипты из папки resources (schema.sql, data.sql) для:
- создания таблиц
- наполнения начальными данными