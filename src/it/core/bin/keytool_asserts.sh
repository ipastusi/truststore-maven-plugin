#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: PKCS12"
  "Your keystore contains 14 entries"
  "89:3c:56:46:8f:7b:4c:24:f8:03:17:d2:70:05:6a:5b"
  "7d:5b:51:26:b4:76:ba:11:db:74:16:0b:bc:53:0d:a7"
  "01:fd:6d:30:fc:a3:ca:51:a8:1b:bc:64:0e:35:03:2d"
  "08:13:34:34:48:07:64:27:4d:bc:cb:14:4d:af:f2:11"
  "06:7f:94:57:85:87:e8:ac:77:de:b2:53:32:5b:bc:99:8b:56:0d"
  "06:7f:94:4a:2a:27:cd:f3:fa:c2:ae:2b:01:f9:08:ee:b9:c4:c6"
  "a7:0e:4a:4c:34:82:b7:7f"
  "04:e1:e7:a4:dc:5c:f2:f3:6d:c0:2b:42:b8:5d:15:9f"
  "05:57:c8:0b:28:26:83:a1:7b:0a:11:44:93:29:6b:79"
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
