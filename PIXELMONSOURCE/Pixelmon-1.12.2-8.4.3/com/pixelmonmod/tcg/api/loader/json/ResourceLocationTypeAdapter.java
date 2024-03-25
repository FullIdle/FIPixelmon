package com.pixelmonmod.tcg.api.loader.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.util.ResourceLocation;

public class ResourceLocationTypeAdapter implements JsonSerializer, JsonDeserializer {
   public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      String text = json.getAsString();
      if (!text.contains(":")) {
         text = "tcg:" + text;
      }

      return new ResourceLocation(text);
   }

   public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
      String text = src.toString();
      if (!text.contains(":")) {
         text = "tcg:" + text;
      }

      return new JsonPrimitive(text);
   }
}
