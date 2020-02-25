package org.jenkinsci.plugins.terraform;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Functions;
import hudson.Extension;

import hudson.remoting.Callable;

import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.model.EnvironmentSpecific;

import hudson.tools.ToolProperty;
import hudson.tools.ToolInstaller;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;

import hudson.slaves.NodeSpecific;

import jenkins.model.Jenkins;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;
import java.util.Collections;

import java.io.File;
import java.io.Serializable;
import java.io.IOException;
import java.io.FileNotFoundException;

public class TerraformInstallation extends ToolInstallation implements EnvironmentSpecific<TerraformInstallation>, NodeSpecific<TerraformInstallation>, Serializable {

    private static final long serialVersionUID = 1L;

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


    @Override
    public void buildEnvVars(EnvVars env) {
        String home = getHome();
        if (home == null) {
            return;
        }
        env.put("PATH+TERRAFORM", home);
    }

    
    public String getExecutablePath(final Launcher launcher) throws IOException, InterruptedException {
        return launcher.getChannel().call(new Callable<String, IOException>() {
            public String call() throws IOException {

                FilePath homeDirectory = new FilePath(new File(getHome()));

                try {
                    if (!(homeDirectory.exists() && homeDirectory.isDirectory())) 
                        throw new FileNotFoundException(Messages.HomeDirectoryNotFound(homeDirectory));
                } catch (InterruptedException ex) {
                    throw new IOException(ex);
                }

                FilePath executable = new FilePath(homeDirectory, getExecutableFilename());

                try {
                    if (!executable.exists())
                        throw new FileNotFoundException(Messages.ExecutableNotFound(homeDirectory));
                } catch (InterruptedException ex) {
                    throw new IOException(ex);
                }

                return executable.getRemote();
            }
        });
    }


    protected String getExecutableFilename() {
        return Functions.isWindows() ? WINDOWS_EXECUTABLE : UNIX_EXECUTABLE;
    }


    @Symbol("terraform")
    @Extension
    public static class DescriptorImpl extends ToolDescriptor<TerraformInstallation> {
    
        @Override
        public String getDisplayName() {
            return "Terraform";
        }


        @Override
        public List<? extends ToolInstaller> getDefaultInstallers() {
            return Collections.singletonList(new TerraformInstaller(null));
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
