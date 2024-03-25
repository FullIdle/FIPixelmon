package com.pixelmonmod.tcg.api.card.attack;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.registries.AttackEffectRegistry;
import com.pixelmonmod.tcg.duel.attack.effects.BaseAttackEffect;
import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import java.lang.reflect.Type;
import org.apache.commons.lang3.StringUtils;

public class BaseAttackEffectTypeAdapter implements JsonDeserializer {
   public BaseAttackEffect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      if (json.isJsonObject()) {
         return null;
      } else {
         String data = json.getAsString();
         if (!StringUtils.isNotBlank(data)) {
            return null;
         } else {
            CoinSide needCoinSide = null;
            boolean isOptional = false;
            if (data.startsWith("HEAD-")) {
               needCoinSide = CoinSide.Head;
               data = data.substring(5);
            } else if (data.startsWith("TAIL-")) {
               needCoinSide = CoinSide.Tail;
               data = data.substring(5);
            } else if (data.startsWith("OPTIONAL-")) {
               isOptional = true;
               data = data.substring(9);
            }

            String[] parameters = data.split(":");
            String code = parameters[0];
            BaseAttackEffect baseAttackEffect = AttackEffectRegistry.get(code);
            if (baseAttackEffect == null) {
               TCG.logger.debug("Couldn't find attack effect with code: " + code);
               return null;
            } else {
               return baseAttackEffect.setOptional(isOptional).setRequiredCoinSide(needCoinSide).parse(parameters);
            }
         }
      }
   }
}
