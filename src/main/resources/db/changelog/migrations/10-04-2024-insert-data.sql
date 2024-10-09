insert into notification_schema.notification_type values
    (1, 'Email'),
    (2, 'Push');

insert into notification_schema.notification values
    (1, 1, 'OrderNotification', false),
    (2, 2, 'OrderNotification', true),
    (3, 1, 'SystemNotification', false),
    (4, 2, 'SystemNotification', false),
    (5, 1, 'SendForRevisionNotification', false),
    (6, 2, 'SendForRevisionNotification', true),
    (7, 1, 'SendForModerationNotification', false),
    (8, 2, 'SendForModerationNotification', true),
    (9, 1, 'ModerationSuccededNotification', false),
    (10, 2, 'ModerationSuccededNotification', true);

insert into notification_schema.template values
(1, 'OrderNotification', '<!doctype html>
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
        <div>Ожидаемое время доставки <b>${expectedDeliveryTime}</b> <br>
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
(4, 'SendForRevisionNotification', '<!doctype html>
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
(5, 'SendForModerationNotification', '<!doctype html>
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
(6, 'ModerationSuccededNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Модерация товара</title>
        </head>
        <body>
        <div>Товар <b>${productId}</b> <b>${productTitle}</b} успешно прошёл проверку<br>
        <div>Ссылка на товар: <b>${productUrl}</b><br>
        </body>
        </html>', 'Created');