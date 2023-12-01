@Library('my-shared-library') _

pipeline {
    agent any

    parameters {

        choice(name: 'action', choices: 'create\ndelete', description: 'Choose Create/Destroy')
        string(name: 'imageRepoName', description: 'name of image repository on ECR', defaultValue: 'medigorx')
        string(name: 'awsAccountId', description: 'ID of aws account to deploy on it', defaultValue: '989481297723')
        string(name: 'imageTag', description: 'tag of image', defaultValue: 'develop')
        string(name: 'hubUser', description: 'username of docker hub', defaultValue: 'franklinfoko')
        string(name: 'awsDefaultregion', description: 'the default aws region where we work', defaultValue: 'us-east-1')
    
    }

    stages {

        // stage('Git Checkout') {

        //     when { expression { params.action == 'create' } }

        //     steps {

        //         script {

        //             gitCheckout(
        //                 branch: "develop",
        //                 url: "https://gitlab.com/team-genesis1/abc-registry.git"
        //             )
        //         }
        //     }
        // }

        // stage('Unit Test Maven') {

        //     when { expression { params.action == 'create' } }

        //     steps {

        //         script {

        //             mvnTest()
        //         }
        //     }
        // }

        // stage('Integration Test Maven') {

        //     when { expression { params.action == 'create' } }

        //     steps {

        //         script {

        //             mvnIntegrationTest()
        //         }
        //     }
        // }

        // stage('Static Code Analysis: SonarQube') {

        //     when { expression { params.action == 'create' } }

        //     steps {

        //         script {
                    
        //             def SonarQubecredentialsId = 'sonarqube_token'
        //             staticCodeAnalysis(SonarQubecredentialsId)
        //         }
        //     }
        // }

        // stage('Quality Gate Status Check: SonarQube') {

        //     when { expression { params.action == 'create' } }

        //     steps {

        //         script {
                    
        //             def SonarQubecredentialsId = 'sonarqube_token'
        //             QualityGateStatus(SonarQubecredentialsId)
        //         }
        //     }
        // }

        stage('Maven Build: Maven') {

            when { expression { params.action == 'create' } }

            steps {

                script {

                    mvnBuild()
                }
            }
        }

        stage('Docker Image Build') {

            when { expression { params.action == 'create' } }

            steps {

                script {

                    dockerBuild("${params.imageRepoName}", "${params.awsAccountId}", "${params.imageTag}", "${params.awsDefaultregion}", "${params.hubUser}")
                }
            }
        }

        // stage('Docker Image Scan: trivy') {

        //     when { expression { params.action == 'create' } }

        //     steps {

        //         script {

        //             dockerImageScan("${params.imageRepoName}", "${params.awsAccountId}", "${params.imageTag}", "${params.awsDefaultregion}")
        //         }
        //     }
        // }

        stage('Docker Image Push: DockerHub & ECR') {

            when { expression { params.action == 'create' } }

            steps {

                script {

                    dockerImagePush("${params.imageRepoName}", "${params.imageTag}", "${params.hubUser}", "${params.awsDefaultregion}", "${params.awsAccountId}" )
                }
            }
        }

        stage('Docker Copy Image: Minikube') {

            when { expression { params.action == 'create' } }

            steps {

                script {

                    withCredentials([usernamePassword(credentialsId: 'kubernetes-server', passwordVariable: 'MINIKUBE_PASSWORD', usernameVariable: 'MINIKUBE_USER')]) {
                    sh "docker save -o ${params.imageRepoName}.tar ${params.imageRepoName}"
                    sh "pwd"
                    sh "ls"
                    sh "sshpass -p '${MINIKUBE_PASSWORD}'  scp -o StrictHostKeyChecking=no ${params.imageRepoName}.tar ${MINIKUBE_USER}@18.213.204.19:/home/centos/test-devops/"
                    sh "sshpass -p '${MINIKUBE_PASSWORD}'  ssh -o StrictHostKeyChecking=no -tt ${MINIKUBE_USER}@18.213.204.19 'docker load -i /home/centos/test-devops/${params.imageRepoName}.tar'"
                   }
                }
            }
        }

        stage('Deploy: Minikube') {

            when { expression { params.action == 'create' } }

            steps {

                script {

                    withCredentials([usernamePassword(credentialsId: 'kubernetes-server', passwordVariable: 'MINIKUBE_PASSWORD', usernameVariable: 'MINIKUBE_USER')]) {

                    //sh "sshpass -p '${MINIKUBE_PASSWORD}' scp -o StrictHostKeyChecking=no abc-deployment.yml ${MINIKUBE_USER}@18.213.204.19:/home/centos/abc/"
                    sh "sshpass -p '${MINIKUBE_PASSWORD}' ssh -o StrictHostKeyChecking=no -tt ${MINIKUBE_USER}@18.213.204.19 'sudo kubectl apply -f /home/centos/test-devops/test-deployment.yml -n test'"
                    sh "sshpass -p '${MINIKUBE_PASSWORD}' ssh -o StrictHostKeyChecking=no -tt ${MINIKUBE_USER}@18.213.204.19 'sudo kubectl rollout restart deployment/test-deployment -n test'"

                   }
                }
            }
        }
        
    }
}

