package com.pixelmonmod.tcg.api.loader.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pixelmonmod.tcg.api.registries.AbilityEffectRegistry;
import com.pixelmonmod.tcg.duel.ability.BaseAbilityEffect;
import java.lang.reflect.Type;

public class AbilityEffectTypeAdapter implements JsonSerializer, JsonDeserializer {
   public BaseAbilityEffect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return !json.isJsonPrimitive() ? null : AbilityEffectRegistry.get(json.getAsString());
   }

   public JsonElement serialize(BaseAbilityEffect src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(src.getCode());
   }
}
