#!/usr/bin/env bash

set -e

./gradlew clean build
VAULT_APP_TOKEN=$VAULT_APP_TOKEN docker-compose -f docker-compose.services.yml up