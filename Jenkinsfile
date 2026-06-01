pipeline {
    agent any

    environment {
        IMAGE_NAME = 'e-commerce-api'
        CONTAINER_NAME = 'e-commerce-api'
        APP_PORT = '8081'
        CONTAINER_PORT = '8080'
        NETWORK = 'app_app-network'
    }

    stages {
        stage('Clone') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                    docker build -t ${IMAGE_NAME}:latest .
                """
            }
        }

        steps {
        sh """
            docker stop ${CONTAINER_NAME} || true
            docker rm ${CONTAINER_NAME} || true

            docker run -d \
                --name ${CONTAINER_NAME} \
                --network ${NETWORK} \
                -p ${APP_PORT}:${CONTAINER_PORT} \
                --restart always \
                --env-file /root/app/e-commerce-api/.env \
                ${IMAGE_NAME}:latest
        """
    }
    }

    post {
        success {
            echo '✅ Deploy e-commerce-api thành công!'
        }
        failure {
            echo '❌ Deploy e-commerce-api thất bại!'
        }
    }
}