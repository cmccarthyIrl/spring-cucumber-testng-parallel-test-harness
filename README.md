# Spring Cucumber TestNG Parallel Test Harness

A robust test automation framework built using Spring Boot, Cucumber, and TestNG for parallel test execution. This project demonstrates how to organize and execute automated tests efficiently in parallel across multiple modules.

## 🚀 Features

- **Multi-Module Architecture**: Separate modules for common functionality, weather tests, and Wikipedia tests
- **Parallel Execution**: Run tests concurrently using TestNG
- **Spring Boot Integration**: Leverage dependency injection and application properties
- **Comprehensive Reporting**: Allure and Extent reports for detailed test results
- **Logging**: Dedicated logs for each test scenario
- **Cross-Environment Support**: Multiple environment configurations (dev, uat, prod)

## 📋 Requirements

- Java 11 or higher
- Maven 3.6 or higher
- Git

## 🔧 Setup & Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/cmccarthyIrl/spring-cucumber-testng-parallel-test-harness.git
   cd spring-cucumber-testng-parallel-test-harness
   ```

2. Install dependencies:
   ```bash
   mvn clean install -DskipTests
   ```

## 🏃‍♂️ Running Tests

### Running All Tests
```bash
mvn clean test
```

### Running Tests for a Specific Module
```bash
mvn clean test -pl weather
# or
mvn clean test -pl wikipedia
```

### Running with Specific Environment
```bash
mvn clean test -Dspring.profiles.active=dev
```

Available profiles: `dev`, `uat`, `prod`, `headless-github`, `cloud-provider`

## 📊 Reporting

After test execution, reports are available at:

- **Allure Reports**: `<module>/target/allure-results/`
- **Extent Reports**: `<module>/target/cucumber/report.html`
- **TestNG Reports**: `<module>/target/surefire-reports/index.html`

To generate and open Allure reports:
```bash
mvn allure:serve
```

## 📁 Project Structure

- **common**: Shared utilities, base classes, and configurations
  - Configuration properties
  - Common test utilities
  - Shared step definitions
  
- **weather**: Weather API testing module
  - Feature files for weather API tests
  - Step definitions for weather tests
  - API client for weather services

- **wikipedia**: Wikipedia functionality tests
  - Feature files for Wikipedia tests
  - Step definitions for Wikipedia interactions
  - Page objects for Wikipedia pages

## 🌐 Environment Configuration

The framework supports multiple environments through Spring profiles:

- **dev**: Development environment
- **uat**: User Acceptance Testing environment
- **prod**: Production environment
- **headless-github**: Headless browser configuration for CI/CD
- **cloud-provider**: Configuration for cloud-based test execution

## 📝 Logging

Test execution logs are stored in the `logs` directory of each module. Log files are named after the test scenario for easy debugging.

## 🤝 Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Maintainers

- [cmccarthyIrl](https://github.com/cmccarthyIrl)
