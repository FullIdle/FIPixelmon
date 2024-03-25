package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.set.CardSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CardSetRegistry {
   private static final Map NAME_REGISTRY = Maps.newConcurrentMap();
   private static final Map ID_REGISTRY = Maps.newConcurrentMap();

   public static CardSet get(String name) {
      return (CardSet)NAME_REGISTRY.get(name.toLowerCase());
   }

   public static CardSet get(int id) {
      return (CardSet)ID_REGISTRY.get(id);
   }

   public static List getAll() {
      return Lists.newArrayList(NAME_REGISTRY.values());
   }

   public static CardSet getRandom() {
      CardSet randomElementFromCollection;
      for(randomElementFromCollection = (CardSet)RandomHelper.getRandomElementFromCollection(getAll()); !randomElementFromCollection.hasPack(); randomElementFromCollection = (CardSet)RandomHelper.getRandomElementFromCollection(getAll())) {
      }

      return randomElementFromCollection;
   }

   public static void load() {
      try {
         Iterator var0 = TCG.loader.loadCardSets().iterator();

         while(var0.hasNext()) {
            CardSet loadAttack = (CardSet)var0.next();
            NAME_REGISTRY.put(loadAttack.getName().toLowerCase(), loadAttack);
            ID_REGISTRY.put(loadAttack.getID(), loadAttack);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
