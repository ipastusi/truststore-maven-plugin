#!groovy

multibranchPipelineJob('truststore-maven-plugin') {
    branchSources {
        git {
            id('23467E02-0123-48BC-BE78-8FDB72576EA3')
            remote('git@github.com:automatictester/truststore-maven-plugin.git')
            credentialsId('github-creds')
        }
    }
}
