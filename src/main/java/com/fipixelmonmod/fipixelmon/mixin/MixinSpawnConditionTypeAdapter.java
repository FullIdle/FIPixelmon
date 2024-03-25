package com.fipixelmonmod.fipixelmon.mixin;

import com.fipixelmonmod.fipixelmon.common.adapter.EnumSpeciesAdapter;
import com.google.gson.*;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import com.pixelmonmod.pixelmon.api.spawning.util.SpawnConditionTypeAdapter;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Type;

@Mixin(value = SpawnConditionTypeAdapter.class,remap = false)
public class MixinSpawnConditionTypeAdapter {
    @Shadow public static Gson gson;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public SpawnCondition deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = (JsonObject)element;
        try {
            return new GsonBuilder().registerTypeAdapter(EnumSpecies.class, EnumSpeciesAdapter.INSTANCE)
                    .create().fromJson(obj,SpawnCondition.class);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }
}
