def err = null
try {

    node {

        stage('Preparation') {
            git credentialsId: '9a501328-9d6f-4528-88d9-324643cbd61d', url: 'https://github.com/VHoogstra/SeriesGuide'
        }

        stage('Dependencies') {
                bat 'npm install -g react-native-cli'
                bat 'npm install'
                bat 'php -v'

                bat 'echo $JAVA_HOME'

        }

        stage('Clean Build') {
        bat 'gradlew clean'
                dir("app") {

                }
        }

        stage('Test') {
                bat 'gradlew test'
        }

    }

} catch (caughtError) {

    err = caughtError
    currentBuild.result = "FAILURE"

} finally {

    if(currentBuild.result == "FAILURE"){
              bat "echo 'Build FAILURE'"
    }else{
         bat "echo 'Build SUCCESSFUL'"
    }

}