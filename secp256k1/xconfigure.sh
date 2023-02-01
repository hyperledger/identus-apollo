#!/usr/bin/env bash

#
# Build for iOS 64bit-ARM variants and iOS Simulator
# - Place the script at project root
# - Customize MIN_IOS_VERSION and other flags as needed
#
# Test Environment
# - macOS 10.14.6
# - iOS 13.1
# - Xcode 11.1
#

Build() {
    # Ensure -fembed-bitcode builds, as workaround for libtool macOS bug
    export MACOSX_DEPLOYMENT_TARGET="12.0"
    # Get the correct toolchain for target platforms
    export CC=$(xcrun --find --sdk "${SDK}" clang)
    export CXX=$(xcrun --find --sdk "${SDK}" clang++)
    export CPP=$(xcrun --find --sdk "${SDK}" cpp)
    export CFLAGS="${HOST_FLAGS} ${OPT_FLAGS}"
    export CXXFLAGS="${HOST_FLAGS} ${OPT_FLAGS}"
    export LDFLAGS="${HOST_FLAGS}"

    EXEC_PREFIX="${PLATFORMS}/${PLATFORM}"
    ./configure \
        --host="${CHOST}" \
        --prefix="${PREFIX}" \
        --exec-prefix="${EXEC_PREFIX}" \
        --enable-static \
        --disable-shared \
        "$@"
        # Avoid Xcode loading dylibs even when staticlibs exist

    make clean
    mkdir -p "${PLATFORMS}" &> /dev/null
    make -j"${MAKE_JOBS}"
    make install
}

echo "Cross building with configure args $@"

# Locations
ScriptDir="$( cd "$( dirname "$0" )" && pwd )"
cd - &> /dev/null
PREFIX="${ScriptDir}"/_build
PLATFORMS="${PREFIX}"/platforms
UNIVERSAL="${PREFIX}"/universal

# Compiler options
OPT_FLAGS="-O3 -g3 -fembed-bitcode"
MAKE_JOBS=8
MIN_IOS_VERSION=13.0

# Build for platforms
SDK="iphoneos"
PLATFORM="arm64-iphoneos"
PLATFORM_IOS=${PLATFORM}
ARCH_FLAGS="-arch arm64"  # -arch armv7 -arch armv7s
HOST_FLAGS="${ARCH_FLAGS} -miphoneos-version-min=${MIN_IOS_VERSION} -isysroot $(xcrun --sdk ${SDK} --show-sdk-path)"
CHOST="arm-apple-darwin"
Build "$@"

SDK="iphonesimulator"
PLATFORM="x86_64-sim"
PLATFORM_SIM_X86=${PLATFORM}
ARCH_FLAGS="-arch x86_64"
HOST_FLAGS="${ARCH_FLAGS} -mios-simulator-version-min=${MIN_IOS_VERSION} -isysroot $(xcrun --sdk ${SDK} --show-sdk-path)"
CHOST="x86_64-apple-darwin"
Build "$@"

# Build for arm64-simulator platform
SDK="iphonesimulator"
PLATFORM="arm64-sim"
PLATFORM_SIM_ARM=${PLATFORM}
ARCH_FLAGS="-arch arm64"
HOST_FLAGS="${ARCH_FLAGS} -mios-simulator-version-min=${MIN_IOS_VERSION} -isysroot $(xcrun --sdk ${SDK} --show-sdk-path)"
CHOST="arm-apple-darwin"
Build "$@"

# Build for macosx platform
#SDK="macosx"
#PLATFORM="arm64"
#PLATFORM_SIM_ARM=${PLATFORM}
#ARCH_FLAGS="-arch arm64"
#HOST_FLAGS="${ARCH_FLAGS} -mios-simulator-version-min=${MIN_IOS_VERSION} -isysroot $(xcrun --sdk ${SDK} --show-sdk-path)"
#CHOST="arm-apple-darwin"
#Build "$@"

# Create universal binary
mkdir -p "${UNIVERSAL}" &> /dev/null
lipo -create -output "${UNIVERSAL}/arm64_x86_x64-iphonesimulator.a" "${PLATFORMS}/${PLATFORM_SIM_ARM}/lib/libsecp256k1.a" "${PLATFORMS}/${PLATFORM_SIM_X86}/lib/libsecp256k1.a"
lipo -create -output "${UNIVERSAL}/arm64-iphoneos.a" "${PLATFORMS}/${PLATFORM_IOS}/lib/libsecp256k1.a"
