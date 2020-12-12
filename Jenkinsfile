#!groovy

node {

    try {
        checkout scm
        stage('Build') {
            sh './gradlew build'
        }
    }
    catch(err) {
        echo "ERROR: ${err}"
        currentBuild.result = currentBuild.result ?: "FAILURE"
    }
}
