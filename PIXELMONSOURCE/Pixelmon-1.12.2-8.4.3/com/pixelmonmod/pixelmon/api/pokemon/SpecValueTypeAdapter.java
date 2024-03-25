package com.pixelmonmod.pixelmon.api.pokemon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class SpecValueTypeAdapter implements JsonDeserializer {
   public SpecValue deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      String key = ((JsonObject)json).get("key").getAsString();
      ISpecType specType = PokemonSpec.getSpecForKey(key);
      return specType == null ? null : (SpecValue)ctx.deserialize(json, specType.getSpecClass());
   }
}
