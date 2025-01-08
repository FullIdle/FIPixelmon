package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.data.PokeBallConfig;
import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import lombok.SneakyThrows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;

@Mixin(value = EnumPokeballs.class,remap = false)
public class MixinEnumPokeballs {
    @SneakyThrows
    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/items/EnumPokeballs;$VALUES:[Lcom/pixelmonmod/pixelmon/enums/items/EnumPokeballs;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private static void regBallEnum(CallbackInfo ci){
        File[] files = FIPixelmon.pokeballFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    PokeBallConfig config = FIPixelmon.GSON.fromJson(new FileReader(file), PokeBallConfig.class);
                    config.inject();
                }
            }
        }
        File[] list = FIPixelmon.fiPixelmonFolder.listFiles();
        if (list != null) {
            for (File file : list) {
                if (!FileHelper.isZip(file)) continue;
                ZipFile zipFile = new ZipFile(file);
                for (String path : FileHelper.getZipFileList(zipFile, "pokeball")) {
                    if (path.endsWith(".json")) {
                        InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(zipFile.getEntry(path)));
                        PokeBallConfig config = FIPixelmon.GSON.fromJson(reader, PokeBallConfig.class);
                        config.inject();
                        reader.close();
                    }
                }
                zipFile.close();
            }
        }
    }
}
