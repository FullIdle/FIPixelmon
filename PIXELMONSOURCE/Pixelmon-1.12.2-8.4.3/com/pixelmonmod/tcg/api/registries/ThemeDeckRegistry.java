package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ThemeDeck;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ThemeDeckRegistry {
   private static final Map NAME_REGISTRY = Maps.newConcurrentMap();
   private static final Map ID_REGISTRY = Maps.newConcurrentMap();

   public static ThemeDeck get(String name) {
      return (ThemeDeck)NAME_REGISTRY.get(name.toLowerCase());
   }

   public static ThemeDeck get(int id) {
      return (ThemeDeck)ID_REGISTRY.get(id);
   }

   public static List getAll() {
      return Lists.newArrayList(NAME_REGISTRY.values());
   }

   public static ThemeDeck getRandom() {
      return (ThemeDeck)RandomHelper.getRandomElementFromCollection(getAll());
   }

   public static void load() {
      try {
         Iterator var0 = TCG.loader.loadThemeDecks().iterator();

         while(var0.hasNext()) {
            ThemeDeck loadThemeDeck = (ThemeDeck)var0.next();
            NAME_REGISTRY.put(loadThemeDeck.getName().toLowerCase(), loadThemeDeck);
            ID_REGISTRY.put(loadThemeDeck.getID(), loadThemeDeck);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
