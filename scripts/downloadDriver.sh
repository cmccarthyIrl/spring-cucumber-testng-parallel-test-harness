#!/bin/bash

sudo apt-get install xvfb

sudo apt-get install unzip

CHROME_DRIVER_VERSION=`curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE`

wget -N http://chromedriver.storage.googleapis.com/${CHROME_DRIVER_VERSION}/chromedriver_linux64.zip
unzip chromedriver_linux64.zip
chmod +x chromedriver
sudo mv -f chromedriver /home/runner/work/spring-cucumber-testng-parallel-test-harness/spring-cucumber-testng-parallel-test-harness/common/src/test/resources/drivers