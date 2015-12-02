package org.jenkinsci.plugins.terraform;


import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.hamcrest.Matchers.is;

import org.mockito.Mockito;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.WithoutJenkins;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

import java.io.FileNotFoundException;

import hudson.EnvVars;
import hudson.Launcher;
import hudson.FilePath;

import hudson.model.Environment;
import hudson.model.BuildListener;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;

import java.util.List;

import jenkins.util.BuildListenerAdapter;



public class TerraformInstallerTest {
    private JenkinsRule.WebClient client;


    @Rule
    public JenkinsRule jenkins = new JenkinsRule();


    @Before
    public void setUp() throws Exception {
        client = jenkins.createWebClient();
    }


    @Test
    public void testTerraformInstallerExists() throws Exception {
        HtmlPage configure = client.goTo("configure");
        HtmlElement e = configure.getElementByName("org-jenkinsci-plugins-terraform-TerraformInstallation");;
        assertNotNull(e);
    }


    @Test(expected = FileNotFoundException.class)
    public void testTerraformInstallation() throws Exception {
        TerraformInstallation installation =
            new TerraformInstallation("test-terraform", "terraform-home", null);

        TerraformInstaller installer = new TerraformInstaller("1");

        installer.performInstallation(installation, jenkins.createOnlineSlave(), jenkins.createTaskListener());

        String exePath = installation.getExecutablePath(jenkins.createLocalLauncher());
    }
}
