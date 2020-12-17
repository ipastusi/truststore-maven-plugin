#!groovy

multibranchPipelineJob('truststore-maven-plugin') {
    branchSources {
        git {
            remote('git@github.com:automatictester/truststore-maven-plugin.git')
            credentialsId('github-creds')
        }
    }
}
