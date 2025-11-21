package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.data.EnumTypeConfig;
import com.pixelmonmod.pixelmon.enums.EnumType;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

@Mixin(
        value = EnumType.class,
        remap = false
)
public class MixinEnumType {
    @Mutable
    @Shadow @Final private static EnumType[] $VALUES;

    @Invoker("<init>")
    private static EnumType create(String enumName, int ordinal, int i, String s, int c, float texX, float texY) {
        throw new IllegalStateException("Unreachable");
    }

    @Unique
    private static EnumType fIPixelmon$create(String enumName, int i, String s, int c, float texX, float texY) {
        val enumType = create(enumName, $VALUES.length, i, s, c, texX, texY);
        $VALUES = ArrayUtils.add($VALUES, enumType);
        return enumType;
    }

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/EnumType;$VALUES:[Lcom/pixelmonmod/pixelmon/enums/EnumType;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private static void registerEnumType(CallbackInfo ci) {
        val files = FIPixelmon.typesFolder.listFiles();
        if (files != null) for (File file : files) {
            if (!file.isFile() || !file.getName().endsWith(".js")) continue;
            try (val reader = new FileReader(file)) {
                val config = FIPixelmon.GSON.fromJson(reader, EnumTypeConfig.class);
                config.inject(fIPixelmon$create(config.getEnumName(), config.getIndex(), config.getName(), config.getColor(), config.getTextureX(), config.getTextureY()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
