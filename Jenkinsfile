#!groovy

properties([
    parameters([
        booleanParam(defaultValue: false, name: 'PUBLISH_TO_DOCKERHUB', description: 'Publish Docker Image to DockerHub?')
    ])
])

def DOCKER_USERNAME = 'sivaprasadreddy'
def DOCKER_IMAGE_NAME = 'devzone'

node {

    try {
        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            try {
                sh './gradlew build -Pci'
            } finally {
                junit 'build/test-results/test/*.xml'
                junit 'build/test-results/integrationTest/*.xml'
                publishHTML(target: [
                    allowMissing         : true,
                    alwaysLinkToLastBuild: true,
                    keepAll              : true,
                    reportDir            : 'build/reports/jacoco/test/html',
                    reportFiles          : 'index.html',
                    reportName           : "Jacoco Report"
                ])
                publishHTML(target: [
                    allowMissing         : true,
                    alwaysLinkToLastBuild: true,
                    keepAll              : true,
                    reportDir            : 'build/reports',
                    reportFiles          : 'dependency-check-report.html',
                    reportName           : "OWASP Report"
                ])
            }
        }

        stage('Publish Docker Image') {
            echo "PUBLISH_TO_DOCKERHUB: ${params.PUBLISH_TO_DOCKERHUB}"
            if(params.PUBLISH_TO_DOCKERHUB) {
                echo "Publishing to dockerhub. DOCKER_USERNAME=${DOCKER_USERNAME}, APPLICATION_NAME=${DOCKER_IMAGE_NAME}"
                def appImage = docker.build("${DOCKER_USERNAME}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}")
                appImage.push()
                appImage.push('latest')
            } else {
                echo "Skipping Publish Docker Image"
            }
        }

    }
    catch(err) {
        echo "ERROR: ${err}"
        currentBuild.result = currentBuild.result ?: "FAILURE"
    }
}
