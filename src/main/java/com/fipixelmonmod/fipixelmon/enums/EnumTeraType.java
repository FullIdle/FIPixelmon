package com.fipixelmonmod.fipixelmon.enums;

import com.pixelmonmod.pixelmon.util.ITranslatable;
import net.minecraft.util.IStringSerializable;

public enum EnumTeraType implements ITranslatable, IStringSerializable {
    Stellar,
    Normal,
    Fire,
    Water,
    Electric,
    Grass,
    Ice,
    Fighting,
    Poison,
    Ground,
    Flying,
    Psychic,
    Bug,
    Rock,
    Ghost,
    Dragon,
    Dark,
    Steel,
    Fairy;

    public static final String TERA_TYPE_KEY = "TeraType";

    @Override
    public String getUnlocalizedName() {
        return "teratype."+this.getName().toLowerCase()+".name";
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static final EnumTeraType[] VALUE = values();

    public static EnumTeraType getEnumTeraTypeFormId(int id) {
        if (id < 0 || id >= VALUE.length) return null;
        return VALUE[id];
    }

    public static EnumTeraType getEnumTeraTypeFormName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            for (EnumTeraType type : VALUE)
                if (type.name().equalsIgnoreCase(name) || type.getName().equalsIgnoreCase(name)) return type;
        }
        return null;
    }
}
