# Spring - Cucumber - TestNG - Test Harness

[![Build Status](https://travis-ci.com/cmccarthyIrl/spring-cucumber-testng-parallel-test-harness.svg?branch=master)](https://travis-ci.com/cmccarthyIrl/spring-cucumber-testng-parallel-test-harness) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/6e4bd0bef1b5467aaaed6bc70a8ca2ae)](https://www.codacy.com/gh/cmccarthyIrl/spring-cucumber-testng-parallel-test-harness/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cmccarthyIrl/spring-cucumber-testng-parallel-test-harness&amp;utm_campaign=Badge_Grade)

# Index

<table> 
<tr>
  <th>Start</th>
  <td>
    | <a href="#maven">Maven</a> 
    | <a href="#quickstart">Quickstart</a> | 
  </td>
</tr>
<tr>
  <th>Run</th>
  <td>
     | <a href="#junit">TestNG</a>
    | <a href="#command-line">Command Line</a>
    | <a href="#ide-support">IDE Support</a>    
    | <a href="#java-jdk">Java JDK</a>    
    | <a href="#troubleshooting">Troubleshooting</a>    |
  </td>
</tr>
<tr>
  <th>Report</th> 
  <td>
     | <a href="#configuration">Configuration</a> 
    | <a href="#environment-switching">Environment Switching</a>
    | <a href="#extent-reports">Spark HTML Reports</a>
    | <a href="#logging">Logging</a> |
  </td>
</tr>
<tr>
  <th>Advanced</th>
  <td>
    | <a href="#hooks">Before / After Hooks</a>
    | <a href="#json-transforms">JSON Transforms</a>
    | <a href="#contributing">Contributing</a> |
    </td>
</tr>
</table>

# Maven

The Framework uses [Spring Boot Test](https://spring.io/guides/gs/testing-web/), [Cucumber](https://cucumber.io/)
, [Rest Assured](https://rest-assured.io/) and [Selenium](https://www.selenium.dev/) client implementations.

Spring `<dependencies>`:

```xml

<dependecies>
    ...
    <dependency>
        <groupId>org.springframework.amqp</groupId>
        <artifactId>spring-rabbit</artifactId>
        <version>${spring-rabbit.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
    </dependency>
    ...
</dependecies>
```

Cucumber & Rest Assured `<dependencies>`:

```xml

<dependecies>
    ...
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>${restassured.version}</version>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>${cucumber.version}</version>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-spring</artifactId>
        <version>${cucumber.version}</version>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-testng</artifactId>
        <version>${cucumber.version}</version>
    </dependency>
    ...
</dependecies>
```

Selenium `<dependencies>`:

```xml

<dependecies>
    ...
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>${selenium-version}</version>
    </dependency>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-server</artifactId>
        <version>${selenium-version}</version>
    </dependency>
    ...
</dependecies>
```

# Quickstart

- [Intellij IDE](https://www.jetbrains.com/idea/) - `Recommended`
- [Java JDK 11](https://jdk.java.net/java-se-ri/11)
- [Apache Maven 3.6.3](https://maven.apache.org/docs/3.6.3/release-notes.html)

# TestNG

By using the [TestNG Framework](https://junit.org/junit4/) we can utilize the [Cucumber Framework](https://cucumber.io/)
and the `@CucumberOptions` Annotation Type to execute the `*.feature` file tests

> Right click the `WikipediParallelRunner` class and select `Run`

```java

@CucumberOptions(
        features = {
                "src/test/resources/feature"
        },
        plugin = {
                "pretty",
                "json:target/cucumber/report.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        })
public class WikipediaParallelRunnerTest extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
```

# Command Line

Normally you will use your IDE to run a `*.feature` file directly or via the `*Test.java` class. With the `Test` class,
we can run tests from the command-line as well.

Note that the `mvn test` command only runs test classes that follow the `*Test.java` naming convention.

You can run a single test or a suite or tests like so :

```
mvn test -Dtest=WikipediaParallelRunnerTest
```

Note that the `mvn clean install` command runs all test Classes that follow the `*Test.java` naming convention

```
mvn clean install
```

# IDE Support

To minimize the discrepancies between IDE versions and Locales the `<sourceEncoding>` is set to `UTF-8`

```xml

<properties>
    ...
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    ...
</properties>
```

# Java JDK

The Java version to use is defined in the `maven-compiler-plugin`

```xml

<build>
    ...
    <pluginManagement>
        <plugins>
            ...
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
            ...
        </plugins>
    </pluginManagement>
    ...
</build>
```

# Configuration

The `AbstractTestDefinition` class is responsible for specifying each Step class as `@SpringBootTest` and
its `@ContextConfiguration`

```java

@ContextConfiguration(classes = {FrameworkContextConfiguration.class})
@SpringBootTest
public class AbstractTestDefinition {
}
```

The `FrameworkContextConfiguration` class is responsible for specifying the Spring `@Configuration`, modules to scan,
properties to use etc

```java

@EnableRetry
@Configuration
@ComponentScan({
        "com.cmccarthy.api", "com.cmccarthy.common",
})
@PropertySource("application.properties")
public class FrameworkContextConfiguration {
}
```

# Environment Switching

There is only one thing you need to do to switch the environment - which is to set `<activeByDefault>` property in the
Master POM.

> By default, the value of `spring.profiles.active` is defined in the `application.properties` file which inherits its value from the Master POM property `<activeByDefault>`

```xml

<profiles>
    ...
    <profile>
        <id>prod</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <activatedProperties>prod</activatedProperties>
        </properties>
    </profile>
    ...
</profiles>
```

You can then specify the profile to use when running Maven from the command line like so:

```
mvn clean install -DactiveProfile=dev
```

Below is an example of the `application.properties` file.

```properties
spring.profiles.active=@activatedProperties@
```

# Extent Reports

The Framework uses [Extent Reports Framework](https://extentreports.com/) to generate the HTML Test Reports

The example below is a report generated automatically by Extent Reports open-source library.

<img src="https://github.com/cmccarthyIrl/spring-cucumber-testng-parallel-test-harness/blob/master/common/src/main/resources/demo/extent-report.jpg" height="400px"/>

# Allure Reports

The Framework uses [Allure Reports](https://docs.qameta.io/allure/) to generate the HTML Test Reports

The example below is a report generated by Allure Reports open-source library.

<img src="https://github.com/cmccarthyIrl/spring-cucumber-testng-parallel-test-harness/blob/master/common/src/main/resources/demo/allure-report.png" height="400px"/>

To generate the above report navigate to the root directory of the module under test and execute the following command

`mvn allure:serve`  or `mvn allure:generate` (for an offline report)

# Logging

The Framework uses [Log4j2](https://logging.apache.org/log4j/2.x/) You can instantiate the logging service in any Class
like so

```java
private final Logger logger=LoggerFactory.getLogger(WikipediaPageSteps.class);
```

you can then use the logger like so :

```java
logger.info("This is a info message");
        logger.warn("This is a warning message");
        logger.debug("This is a info message");
        logger.error("This is a error message");
```

# Before / After Hooks

The [Logback](http://logback.qos.ch/) logging service is initialized from the `Hooks.class`

As the Cucumber Hooks are implemented by all steps we can configure the `@CucumberContextConfiguration` like so :

```java

@CucumberContextConfiguration
public class Hooks extends AbstractTestDefinition {

    private static boolean initialized = false;
    private static final Object lock = new Object();

    @Autowired
    private HookUtil hookUtil;
    @Autowired
    private DriverManager driverManager;

    @Before
    public void beforeScenario(Scenario scenario) {
        synchronized (lock) {
            if (!initialized) {
                if (!driverManager.isDriverExisting()) {
                    driverManager.downloadDriver();
                }
                initialized = true;
            }
        }
        driverManager.createDriver();
    }

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.endOfTest(scenario);
        WebDriverRunner.closeWebDriver();
    }
}
```

# JSON Transforms

[Rest Assured IO](https://rest-assured.io/) is used to map the `Response` Objects to their respective `POJO` Classes

```xml

<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>3.0.0</version>
</dependency>
```

# Troubleshooting

- Execute the following commands to resolve any dependency issues
    1. `cd ~/install directory path/spring-cucumber-testng-parallel-test-harness`
    2. `mvn clean install -DskipTests`

# Contributing

Spotted a mistake? Questions? Suggestions?

[Open an Issue](https://github.com/cmccarthyIrl/spring-cucumber-testng-parallel-test-harness/issues)


