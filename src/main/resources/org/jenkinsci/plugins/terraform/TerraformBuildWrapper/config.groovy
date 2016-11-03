package org.jenkinsci.plugins.terraform.TerraformBuildWrapper;

f = namespace('/lib/form')


f.block() {
    f.div(style: "margin: 0px 0px") {
        f.table(style: "width: 100%") {
            f.entry(field: 'terraformInstallation', title: _('Terraform Installation')) {
                f.select();
            }

            f.entry(field:'doGetUpdate', title: _('Update modules'), description: 'Run terraform get with -update flag') {
                f.checkbox();
            }

            f.entry(field:'doNotApply', title: _('Do not apply automatically'), description: 'Do everything except apply') {
                f.checkbox();
            }

            f.radioBlock(checked: descriptor.isInlineConfigChecked(instance), name: 'config', value: 'inline', title: 'Configuration Text') {
                f.entry(title: 'Terraform Text Configuration', field: 'inlineConfig', description: 'Inline configuration') {
                    f.textarea();
                }
            }
            f.radioBlock(checked: descriptor.isFileConfigChecked(instance), name: 'config', value: 'file', title: 'Configuration Path') {
                f.entry(title: 'Terraform File Configuration', field: 'fileConfig', description: 'Relative Path to workspace directory containing configuration files') {
                    f.textbox();
                }
            }

            f.entry(field: 'variables', title: _('Resource Variables (Optional)'), description: 'Resource variables will be passed to Terraform as a file') {
                f.textarea();
            }

            f.advanced() {
                f.entry(field: 'doDestroy', title: _('Destroy On Build Completion'), description: 'Run destroy command to delete infrastructure on build completion') {
                    f.checkbox();
                }
            }
        }
    }
}
