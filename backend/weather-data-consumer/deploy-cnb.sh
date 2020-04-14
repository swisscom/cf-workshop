#!/usr/bin/env bash
./gradlew bootBuildImage
LATEST_TAG=$(docker images robachmann/weather-data-consumer --format "{{.Tag}}" | sort -r | head -n 1)
docker push robachmann/weather-data-consumer:$LATEST_TAG
sed -i "s/weather-data-consumer:.*/weather-data-consumer:$LATEST_TAG/g" manifest-cnb.yml
cf target -s backend
cf push -f manifest-cnb.yml
