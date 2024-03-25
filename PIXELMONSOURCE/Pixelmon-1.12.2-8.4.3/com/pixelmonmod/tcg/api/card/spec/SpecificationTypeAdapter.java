package com.pixelmonmod.tcg.api.card.spec;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class SpecificationTypeAdapter implements JsonSerializer, JsonDeserializer {
   private final Class clazz;

   public SpecificationTypeAdapter(Class clazz) {
      this.clazz = clazz;
   }

   public Specification deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return json.isJsonObject() ? (Specification)context.deserialize(json, this.clazz) : SpecificationFactory.create(this.clazz, json.getAsString());
   }

   public JsonElement serialize(Specification spec, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(spec.toString());
   }
}
