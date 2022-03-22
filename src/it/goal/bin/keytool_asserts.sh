#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: PKCS12"
  "Your keystore contains 3 entries"
  "89:3c:56:46:8f:7b:4c:24:f8:03:17:d2:70:05:6a:5b"
  "7d:5b:51:26:b4:76:ba:11:db:74:16:0b:bc:53:0d:a7"
  "01:fd:6d:30:fc:a3:ca:51:a8:1b:bc:64:0e:35:03:2d"
)

RESULT=$(keytool -list -keystore goal_truststore.p12 -storepass changeit)

for ENTRY in "${EXPECTED_OUTPUT[@]}"; do
  FOUND=$(echo "${RESULT}" | grep -iq "${ENTRY}" && echo 1 || echo 0)

  if [ "${FOUND}" != "1" ]; then
    echo "Expected output '${ENTRY}' not found"
    exit 1
  else
    echo "Expected output '${ENTRY}' found"
  fi
done
