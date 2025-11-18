package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.data.MegaStoneConfig;
import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import lombok.SneakyThrows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;

@Mixin(value = EnumMegaPokemon.class, remap = false)
public class MixinEnumMegaPokemon {
    @SneakyThrows
    @Inject(
            method = "<clinit>",
            at = @At(value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/EnumMegaPokemon;$VALUES:[Lcom/pixelmonmod/pixelmon/enums/EnumMegaPokemon;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private static void regEnumMegaPokemon(CallbackInfo ci) {
        File[] files = FIPixelmon.megastoneFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    MegaStoneConfig config = FIPixelmon.GSON.fromJson(new FileReader(file), MegaStoneConfig.class);
                    config.inject();
                }
            }
        }

        File[] list = FIPixelmon.fiPixelmonFolder.listFiles();
        if (list != null) {
            for (File file : list) {
                if (!FileHelper.isZip(file)) continue;
                ZipFile zipFile = new ZipFile(file);
                for (String path : FileHelper.getZipFileList(zipFile, "megastone")) {
                    if (path.endsWith(".json")) {
                        InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(zipFile.getEntry(path)));
                        MegaStoneConfig config = FIPixelmon.GSON.fromJson(reader, MegaStoneConfig.class);
                        config.inject();
                        reader.close();
                    }
                }
                zipFile.close();
            }
        }
    }
}
