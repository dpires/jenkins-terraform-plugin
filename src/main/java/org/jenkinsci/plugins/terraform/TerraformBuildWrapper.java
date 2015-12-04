package org.jenkinsci.plugins.terraform;


import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Extension;
import hudson.CopyOnWrite;

import hudson.util.ListBoxModel;
import hudson.util.FormValidation;
import hudson.util.ArgumentListBuilder;

import hudson.model.Result;
import hudson.model.Computer;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;

import hudson.model.Descriptor.FormException;

import hudson.tasks.Recorder;
import hudson.tasks.Publisher;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildWrapperDescriptor;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.DataBoundConstructor;

import net.sf.json.JSONObject;

import java.util.logging.Logger;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.FileNotFoundException;




public class TerraformBuildWrapper extends BuildWrapper {

    private final String variables;
    private final boolean doDestroy;
    private final Configuration config;
    private final String terraformInstallation;
    private FilePath configFile;
    private FilePath variablesFile;

    private static final Logger LOGGER = Logger.getLogger(TerraformBuildWrapper.class.getName());


    @DataBoundConstructor
    public TerraformBuildWrapper(String variables, String terraformInstallation, boolean doDestroy, Configuration config) {
        this.config = config;
        this.doDestroy = doDestroy;
        this.variables = variables;
        this.terraformInstallation = terraformInstallation;
    }


    public Configuration getConfig() {
        return this.config;
    }


    public String getInlineConfig() {
        return this.config.getInlineConfig();
    }


    public String getFileConfig() {
        return this.config.getFileConfig();
    }


    public String getConfigMode() {
        return this.config.getValue();
    }

    
    public boolean doDestroy() {
        return this.doDestroy;
    }


    public boolean getDoDestroy() {
        return this.doDestroy;
    }


    public String getTerraformInstallation() {
        return this.terraformInstallation;
    }


    public String getVariables() {
        return this.variables;
    }


    public TerraformInstallation getInstallation() {
        for (TerraformInstallation installation : ((DescriptorImpl) getDescriptor()).getInstallations()) {
            if (terraformInstallation != null &&
                installation.getName().equals(terraformInstallation)) {
                return installation;
            }
        }
        return null;
    }


    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();        
    }


    public String getExecutable(EnvVars env, BuildListener listener, Launcher launcher) throws IOException, InterruptedException {
        TerraformInstallation terraform = getInstallation().forNode(Computer.currentComputer().getNode(), listener).forEnvironment(env);
        return terraform.getExecutablePath(launcher);
    }


    @Override
    public Environment setUp(AbstractBuild build, final Launcher launcher, final BuildListener listener) throws IOException, InterruptedException {

        ArgumentListBuilder args = new ArgumentListBuilder();
            
        try {
            EnvVars env = build.getEnvironment(listener);

            String executable = getExecutable(env, listener, launcher);

            args.add(executable);

            FilePath workspacePath = build.getWorkspace();

            if (!isNullOrEmpty(getInlineConfig())) {
                configFile = workspacePath.createTextTempFile("terraform", ".tf", getInlineConfig());
                if (configFile == null || !configFile.exists()) {
                    throw new FileNotFoundException("Configuration could not be created.");
                }
            } else {
                if (!isNullOrEmpty(getFileConfig())) {
                    workspacePath = new FilePath(build.getWorkspace(), getFileConfig());
                    if (!workspacePath.isDirectory()) {
                        throw new FileNotFoundException(String.format("Configuration path not found [%s]", workspacePath));
                    }
                }
            }

            args.add("apply");

            if (!isNullOrEmpty(getVariables())) {
                variablesFile = workspacePath.createTextTempFile("variables", ".tfvars", getVariables());
                args.add("-var-file="+variablesFile.getRemote());
            }

            int result = launcher.launch().pwd(workspacePath.getRemote()).cmds(args).stdout(listener).join();

            if (result != 0) {
                deleteTemporaryFiles();
                return null;
            }
    
        } catch (Exception ex) {
            LOGGER.severe(exceptionToString(ex));
            listener.fatalError(exceptionToString(ex));
            deleteTemporaryFiles();
            return null;
        }
        
        return new Environment() {

            @Override
            public boolean tearDown(AbstractBuild build, BuildListener listener) throws IOException, InterruptedException {

                if (doDestroy()) {

                    ArgumentListBuilder args = new ArgumentListBuilder();

                    try {
                        EnvVars env = build.getEnvironment(listener);

                        args.add(getExecutable(env, listener, launcher));

                        args.add("destroy");

                        args.add("--force");

                        if (!isNullOrEmpty(getVariables())) {
                            args.add("-var-file="+variablesFile.getRemote());
                        }

                        int result = launcher.launch().pwd(build.getWorkspace().getRemote()).cmds(args).stdout(listener).join();

                        if (result != 0) {
                            deleteTemporaryFiles();
                            return false;
                        }

                    } catch (Exception ex) {
                        LOGGER.severe(exceptionToString(ex));
                        listener.fatalError(exceptionToString(ex));
                        deleteTemporaryFiles();
                        return false;
                    }
                }

                deleteTemporaryFiles();

                return true;
            }
        };
    }


    private void deleteTemporaryFiles() throws IOException, InterruptedException {
        if (variablesFile != null && variablesFile.exists())
            variablesFile.delete();

        if (configFile != null && configFile.exists())
            configFile.delete();
    }


    private boolean isNullOrEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? true : false;
    }


    private String exceptionToString(Exception ex) {
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    
    @Extension
    public static final class DescriptorImpl extends BuildWrapperDescriptor {

        @CopyOnWrite
        private volatile TerraformInstallation[] installations = new TerraformInstallation[0];


        public DescriptorImpl() {
            super(TerraformBuildWrapper.class);
            load();
        }


        public TerraformInstallation[] getInstallations() {
            return this.installations;
        }

    
        public void setInstallations(TerraformInstallation[] installations) {
            this.installations = installations;
            save();
        }


        public ListBoxModel doFillTerraformInstallationItems() {
            ListBoxModel m = new ListBoxModel();
            for (TerraformInstallation inst : installations) {
                m.add(inst.getName());
            }
            return m;
        }

        
        public boolean isInlineConfigChecked(TerraformBuildWrapper instance) {
            boolean result = true;
            if (instance != null)
                return (instance.getInlineConfig() != null); 

            return result;
        }


        public boolean isFileConfigChecked(TerraformBuildWrapper instance) {
            boolean result = false;
            if (instance != null)
                return (instance.getFileConfig() != null); 

            return result;
        }


        public boolean isApplicable(AbstractProject<?, ?> project) {
            return true;
        }
    

        public String getDisplayName() {
            return "Terraform";
        }
    }
}
