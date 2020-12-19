#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: PKCS12"
  "Your keystore contains 8 entries"
  "89:3c:56:46:8f:7b:4c:24:f8:03:17:d2:70:05:6a:5b"
  "7d:5b:51:26:b4:76:ba:11:db:74:16:0b:bc:53:0d:a7"
  "1f:d6:d3:0f:ca:3c:a5:1a:81:bb:c6:40:e3:50:32d"
)

for entry in "${EXPECTED_OUTPUT[@]}"; do
  result=$(keytool -list -keystore target/truststore.p12 -storepass topsecret | grep -q "${entry}" && echo 1 || echo 0)
  if [ "${result}" != "1" ]; then
    echo "Expected output '${entry}' not found"
    exit 1
  else
    echo "Expected output '${entry}' found"
  fi
done
