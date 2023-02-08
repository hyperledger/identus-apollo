#!/usr/bin/env bash
set -e

cp xconfigure.sh secp256k1

cd secp256k1

./autogen.sh
sh xconfigure.sh --enable-experimental --enable-module_ecdh --enable-module-recovery --enable-module-schnorrsig --enable-benchmark=no --enable-shared=no --enable-exhaustive-tests=no --enable-tests=no

mkdir -p ../build/ios/arm64-sim
cp -v _build/platforms/arm64-sim/lib/libsecp256k1.a ../build/ios/arm64-sim

mkdir -p ../build/ios/arm64-iphoneos
cp -v _build/platforms/arm64-iphoneos/lib/libsecp256k1.a ../build/ios/arm64-iphoneos

mkdir -p ../build/ios/x86_64-sim
cp -v _build/platforms/x86_64-sim/lib/libsecp256k1.a ../build/ios/x86_64-sim

mkdir -p ../build/ios/real
cp -v _build/universal/arm64-iphoneos.a ../build/ios/real

mkdir -p ../build/ios/sim
cp -v _build/universal/arm64_x86_x64-iphonesimulator.a ../build/ios/sim

rm -rf _build
make clean
