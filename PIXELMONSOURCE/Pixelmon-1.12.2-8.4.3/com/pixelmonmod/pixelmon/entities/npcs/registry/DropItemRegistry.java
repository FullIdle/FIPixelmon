package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.raids.WeightedItemStacks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.RemapHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity8HoldsItems;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalMachines;
import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalRecords;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBlankTechnicalMachine;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class DropItemRegistry {
   public static ArrayList tier1 = new ArrayList();
   public static ArrayList tier2 = new ArrayList();
   public static ArrayList tier3 = new ArrayList();
   public static ArrayList ultraSpace = new ArrayList();
   public static Map pokemonDrops = new HashMap();
   public static HashMap bossDrops = new HashMap();
   public static HashMap raidDrops = new HashMap();
   private static final Set futureDrops = new HashSet(Arrays.asList("pixelmon:clever_wing", "pixelmon:genius_wing", "pixelmon:honey"));

   public static void registerDropItems() throws FileNotFoundException {
      Pixelmon.LOGGER.info("Registering all drops.");
      String path = Pixelmon.modDirectory + "/pixelmon/drops/";
      File dropsDir = new File(path);
      if (PixelmonConfig.useExternalJSONFilesDrops && !dropsDir.isDirectory()) {
         File baseDir = new File(Pixelmon.modDirectory + "/pixelmon");
         if (!baseDir.isDirectory()) {
            baseDir.mkdir();
         }

         Pixelmon.LOGGER.info("Creating drops directory.");
         dropsDir.mkdir();
         extractDropsDir(dropsDir);
      }

      Object iStream;
      if (!PixelmonConfig.useExternalJSONFilesDrops) {
         iStream = DropItemRegistry.class.getResourceAsStream("/assets/pixelmon/drops/pokechestdrops.json");
      } else {
         iStream = new FileInputStream(new File(dropsDir, "pokechestdrops.json"));
      }

      JsonObject json = (new JsonParser()).parse(new InputStreamReader((InputStream)iStream, StandardCharsets.UTF_8)).getAsJsonObject();
      registerTierDrops(json, "tier1", tier1);
      registerTierDrops(json, "tier2", tier2);
      registerTierDrops(json, "tier3", tier3);
      registerTierDrops(json, "ultraSpace", ultraSpace);
      registerPokemonDrops();
      if (!PixelmonConfig.useExternalJSONFilesDrops) {
         iStream = DropItemRegistry.class.getResourceAsStream("/assets/pixelmon/drops/raiddrops.json");
      } else {
         iStream = new FileInputStream(new File(dropsDir, "raiddrops.json"));
      }

      JsonObject jsonObject = (new JsonParser()).parse(new InputStreamReader((InputStream)iStream, StandardCharsets.UTF_8)).getAsJsonObject();
      registerRaidDrops(jsonObject);
      if (!PixelmonConfig.useExternalJSONFilesDrops) {
         iStream = DropItemRegistry.class.getResourceAsStream("/assets/pixelmon/drops/bossdrops.json");
      } else {
         iStream = new FileInputStream(new File(dropsDir, "bossdrops.json"));
      }

      jsonObject = (new JsonParser()).parse(new InputStreamReader((InputStream)iStream, StandardCharsets.UTF_8)).getAsJsonObject();
      registerBossDrops(jsonObject);
      if (!PixelmonConfig.useExternalJSONFilesDrops) {
         iStream = DropItemRegistry.class.getResourceAsStream("/assets/pixelmon/drops/blankimprintrate.json");
      } else {
         iStream = new FileInputStream(new File(dropsDir, "blankimprintrate.json"));
      }

      jsonObject = (new JsonParser()).parse(new InputStreamReader((InputStream)iStream, StandardCharsets.UTF_8)).getAsJsonObject();
      registerBlankImprintRates(jsonObject);
   }

   private static void registerPokemonDrops() throws FileNotFoundException {
      String path = Pixelmon.modDirectory + "/pixelmon/drops/";
      File dropsDir = new File(path);
      Object iStream;
      if (!PixelmonConfig.useExternalJSONFilesDrops) {
         iStream = DropItemRegistry.class.getResourceAsStream("/assets/pixelmon/drops/pokedrops.json");
      } else {
         iStream = new FileInputStream(new File(dropsDir, "pokedrops.json"));
      }

      JsonArray json = (new JsonParser()).parse(new InputStreamReader((InputStream)iStream, StandardCharsets.UTF_8)).getAsJsonArray();
      registerPokemonDrops(json);
   }

   private static void registerBlankImprintRates(JsonObject jsonObject) {
      ItemBlankTechnicalMachine.maxImprintCount.clear();
      Gen8TechnicalMachines[] var1 = Gen8TechnicalMachines.values();
      int var2 = var1.length;

      int var3;
      int rate;
      for(var3 = 0; var3 < var2; ++var3) {
         ITechnicalMove move = var1[var3];
         rate = jsonObject.getAsJsonObject(move.prefix()).get(move.getAttackName()).getAsInt();
         ItemBlankTechnicalMachine.maxImprintCount.put(move, rate);
      }

      Gen8TechnicalRecords[] var6 = Gen8TechnicalRecords.values();
      var2 = var6.length;

      for(var3 = 0; var3 < var2; ++var3) {
         ITechnicalMove move = var6[var3];
         rate = jsonObject.getAsJsonObject(move.prefix()).get(move.getAttackName()).getAsInt();
         ItemBlankTechnicalMachine.maxImprintCount.put(move, rate);
      }

   }

   private static void registerBossDrops(JsonObject jsonObject) {
      EnumBossMode[] var1 = EnumBossMode.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumBossMode bossMode = var1[var3];
         ArrayList items = new ArrayList();
         registerBossDropArray(jsonObject, bossMode.name().toLowerCase(), items);
         bossDrops.put(bossMode, items);
      }

   }

   private static void registerBossDropArray(JsonObject jsonObject, String key, List dropArray) {
      if (jsonObject.has(key)) {
         JsonArray megaDropsArray = jsonObject.get(key).getAsJsonArray();

         for(int i = 0; i < megaDropsArray.size(); ++i) {
            String itemString = megaDropsArray.get(i).getAsString();
            ItemStack itemStack = parseItem(itemString, "bossdrops.json");
            if (itemStack == null) {
               if (!futureDrops.contains(itemString)) {
                  Pixelmon.LOGGER.error("Boss drop item not found: " + itemString);
               }
            } else {
               dropArray.add(itemStack);
            }
         }
      }

   }

   private static void registerRaidDrops(JsonObject jsonObject) {
      for(int i = 1; i <= 5; ++i) {
         HashMap items = new HashMap();
         EnumType[] var3 = EnumType.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            EnumType type = var3[var5];
            items.put(type, WeightedItemStacks.create());
         }

         items.put((Object)null, WeightedItemStacks.create());
         registerRaidDropMap(jsonObject, String.valueOf(i), items);
         raidDrops.put(i, items);
      }

   }

   private static void registerRaidDropMap(JsonObject jsonObject, String key, HashMap map) {
      if (jsonObject.has(key)) {
         JsonObject outer = jsonObject.get(key).getAsJsonObject();
         HashSet types = new HashSet(Arrays.asList(EnumType.values()));
         types.add((Object)null);
         Iterator var5 = types.iterator();

         while(true) {
            EnumType type;
            WeightedItemStacks stacks;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               type = (EnumType)var5.next();
               stacks = (WeightedItemStacks)map.get(type);
            } while(!outer.has(type == null ? "Any" : type.name()));

            JsonArray inner = outer.get(type == null ? "Any" : type.name()).getAsJsonArray();

            for(int i = 0; i < inner.size(); ++i) {
               JsonObject itemWithWeight = inner.get(i).getAsJsonObject();
               if (itemWithWeight.has("item") && itemWithWeight.has("weight")) {
                  String itemString = itemWithWeight.get("item").getAsString();
                  int itemWeight = itemWithWeight.get("weight").getAsInt();
                  ItemStack itemStack = parseItem(itemString, "raiddrops.json");
                  if (itemStack == null) {
                     if (!futureDrops.contains(itemString)) {
                        Pixelmon.LOGGER.error("Raid drop item not found: " + itemString);
                     }
                  } else {
                     stacks.add(itemWeight, itemStack);
                  }
               }
            }
         }
      }
   }

   private static void registerPokemonDrops(JsonArray jsonArray) {
      for(int i = 0; i < jsonArray.size(); ++i) {
         JsonObject pokemonDropObject = jsonArray.get(i).getAsJsonObject();
         String pokemonName = pokemonDropObject.get("pokemon").getAsString();
         PokemonSpec pokemon = new PokemonSpec(new String[]{pokemonName, pokemonDropObject.has("form") ? "form:" + pokemonDropObject.get("form").getAsInt() : ""});
         if (pokemon.name != null && EnumSpecies.hasPokemon(pokemon.name)) {
            PokemonDropInformation drop = new PokemonDropInformation(pokemon, pokemonDropObject);
            pokemonDrops.merge(EnumSpecies.getFromNameAnyCaseNoTranslate(pokemon.name), Sets.newHashSet(new PokemonDropInformation[]{drop}), (oldSet, newSet) -> {
               oldSet.add(drop);
               return oldSet;
            });
         }
      }

   }

   private static void registerTierDrops(JsonObject json, String tierName, List tierList) {
      if (json.has(tierName)) {
         JsonArray jsonArray = JsonUtils.func_151214_t(json, tierName);

         for(int i = 0; i < jsonArray.size(); ++i) {
            String itemName = jsonArray.get(i).getAsString();
            Item item = null;
            ResourceLocation loc = new ResourceLocation(itemName);
            item = (Item)Item.field_150901_e.func_82594_a(loc);
            if (item == null) {
               ItemStack tm = RemapHandler.findNewTMFor(itemName, 1);
               if (tm != null) {
                  if (NPCRegistryShopkeepers.shopItems.containsKey(itemName)) {
                     BaseShopItem bItem = (BaseShopItem)NPCRegistryShopkeepers.shopItems.get(itemName);
                     tierList.add(bItem.itemStack);
                  } else {
                     tierList.add(tm);
                  }
                  continue;
               }
            }

            if (item == null) {
               Pixelmon.LOGGER.info("Item not found: " + itemName + " in pokechestdrops.json.");
            } else if (NPCRegistryShopkeepers.shopItems.containsKey(itemName)) {
               BaseShopItem bItem = (BaseShopItem)NPCRegistryShopkeepers.shopItems.get(itemName);
               tierList.add(bItem.itemStack);
            } else {
               tierList.add(new ItemStack(item));
            }
         }
      }

   }

   private static void extractDropsDir(File dropsDir) {
      ServerNPCRegistry.extractFile("/assets/pixelmon/drops/pokechestdrops.json", dropsDir, "pokechestdrops.json");
      ServerNPCRegistry.extractFile("/assets/pixelmon/drops/pokedrops.json", dropsDir, "pokedrops.json");
      ServerNPCRegistry.extractFile("/assets/pixelmon/drops/bossdrops.json", dropsDir, "bossdrops.json");
      ServerNPCRegistry.extractFile("/assets/pixelmon/drops/blankimprintrate.json", dropsDir, "blankimprintrate.json");
      ServerNPCRegistry.extractFile("/assets/pixelmon/drops/raiddrops.json", dropsDir, "raiddrops.json");
   }

   public static ItemStack getTier1Drop() {
      return (ItemStack)RandomHelper.getRandomElementFromList(tier1);
   }

   public static ItemStack getTier2Drop() {
      return (ItemStack)RandomHelper.getRandomElementFromList(tier2);
   }

   public static ItemStack getTier3Drop() {
      return (ItemStack)RandomHelper.getRandomElementFromList(tier3);
   }

   public static ItemStack getUltraSpaceDrop() {
      return (ItemStack)RandomHelper.getRandomElementFromList(ultraSpace);
   }

   public static ArrayList getDropsForPokemon(Entity8HoldsItems pixelmon) {
      ArrayList drops = Lists.newArrayList();
      ((Set)pokemonDrops.get(pixelmon.getSpecies())).stream().filter((d) -> {
         return d.pokemon.matches(pixelmon.getPokemonData());
      }).forEach((dropInfo) -> {
         drops.addAll(dropInfo.getDrops(pixelmon));
      });
      return drops;
   }

   public static ItemStack parseItem(String name, String filename) {
      String[] splits = name.split(":");
      int data = 0;
      if (splits.length > 2) {
         data = Integer.parseInt(splits[2]);
         name = splits[0] + ":" + splits[1];
      }

      ResourceLocation location = new ResourceLocation(name);
      Item item = (Item)Item.field_150901_e.func_82594_a(location);
      if (item == null) {
         if (!futureDrops.contains(name)) {
            Pixelmon.LOGGER.info("Item not found: " + name + " in " + filename);
         }

         return null;
      } else {
         return new ItemStack(item, 1, data);
      }
   }

   public static ArrayList getBossDrops(Entity8HoldsItems pixelmon, EntityPlayerMP player) {
      ArrayList drops = new ArrayList();
      EnumBossMode bossMode = pixelmon.getBossMode();
      addRandomBossDrops(drops, (List)bossDrops.get(bossMode), bossMode.getCandyChance());
      PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
      EnumSpecies pokemon = pixelmon.getBaseStats().getSpecies();
      if (bossMode.isMegaBossPokemon() && pokemon.hasMega()) {
         int form = pixelmon.getPokemonData().getForm();
         if (form > 0) {
            if (EnumMegaPokemon.getMega(pokemon).getMegaEvoItems()[form - 1] == ItemStack.field_190927_a.func_77973_b()) {
               return drops;
            }

            if (!storage.isMegaItemObtained(pokemon, form)) {
               drops.add(new ItemStack(EnumMegaPokemon.getMega(pokemon).getMegaEvoItems()[form - 1]));
               storage.obtainedItem(pokemon, form, player);
            } else if (RandomHelper.getRandomChance(0.025F)) {
               drops.add(new ItemStack(EnumMegaPokemon.getMega(pokemon).getMegaEvoItems()[form - 1]));
            }
         }
      }

      return drops;
   }

   private static void addRandomBossDrops(List drops, List bossDropStore, float candyChance) {
      if (RandomHelper.getRandomChance(candyChance)) {
         drops.add(new ItemStack(PixelmonItems.rareCandy));
      }

      ItemStack megaDrop = (ItemStack)RandomHelper.getRandomElementFromList(bossDropStore);
      if (megaDrop != null) {
         megaDrop = megaDrop.func_77946_l();
         megaDrop.func_190920_e(1);
         drops.add(megaDrop);
      }
   }
}
