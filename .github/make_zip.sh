#!/bin/bash

mkdir output
cp app/build/outputs/apk/release/AOSPMods-signed.apk MagiskMod/system/priv-app/AOSPMods/AOSPMods.apk
cd MagiskMod;
zip -r ../output/AOSPMods.zip *;