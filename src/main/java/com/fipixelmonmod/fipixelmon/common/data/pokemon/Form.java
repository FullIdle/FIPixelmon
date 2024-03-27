package com.fipixelmonmod.fipixelmon.common.data.pokemon;

import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;

public enum Form implements IEnumForm {
    ;

    public String getFormSuffix() {
        return "-" + this.name().toLowerCase();
    }

    public byte getForm() {
        return (byte)this.ordinal();
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getUnlocalizedName() {
        return null;
    }
}
