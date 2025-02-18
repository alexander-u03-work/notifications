delete from notification_schema.notification;

delete from notification_schema.template;

insert into notification_schema.notification values
     (1, 1, 'OrderCreateNotification', false),
     (2, 2, 'OrderCreateNotification', true),
     (3, 1, 'SystemNotification', false),
     (4, 2, 'SystemNotification', false),
     (5, 1, 'ProductRevisionNotification', false),
     (6, 2, 'ProductRevisionNotification', true),
     (7, 1, 'ProductCreateNotification', false),
     (8, 2, 'ProductCreateNotification', true),
     (9, 1, 'ProductAcceptNotification', false),
     (10, 2, 'ProductAcceptNotification', true),
     (11, 1, 'UserRevisionNotification', false),
     (12, 2, 'UserRevisionNotification', true),
     (13, 1, 'UserCreateNotification', false),
     (14, 2, 'UserCreateNotification', true),
     (15, 1, 'UserAcceptNotification', false),
     (16, 2, 'UserAcceptNotification', true);

insert into notification_schema.template values
(1, 'OrderCreateNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Информация о заказе</title>
        </head>
        <body>
        <div>Заказ <b>${orderId}</b> <br>
        <div>Был принят в работу<br>
        <div>Ожидаемое время доставки <b>${deliveryDate}</b> <br>
        <div>Способ оплаты <b>${payment}</b> <br>
        </body>
        </html>', 'Created'),
(2, 'SystemNotification', '<html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Информация о заказе</title>
        </head>
        <body>
        <div>Акция <b>${promotionName}</b> <br>
        <div>${promotionText}<br>
        </body>
        </html>', 'Created'),
(3, 'ChangeStatusOrderNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Информация о заказе</title>
        </head>
        <body>
        <div>Заказ <b>${orderId}</b> <br>
        <div>Поменял статус <b>${orderStatus}</b><br>
        </body>
        </html>', 'Created'),
(4, 'ProductRevisionNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Модерация товара</title>
        </head>
        <body>
        <div>Проблема с карточкой товара <b>${productId}</b> <b>${productTitle}</b><br>
        <div><b>${issueType}</b><br>
        <div>Ссылка на товар: <b>${productUrl}</b><br>
        <div>Комментарий модератора: <b>${comment}</b><br>
        </body>
        </html>', 'Created'),
(5, 'ProductCreateNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Модерация товара</title>
        </head>
        <body>
        <div>Товар <b>${productId}</b> <b>${productTitle}</b> ожидает своей очереди на проверку<br>
        <div>Ссылка на товар: <b>${productUrl}</b><br>
        </body>
        </html>', 'Created'),
(6, 'ProductAcceptNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Модерация товара</title>
        </head>
        <body>
        <div>Товар <b>${productId}</b> <b>${productTitle}</b> успешно прошёл проверку<br>
        <div>Ссылка на товар: <b>${productUrl}</b><br>
        </body>
        </html>', 'Created'),
(7, 'UserRevisionNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Модерация аккаунта</title>
        </head>
        <body>
        <div>Проблема с заполнением полей информации о пользователе <b>${userId}</b><br>
        <div><b>${issueType}</b><br>
        <div>Комментарий модератора: <b>${comment}</b><br>
        </body>
        </html>', 'Created'),
(8, 'UserCreateNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Модерация аккаунта</title>
        </head>
        <body>
        <div>Пользователь <b>${userId}</b> изменил свои данные и, возможно, ожидает своей очереди на проверку<br>
        </body>
        </html>', 'Created'),
(9, 'UserAcceptNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Модерация аккаунта</title>
        </head>
        <body>
        <div>Ваш аккаунт <b>${userId}</b> успешно прошёл проверку<br>
        </body>
        </html>', 'Created');