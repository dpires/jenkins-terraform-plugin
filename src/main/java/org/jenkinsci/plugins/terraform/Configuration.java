package org.jenkinsci.plugins.terraform;


import org.kohsuke.stapler.DataBoundConstructor;




public class Configuration {
    private final String value;
    private final String fileConfig;
    private final String inlineConfig;
    private Mode mode;

    public enum Mode {
        INLINE, FILE
    }


    @DataBoundConstructor
    public Configuration(String value, String inlineConfig, String fileConfig) {
        this.value = value;
        this.fileConfig = fileConfig;
        this.inlineConfig = inlineConfig;
        this.mode = Mode.valueOf(this.value.trim().toUpperCase());
    }


    public Mode getMode() {
        return this.mode;
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
