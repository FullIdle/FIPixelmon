package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.adapter.EnumSpeciesAdapter;
import com.google.gson.*;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import com.pixelmonmod.pixelmon.api.spawning.util.SpawnConditionTypeAdapter;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Type;

@Mixin(value = SpawnConditionTypeAdapter.class, remap = false)
public class MixinSpawnConditionTypeAdapter {
    @Shadow
    @Mutable
    public static Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(EnumSpecies.class, EnumSpeciesAdapter.INSTANCE)
            .create();
}
