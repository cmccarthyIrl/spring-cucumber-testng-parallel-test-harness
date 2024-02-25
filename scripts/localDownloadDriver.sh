#!/bin/bash

set -x
export SCRIPT_DIR=../common/src/main/resources
cd $SCRIPT_DIR

ROOT_DIR=$(pwd)
TEST_RESOURCES=$ROOT_DIR
FILE_EXTENSION=""

case "$OSTYPE" in
solaris*) echo "$OSTYPE not supported" ;;
darwin*) OS='mac64' ;;
linux*) OS='linux64' ;;
bsd*) echo "$OSTYPE not supported" ;;
msys*) OS='win32' FILE_EXTENSION=".exe" ;;
*) echo "unknown $OSTYPE" ;;
esac

if [ ! -f "$TEST_RESOURCES/drivers/chromedriver"$FILE_EXTENSION ]; then
  cd "$TEST_RESOURCES/drivers" &&
    mkdir "temp" &&
    cd "temp" &&
    curl -L -k --output driver.zip https://www.nuget.org/api/v2/package/Selenium.WebDriver.ChromeDriver/ --ssl-no-revoke &&
    unzip driver.zip &&
    cd driver/$OS &&
    cp "chromedriver$FILE_EXTENSION" "$TEST_RESOURCES/drivers" &&
    chmod +700 "$TEST_RESOURCES/drivers/chromedriver"$FILE_EXTENSION &&
    cd ../../../ &&
    rm -rf temp &&
    cd $ROOT_DIR
fi

if [ ! -f "$TEST_RESOURCES/drivers/geckodriver"$FILE_EXTENSION ]; then
  cd "$TEST_RESOURCES/drivers" &&
    mkdir "temp" &&
    cd "temp" &&
    curl -L -k --output driver.zip https://www.nuget.org/api/v2/package/Selenium.WebDriver.GeckoDriver/ --ssl-no-revoke &&
    unzip driver.zip &&
    cd driver/$OS &&
    cp "geckodriver$FILE_EXTENSION" "$TEST_RESOURCES/drivers" &&
    chmod +700 "$TEST_RESOURCES/drivers/geckodriver"$FILE_EXTENSION &&
    cd ../../../ &&
    rm -rf temp &&
    cd $ROOT_DIR
fi

#uncomment to keep bash open
#! /bin/bash
