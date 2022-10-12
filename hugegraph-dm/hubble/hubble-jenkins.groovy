pipeline {
    options { disableConcurrentBuilds() }
    agent { label 'LINUXOS' }
    environment {
        git_url = "git@gitlab.gz.cvte.cn:xdm/dm-secondary-development/hugegraph-toolchain.git"
        git_branch = "master"
        image_name_with_repo = "dm/hugegraph-hubble"

        build_cmd =''' cd ./hugegraph-hubble \
                        &&  mvn clean package -Dmaven.test.skip=true \
                        && rm -rf ./hugegraph-hubble/bin/start-hubble.sh \
                        && cp ../hugegraph-dm/hubble/start-hubble.sh ./hugegraph-hubble/bin/start-hubble.sh \
                        && ls -lt '''
        //jenkins 本地Dockerfile路径
        dockerfile_path = "./hugegraph-dm/hubble/Dockerfile"
    }
    stages {
        stage('初始化环境') {
            steps {
                checkout([
                        $class: 'GitSCM',
                        branches: [[name: "${git_branch}"]],
                        userRemoteConfigs: [[credentialsId: 'b0fbde49-1a9a-4b6e-a45e-aaf932abfc9f', url: "${git_url}"]]
                ])
                sh 'git reset --hard'
                script {
                    GIT_COMMIT_NUMBER = sh (script: 'git rev-parse --short HEAD', returnStdout: true ).trim()
                }
                //把Dockerfile写到文件里
                // sh "cat <<EOF > ${dockerfile_path} ${dockerfile_content}\nEOF"
            }
        }

        stage('编译打包') {
            steps {
                sh "${build_cmd}"
            }
        }

        stage('发布镜像到内网仓库') {
            steps {
                script {
                    imageTag = "${git_branch}-${GIT_COMMIT_NUMBER}"
                }
                withDockerRegistry([credentialsId: '6e5c1650-13f9-435e-ad7e-c0a20d0774a1', url: 'https://registry.gz.cvte.cn']) {
                    script{
                        def customImage = docker.build("registry.gz.cvte.cn/${image_name_with_repo}:${imageTag}"," --pull=true --no-cache=true -f ${dockerfile_path} . ")
                        customImage.push()
                    }
                }
            }
        }
    }
}