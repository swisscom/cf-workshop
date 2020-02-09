#!/usr/bin/env bash
cf create-service task-scheduler free task-scheduler
cf bind-service task-app task-scheduler -c '{"schedule":"*/15 * * * *", "task":"source <(jq -r .\"start_command\" /home/vcap/staging_info.yml)"}'
