#!/usr/bin/env bash

EXPECTED_OUTPUT=(
  "Keystore type: BCFKS"
  "89:3c:56:46:8f:7b:4c:24:f8:03:17:d2:70:05:6a:5b"
  "7d:5b:51:26:b4:76:ba:11:db:74:16:0b:bc:53:0d:a7"
  "01:fd:6d:30:fc:a3:ca:51:a8:1b:bc:64:0e:35:03:2d"
  "04:e1:e7:a4:dc:5c:f2:f3:6d:c0:2b:42:b8:5d:15:9f"
  "05:57:c8:0b:28:26:83:a1:7b:0a:11:44:93:29:6b:79"
)

RESULT=$(keytool -list -keystore target/property_configured_truststore.bcsfks -storepass topsecret -storetype bcfks -providerpath ../../../bin/bcprov-jdk15to18-1.70.jar -provider org.bouncycastle.jce.provider.BouncyCastleProvider)

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
