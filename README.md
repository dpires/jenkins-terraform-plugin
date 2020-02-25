Terraform plugin
================

[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/terraform.svg)](https://plugins.jenkins.io/terraform)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/terraform.svg?color=blue)](https://plugins.jenkins.io/terraform)
[![GitHub release](https://img.shields.io/github/release/jenkinsci/terraform-plugin.svg?label=changelog)](https://github.com/jenkinsci/terraform-plugin/releases/latest)

## About this plugin

The Terraform plugin for [Jenkins](https://jenkins.io) adds an installer for [Terraform](https://hashicorp.com/terraform) and provides a job build wrapper for creating (and destroying) infrastructure. 

The installer generates a list of Terraform binaries for installation from [bintray.com](http://bintray.com), see [Terraform Crawler](https://github.com/jenkinsci/backend-crawler/blob/master/terraform.groovy).

## Usage

### Tool Installation

Terraform binaries can be installed via  _Manage Jenkins > Global Tool Configuration_. 
Select a label, version and platform to install.

![](https://wiki.jenkins.io/download/thumbnails/85590094/terraform-install.png?version=1&modificationDate=1449189892000&api=v2)

### Job Configuration

This plugin works as a build wrapper and can be invoked by selecting
Terraform under the Build Environment section of your job configuration.

A workspace directory **terraform-plugin** will be created, this is
where temporary files are created and automatically deleted after runs.

This directory also contains the generated tfstate file
**terraform-plugin.tfstate**.

This generated tfstate file is not deleted and is always passed to
Terraform using
**-state=workspace/terraform-plugin/terraform-plugin.tfstate** as an
extra safety measure so as to not use any other tfstate file you might
have in a workspace.

The first step is to decide how you would like to pass your
configurations to Terraform, there are 2 options:

#### Option 1: Configuration text

![](https://wiki.jenkins.io/download/thumbnails/85590094/terraform-configuration-text.png?version=1&modificationDate=1449190253000&api=v2)

This option allows you to copy what would be in a resource file (.tf)
into a textarea. This text will then be written to a temporary file
(workspace/terraform-plugin/terraform-TEMP.tf) and parsed by Terraform.

#### Option 2: Configuration path

![](https://wiki.jenkins.io/download/thumbnails/85590094/terraform-configuration-path.png?version=1&modificationDate=1449190352000&api=v2)

This option provides a directory path (relative to your workspace) to
enter where your configuration files (.tf) exist. If no path is given,
it defaults to the workspace path.

### Additional configuration options

#### Update modules

![](https://wiki.jenkins.io/download/thumbnails/85590094/update_modules.png?version=1&modificationDate=1468773167000&api=v2)

Terraform will run the get command by default, use this option to run
Terraform get with the -update flag.

#### Resource variables 

![](https://wiki.jenkins.io/download/thumbnails/85590094/terraform-resource-variables.png?version=1&modificationDate=1449190478000&api=v2)

If you are using resource files that reference external variables, set
these variables here.

These variables will be written to a temporary file
(workspace/terraform-plugin/variables-TEMP.tfvars) and will be passed to
Terraform using the **--var-file=** option.


### Advanced

![](https://wiki.jenkins.io/download/thumbnails/85590094/terraform-destroy.png?version=1&modificationDate=1449190568000&api=v2)

If you want to destroy the architecture you have created once the build
is complete, click the advanced button and check Destroy On Build
Completion.

This will call Terraform with the **destroy --force** option which will
look at the generated terraform-plugin.tfstate file
(workspace/terraform-plugin/terraform-plugin.tfstate) and destroy
everything under Terraform supervision.

## Developer Instructions

This plugin uses gradle wrapper, so the only dependency is a working JDK (7/8).

* To build the .hpi plugin (build/libs/jenkins-terraform.hpi)
```
./gradlew jpi
```
* To build and install in a local jenkins server running at http://localhost:8080/
```
./gradlew server
```

* To prepare a release (and set next release version)
```
./gradlew release
```

* To publish a release to update center (for Maintainer(s))
```
./gradlew publish
```

## License

[MIT](LICENSE)

## More information

* [Changelog](https://github.com/jenkinsci/terraform-plugin/releases)
* [Wiki](https://plugins.jenkins.io/terraform/)

