#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: BCFKS"
  "Your keystore contains 7 entries"
  "06:7f:94:4a:2a:27:cd:f3:fa:c2:ae:2b:01:f9:08:ee:b9:c4:c6"
  "06:7f:94:57:85:87:e8:ac:77:de:b2:53:32:5b:bc:99:8b:56:0d"
  "08:13:34:34:48:07:64:27:4d:bc:cb:14:4d:af:f2:11"
  "a7:0e:4a:4c:34:82:b7:7f"
  "04:e1:e7:a4:dc:5c:f2:f3:6d:c0:2b:42:b8:5d:15:9f"
  "05:57:c8:0b:28:26:83:a1:7b:0a:11:44:93:29:6b:79"
)

RESULT=$(keytool -list -keystore target/truststore.bcfks -storetype bcfks -storepass topsecret -providerpath ../../../bin/bcprov-jdk15to18-1.70.jar -provider org.bouncycastle.jce.provider.BouncyCastleProvider)

for ENTRY in "${EXPECTED_OUTPUT[@]}"; do
  FOUND=$(echo "${RESULT}" | grep -q "${ENTRY}" && echo 1 || echo 0)

  if [ "${FOUND}" != "1" ]; then
    echo "Expected output '${ENTRY}' not found"
    exit 1
  else
    echo "Expected output '${ENTRY}' found"
  fi
done
