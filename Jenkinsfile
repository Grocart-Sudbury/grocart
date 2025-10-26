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

        stage('Build and Deploy') {
            steps {
                dir("${WORK_DIR}") {
                    withCredentials([
                        string(credentialsId: 'db-password', variable: 'DB_PASSWORD'),
                        string(credentialsId: 'postmark-api-key', variable: 'POSTMARK_API_KEY'),
                        string(credentialsId: 'stripe-secret-key', variable: 'STRIPE_SECRET_KEY')
                    ]) {
                        sh """
                            echo "🛠 Building Docker image..."
                            docker stop $CONTAINER_NAME || true
                            docker rm $CONTAINER_NAME || true
                            docker build -t $IMAGE_NAME .

                            echo "🚀 Running Docker container..."
                            docker run -d --name $CONTAINER_NAME \
                                -e DB_PASSWORD=$DB_PASSWORD \
                                -e POSTMARK_API_KEY=$POSTMARK_API_KEY \
                                -e STRIPE_SECRET_KEY=$STRIPE_SECRET_KEY \
                                -p $PORT:$PORT $IMAGE_NAME
                        """
                    }
                }
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
