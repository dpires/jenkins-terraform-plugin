package org.jenkinsci.plugins.terraform.TerraformInstallation;
                                                                               
f = namespace('/lib/form')
f.entry(field: 'name', title: _('Name')) {
    f.textbox();
}
f.entry(field: 'home', title: _('Install directory')) {
    f.textbox();
}
