package com.pixelmonmod.pixelmon.battles.attacks.animations;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.EnumEffectType;
import java.lang.reflect.Type;

public class AttackAnimationDataAdapter implements JsonDeserializer {
   public static final AttackAnimationDataAdapter ADAPTER = new AttackAnimationDataAdapter();

   public AttackAnimationData deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      String id;
      EnumEffectType[] var5;
      int var6;
      int var7;
      EnumEffectType effect;
      if (json.isJsonObject()) {
         id = json.getAsJsonObject().remove("id").getAsString();
         var5 = EnumEffectType.values();
         var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            effect = var5[var7];
            if (effect.name().equalsIgnoreCase(id)) {
               return (AttackAnimationData)ctx.deserialize(json, ((AttackAnimationData)effect.dataSupplier.get()).getClass());
            }
         }
      } else {
         id = json.getAsString();
         var5 = EnumEffectType.values();
         var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            effect = var5[var7];
            if (effect.name().equalsIgnoreCase(id)) {
               return (AttackAnimationData)effect.dataSupplier.get();
            }
         }
      }

      return null;
   }
}
