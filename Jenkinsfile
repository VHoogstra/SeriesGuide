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
        bat '/gradlew clean'
                dir("app") {

                }
        }

        stage('Build release ') {
            parameters {
                credentials credentialType: 'org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl', defaultValue: '5d34f6f7-b641-4785-frd5-c93b67e71b6b', description: '', name: 'keystore', required: true
            }
            dir("android") {
                bat './gradlew assembleRelease'
            }
        }

        stage('Compile') {
            archiveArtifacts artifacts: '**/*.apk', fingerprint: true, onlyIfSuccessful: true
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