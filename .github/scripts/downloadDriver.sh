#!/bin/bash

sudo apt-get install xvfb

sudo apt-get install unzip

#CHROME_DRIVER_VERSION=`curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE`
#
#wget -N http://chromedriver.storage.googleapis.com/${CHROME_DRIVER_VERSION}/chromedriver_linux64.zip
#unzip chromedriver_linux64.zip
#chmod +x chromedriver
#sudo mv -f chromedriver /home/runner/work/spring-cucumber-testng-parallel-test-harness/wikipedia/src/test/resources/drivers

TEST_RESOURCES=/home/runner/work/spring-cucumber-testng-parallel-test-harness/wikipedia/src/test/resources/drivers

mkdir "temp" &&
cd "temp" &&
curl -L -k --output driver.zip https://www.nuget.org/api/v2/package/Selenium.WebDriver.ChromeDriver/ --ssl-no-revoke &&
unzip driver.zip &&
cd driver/linux64 &&
cp "chromedriver" "$TEST_RESOURCES" &&
chmod +700 "$TEST_RESOURCES/chromedriver" &&
cd ../../../ &&
rm -rf temp

