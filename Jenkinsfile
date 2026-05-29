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

        stage('Deploy') {
            steps {
                sh """
                    # Dừng và xóa container cũ nếu có
                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true

                    # Chạy container mới
                    docker run -d \
                        --name ${CONTAINER_NAME} \
                        --network ${NETWORK} \
                        -p ${APP_PORT}:${CONTAINER_PORT} \
                        --restart always \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://db-e-commerce:5432/db_e_commerce \
                        -e SPRING_DATASOURCE_USERNAME=postgres \
                        -e SPRING_DATASOURCE_PASSWORD=123456 \
                        ${IMAGE_NAME}:latest
                """
            }
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