<h1 align="center">Task Management System</h1>
<p align="center">
  Система управления задачами, разработанная на Spring Boot с использованием JWT для аутентификации.
</p>

<br>

## Обзор

Этот проект представляет собой систему управления задачами, разработанную с использованием Spring Boot. Она позволяет пользователям создавать, обновлять, удалять и просматривать задачи, а также управлять пользователями и комментариями к задачам. Система обеспечивает безопасность доступа с помощью JWT и поддерживает разграничение ролей (администратор/обычный пользователь).

<br>

## Технологии

*   **Spring Boot**: Фреймворк для создания Java-приложений.
*   **Spring Security**: Для аутентификации и авторизации.
*   **JWT (JSON Web Tokens)**: Для генерации и проверки токенов доступа.
*   **PostgreSQL**: База данных для хранения данных.
*   **Docker**: Для контейнеризации приложения и базы данных.
*   **Swagger**: Для документирования API.

<br>

## Структура Проекта


*   `controller`: Контроллеры для обработки HTTP-запросов.
*   `model`: Модели данных (сущности).
*   `repository`: Репозитории для доступа к данным.
*   `service`: Сервисы, содержащие бизнес-логику приложения.
*   `security`: Конфигурация безопасности, включая настройки JWT.
*   `application.yml`: Основные настройки приложения.
*   `application-dev.yml`: Настройки для профиля разработки.
*   `application-prod.yml`: Настройки для профиля продакшена.
*   `docker-compose.yml`: Файл для запуска приложения и базы данных в Docker.

<br>

## Установка и Запуск

### Предварительные требования

1.  Java 17 (или выше).
2.  Maven.
3.  Docker.
4.  Docker Compose.

### Шаги для запуска

1.  **Клонирование репозитория:**

    ```bash
    git clone <ссылка на ваш репозиторий>
    cd taskmanagementsystem
    ```

2.  **Сборка проекта:**

    ```bash
    mvn clean install
    ```

3.  **Запуск с использованием Docker Compose:**

    ```bash
    docker-compose up --build
    ```

    Это создаст и запустит контейнеры для приложения и базы данных. Приложение будет доступно по адресу `http://localhost:8080`.

4.  **Запуск вручную (альтернативный вариант):**
     Если вы хотите запустить приложение вручную:
       ```bash
        java -jar target/taskmanagementsystem-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
       ```
        Этот вариант потребует, чтобы у вас была запущена база данных PostgreSQL (см. ниже).

<br>

### Доступ к Swagger UI

После запуска приложения, вы можете просмотреть документацию API и протестировать его через Swagger UI по адресу:

<br>

### Настройка Профиля

Приложение поддерживает профили `dev` и `prod`. По умолчанию используется профиль `dev`. Для изменения активного профиля:
* **Через переменную окружения**
     ```bash
        export SPRING_PROFILES_ACTIVE=prod
     ```
   *  **При запуске вручную**
     ```bash
        java -jar target/taskmanagementsystem-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
      ```

<br>

### Доступ к Базе Данных

База данных PostgreSQL будет доступна на `localhost:5432`. Учетные данные для доступа к базе данных:

*   **URL:** `jdbc:postgresql://localhost:5432/task_management_dev`
*   **Username:** `postgres`
*   **Password:** `postgres`

*Примечание: Вы можете изменить учетные данные в файлах `application.yml`, `application-dev.yml` или `application-prod.yml`*

<br>

## API Endpoints

### Аутентификация

*   `POST /api/auth/login`: Аутентификация пользователя и генерация JWT токена.
*   `POST /api/auth/register`: Регистрация нового пользователя.

### Задачи

*   `POST /api/tasks/create`: Создание новой задачи (только для администраторов).
*   `GET /api/tasks/{id}`: Получение задачи по ID.
*   `GET /api/tasks/all`: Получение всех задач (только для администраторов).
*   `PUT /api/tasks/update/{id}`: Обновление задачи по ID.
*   `DELETE /api/tasks/delete/{id}`: Удаление задачи по ID (только для администраторов).
*   `GET /api/tasks/author/{authorId}`: Получение задач по ID автора (только для администраторов).
*   `GET /api/tasks/assignee/{assigneeId}`: Получение задач по ID исполнителя.

### Комментарии

*   `POST /api/comments`: Создание нового комментария.
*   `GET /api/comments/task/{taskId}`: Получение комментариев по ID задачи.

### Пользователи

*   `POST /api/users`: Создание нового пользователя. *Примечание: Этот эндпоинт обычно используется для создания администраторов, рекомендуется использовать `POST /api/auth/register` для создания обычных пользователей*.

<br>