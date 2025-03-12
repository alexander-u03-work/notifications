echo "Host: $host"
echo "Environment: $env_layer"
echo "Notifications version: $notifications_version"
echo "Verbose level: $verbose_level"

ansible-playbook deploy/deploy-app.yml \
-i deploy/inventory/$env_layer/hosts \
-$verbose_level \
-e "raggies_host=$host \
env_layer=$env_layer \
notifications_version=$notifications_version"
