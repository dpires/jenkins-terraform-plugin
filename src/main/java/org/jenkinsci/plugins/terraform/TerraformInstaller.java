package org.jenkinsci.plugins.terraform;


import hudson.Extension;

import hudson.tools.ToolInstallation;
import hudson.tools.DownloadFromUrlInstaller;

import org.kohsuke.stapler.DataBoundConstructor;




public class TerraformInstaller extends DownloadFromUrlInstaller {

    @DataBoundConstructor
    public TerraformInstaller(String id) {
        super(id);
    }


    @Extension
    public static final class DescriptorImpl extends DownloadFromUrlInstaller.DescriptorImpl<TerraformInstaller> {
        @Override
        public String getDisplayName() {
            return Messages.InstallFromBintray();
        }


        @Override
        public boolean isApplicable(Class<? extends ToolInstallation> toolType) {
            return toolType == TerraformInstallation.class;
        }
    }
}
