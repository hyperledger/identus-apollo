#!/usr/bin/env bash
# Install Homebrew
echo "install Homebrew"
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
# Install autoconf, automake, libtool
echo "install autoconf, automake, libtool"
brew install autoconf automake libtool
