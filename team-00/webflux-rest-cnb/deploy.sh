#!/usr/bin/env bash
./gradlew bootBuildImage
LATEST_TAG=$(docker images robachmann/webflux-rest-cnb  --format "{{.Tag}}" | sort -r | head -n 1)
docker push robachmann/webflux-rest-cnb:$LATEST_TAG
sed -i "s/webflux-rest-cnb:.*/webflux-rest-cnb:$LATEST_TAG/g" manifest.yml
cf target -s team-00
cf push -f manifest.yml
