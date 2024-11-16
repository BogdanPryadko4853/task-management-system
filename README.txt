Task Management System
Этот проект представляет собой систему управления задачами, разработанную с использованием Spring Boot. Он позволяет создавать, обновлять, удалять и просматривать задачи, а также управлять пользователями и комментариями к задачам.

Технологии
Spring Boot: Фреймворк для создания приложений на Java.

Spring Security: Для аутентификации и авторизации.

JWT: Для генерации и проверки токенов.

PostgreSQL: База данных для хранения данных.

Docker: Для контейнеризации приложения и базы данных.

Swagger: Для документирования API.

Структура проекта
src/main/java/com/example/taskmanagementsystem: Основной код приложения.

controller: Контроллеры для обработки HTTP запросов.

model: Модели данных.

repository: Репозитории для доступа к данным.

service: Сервисы для бизнес-логики.

security: Конфигурация безопасности и JWT.

src/main/resources: Ресурсы приложения.

application.yml: Основные настройки приложения.

application-dev.yml: Настройки для разработки.

application-prod.yml: Настройки для продакшена.

docker-compose.yml: Файл для запуска приложения и базы данных в Docker.

Установка и запуск
Предварительные требования
Java 17

Maven

Docker

Docker Compose

Шаги для запуска
Клонирование репозитория

sh
Copy
git clone https://github.com/yourusername/taskmanagementsystem.git
cd taskmanagementsystem
Сборка проекта

Убедитесь, что у вас установлен Maven, и выполните следующую команду для сборки проекта:

sh
Copy
mvn clean install
Запуск с использованием Docker Compose

Для запуска приложения и базы данных в Docker, используйте следующую команду:

sh
Copy
docker-compose up --build
Это создаст и запустит контейнеры для приложения и базы данных. Приложение будет доступно по адресу http://localhost:8080.

Доступ к Swagger UI

После запуска приложения, вы можете просмотреть документацию API и протестировать его через Swagger UI по адресу:

Copy
http://localhost:8080/swagger-ui.html
Настройка профиля
Приложение поддерживает два профиля: dev и prod. По умолчанию используется профиль dev. Вы можете изменить активный профиль, установив переменную окружения SPRING_PROFILES_ACTIVE:

sh
Copy
export SPRING_PROFILES_ACTIVE=prod
Запуск вручную
Если вы хотите запустить приложение вручную, выполните следующую команду:

sh
Copy
java -jar target/taskmanagementsystem-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
Доступ к базе данных
База данных PostgreSQL будет доступна на localhost:5432. Учетные данные для доступа к базе данных:

URL: jdbc:postgresql://localhost:5432/task_management_dev

Username: postgres

Password: postgres

API Endpoints
Аутентификация
POST /api/auth/login - Аутентификация пользователя и генерация JWT токена.

POST /api/auth/register - Регистрация нового пользователя.

Задачи
POST /api/tasks/create - Создание новой задачи (только для администраторов).

GET /api/tasks/{id} - Получение задачи по ID.

GET /api/tasks/all - Получение всех задач (только для администраторов).

PUT /api/tasks/update/{id} - Обновление задачи по ID.

DELETE /api/tasks/delete/{id} - Удаление задачи по ID (только для администраторов).

GET /api/tasks/author/{authorId} - Получение задач по ID автора (только для администраторов).

GET /api/tasks/assignee/{assigneeId} - Получение задач по ID исполнителя.

Комментарии
POST /api/comments - Создание нового комментария.

GET /api/comments/task/{taskId} - Получение комментариев по ID задачи.

Пользователи
POST /api/users - Создание нового пользователя.