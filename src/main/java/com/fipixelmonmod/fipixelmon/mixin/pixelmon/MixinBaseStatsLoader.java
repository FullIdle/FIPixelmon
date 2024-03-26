package com.fipixelmonmod.fipixelmon.mixin.pixelmon;


import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.common.adapter.EnumSpeciesAdapter;
import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.fipixelmonmod.fipixelmon.common.util.ReflectUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
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

import java.io.*;

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
            .create();;

    @Shadow
    private static void prepare(EnumSpecies species, BaseStats bs) {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static BaseStats getBaseStatsFromAssets(EnumSpecies species) throws IOException {
        String path;
        InputStreamReader insReader;
        if (Cache.extraEnumSpecies.contains(species)) {
            path = FIPixelmon.statsFolder.getAbsolutePath() + File.separator + ReflectUtil.getEnumSpeciesDex(species) + ".json";
            insReader = new FileReader(path);
        } else {
            path = "/assets/pixelmon/stats/" + species.getNationalPokedexNumber() + ".json";
            insReader = new InputStreamReader(BaseStats.class.getResourceAsStream(path));
        }
        try (Reader reader = insReader) {
            BaseStats bs = GSON.fromJson(reader, BaseStats.class);
            prepare(species, bs);
            insReader.close();
            return bs;
        } catch (Exception e) {
            Pixelmon.LOGGER.error("Couldn't load internal stat JSON: " + path);
            throw e;
        }
    }
}
