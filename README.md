# jenkins-terraform-plugin

[![Build Status](https://travis-ci.org/dpires/jenkins-terraform-plugin.png?branch=master)](https://travis-ci.org/dpires/jenkins-terraform-plugin)

## Usage

This plugin adds an installer for Terraform and provides a build wrapper for creating and destroying infrastructure.

## Installation

### Jenkins Configuration
1. Under configure system, add a Terraform installation and choose install from bintray.com

### Jenkins Job Configuration

1. Check Terraform under Build Environment
2. You have 2 options for configuration, Text-based (inline) or provide a workspace relative path to your configuraton directory (where your .tf files are located)
3. You have the option of added additional variables, these will be written to a variable file and passed to terraform with -var-file=
4. If you wish to destroy your infrastructure, click on advanced and check the Destroy infrastructure on completion checkbox, this will call terraform with destroy and will delete any infrastructure that you have created.


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
