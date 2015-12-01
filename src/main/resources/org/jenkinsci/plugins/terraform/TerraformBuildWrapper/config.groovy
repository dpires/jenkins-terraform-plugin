package org.jenkinsci.plugins.terraform.TerraformBuildWrapper;
                                                                               
f = namespace('/lib/form')


f.block() {
    f.div(style: "margin: 0px 0px") {
        f.table(style: "width: 100%") {
            f.entry(field: 'terraformInstallation', title: _('Terraform Installation')) {
                f.select();
            }

            f.radioBlock(checked: descriptor.isInlineConfigChecked(instance), name: 'config', value: 'inline', title: 'Configuration Text') {
                f.entry(title: 'Terraform Text Configuration', field: 'inlineConfig', description: 'Inline configuration') { 
                    f.textarea(); 
                }
            }
            f.radioBlock(checked: descriptor.isFileConfigChecked(instance), name: 'config', value: 'file', title: 'Configuration Path') {
                f.entry(title: 'Terraform File Configuration', field: 'fileConfig', description: 'Relative Path to workspace directory containing .tf configurations') { 
                    f.textbox(); 
                }
            }

            f.entry(field: 'variables', title: _('Optional Parameters'), description: 'Optional variables, these will be written to a variable file and passed to terraform with -var-file') {
                f.textarea();
            }

            f.advanced() {
                f.entry(field: 'doDestroy', title: _('Destroy infrastructure on completion'), description: 'Run -destroy to delete infrastructure on build completion') {
                    f.checkbox();
                }
            }
        }
    }
}

