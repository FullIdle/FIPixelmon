package com.pixelmonmod.pixelmon.worldGeneration.loot;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class PixelmonLootTableList {
   public static final ResourceLocation ULTRA_DESERT = register("chests/ultra_desert");
   public static final ResourceLocation POKEMON_CENTER = register("chests/pokemon_center");
   public static final ResourceLocation DROWNED_DUNGEON = register("chests/drowned_dungeon");
   public static final ResourceLocation BOAT_MAGIKARP = register("chests/boat_magikarp");
   public static final ResourceLocation BOAT_WOOD = register("chests/boat_wood");
   public static final ResourceLocation BOAT_QUARTZ = register("chests/boat_quartz");
   public static final ResourceLocation TOWER_OF_WATERS = register("chests/watertower");
   public static final ResourceLocation TOWER_OF_DARKNESS = register("chests/darknesstower");

   private static ResourceLocation register(String id) {
      return LootTableList.func_186375_a(new ResourceLocation("pixelmon", id));
   }
}
