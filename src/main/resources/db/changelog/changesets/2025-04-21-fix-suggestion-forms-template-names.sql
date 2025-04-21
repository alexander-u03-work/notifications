update notification_schema.notification
set name = 'SuggestionImprovementRequestCreateNotification'
where id in (17, 18);
update notification_schema.notification
set name = 'SuggestNewShopRequestCreateNotification'
where id in (19, 20);

update notification_schema.template
set name = 'SuggestionImprovementRequestCreateNotification'
where id = 10;
update notification_schema.template
set name = 'SuggestNewShopRequestCreateNotification'
where id = 11;

