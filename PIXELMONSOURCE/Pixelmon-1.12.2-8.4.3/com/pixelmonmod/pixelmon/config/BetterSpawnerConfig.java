package com.pixelmonmod.pixelmon.config;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.spawning.CompositeSpawnCondition;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import com.pixelmonmod.pixelmon.api.spawning.util.SpawnConditionTypeAdapter;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class BetterSpawnerConfig {
   public static BetterSpawnerConfig INSTANCE = null;
   public static final String PATH = "config/pixelmon/BetterSpawnerConfig.json";
   public static final HashMap intervalTimes = new HashMap();
   public CompositeSpawnCondition globalCompositeCondition = new CompositeSpawnCondition();
   public ArrayList globalRarityMultipliers = new ArrayList();
   public HashMap autoTagSpecs = new HashMap();
   public HashMap intervalSeconds = new HashMap();
   private HashMap blockCategories = new HashMap();
   private HashMap biomeCategories = new HashMap();
   public transient Set landBlockCategory = Sets.newHashSet();
   public transient Set waterBlockCategory = Sets.newHashSet();
   public transient Set airBlockCategory = Sets.newHashSet();
   public transient Set lavaBlockCategory = Sets.newHashSet();
   public transient Set seesSkyBlockCategory = Sets.newHashSet();
   public transient Set treeTopBlockCategory = Sets.newHashSet();
   public transient Set structureBlockCategory = Sets.newHashSet();
   public transient HashMap cachedBlockCategories = new HashMap();
   public transient HashMap cachedBiomeCategories = new HashMap();

   public static void load() {
      try {
         Gson gson = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(SpawnCondition.class, new SpawnConditionTypeAdapter()).registerTypeAdapter(PokemonSpec.class, PokemonSpec.SPEC_ADAPTER).create();
         if (!PixelmonConfig.useBetterSpawnerConfig) {
            InputStream stream = ServerNPCRegistry.class.getResourceAsStream("/assets/pixelmon/spawning/BetterSpawnerConfig.json");
            INSTANCE = (BetterSpawnerConfig)gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BetterSpawnerConfig.class);
         } else {
            File file = new File("config/pixelmon/BetterSpawnerConfig.json");
            file.getParentFile().mkdirs();
            INSTANCE = null;

            try {
               if (file.exists()) {
                  INSTANCE = (BetterSpawnerConfig)gson.fromJson(new FileReader("config/pixelmon/BetterSpawnerConfig.json"), BetterSpawnerConfig.class);
               }
            } catch (Exception var4) {
               var4.printStackTrace();
            }

            if (INSTANCE == null) {
               InputStream stream = ServerNPCRegistry.class.getResourceAsStream("/assets/pixelmon/spawning/BetterSpawnerConfig.json");
               INSTANCE = (BetterSpawnerConfig)gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BetterSpawnerConfig.class);
               if (!file.exists()) {
                  PrintWriter pw = new PrintWriter(file);
                  pw.write(gson.toJson(INSTANCE));
                  pw.flush();
                  pw.close();
               } else {
                  Pixelmon.LOGGER.error("Could not parse the BetterSpawnerConfig.json. Check it in a JSON parser. Using default options.");
               }
            }
         }

         INSTANCE.cachedBlockCategories = convert(INSTANCE.blockCategories, Block.class);
         INSTANCE.cachedBiomeCategories = convert(INSTANCE.biomeCategories, Biome.class);
         INSTANCE.landBlockCategory = (Set)INSTANCE.cachedBlockCategories.get("land");
         INSTANCE.waterBlockCategory = (Set)INSTANCE.cachedBlockCategories.get("water");
         INSTANCE.airBlockCategory = (Set)INSTANCE.cachedBlockCategories.get("air");
         INSTANCE.lavaBlockCategory = (Set)INSTANCE.cachedBlockCategories.get("lava");
         INSTANCE.seesSkyBlockCategory = (Set)INSTANCE.cachedBlockCategories.get("seesSkyException");
         INSTANCE.treeTopBlockCategory = (Set)INSTANCE.cachedBlockCategories.get("treeTop");
         INSTANCE.structureBlockCategory = (Set)INSTANCE.cachedBlockCategories.get("structure");
         if (INSTANCE.globalCompositeCondition != null) {
            INSTANCE.globalCompositeCondition.onImport();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private static HashMap convert(HashMap values, Class clazz) {
      HashMap converted = new HashMap();
      IForgeRegistry registry = RegistryManager.ACTIVE.getRegistry(clazz);

      Map.Entry entry;
      HashSet ts;
      label38:
      for(Iterator var4 = values.entrySet().iterator(); var4.hasNext(); converted.put(entry.getKey(), ts)) {
         entry = (Map.Entry)var4.next();
         ts = Sets.newHashSet();
         if (entry.getValue() != null) {
            Iterator var7 = ((ArrayList)entry.getValue()).iterator();

            while(true) {
               while(true) {
                  if (!var7.hasNext()) {
                     continue label38;
                  }

                  String string = (String)var7.next();
                  Iterator var9 = registry.getEntries().iterator();

                  while(var9.hasNext()) {
                     Map.Entry entry2 = (Map.Entry)var9.next();
                     if (((ResourceLocation)entry2.getKey()).toString().equalsIgnoreCase(string) || ((ResourceLocation)entry2.getKey()).func_110623_a().equalsIgnoreCase(string)) {
                        ts.add(entry2.getValue());
                        break;
                     }
                  }
               }
            }
         }
      }

      return converted;
   }

   public static boolean checkInterval(String interval) {
      if (interval == null) {
         return true;
      } else if (!INSTANCE.intervalSeconds.containsKey(interval)) {
         return true;
      } else if ((Integer)INSTANCE.intervalSeconds.get(interval) == -1) {
         return false;
      } else {
         Long last = (Long)intervalTimes.get(interval);
         if (last != null && INSTANCE.intervalSeconds.containsKey(interval)) {
            return last < System.currentTimeMillis();
         } else {
            return true;
         }
      }
   }

   public static void consumeInterval(String interval) {
      if (INSTANCE.intervalSeconds.containsKey(interval)) {
         intervalTimes.put(interval, System.currentTimeMillis() + (long)((Integer)INSTANCE.intervalSeconds.get(interval) * 1000));
      }

   }

   public static Set getLandBlocks() {
      return INSTANCE.landBlockCategory;
   }

   public static Set getWaterBlocks() {
      return INSTANCE.waterBlockCategory;
   }

   public static Set getLavaBlocks() {
      return INSTANCE.lavaBlockCategory;
   }

   public static Set getAirBlocks() {
      return INSTANCE.airBlockCategory;
   }

   public static Set getSeesSkyExceptionBlocks() {
      return INSTANCE.seesSkyBlockCategory;
   }

   public static Set getStructureBlocks() {
      return INSTANCE.structureBlockCategory;
   }

   public static Set getTreeTopBlocks() {
      return INSTANCE.treeTopBlockCategory;
   }

   public static Set getBlockCategory(String blockCategory) {
      return (Set)(INSTANCE.cachedBlockCategories.containsKey(blockCategory) ? (Set)INSTANCE.cachedBlockCategories.get(blockCategory) : Sets.newHashSet());
   }

   public static boolean doesBlockSeeSky(IBlockState state) {
      if (state == null) {
         return false;
      } else {
         return getAirBlocks().contains(state.func_177230_c()) || getSeesSkyExceptionBlocks().contains(state.func_177230_c()) || !state.func_191058_s();
      }
   }
}
