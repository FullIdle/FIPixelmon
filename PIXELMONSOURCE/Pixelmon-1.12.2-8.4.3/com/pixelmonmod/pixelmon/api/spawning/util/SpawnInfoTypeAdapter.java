package com.pixelmonmod.pixelmon.api.spawning.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import java.lang.reflect.Type;

public class SpawnInfoTypeAdapter implements JsonSerializer, JsonDeserializer {
   public SpawnInfo deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      JsonObject obj = (JsonObject)element;

      try {
         return (SpawnInfo)ctx.deserialize(obj, (Type)SpawnInfo.spawnInfoTypes.get(obj.get("typeID").getAsString()));
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public JsonElement serialize(SpawnInfo spawnInfo, Type type, JsonSerializationContext ctx) {
      spawnInfo.onExport();
      return ctx.serialize(((Class)SpawnInfo.spawnInfoTypes.get(spawnInfo.typeID)).cast(spawnInfo));
   }
}
