#!/bin/bash

sudo apt-get install xvfb

sudo apt-get install unzip

echo "pwd = $(pwd)"



ROOT_DIR=$(pwd)
mkdir "temp" &&
cd "temp" &&
curl -L -k --output driver.zip https://www.nuget.org/api/v2/package/Selenium.WebDriver.ChromeDriver/ --ssl-no-revoke &&
unzip driver.zip &&
cd driver/linux64 &&
mv "chromedriver" /usr/bin/chromedriver
chown root:root /usr/bin/chromedriver
chmod +x /usr/bin/chromedriver
cd ../../../ &&
rm -rf temp