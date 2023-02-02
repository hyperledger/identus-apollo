# Secp256k1

The bitcoin C lib for generating key pairs using Secp256k1 curve. This is only being used for iOS at the time being but will add support for other target like Linux in the future if/when needed.

## Build Steps

For macOS, you need to have the following installed:

1. brew
2. autoconf
3. automake
4. libtool

For brew,
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

For autoconf & automake, libtool,
```bash
brew install autoconf automake libtool
```

## TODO

* Have the installation of brew, autoconf, automake and libtool through Gradle script to make it more developer friendly.
