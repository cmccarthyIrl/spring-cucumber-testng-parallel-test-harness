#!/bin/bash

sudo apt-get install xvfb

sudo apt-get install unzip

mkdir "temp" &&
cd "temp" &&
curl -L -k --output driver.zip https://www.nuget.org/api/v2/package/Selenium.WebDriver.ChromeDriver/ --ssl-no-revoke &&
unzip driver.zip &&
cd driver/linux64 &&
cp "chromedriver" "/home/runner/work/spring-cucumber-testng-parallel-test-harness/spring-cucumber-testng-parallel-test-harness/wikipedia/src/test/resources/drivers" &&
chmod +700 "/home/runner/work/spring-cucumber-testng-parallel-test-harness/spring-cucumber-testng-parallel-test-harness/wikipedia/src/test/resources/drivers/chromedriver" &&
cd ../../../ &&
rm -rf temp