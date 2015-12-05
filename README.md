# jenkins-terraform-plugin

[![Build Status](https://jenkins.ci.cloudbees.com/buildStatus/icon?job=plugins/terraform-plugin)](https://jenkins.ci.cloudbees.com/job/plugins/job/terraform-plugin/)

## Usage

This plugin adds an installer for Terraform and provides a build wrapper for creating and destroying infrastructure.

The installer generates a list of Terraform binaries for installation from [bintray.com](http://bintray.com), see [Terraform Crawler](https://github.com/jenkinsci/backend-crawler/blob/master/terraform.groovy).

See the Jenkins plugin wiki for more details: [Terraform Plugin Wiki](https://wiki.jenkins-ci.org/display/JENKINS/Terraform+Plugin).

# Developer Instructions

This plugin uses gradle wrapper, so the only dependency is a working JDK (7/8).

* To build the .hpi plugin (build/libs/jenkins-terraform.hpi)
```
./gradlew jpi
```
* To build and install in a local jenkins server running at http://localhost:8080/
```
./gradlew server
```

## License

[MIT](LICENSE)
