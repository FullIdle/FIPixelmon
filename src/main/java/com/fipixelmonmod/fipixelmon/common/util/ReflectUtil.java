package com.fipixelmonmod.fipixelmon.common.util;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ReflectUtil {
    public static final Field enumSpeciesDex;
    static {
        enumSpeciesDex = ReflectionHelper.findField(EnumSpecies.class,"nationalDex","nationalDex");
        enumSpeciesDex.setAccessible(true);
    }

    @SneakyThrows
    public static int getEnumSpeciesDex(EnumSpecies es){
        return (int) enumSpeciesDex.get(es);
    }
}
