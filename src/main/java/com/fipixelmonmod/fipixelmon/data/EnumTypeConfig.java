package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @see com.pixelmonmod.pixelmon.enums.EnumType
 * @see com.fipixelmonmod.fipixelmon.mixin.pixelmon.MixinEnumType
 */
@Getter
@Setter //给GSON通行的
public class EnumTypeConfig {
    private int index;
    private String enumName;
    private String name;
    private int color;
    private float textureX;
    private float textureY;

    transient private EnumType enumType;

    public static final Map<String, EnumTypeConfig> extraTypes = new HashMap<>();

    public void inject(EnumType type) {
        this.enumType = type;
        extraTypes.put(name, this);
    }
}
