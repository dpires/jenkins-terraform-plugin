package org.jenkinsci.plugins.terraform;


import org.kohsuke.stapler.DataBoundConstructor;




public class Configuration {
    private final String value;
    private final String fileConfig;
    private final String inlineConfig;


    @DataBoundConstructor
    public Configuration(String value, String inlineConfig, String fileConfig) {
        this.value = value;
        this.fileConfig = fileConfig;
        this.inlineConfig = inlineConfig;
    }


    public String getInlineConfig() {
        return this.inlineConfig;
    }


    public String getFileConfig() {
        return this.fileConfig;
    }


    public String getValue() {
        return this.value;
    } 
}
