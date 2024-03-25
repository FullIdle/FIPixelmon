package com.pixelmonmod.pixelmon.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.ResourceLocation;

public class ResourceLocationAdapter implements JsonDeserializer {
   public static final ResourceLocationAdapter ADAPTER = new ResourceLocationAdapter();

   public ResourceLocation deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      return new ResourceLocation(json.getAsString());
   }
}
