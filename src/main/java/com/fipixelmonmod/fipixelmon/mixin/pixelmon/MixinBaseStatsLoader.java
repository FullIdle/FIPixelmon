package com.fipixelmonmod.fipixelmon.mixin.pixelmon;


import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStatsLoader;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mixin(value = BaseStatsLoader.class, remap = false)
public class MixinBaseStatsLoader {
    @Shadow
    private static void prepare(EnumSpecies species, BaseStats bs) {
    }

    @Shadow @Final public static transient Gson GSON;

    @Inject(
            method = "getBaseStatsFromAssets",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getBaseStatsFromAssets(EnumSpecies species, CallbackInfoReturnable<BaseStats> cir) throws IOException {
        if (PokemonConfig.extraPokemonConfig.containsKey(species)) {
            PokemonConfig config = PokemonConfig.extraPokemonConfig.get(species);
            InputStreamReader insReader = null;
            ZipFile zipFile = null;
            if (config.isFromZip()) {
                ZipEntry entry = (zipFile = new ZipFile(config.getFromZip()))
                        .getEntry("stats/" + species.getNationalPokedexNumber() + ".json");
                if (entry != null) insReader = new InputStreamReader(zipFile.getInputStream(entry));
            } else {
                File file = new File(FIPixelmon.statsFolder.getAbsolutePath() + File.separator + species.getNationalPokedexNumber() + ".json");
                if (file.exists()) insReader = new FileReader(file);
            }
            if (insReader != null) {
                BaseStats bs = GSON.fromJson(insReader, BaseStats.class);
                prepare(species, bs);
                insReader.close();
                if (zipFile != null) zipFile.close();
                cir.setReturnValue(bs);
                cir.cancel();
            }
        }
    }
}
