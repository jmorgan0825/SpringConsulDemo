#!/bin/bash
set -e

[ -z "${VAULT_ADDR}" ] && { echo "Must set VAULT_ADDR"; exit 1; }

cr=`echo '\n.'`
cr=${cr%.}
read -p "Running this script will initialize & unseal Vault,${cr}then put your unseal keys and root token into Consul KV.${cr}${cr}If you're sure you want to continue, type 'yes': `echo '\n> '`" ANSWER

if [ "$ANSWER" != "yes" ]; then
  echo
  echo "Exiting without initializing & unsealing Vault, no keys or tokens were stored."
  echo
  exit 1
fi

cget() { curl -sf "http://127.0.0.1:8500/v1/kv/service/vault/$1?raw"; }

if [ ! $(cget root-token) ]; then
  echo "Initialize Vault"
  vault init | tee /tmp/vault.init > /dev/null

  # Store master keys in consul for operator to retrieve and remove
  COUNTER=1
  cat /tmp/vault.init | grep '^Unseal' | awk '{print $4}' | for key in $(cat -); do
    curl -fX PUT 127.0.0.1:8500/v1/kv/service/vault/unseal-key-$COUNTER -d $key
    COUNTER=$((COUNTER + 1))
  done

  export ROOT_TOKEN=$(cat /tmp/vault.init | grep '^Initial' | awk '{print $4}')

  curl -fX PUT 127.0.0.1:8500/v1/kv/service/vault/root-token -d $ROOT_TOKEN

  echo "Unsealing Vault"
  vault unseal $(cget unseal-key-1) > /dev/null
  vault unseal $(cget unseal-key-2) > /dev/null
  vault unseal $(cget unseal-key-3) > /dev/null

  echo $ROOT_TOKEN | vault auth - > /dev/null

  export APP_TOKEN=$(vault token-create | grep '^token\s' | awk '{print $2}')

  echo "APP_TOKEN: $APP_TOKEN"
  curl -fX PUT 127.0.0.1:8500/v1/kv/service/vault/app-token -d $APP_TOKEN

  echo "Remove master keys from disk"
  rm /tmp/vault.init
  echo "Vault setup complete."
else
  echo "Vault has already been initialized, skipping."
fi
