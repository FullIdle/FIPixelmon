package com.pixelmonmod.pixelmon.listener;

import com.google.common.collect.ImmutableList;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PixelmonLootTables {
   private static final List TABLES = ImmutableList.of("inject/abandoned_mineshaft", "inject/desert_pyramid", "inject/end_city_treasure", "inject/igloo_chest", "inject/jungle_temple", "inject/jungle_temple_dispenser", "inject/nether_bridge", "inject/simple_dungeon", "inject/spawn_bonus_chest", "inject/stronghold_corridor", "inject/stronghold_crossing", "inject/stronghold_library", new String[]{"inject/village_blacksmith", "inject/woodland_mansion"});

   public static void register() {
      if (PixelmonConfig.injectIntoLootTables) {
         Iterator var0 = TABLES.iterator();

         while(var0.hasNext()) {
            String str = (String)var0.next();
            if (!PixelmonConfig.lootIgnoreList.contains(str.split("\\/")[1])) {
               LootTableList.func_186375_a(new ResourceLocation("pixelmon", str));
            }
         }

      }
   }

   @SubscribeEvent
   public static void onLootTableLoad(LootTableLoadEvent event) {
      if (PixelmonConfig.injectIntoLootTables) {
         String prefix = "minecraft:chests/";
         String name = event.getName().toString();
         if (name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            if (PixelmonConfig.lootIgnoreList.contains(file)) {
               return;
            }

            switch (file) {
               case "abandoned_mineshaft":
               case "desert_pyramid":
               case "end_city_treasure":
               case "igloo_chest":
               case "jungle_temple":
               case "jungle_temple_dispenser":
               case "nether_bridge":
               case "simple_dungeon":
               case "spawn_bonus_chest":
               case "stronghold_corridor":
               case "stronghold_crossing":
               case "stronghold_library":
               case "village_blacksmith":
               case "woodland_mansion":
                  event.getTable().addPool(getInjectPool(file));
            }
         }

      }
   }

   private static LootPool getInjectPool(String entryName) {
      return new LootPool(new LootEntry[]{getInjectEntry(entryName, 1)}, new LootCondition[0], new RandomValueRange(1.0F), new RandomValueRange(0.0F, 1.0F), "pixelmon_inject_pool");
   }

   private static LootEntryTable getInjectEntry(String name, int weight) {
      return new LootEntryTable(new ResourceLocation("pixelmon", "inject/" + name), weight, 0, new LootCondition[0], "pixelmon_inject_entry");
   }
}
