#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: JKS"
  "Your keystore contains 2 entries"
)

RESULT=$(keytool -list -keystore target/truststore.jks -storepass topsecret)

for ENTRY in "${EXPECTED_OUTPUT[@]}"; do
  FOUND=$(echo "${RESULT}" | grep -iq "${ENTRY}" && echo 1 || echo 0)

  if [ "${FOUND}" != "1" ]; then
    echo "Expected output '${ENTRY}' not found"
    exit 1
  else
    echo "Expected output '${ENTRY}' found"
  fi
done
