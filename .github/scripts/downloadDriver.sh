#!/bin/bash

sudo apt-get install xvfb

sudo apt-get install unzip

cd "$(pwd)$1" &&

ROOT_DIR=$(pwd)
mkdir "temp" &&
cd "temp" &&
curl -L -k --output driver.zip https://www.nuget.org/api/v2/package/Selenium.WebDriver.ChromeDriver/ --ssl-no-revoke &&
unzip driver.zip &&
cd driver/linux64 &&
ls &&
chmod +x chromedriver &&
sudo mv -f chromedriver "$ROOT_DIR" &&
cd ../../../ &&
rm -rf temp