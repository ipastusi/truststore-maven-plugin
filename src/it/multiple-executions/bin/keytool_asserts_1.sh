#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: PKCS12"
  "Your keystore contains 2 entries"
  "89:3c:56:46:8f:7b:4c:24:f8:03:17:d2:70:05:6a:5b"
)

RESULT=$(keytool -list -keystore target/truststore-1.p12 -storepass changeit)

for ENTRY in "${EXPECTED_OUTPUT[@]}"; do
  FOUND=$(echo "${RESULT}" | grep -q "${ENTRY}" && echo 1 || echo 0)

  if [ "${FOUND}" != "1" ]; then
    echo "Expected output '${ENTRY}' not found"
    exit 1
  else
    echo "Expected output '${ENTRY}' found"
  fi
done
