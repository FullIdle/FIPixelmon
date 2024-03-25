package com.pixelmonmod.pixelmon.api.attackAnimations;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class AttackAnimationTypeAdapter implements JsonSerializer, JsonDeserializer {
   public static final AttackAnimationTypeAdapter ADAPTER = new AttackAnimationTypeAdapter();

   public JsonElement serialize(AttackAnimation animation, Type type, JsonSerializationContext ctx) {
      return ctx.serialize(AttackAnimationRegistry.getKeyForAnimation(animation));
   }

   public AttackAnimation deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      try {
         return json.isJsonObject() ? (AttackAnimation)ctx.deserialize(json, (Type)AttackAnimationRegistry.animations.get(json.getAsJsonObject().remove("id").getAsString())) : (AttackAnimation)((Class)AttackAnimationRegistry.animations.get(json.getAsString())).newInstance();
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }
}
