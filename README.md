# raggies-notifications

## Стенд

Notification и Casdoor используют одну базу PostgreSQL `raggies`. Liquibase
создаёт схему Notification. На пустом томе образ PostgreSQL выполняет
`casdoor/2026-11-22-init_schema.sql` и создаёт `casdoor_schema` до запуска Casdoor.
При переходе с SQLite демо-пользователь создаётся заново. Подписки, привязанные
к его прежнему JWT `sub`, нужно создать для нового пользователя.

### Локальный запуск

1. Положите подготовленный файл инициализации в `casdoor/init_data.json`.
2. Укажите `initAdminPassword` в `casdoor/casdoor.env`. Для локальной справки
   учётные данные можно хранить в `casdoor/credentials.json`. Эти два файла и
   заполненный `casdoor/init_data.json` исключены из Git и сборки Docker.
3. Для запуска только Casdoor с PostgreSQL выполните `docker compose up -d casdoor`.
   Для полного стенда выполните `docker compose --profile app up -d --build`.
   Notification собирается на Java 21 и скачивает `raggies-common:1.0.0` из JitPack.

Если на существующем томе ещё нет `casdoor_schema`, один раз выполните
`docker compose up -d --wait postgres`, затем
`docker compose exec -T postgres psql -U postgres -d raggies -v ON_ERROR_STOP=1 < casdoor/init_schema.sql`.
На Render примените тот же SQL к общей базе перед первым запуском Casdoor.

При запуске Notification из IDE поднимите зависимости командой
`docker compose up -d postgres rabbitmq smtp casdoor`. По умолчанию Notification
использует issuer `http://localhost:8000`. При необходимости явно задайте URL ключей
`SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI=http://localhost:8000/.well-known/jwks`.

Casdoor доступен на `http://localhost:8000`, Notification на `http://localhost:8180`,
MailHog на `http://localhost:8025`. Casdoor загружает организацию `raggies-org`,
приложение Regis Notification и пользователя `demo`. Для проверки защищённого API
можно получить токен пользователя `demo` через `/api/login/oauth/access_token`
с `grant_type=password`, `client_id=raggies-notification-demo`, `client_secret`,
`username=demo` и `password`, затем передать `access_token` в Swagger через Authorize.
Демо-пользователь получает роль `ADMIN` в Notification. Его учётные данные следует
передавать только тем, кто проверяет стенд.

### GitHub Actions

Workflow **Notification CI** по пушу в  `master` запускает образ с
PostgreSQL, RabbitMQ и MailHog, проверяет HTTP-ответ и прогоняет регрессию
в Docker. Отчёт Gradle доступен в артефакте `notification-regression`.
После успешного прогона на основной ветке образ с тегом SHA публикуется в GHCR.

Для публикации статической страницы выберите **GitHub Actions** в настройках
Pages, задайте Actions-переменную `PAGES_ENABLED=true` и запустите workflow
**Project page**. Если задана переменная `RENDER_PUBLIC_URL`, на странице
появится ссылка на Swagger.

Для автоматического деплоя создайте на Render сервис из образа
`ghcr.io/<владелец>/<репозиторий>:<SHA>` и настройте доступ Render к образу.
Укажите подключения к PostgreSQL, RabbitMQ, SMTP и Casdoor, затем добавьте
в GitHub Actions секрет `RENDER_DEPLOY_HOOK_URL` и переменную `RENDER_PUBLIC_URL`.
CI передаёт Render тег проверенного образа. Деплой считается подтверждённым,
когда публичный `/actuator/info` возвращает SHA этого коммита.
