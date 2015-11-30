package org.jenkinsci.plugins.terraform;


import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import org.jvnet.hudson.test.JenkinsRule;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;




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
}
