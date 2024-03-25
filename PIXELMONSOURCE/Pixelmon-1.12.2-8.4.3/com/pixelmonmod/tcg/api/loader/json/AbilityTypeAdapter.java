package com.pixelmonmod.tcg.api.loader.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import com.pixelmonmod.tcg.api.registries.AbilityCardRegistry;
import java.lang.reflect.Type;

public class AbilityTypeAdapter implements JsonSerializer, JsonDeserializer {
   public AbilityCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return json.isJsonObject() ? null : AbilityCardRegistry.get(json.getAsString());
   }

   public JsonElement serialize(AbilityCard src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject object = new JsonObject();
      object.addProperty("id", src.getID());
      object.addProperty("type", src.getType().name());
      return object;
   }
}
