pipeline{
    agent any
    stages {
        stage ('Compile Stage') {
            steps {
                withMaven(maven: 'MAVEN') {
                    sh 'mvn clean compile'
                }
            }
        }
    stage ('Test Stage') {
            steps {
                withMaven(maven: 'MAVEN') {
                    sh 'mvn test'
                }
            }
        }
        stage ('Cucumber Reports') {
            steps {
                cucumber buildStatus: "UNSTABLE",
                    fileIncludePattern: "**/cucumber.json",
                    jsonReportDirectory: 'target'
            }
        }
    }
}