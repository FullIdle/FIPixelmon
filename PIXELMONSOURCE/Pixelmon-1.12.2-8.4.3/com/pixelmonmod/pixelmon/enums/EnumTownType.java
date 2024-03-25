package com.pixelmonmod.pixelmon.enums;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public enum EnumTownType {
   basicTown(0, 5, Biomes.field_76772_c, Blocks.field_150347_e, "resources/pixelmon/structures/basicTown/"),
   desertTown(1, 5, Biomes.field_76769_d, Blocks.field_150322_A, "resources/pixelmon/structures/desertTown/"),
   snowTown(2, 5, Biomes.field_76774_n, Blocks.field_150417_aV, "resources/pixelmon/structures/snowTown/");

   public int townId;
   public int rarity;
   public Biome biome;
   public Block block;
   public String folderPath;

   private EnumTownType(int townId, int rarity, Biome biome, Block pathBlock, String folderPath) {
      this.townId = townId;
      this.rarity = rarity;
      this.biome = biome;
      this.block = pathBlock;
      this.folderPath = folderPath;
   }

   public int getId() {
      return this.townId;
   }

   public int getRarity() {
      return this.rarity;
   }

   public Biome getBiome() {
      return this.biome;
   }

   public Block getPathBlock() {
      return this.block;
   }

   public static EnumTownType getTownFromBiome(Biome biome) {
      EnumTownType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumTownType e = var1[var3];
         if (e.getBiome() == biome) {
            return e;
         }
      }

      return null;
   }

   public static EnumTownType getTownFromId(int Id) {
      EnumTownType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumTownType e = var1[var3];
         if (e.getId() == Id) {
            return e;
         }
      }

      return null;
   }

   public static boolean hasTownType(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}
