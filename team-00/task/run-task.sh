#!/usr/bin/env bash
cf run-task task-app 'source <(jq -r ."start_command" /home/vcap/staging_info.yml)' --name "$(date "+%Y-%m-%dT%H:%M:%S")"
