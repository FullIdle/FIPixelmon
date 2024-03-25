package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.IOUtils;

public class ServerNPCRegistry {
   public static String en_us = "en_us";
   public static HashMap data = new HashMap();
   public static NPCRegistryTrainers trainers = new NPCRegistryTrainers();
   public static NPCRegistryVillagers villagers = new NPCRegistryVillagers();
   public static NPCRegistryShopkeepers shopkeepers = new NPCRegistryShopkeepers();

   public static void registerNPCS(String langCode) throws Exception {
      Pixelmon.LOGGER.info("Registering NPCs.");
      String lowerLangCode = langCode.toLowerCase();
      NPCRegistryData thisData = null;
      if (data.containsKey(lowerLangCode)) {
         thisData = (NPCRegistryData)data.get(lowerLangCode);
      } else {
         thisData = new NPCRegistryData();
      }

      JsonObject json = readJsonFile("npcs/npcs.json", PixelmonConfig.useExternalJSONFilesNPCs);
      JsonArray jsonarray;
      int i;
      JsonObject jsonelement1;
      String name;
      if (json.has("villagers")) {
         jsonarray = JsonUtils.func_151214_t(json, "villagers");

         for(i = 0; i < jsonarray.size(); ++i) {
            try {
               jsonelement1 = jsonarray.get(i).getAsJsonObject();
               name = jsonelement1.get("name").getAsString();
               villagers.loadVillager(thisData, name, lowerLangCode);
            } catch (Exception var17) {
               printLoadError(var17);
            }
         }
      }

      if (json.has("shopKeepers")) {
         jsonarray = JsonUtils.func_151214_t(json, "shopKeepers");

         for(i = 0; i < jsonarray.size(); ++i) {
            try {
               jsonelement1 = jsonarray.get(i).getAsJsonObject();
               name = jsonelement1.get("name").getAsString();
               shopkeepers.loadShopkeeper(thisData, name, lowerLangCode);
            } catch (Exception var16) {
               printLoadError(var16);
            }
         }
      }

      if (json.has("trainers")) {
         JsonObject object = JsonUtils.func_151210_l(json.get("trainers"), "trainers");
         JsonArray typesArray = JsonUtils.func_151214_t(object, "types");
         BaseTrainer._index = 0;

         String name;
         for(int i = 0; i < typesArray.size(); ++i) {
            JsonObject jsonelement1 = typesArray.get(i).getAsJsonObject();
            name = jsonelement1.get("name").getAsString();
            BaseTrainer trainer = new BaseTrainer(name);
            JsonArray texturesArray = JsonUtils.func_151214_t(jsonelement1, "textures");

            for(int j = 0; j < texturesArray.size(); ++j) {
               String texture = texturesArray.get(j).getAsString();
               trainer.addTexture(texture);
            }

            thisData.trainerTypes.add(trainer);
            if (trainer.name.equals("Steve")) {
               NPCRegistryTrainers.Steve = trainer;
            }
         }

         data.put(lowerLangCode, thisData);
         JsonArray listArray = JsonUtils.func_151214_t(object, "list");

         for(int i = 0; i < listArray.size(); ++i) {
            name = "";

            try {
               JsonObject jsonelement1 = listArray.get(i).getAsJsonObject();
               name = jsonelement1.get("name").getAsString();
               trainers.loadTrainer(thisData, name, lowerLangCode);
            } catch (LanguageNotFoundException var14) {
               Pixelmon.LOGGER.info("Missing NPC Trainer of type " + name + " for language " + lowerLangCode + ".");
               trainers.loadTrainer(thisData, name, en_us);
            } catch (Exception var15) {
               printLoadError(var15);
            }
         }
      }

      if (json.has("gymnpcs")) {
         jsonarray = JsonUtils.func_151214_t(json, "gymnpcs");

         for(i = 0; i < jsonarray.size(); ++i) {
            try {
               jsonelement1 = jsonarray.get(i).getAsJsonObject();
               name = jsonelement1.get("name").getAsString();
               loadGymNPC(thisData, name, lowerLangCode);
            } catch (Exception var13) {
               printLoadError(var13);
            }
         }
      }

      if (thisData.npcs.size() == 0 && thisData.trainers.size() == 0) {
         throw new LanguageNotFoundException();
      } else {
         data.put(lowerLangCode, thisData);
      }
   }

   private static void printLoadError(Exception e) {
      Pixelmon.LOGGER.error(e.getMessage());
      e.printStackTrace();
   }

   private static void loadGymNPC(NPCRegistryData thisData, String name, String langCode) throws Exception {
      try {
         JsonObject json = readJsonFile("npcs/gyms/" + name + "_" + langCode.toLowerCase() + ".json", PixelmonConfig.useExternalJSONFilesNPCs);
         if (json == null) {
            if (langCode.equals("en_us")) {
               throw new FileNotFoundException("Error in Gym NPC:" + name + "_" + langCode.toLowerCase());
            }

            return;
         }

         GymNPCData data = new GymNPCData(name);
         data.type = EnumNPCType.getFromString(json.get("npctype").getAsString());
         if (json.has("winnings")) {
            data.winnings = json.get("winnings").getAsInt();
         }

         JsonArray jsonarray;
         int i;
         JsonObject jsonelement1;
         String opening;
         if (json.has("skins")) {
            jsonarray = JsonUtils.func_151214_t(json, "skins");

            for(i = 0; i < jsonarray.size(); ++i) {
               jsonelement1 = jsonarray.get(i).getAsJsonObject();
               opening = jsonelement1.get("filename").getAsString();
               data.addTexture(opening);
            }
         }

         if (json.has("names")) {
            jsonarray = JsonUtils.func_151214_t(json, "names");

            for(i = 0; i < jsonarray.size(); ++i) {
               jsonelement1 = jsonarray.get(i).getAsJsonObject();
               opening = jsonelement1.get("name").getAsString();
               data.addName(opening);
            }
         }

         if (json.has("chat")) {
            if (data.type != EnumNPCType.ChattingNPC) {
               if (data.type == EnumNPCType.Trainer) {
                  jsonarray = JsonUtils.func_151214_t(json, "chat");

                  for(i = 0; i < jsonarray.size(); ++i) {
                     jsonelement1 = jsonarray.get(i).getAsJsonObject();
                     opening = jsonelement1.get("opening").getAsString();
                     String win = jsonelement1.get("win").getAsString();
                     String lose = jsonelement1.get("lose").getAsString();
                     data.addChat(opening, win, lose);
                  }
               }
            } else {
               jsonarray = JsonUtils.func_151214_t(json, "chat");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  JsonArray lines = jsonelement1.get("lines").getAsJsonArray();
                  ArrayList lineList = new ArrayList();

                  for(int j = 0; j < lines.size(); ++j) {
                     JsonObject object = lines.get(j).getAsJsonObject();
                     String text = object.get("text").getAsString();
                     lineList.add(text);
                  }

                  String[] lineArray = new String[lineList.size()];
                  lineArray = (String[])lineList.toArray(lineArray);
                  data.addChat(lineArray);
               }
            }
         }

         thisData.gymnpcs.put(data.id, data);
      } catch (Exception var13) {
         Pixelmon.LOGGER.error("Error in Gym NPC: " + name + "_" + langCode.toLowerCase());
         printLoadError(var13);
      }

   }

   public static GymNPCData getGymMember(String id) {
      GymNPCData npc = null;
      npc = (GymNPCData)((NPCRegistryData)data.get(en_us)).gymnpcs.get(id);
      return npc;
   }

   public static String getRandomName() {
      List list;
      NPCRegistryData npcData;
      BaseTrainer trainer;
      for(list = null; list == null; list = (List)npcData.trainers.get(trainer)) {
         npcData = (NPCRegistryData)data.get(en_us);
         trainer = (BaseTrainer)RandomHelper.getRandomElementFromList(npcData.trainerTypes);
      }

      TrainerData td = (TrainerData)RandomHelper.getRandomElementFromList(list);
      return (String)RandomHelper.getRandomElementFromList(td.names);
   }

   public static GymNPCData getTranslatedGymMemberData(String langCode, String id) {
      String lowerLangCode = langCode.toLowerCase();
      if (!data.containsKey(lowerLangCode)) {
         try {
            registerNPCS(lowerLangCode);
         } catch (LanguageNotFoundException var4) {
            data.put(lowerLangCode, data.get(en_us));
         } catch (Exception var5) {
         }
      }

      GymNPCData npc = (GymNPCData)((NPCRegistryData)data.get(langCode.toLowerCase())).gymnpcs.get(id);
      if (npc == null) {
         npc = (GymNPCData)((NPCRegistryData)data.get(en_us)).gymnpcs.get(id);
      }

      return npc;
   }

   public static String getTranslatedGymMemberName(String langCode, String id, int index) {
      ArrayList names = getTranslatedGymMemberData(langCode.toLowerCase(), id).names;
      if (index >= names.size()) {
         index = 0;
      }

      return (String)names.get(index);
   }

   public static String[] getTranslatedGymMemberChat(String langCode, String id, int index) {
      ArrayList chat = getTranslatedGymMemberData(langCode.toLowerCase(), id).chat;
      if (index >= chat.size()) {
         index = 0;
      }

      return (String[])chat.get(index);
   }

   static void extractNpcsDir(File npcsDir) {
      JsonObject json = null;

      try {
         json = readJsonFile("npcs/index.json", false);
      } catch (IOException var6) {
         var6.printStackTrace();
         return;
      }

      if (json.has("base")) {
         JsonArray baseArray = JsonUtils.func_151214_t(json, "base");

         for(int i = 0; i < baseArray.size(); ++i) {
            String filename = baseArray.get(i).getAsString() + ".json";
            extractFile("/assets/pixelmon/npcs/" + filename, npcsDir, filename);
         }
      }

      String filename;
      File villagersDir;
      JsonArray skArray;
      int i;
      if (json.has("shopKeepers")) {
         villagersDir = new File(npcsDir, "shopKeepers");
         villagersDir.mkdir();
         skArray = JsonUtils.func_151214_t(json, "shopKeepers");

         for(i = 0; i < skArray.size(); ++i) {
            filename = skArray.get(i).getAsString() + ".json";
            extractFile("/assets/pixelmon/npcs/shopKeepers/" + filename, villagersDir, filename);
         }
      }

      if (json.has("trainers")) {
         villagersDir = new File(npcsDir, "trainers");
         villagersDir.mkdir();
         skArray = JsonUtils.func_151214_t(json, "trainers");

         for(i = 0; i < skArray.size(); ++i) {
            filename = skArray.get(i).getAsString() + ".json";
            extractFile("/assets/pixelmon/npcs/trainers/" + filename, villagersDir, filename);
         }
      }

      if (json.has("villagers")) {
         villagersDir = new File(npcsDir, "villagers");
         villagersDir.mkdir();
         skArray = JsonUtils.func_151214_t(json, "villagers");

         for(i = 0; i < skArray.size(); ++i) {
            filename = skArray.get(i).getAsString() + ".json";
            extractFile("/assets/pixelmon/npcs/villagers/" + filename, villagersDir, filename);
         }
      }

      if (json.has("gyms")) {
         villagersDir = new File(npcsDir, "gyms");
         villagersDir.mkdir();
         skArray = JsonUtils.func_151214_t(json, "gyms");

         for(i = 0; i < skArray.size(); ++i) {
            filename = skArray.get(i).getAsString() + ".json";
            extractFile("/assets/pixelmon/npcs/gyms/" + filename, villagersDir, filename);
         }
      }

   }

   public static void extractFile(String resourceName, File npcsDir, String filename) {
      File file = new File(npcsDir, filename);
      if (!file.exists()) {
         try {
            InputStream internal = ServerNPCRegistry.class.getResourceAsStream(resourceName);
            OutputStream external = new FileOutputStream(file);
            IOUtils.copy(internal, external);
            internal.close();
            external.close();
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   public static ITrainerData getTranslatedData(String langCode, BaseTrainer baseTrainer, String id) {
      ITrainerData data = null;
      data = trainers.getTranslatedData(langCode, baseTrainer, id);
      if (data == null) {
         data = getTranslatedGymMemberData(langCode, id);
      }

      return (ITrainerData)data;
   }

   public static ArrayList getEnglishNPCs() {
      return ((NPCRegistryData)data.get(en_us)).npcs;
   }

   public static ArrayList getEnglishShopkeepers() {
      return ((NPCRegistryData)data.get(en_us)).shopkeepers;
   }

   public static JsonObject readJsonFile(String file, boolean external) throws IOException {
      File pixelmon = null;
      if (external) {
         pixelmon = new File(Pixelmon.modDirectory + "/pixelmon/" + file);
         if (!pixelmon.exists()) {
            return null;
         }
      }

      try {
         InputStream inputStream = external ? new FileInputStream(pixelmon) : ServerNPCRegistry.class.getResourceAsStream("/assets/pixelmon/" + file);
         Throwable var4 = null;

         JsonObject var5;
         try {
            var5 = (new JsonParser()).parse(new InputStreamReader((InputStream)inputStream, StandardCharsets.UTF_8)).getAsJsonObject();
         } catch (Throwable var15) {
            var4 = var15;
            throw var15;
         } finally {
            if (inputStream != null) {
               if (var4 != null) {
                  try {
                     ((InputStream)inputStream).close();
                  } catch (Throwable var14) {
                     var4.addSuppressed(var14);
                  }
               } else {
                  ((InputStream)inputStream).close();
               }
            }

         }

         return var5;
      } catch (NullPointerException var17) {
         return null;
      }
   }
}
