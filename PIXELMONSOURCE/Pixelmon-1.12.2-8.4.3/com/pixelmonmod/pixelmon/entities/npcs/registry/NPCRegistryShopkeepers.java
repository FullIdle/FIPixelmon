package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.RemapHandler;
import com.pixelmonmod.pixelmon.enums.EnumShopKeeperType;
import com.pixelmonmod.pixelmon.items.helpers.ItemHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class NPCRegistryShopkeepers {
   public static HashMap shopItems = new HashMap();

   void loadShopkeeper(NPCRegistryData thisData, String name, String langCode) throws Exception {
      try {
         String path = Pixelmon.modDirectory + "/pixelmon/npcs/shopKeepers/";
         File skDir = new File(path);
         InputStream istream = null;
         if (!PixelmonConfig.useExternalJSONFilesNPCs) {
            istream = ServerNPCRegistry.class.getResourceAsStream("/assets/pixelmon/npcs/shopKeepers/" + name + "_" + langCode.toLowerCase() + ".json");
         } else {
            File file = new File(skDir, name + "_" + langCode.toLowerCase() + ".json");
            if (file.exists()) {
               istream = new FileInputStream(file);
            }
         }

         if (istream == null) {
            if (langCode.equals("en_us")) {
               throw new Exception("Error in shopkeeper " + name + "_" + langCode.toLowerCase() + ".");
            }
         } else {
            ShopkeeperData data = new ShopkeeperData(name);
            JsonObject json = (new JsonParser()).parse(new InputStreamReader((InputStream)istream, StandardCharsets.UTF_8)).getAsJsonObject();
            if (json.has("data")) {
               JsonObject object = JsonUtils.func_151210_l(json.get("data"), "data");
               object.get("type").getAsString();
               data.type = EnumShopKeeperType.getFromString(JsonUtils.func_151200_h(object, "type"));
               if (object.has("biomes")) {
                  JsonArray biomearray = JsonUtils.func_151214_t(object, "biomes");

                  for(int i = 0; i < biomearray.size(); ++i) {
                     data.addBiome(biomearray.get(i).getAsString());
                  }
               }
            }

            String itemName;
            JsonArray jsonarray;
            int i;
            JsonObject jsonelement1;
            if (json.has("textures")) {
               jsonarray = JsonUtils.func_151214_t(json, "textures");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  itemName = jsonelement1.get("name").getAsString();
                  data.addTexture(itemName);
               }
            }

            if (json.has("names")) {
               jsonarray = JsonUtils.func_151214_t(json, "names");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  itemName = jsonelement1.get("name").getAsString();
                  data.addName(itemName);
               }
            }

            if (json.has("chat")) {
               jsonarray = JsonUtils.func_151214_t(json, "chat");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  itemName = jsonelement1.get("hello").getAsString();
                  String goodbye = jsonelement1.get("goodbye").getAsString();
                  data.addChat(itemName, goodbye);
               }
            }

            if (json.has("items")) {
               jsonarray = JsonUtils.func_151214_t(json, "items");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  itemName = jsonelement1.get("name").getAsString();
                  float multi = 1.0F;
                  if (jsonelement1.has("multi")) {
                     multi = jsonelement1.get("multi").getAsFloat();
                  }

                  float rarity = 1.0F;
                  if (jsonelement1.has("rarity")) {
                     rarity = jsonelement1.get("rarity").getAsFloat();
                  }

                  boolean canPriceVary = true;
                  if (jsonelement1.has("variation")) {
                     canPriceVary = jsonelement1.get("variation").getAsBoolean();
                  }

                  if (!shopItems.containsKey(itemName)) {
                     Pixelmon.LOGGER.info("Item mismatch: no item found for " + itemName + " in " + name + "_" + langCode.toLowerCase() + ".json");
                  } else {
                     ShopItem item = new ShopItem((BaseShopItem)shopItems.get(itemName), multi, rarity, canPriceVary);
                     data.addItem(item);
                  }
               }
            }

            thisData.shopkeepers.add(data);
            if (data.type == EnumShopKeeperType.Spawn) {
               thisData.shopkeeperSpawns.add(data);
            }

         }
      } catch (Exception var17) {
         throw new Exception("Error in shopKeeper " + name + "_" + langCode.toLowerCase(), var17);
      }
   }

   public void registerShopItems() throws Exception {
      Pixelmon.LOGGER.info("Registering shop items.");
      String path = Pixelmon.modDirectory + "/pixelmon/npcs/";
      File npcsDir = new File(path);
      if (PixelmonConfig.useExternalJSONFilesNPCs && !npcsDir.isDirectory()) {
         File baseDir = new File(Pixelmon.modDirectory + "/pixelmon");
         if (!baseDir.isDirectory()) {
            baseDir.mkdir();
         }

         Pixelmon.LOGGER.info("Creating NPCs directory.");
         npcsDir.mkdir();
         ServerNPCRegistry.extractNpcsDir(npcsDir);
      }

      Object istream;
      if (!PixelmonConfig.useExternalJSONFilesNPCs) {
         istream = ServerNPCRegistry.class.getResourceAsStream("/assets/pixelmon/npcs/shopItems.json");
      } else {
         istream = new FileInputStream(new File(npcsDir, "shopItems.json"));
      }

      JsonObject json = (new JsonParser()).parse(new InputStreamReader((InputStream)istream, StandardCharsets.UTF_8)).getAsJsonObject();
      if (json.has("items")) {
         JsonArray jsonarray = JsonUtils.func_151214_t(json, "items");

         for(int i = 0; i < jsonarray.size(); ++i) {
            JsonObject itemElement = jsonarray.get(i).getAsJsonObject();
            String name = itemElement.get("name").getAsString();
            String id = null;
            if (itemElement.has("id")) {
               id = itemElement.get("id").getAsString();
            } else {
               id = name;
            }

            int buy = -1;
            if (itemElement.has("buy")) {
               buy = itemElement.get("buy").getAsInt();
            }

            int sell = -1;
            if (itemElement.has("sell")) {
               sell = itemElement.get("sell").getAsInt();
            }

            int itemData = 0;
            if (itemElement.has("itemData")) {
               itemData = itemElement.get("itemData").getAsInt();
            }

            String nbtData = null;
            if (itemElement.has("nbtData")) {
               nbtData = itemElement.get("nbtData").getAsString();
            }

            Item item = null;
            ResourceLocation loc = new ResourceLocation(name);
            item = (Item)Item.field_150901_e.func_82594_a(loc);
            ItemStack itemStack;
            BaseShopItem baseItem;
            if (item == null) {
               itemStack = RemapHandler.findNewTMFor(name, 1);
               if (itemStack != null) {
                  baseItem = new BaseShopItem(id, itemStack, buy, sell);
                  shopItems.put(id, baseItem);
                  continue;
               }
            }

            if (item == null) {
               Pixelmon.LOGGER.info("Item not found: " + name + " in shopItems.json");
            } else {
               itemStack = new ItemStack(item, 1, itemData);
               if (nbtData != null) {
                  itemStack.func_77982_d(JsonToNBT.func_180713_a(nbtData));
               }

               baseItem = new BaseShopItem(id, itemStack, buy, sell);
               shopItems.put(id, baseItem);
            }
         }
      }

   }

   public ShopkeeperData getRandom() {
      ShopkeeperData sk = null;
      NPCRegistryData data = (NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us);
      if (data == null) {
         return null;
      } else {
         while(sk == null) {
            sk = (ShopkeeperData)RandomHelper.getRandomElementFromList(data.shopkeepers);
         }

         return sk;
      }
   }

   public ShopkeeperData getById(String id) {
      Iterator var2 = ServerNPCRegistry.getEnglishShopkeepers().iterator();

      ShopkeeperData sk;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         sk = (ShopkeeperData)var2.next();
      } while(!sk.id.equalsIgnoreCase(id));

      return sk;
   }

   public ShopkeeperData getRandom(EnumShopKeeperType type) {
      ShopkeeperData sk = null;
      NPCRegistryData npcData = (NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us);
      if (npcData == null) {
         return null;
      } else {
         boolean hasType = false;
         Iterator var5 = npcData.shopkeepers.iterator();

         while(var5.hasNext()) {
            ShopkeeperData shopkeeperData = (ShopkeeperData)var5.next();
            if (shopkeeperData != null && shopkeeperData.type == type) {
               hasType = true;
               break;
            }
         }

         if (!hasType) {
            return null;
         } else {
            while(sk == null || sk.type != type) {
               sk = (ShopkeeperData)RandomHelper.getRandomElementFromList(npcData.shopkeepers);
            }

            return sk;
         }
      }
   }

   public ShopkeeperData getTranslatedData(String langCode, String id) {
      if (!ServerNPCRegistry.data.containsKey(langCode.toLowerCase())) {
         try {
            ServerNPCRegistry.registerNPCS(langCode.toLowerCase());
         } catch (LanguageNotFoundException var6) {
            ServerNPCRegistry.data.put(langCode.toLowerCase(), ServerNPCRegistry.data.get(ServerNPCRegistry.en_us));
         } catch (Exception var7) {
         }
      }

      NPCRegistryData npcData = (NPCRegistryData)ServerNPCRegistry.data.get(langCode);
      Iterator var4;
      ShopkeeperData npc;
      if (npcData != null) {
         var4 = npcData.shopkeepers.iterator();

         while(var4.hasNext()) {
            npc = (ShopkeeperData)var4.next();
            if (npc.id.equals(id)) {
               return npc;
            }
         }
      }

      var4 = ServerNPCRegistry.getEnglishShopkeepers().iterator();

      do {
         if (!var4.hasNext()) {
            return null;
         }

         npc = (ShopkeeperData)var4.next();
      } while(!npc.id.equals(id));

      return npc;
   }

   public ShopkeeperChat getTranslatedChat(String langCode, String npcIndex, int index) {
      ArrayList chat = this.getTranslatedData(langCode.toLowerCase(), npcIndex).chat;
      if (index >= chat.size()) {
         index = 0;
      }

      return (ShopkeeperChat)chat.get(index);
   }

   public String getJsonName(String npcIndex) {
      Iterator var2 = ServerNPCRegistry.getEnglishShopkeepers().iterator();

      ShopkeeperData npc;
      do {
         if (!var2.hasNext()) {
            return "";
         }

         npc = (ShopkeeperData)var2.next();
      } while(!npc.id.equals(npcIndex));

      return npc.id;
   }

   public ShopkeeperData getNext(String npcIndex) {
      List shopkeepers = ServerNPCRegistry.getEnglishShopkeepers();
      int numShopkeepers = shopkeepers.size();

      for(int i = 0; i < numShopkeepers; ++i) {
         ShopkeeperData npc = (ShopkeeperData)shopkeepers.get(i);
         if (npc.id.equals(npcIndex)) {
            if (i < numShopkeepers - 1) {
               return (ShopkeeperData)shopkeepers.get(i + 1);
            }

            return (ShopkeeperData)shopkeepers.get(0);
         }
      }

      return (ShopkeeperData)shopkeepers.get(0);
   }

   public ShopItem getItem(String npcIndex, String itemID) {
      ShopkeeperData shopkeeper = this.getById(npcIndex);
      return shopkeeper == null ? null : shopkeeper.getItem(itemID);
   }

   public BaseShopItem getItem(String name) {
      if (name == null) {
         return null;
      } else {
         if (name.startsWith("item.")) {
            name = name.replace("item.", "");
         }

         BaseShopItem item = (BaseShopItem)shopItems.get(name);
         if (item == null && PixelmonConfig.verbose) {
            Pixelmon.LOGGER.info("Missing item requested: " + name);
         }

         return (BaseShopItem)shopItems.get(name);
      }
   }

   public BaseShopItem getItem(ItemStack itemStack) {
      Iterator var2 = shopItems.values().iterator();

      BaseShopItem shopItem;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         shopItem = (BaseShopItem)var2.next();
      } while(!ItemHelper.isItemStackEqual(shopItem.itemStack, itemStack));

      return shopItem;
   }

   public String getTranslatedName(String langCode, String npcIndex, int nameIndex) {
      ArrayList names = this.getTranslatedData(langCode.toLowerCase(), npcIndex).names;
      if (nameIndex >= names.size()) {
         nameIndex = 0;
      }

      return (String)names.get(nameIndex);
   }

   public boolean hasRoaming() {
      NPCRegistryData data = (NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us);
      return data == null ? false : !data.shopkeeperSpawns.isEmpty();
   }

   public ShopkeeperData getRandomSpawning(String biomeID) {
      ArrayList keepers = new ArrayList();
      Iterator var3 = ((NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us)).shopkeeperSpawns.iterator();

      while(var3.hasNext()) {
         ShopkeeperData data = (ShopkeeperData)var3.next();
         if (data.biomes.contains(biomeID)) {
            keepers.add(data);
         }
      }

      ShopkeeperData sk = null;
      if (!keepers.isEmpty()) {
         while(sk == null) {
            sk = (ShopkeeperData)RandomHelper.getRandomElementFromList(keepers);
         }
      }

      return sk;
   }

   public String[] getRoamingBiomes() {
      ArrayList biomeNames = new ArrayList();
      Iterator var2 = ((NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us)).shopkeeperSpawns.iterator();

      while(var2.hasNext()) {
         ShopkeeperData data = (ShopkeeperData)var2.next();
         Iterator var4 = data.biomes.iterator();

         while(var4.hasNext()) {
            String biome = (String)var4.next();
            if (!biomeNames.contains(biome)) {
               biomeNames.add(biome);
            }
         }
      }

      return (String[])biomeNames.toArray(new String[biomeNames.size()]);
   }
}
