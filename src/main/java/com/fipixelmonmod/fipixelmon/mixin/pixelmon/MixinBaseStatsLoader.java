package com.fipixelmonmod.fipixelmon.mixin.pixelmon;


import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.adapter.EnumSpeciesAdapter;
import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.AttackBaseAdapter;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.AttackTypeAdapter;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStatsLoader;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.EvoConditionTypeAdapter;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.EvolutionTypeAdapter;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.EvoCondition;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.*;
import java.util.zip.ZipFile;

@Mixin(value = BaseStatsLoader.class, remap = false)
public class MixinBaseStatsLoader {
    @Mutable
    @Shadow
    @Final
    public static transient Gson GSON = (new GsonBuilder())
            .setPrettyPrinting()
            .registerTypeAdapter(EnumSpecies.class, EnumSpeciesAdapter.INSTANCE)
            .registerTypeAdapter(Evolution.class, new EvolutionTypeAdapter())
            .registerTypeAdapter(EvoCondition.class, new EvoConditionTypeAdapter())
            .registerTypeAdapter(Attack.class, new AttackTypeAdapter())
            .registerTypeAdapter(AttackBase.class, new AttackBaseAdapter())
            .registerTypeAdapter(PokemonSpec.class, PokemonSpec.SPEC_ADAPTER)
            .registerTypeHierarchyAdapter(ITechnicalMove.class, new ITechnicalMove.Adapter())
            .create();

    @Shadow
    private static void prepare(EnumSpecies species, BaseStats bs) {
    }

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
                zipFile = new ZipFile(config.getFromZip());
                insReader = new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("stats/" + species.getNationalPokedexNumber() + ".json")));
            } else {
                if (species.getNationalPokedexInteger() > 906 || config.isReplace())
                    insReader = new FileReader(FIPixelmon.statsFolder.getAbsolutePath() + File.separator + species.getNationalPokedexNumber() + ".json");
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
