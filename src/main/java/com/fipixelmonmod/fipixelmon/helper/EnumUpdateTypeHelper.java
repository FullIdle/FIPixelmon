package com.fipixelmonmod.fipixelmon.helper;

import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import lombok.Getter;
import net.minecraftforge.common.util.EnumHelper;

public class EnumUpdateTypeHelper {
    @Getter
    private static EnumUpdateType TeraType;

    public static void inject() {
        TeraType = EnumHelper.addEnum(EnumUpdateType.class, "TeraType", new Class[0]);
    }
}
