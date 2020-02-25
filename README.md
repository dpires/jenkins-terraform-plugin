Terraform plugin
================

[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/terraform.svg)](https://plugins.jenkins.io/terraform)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/terraform.svg?color=blue)](https://plugins.jenkins.io/terraform)
[![GitHub release](https://img.shields.io/github/release/jenkinsci/terraform-plugin.svg?label=changelog)](https://github.com/jenkinsci/terraform-plugin/releases/latest)


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
