#!/usr/bin/env bash

set -e

[[ -z "$VAULT_APP_TOKEN" ]] && { echo "Set VAULT_APP_TOKEN"; exit 1; }

./gradlew clean build
docker-compose -f docker-compose.services.yml up -d --build --scale service=5