package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.JsonUtils;

public class NPCRegistryVillagers {
   void loadVillager(NPCRegistryData thisData, String name, String langCode) throws Exception {
      try {
         String path = Pixelmon.modDirectory + "/pixelmon/npcs/villagers/";
         File vDir = new File(path);
         InputStream istream = null;
         if (!PixelmonConfig.useExternalJSONFilesNPCs) {
            istream = ServerNPCRegistry.class.getResourceAsStream("/assets/pixelmon/npcs/villagers/" + name + "_" + langCode.toLowerCase() + ".json");
         } else {
            File file = new File(vDir, name + "_" + langCode.toLowerCase() + ".json");
            if (file.exists()) {
               istream = new FileInputStream(file);
            }
         }

         if (istream == null) {
            if (langCode.equals("en_us")) {
               throw new FileNotFoundException("Error loading villager " + name + "_" + langCode.toLowerCase());
            }
         } else {
            GeneralNPCData data = new GeneralNPCData(name);
            InputStreamReader reader = new InputStreamReader((InputStream)istream, StandardCharsets.UTF_8);
            JsonObject json = (new JsonParser()).parse(reader).getAsJsonObject();
            JsonArray jsonarray;
            int i;
            JsonObject jsonelement1;
            String npcname;
            if (json.has("skins")) {
               jsonarray = JsonUtils.func_151214_t(json, "skins");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  npcname = jsonelement1.get("filename").getAsString();
                  data.addTexture(npcname);
               }
            }

            if (json.has("names")) {
               jsonarray = JsonUtils.func_151214_t(json, "names");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  npcname = jsonelement1.get("name").getAsString();
                  data.addName(npcname);
               }
            }

            if (json.has("chat")) {
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

            thisData.npcs.add(data);
         }
      } catch (FileNotFoundException var18) {
         throw var18;
      } catch (Exception var19) {
         throw new Exception("Error in villager " + name + "_" + langCode.toLowerCase(), var19);
      }
   }

   public GeneralNPCData getNext(String index) {
      ArrayList npcListUs = ServerNPCRegistry.getEnglishNPCs();
      if (npcListUs.isEmpty()) {
         return null;
      } else {
         for(int i = 0; i < npcListUs.size(); ++i) {
            if (((GeneralNPCData)npcListUs.get(i)).id.equals(index)) {
               return (GeneralNPCData)npcListUs.get((i + 1) % npcListUs.size());
            }
         }

         return (GeneralNPCData)npcListUs.get(0);
      }
   }

   public GeneralNPCData getData(String id) {
      ArrayList npcListUs = ServerNPCRegistry.getEnglishNPCs();
      if (npcListUs.isEmpty()) {
         return null;
      } else {
         Iterator var3 = npcListUs.iterator();

         GeneralNPCData data;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            data = (GeneralNPCData)var3.next();
         } while(!id.equals(data.id));

         return data;
      }
   }

   public GeneralNPCData getRandom() {
      return (GeneralNPCData)RandomHelper.getRandomElementFromList(ServerNPCRegistry.getEnglishNPCs());
   }

   public GeneralNPCData getTranslatedData(String langCode, String id) {
      if (!ServerNPCRegistry.data.containsKey(langCode.toLowerCase())) {
         try {
            ServerNPCRegistry.registerNPCS(langCode.toLowerCase());
         } catch (LanguageNotFoundException var6) {
            ServerNPCRegistry.data.put(langCode.toLowerCase(), ServerNPCRegistry.data.get(ServerNPCRegistry.en_us));
         } catch (Exception var7) {
         }
      }

      NPCRegistryData translatedData = (NPCRegistryData)ServerNPCRegistry.data.get(langCode);
      Iterator var4;
      GeneralNPCData npc;
      if (translatedData != null) {
         var4 = translatedData.npcs.iterator();

         while(var4.hasNext()) {
            npc = (GeneralNPCData)var4.next();
            if (npc.id.equals(id)) {
               return npc;
            }
         }
      }

      var4 = ServerNPCRegistry.getEnglishNPCs().iterator();

      do {
         if (!var4.hasNext()) {
            return null;
         }

         npc = (GeneralNPCData)var4.next();
      } while(!npc.id.equals(id));

      return npc;
   }

   public String getTranslatedName(String langCode, String id, int index) {
      ArrayList names = this.getTranslatedData(langCode.toLowerCase(), id).names;
      if (index >= names.size()) {
         index = 0;
      }

      return (String)names.get(index);
   }

   public String[] getTranslatedChat(String langCode, String id, int index) {
      ArrayList chat = this.getTranslatedData(langCode.toLowerCase(), id).chat;
      if (index >= chat.size()) {
         index = 0;
      }

      return (String[])chat.get(index);
   }
}
