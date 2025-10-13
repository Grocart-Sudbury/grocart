pipeline {
    agent any

    environment {
        IMAGE_NAME = "grocart-app"
        CONTAINER_NAME = "grocart-app"
        PORT = "9091"
        REPO_URL = "https://github.com/Grocart-Sudbury/grocart.git"
        WORK_DIR = "${WORKSPACE}/grocart"
    }

    stages {
        stage('Checkout Code') {
            steps {
                sh """
                    if [ ! -d "$WORK_DIR" ]; then
                        git clone $REPO_URL $WORK_DIR
                    else
                        cd $WORK_DIR
                        git pull
                    fi
                """
            }
        }

        stage('Build Docker Image') {
            steps {
                dir("${WORK_DIR}") {
                    sh """
                        # Stop and remove old container if exists
                        docker stop $CONTAINER_NAME || true
                        docker rm $CONTAINER_NAME || true

                        # Build new Docker image
                        docker build -t $IMAGE_NAME .
                    """
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                sh """
                    # Run new container
                    docker run -d --name $CONTAINER_NAME -p $PORT:$PORT $IMAGE_NAME
                """
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful! App running on port ${PORT}"
        }
        failure {
            echo "❌ Deployment failed!"
        }
    }
}
