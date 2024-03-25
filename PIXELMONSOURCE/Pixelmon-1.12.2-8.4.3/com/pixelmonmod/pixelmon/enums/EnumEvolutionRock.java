package com.pixelmonmod.pixelmon.enums;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public enum EnumEvolutionRock {
   MossyRock(new Biome[]{Biomes.field_76767_f, Biomes.field_76785_t}),
   IcyRock(new Biome[]{Biomes.field_76774_n, Biomes.field_76775_o});

   public Biome[] biomes;

   private EnumEvolutionRock(Biome[] biomes) {
      this.biomes = biomes;
   }

   public static EnumEvolutionRock getEvolutionRock(String name) {
      try {
         return valueOf(name);
      } catch (Exception var2) {
         return null;
      }
   }

   public static boolean hasEvolutionRock(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}
