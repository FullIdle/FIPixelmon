package com.pixelmonmod.pixelmon.api.spawning.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import java.lang.reflect.Type;

public class SpawnConditionTypeAdapter implements JsonDeserializer {
   public static Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

   public SpawnCondition deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      JsonObject obj = (JsonObject)element;

      try {
         return (SpawnCondition)gson.fromJson(obj, SpawnCondition.targetedSpawnCondition);
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }
}
