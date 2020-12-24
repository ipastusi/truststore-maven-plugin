#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: PKCS12"
  "Your keystore contains 10 entries"
  "89:3c:56:46:8f:7b:4c:24:f8:03:17:d2:70:05:6a:5b"
  "7d:5b:51:26:b4:76:ba:11:db:74:16:0b:bc:53:0d:a7"
  "01:fd:6d:30:fc:a3:ca:51:a8:1b:bc:64:0e:35:03:2d"
  "04:e1:e7:a4:dc:5c:f2:f3:6d:c0:2b:42:b8:5d:15:9f"
  "05:57:c8:0b:28:26:83:a1:7b:0a:11:44:93:29:6b:79"
)

for entry in "${EXPECTED_OUTPUT[@]}"; do
  result=$(keytool -list -keystore target/property_configured_truststore.p12 -storepass topsecret | grep -q "${entry}" && echo 1 || echo 0)
  if [ "${result}" != "1" ]; then
    echo "Expected output '${entry}' not found"
    exit 1
  else
    echo "Expected output '${entry}' found"
  fi
done
