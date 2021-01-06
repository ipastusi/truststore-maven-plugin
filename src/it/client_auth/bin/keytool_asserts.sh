#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: jks"
  "Your keystore contains 1 entry"
  "a1:71:fd:71:d0:a8:84:6d"
)

RESULT=$(keytool -list -keystore target/truststore.jks -storepass changeit)

for ENTRY in "${EXPECTED_OUTPUT[@]}"; do
  FOUND=$(echo "${RESULT}" | grep -q "${ENTRY}" && echo 1 || echo 0)

  if [ "${FOUND}" != "1" ]; then
    echo "Expected output '${ENTRY}' not found"
    exit 1
  else
    echo "Expected output '${ENTRY}' found"
  fi
done
