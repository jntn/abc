#!/bin/bash

# Install npm dependencies
yarn

# We need wget
yum install -y wget

# Let's get java
wget -q https://d2znqt9b1bc64u.cloudfront.net/amazon-corretto-8.202.08.2-linux-x64.tar.gz
tar -xzf amazon-corretto-8.202.08.2-linux-x64.tar.gz

export JAVA_HOME=$PWD/amazon-corretto-8.202.08.2-linux-x64
export PATH=$JAVA_HOME/bin:$PATH

# Build
yarn release