package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.tcg.api.card.personalization.Board;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.util.ResourceLocation;

public class BoardRegistry {
   private static final ConcurrentMap REGISTRY = Maps.newConcurrentMap();
   public static final Board STANDARD = register(new Board("Default", "", (ResourceLocation)null));

   public static Board register(Board t) {
      REGISTRY.put(t.getName().toLowerCase(), t);
      return t;
   }

   public static Board get(String name) {
      return (Board)REGISTRY.get(name.toLowerCase());
   }

   public static Board getRandomBoard() {
      return (Board)RandomHelper.getRandomElementFromCollection(REGISTRY.values());
   }

   public static void load() {
   }
}
