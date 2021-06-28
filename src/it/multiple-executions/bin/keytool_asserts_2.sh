#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: JKS"
  "Your keystore contains 2 entries"
  "7d:5b:51:26:b4:76:ba:11:db:74:16:0b:bc:53:0d:a7"
)

RESULT=$(keytool -list -keystore target/truststore-2.jks -storepass topsecret)

for ENTRY in "${EXPECTED_OUTPUT[@]}"; do
  FOUND=$(echo "${RESULT}" | grep -q "${ENTRY}" && echo 1 || echo 0)

  if [ "${FOUND}" != "1" ]; then
    echo "Expected output '${ENTRY}' not found"
    exit 1
  else
    echo "Expected output '${ENTRY}' found"
  fi
done
