package org.smartregister.goldsmith.domain;

import org.smartregister.view.dialog.DialogOption;

public class RegisterSelectDialogOption implements DialogOption {
    String name;

    public RegisterSelectDialogOption(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }
}
