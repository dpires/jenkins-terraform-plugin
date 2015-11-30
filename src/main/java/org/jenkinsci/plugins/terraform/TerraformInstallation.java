package org.jenkinsci.plugins.terraform;


import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Functions;
import hudson.Extension;

import hudson.remoting.Callable;

import hudson.model.Node;
import hudson.model.TaskListener;

import hudson.tools.ToolProperty;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;

import jenkins.model.Jenkins;

import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;




public class TerraformInstallation extends ToolInstallation {

    private static final String UNIX_EXECUTABLE = "terraform";
    private static final String WINDOWS_EXECUTABLE = "terraform.exe";


    @DataBoundConstructor
    public TerraformInstallation(String name, String home, List<? extends ToolProperty<?>> properties) {
        super(name, home, properties);
    }


    public TerraformInstallation forNode(Node node, TaskListener log) throws IOException, InterruptedException {
        return new TerraformInstallation(getName(), translateFor(node, log), getProperties().toList());
    }


    public TerraformInstallation forEnvironment(EnvVars environment) {
        return new TerraformInstallation(getName(), environment.expand(getHome()), getProperties().toList());
    }

    
    public String getExecutablePath(final Launcher launcher) throws IOException, InterruptedException {
        return launcher.getChannel().call(new Callable<String, IOException>() {
            public String call() throws IOException {

                FilePath homeDirectory = new FilePath(new File(getHome()));

                try {
                if (!(homeDirectory.exists() && homeDirectory.isDirectory())) 
                    throw new FileNotFoundException(String.format("Home directory not found. [%s]", homeDirectory));
                } catch (InterruptedException ex) {

                }

                FilePath executable = new FilePath(homeDirectory, getExecutableFilename());

                try {
                if (!executable.exists())
                    throw new FileNotFoundException(String.format("Executable not found. [%s]", homeDirectory));
                } catch (InterruptedException ex) { }

                return executable.getRemote();
            }
        });
    }


    protected String getExecutableFilename() {
        return Functions.isWindows() ? WINDOWS_EXECUTABLE : UNIX_EXECUTABLE;
    } 


    @Extension
    public static class DescriptorImpl extends ToolDescriptor<TerraformInstallation> {
    
        @Override
        public String getDisplayName() {
            return "Terraform";
        }


        @Override
        public TerraformInstallation[] getInstallations() {
            return Jenkins.getInstance().getDescriptorByType(TerraformBuildWrapper.DescriptorImpl.class).getInstallations();
        }


        @Override
        public void setInstallations(TerraformInstallation... installations) {
            Jenkins.getInstance().getDescriptorByType(TerraformBuildWrapper.DescriptorImpl.class).setInstallations(installations);
        }
    }
}
