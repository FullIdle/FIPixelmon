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

    @Override
    public boolean isTemporary() {
        return this.data.temporary;
    }

    @Setter
    @Getter
    public static class FormData {
        private String pokeName = "UNNAMED";
        private int form = -1;
        private String formName = "UNNAMED";
        private String model = null;
        private String flyingModel = null;
        private boolean temporary = true;
        private String geoModel = null;
        private String geoAnimation = null;
        @Setter
        private transient EnumForm enumForm;
    }
}
