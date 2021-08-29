#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: PKCS12"
)

RESULT=$(keytool -list -keystore target/truststore.p12 -storepass topsecret)

for ENTRY in "${EXPECTED_OUTPUT[@]}"; do
  FOUND=$(echo "${RESULT}" | grep -q "${ENTRY}" && echo 1 || echo 0)

  if [ "${FOUND}" != "1" ]; then
    echo "Expected output '${ENTRY}' not found"
    exit 1
  else
    echo "Expected output '${ENTRY}' found"
  fi
done

if [[ "${RESULT}" =~ .*Your\ keystore\ contains\ [0-9]{2,3}\ entries.* ]]; then
  echo "Expected output regex matched"
else
  echo "Expected output regex not matched"
  exit 1
fi
