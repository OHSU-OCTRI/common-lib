pipeline {
  agent any
  options {
    quietPeriod(30)
    buildDiscarder(logRotator(numToKeepStr: '3'))
  }
  triggers {
    pollSCM('')
  }
  tools {
    maven '3.8.2'
    jdk 'JDK17'
  }
  stages {
    stage('Prepare') {
      steps {
        deleteDir()
        checkout scm
      }
    }
    stage('Build') {
      steps {
        octriMavenBuild(deployArtifacts: env.BRANCH_NAME == 'main')
      }
    }
    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }
  }
  post {
    always {
      junit '**/surefire-reports/**/*.xml'
    }
    unsuccessful {
      emailStatusChange()
    }
    fixed {
      emailStatusChange()
    }
  }
}
