package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class EvolutionTypeAdapter implements JsonDeserializer, JsonSerializer {
   public Evolution deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      return (Evolution)ctx.deserialize(json, (Type)Evolution.evolutionTypes.get(((JsonObject)json).get("evoType").getAsString()));
   }

   public JsonElement serialize(Evolution evolution, Type type, JsonSerializationContext ctx) {
      return ctx.serialize(evolution, (Type)Evolution.evolutionTypes.get(evolution.evoType));
   }
}
