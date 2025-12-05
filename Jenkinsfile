pipeline {
    agent { label 'slave3' }

    environment {
        APP_JAR = "target/simple-parcel-service-app-1.0-SNAPSHOT.jar"
        APP_PORT = "8080"
    }

    stages {
        stage('Checkout Code') {
            steps {
                sh 'echo "=== Running Jenkinsfile from branch: $BRANCH_NAME ==="'
    sh 'git rev-parse --abbrev-ref HEAD || true'
            }
        }

        stage('Set up Java & Maven') {
            steps {
                echo "Java Version:"
                sh "java -version"

                echo "Maven Version:"
                sh "mvn -version"
            }
        }

        stage('Build with Maven') {
            steps {
                sh "mvn clean package"
            }
        }

        stage('Upload Artifact') {
            steps {
                archiveArtifacts artifacts: "${APP_JAR}", fingerprint: true
            }
        }

        stage('Run Application') {
            steps {
                sh '''
                    echo "Starting Spring Boot Application..."
                    nohup mvn spring-boot:run > app.log 2>&1 &
                    echo $! > app.pid
                    sleep 15
                '''
            }
        }

        stage('Validate App is Running') {
            steps {
                sh '''
                    echo "Waiting for the app to stabilize..."
                    sleep 15

                    echo "Checking if the app is running..."
                    RESPONSE=$(curl --write-out "%{http_code}" --silent --output /dev/null http://localhost:${APP_PORT})

                    if [ "$RESPONSE" -eq 200 ]; then
                        echo "The app is running successfully!"
                    else
                        echo "The app failed to start. HTTP response code: $RESPONSE"
                        exit 1
                    fi
                '''
            }
        }

        stage('Wait for 2 Minutes') {
            steps {
                echo "Keeping the app alive for 2 minutes..."
                sh "sleep 120"
            }
        }
    }

    post {
        always {
            sh '''
                echo "Stopping the Spring Boot application gracefully..."

                if [ -f app.pid ]; then
                    kill $(cat app.pid) || true
                fi

                mvn spring-boot:stop || true
            '''
        }
    }
}
