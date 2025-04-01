echo "Host: $host"
echo "Environment: $ENVIRONMENT"
echo "Notifications version: $NOTIFICATIONS_BUILD_VERSION"
echo "Verbose level: $VERBOSE_LEVEL"

ansible-playbook deploy/deploy-app.yml \
-i deploy/inventory/$ENVIRONMENT/hosts \
-$VERBOSE_LEVEL \
-e "raggies_host=$host \
env_layer=$ENVIRONMENT \
notifications_version=$NOTIFICATIONS_BUILD_VERSION" \
-e "@deploy/vault/$ENVIRONMENT.yml" \
--vault-password-file=.vault_pass
