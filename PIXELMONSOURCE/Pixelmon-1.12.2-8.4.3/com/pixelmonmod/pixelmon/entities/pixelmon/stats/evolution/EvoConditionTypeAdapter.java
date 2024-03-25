package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.EvoCondition;
import java.lang.reflect.Type;

public class EvoConditionTypeAdapter implements JsonDeserializer, JsonSerializer {
   public EvoCondition deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      return (EvoCondition)ctx.deserialize(json, (Type)EvoCondition.evoConditionTypes.get(((JsonObject)json).get("evoConditionType").getAsString()));
   }

   public JsonElement serialize(EvoCondition src, Type type, JsonSerializationContext ctx) {
      return ctx.serialize(src, (Type)EvoCondition.evoConditionTypes.get(src.evoConditionType));
   }
}
