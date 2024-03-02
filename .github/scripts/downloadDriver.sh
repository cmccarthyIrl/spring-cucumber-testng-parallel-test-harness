#!/bin/bash

sudo apt-get install xvfb

sudo apt-get install unzip

ROOT_DIR=$(pwd)
mkdir "temp" &&
cd "temp" &&
curl -L -k --output driver.zip https://www.nuget.org/api/v2/package/Selenium.WebDriver.ChromeDriver/ --ssl-no-revoke &&
unzip driver.zip &&
cd driver/linux64 &&
mv "chromedriver" "$ROOT_DIR/wikipedia/src/test/resources/drivers" &&
chown root:root "$ROOT_DIR/wikipedia/src/test/resources/drivers/chromedriver" &&
chmod +x "$ROOT_DIR/wikipedia/src/test/resources/drivers/chromedriver"
cd ../../../ &&
rm -rf temp