pipeline {
    agent any

    environment {
        IMAGE_NAME = "grocart-app"
        CONTAINER_NAME = "grocart_container"
        APP_PORT = "9091"  // match the port your app exposes
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Grocart-Sudbury/grocart.git', branch: 'main'
            }
        }

        stage('Build & Package') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Stop & Remove old container (if exists)') {
            steps {
                script {
                    sh """
                    docker ps -q --filter "name=${CONTAINER_NAME}" | grep -q . && \
                      docker stop ${CONTAINER_NAME} && \
                      docker rm ${CONTAINER_NAME} || \
                      echo "No existing container"
                    """
                }
            }
        }

        stage('Run Container') {
            steps {
                sh "docker run -d --name ${CONTAINER_NAME} -p ${APP_PORT}:${APP_PORT} ${IMAGE_NAME}"
            }
        }
    }

    post {
        success {
            echo "✅ Grocart deployed successfully (port ${APP_PORT})"
        }
        failure {
            echo "❌ Deployment failed"
        }
    }
}
