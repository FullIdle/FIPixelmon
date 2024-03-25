package com.fipixelmonmod.fipixelmon.common.util;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class ReflectUtil {
    public static final Field enumSpeciesDex;
    static {
        Field field = null;
        try {
            field = EnumSpecies.class.getDeclaredField("nationalDex");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        enumSpeciesDex = field;
    }

    @SneakyThrows
    public static int getEnumSpeciesDex(EnumSpecies es){
        return (int) enumSpeciesDex.get(es);
    }
}
