package com.fipixelmonmod.fipixelmon.enums;

import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum EnumForm implements IEnumForm {
    ;
    private final FormData data;

    EnumForm(FormData data) {
        this.data = data;
    }

    public String getFormSuffix() {
        return "-" + this.name().toLowerCase();
    }

    @Override
    public byte getForm() {
        return (byte) data.getForm();
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getUnlocalizedName() {
        return "pixelmon." + data.pokeName.toLowerCase() + ".form." + this.name().toLowerCase();
    }

    @Setter
    @Getter
    public static class FormData {
        private String pokeName;
        private int form;
        private String formName;
        private String model;
        private String flyingModel;
        @Setter
        private transient EnumForm enumForm;
    }
}
