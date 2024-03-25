package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AbilityCardRegistry {
   private static final Map REGISTRY = Maps.newConcurrentMap();

   public static AbilityCard get(String name) {
      return (AbilityCard)REGISTRY.get(name.toLowerCase());
   }

   public static List getAll() {
      return Lists.newArrayList(REGISTRY.values());
   }

   public static void load() {
      AbilityCard loadAbility;
      try {
         for(Iterator var0 = TCG.loader.loadAbilities().iterator(); var0.hasNext(); REGISTRY.put(loadAbility.getID().toLowerCase(), loadAbility)) {
            loadAbility = (AbilityCard)var0.next();
            if (loadAbility.getEffect() != null) {
               REGISTRY.put(loadAbility.getEffect().getCode().toLowerCase(), loadAbility);
            }
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
