package com.pixelmonmod.pixelmon.api.spawning.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
import java.lang.reflect.Type;

public class SpawnSetTypeAdapter implements JsonDeserializer {
   public SpawnSet deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      try {
         SpawnSet spawnSet = (SpawnSet)SpawnInfo.GSON.fromJson((JsonObject)element, SpawnSet.class);
         spawnSet.onImport();
         return spawnSet;
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }
}
