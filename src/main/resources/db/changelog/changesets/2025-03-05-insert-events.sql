insert into notification_schema.notification values
    (17, 1, 'SuggestionImprovementEventCreateNotification', false),
    (18, 2, 'SuggestionImprovementEventCreateNotification', true),
    (19, 1, 'ShopRegisterEventCreateNotification', false),
    (20, 2, 'ShopRegisterEventCreateNotification', false);

insert into notification_schema.template values
(10, 'SuggestionImprovementEventCreateNotification', '<!doctype html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
            <meta http-equiv="X-UA-Compatible" content="ie=edge">
            <title>Предложение по улучшению качества сервиса</title>
        </head>
        <body>
            <div>Пользователь <b>${username}</b> предложил улучшить качество сервиса<br>
            <div>Предложение: <b>${suggestion}</b><br>
        </body>
        </html>', 'Created'),
(11, 'ShopRegisterEventCreateNotification', '<html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, user-scalable=no,initialscale=1.0, maximum-scale=1.0, minimum-scale=1.0">
            <meta http-equiv="X-UA-Compatible" content="ie=edge">
            <title>Предложение по добавлению нового магазина</title>
        </head>
        <body>
            <div>Пользователь <b>${username}</b> предложил добавить магазин <b>${shopName}</b><br>
            <div>Номер телефона: <b>${phoneNumber}</b><br>
            <div>${suggestion}<br>
        </body>
        </html>', 'Created');