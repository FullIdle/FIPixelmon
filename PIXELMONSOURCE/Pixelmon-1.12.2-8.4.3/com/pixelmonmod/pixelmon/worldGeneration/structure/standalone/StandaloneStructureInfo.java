package com.pixelmonmod.pixelmon.worldGeneration.structure.standalone;

import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureInfo;
import java.util.ArrayList;
import net.minecraft.world.biome.Biome;

public class StandaloneStructureInfo extends StructureInfo {
   int rarity = 0;
   ArrayList biomes = new ArrayList();

   public void setRarity(int rarity) {
      this.rarity = rarity;
   }

   public int getRarity() {
      return this.rarity;
   }

   public void addBiome(Biome biome) {
      this.biomes.add(biome);
   }

   public ArrayList getBiomes() {
      return this.biomes;
   }
}
