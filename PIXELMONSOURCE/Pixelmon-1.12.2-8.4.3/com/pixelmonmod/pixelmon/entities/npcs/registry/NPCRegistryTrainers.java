package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.JsonUtils;

public class NPCRegistryTrainers {
   public static BaseTrainer Steve;

   void loadTrainer(NPCRegistryData thisData, String name, String langCode) throws Exception {
      try {
         String path = Pixelmon.modDirectory + "/pixelmon/npcs/trainers/";
         File tDir = new File(path);
         InputStream istream = null;
         if (!PixelmonConfig.useExternalJSONFilesNPCs) {
            istream = ServerNPCRegistry.class.getResourceAsStream("/assets/pixelmon/npcs/trainers/" + name + "_" + langCode.toLowerCase() + ".json");
         } else {
            File file = new File(tDir, name + "_" + langCode.toLowerCase() + ".json");
            if (file.exists()) {
               istream = new FileInputStream(file);
            }
         }

         if (istream == null && !langCode.equals(ServerNPCRegistry.en_us)) {
            throw new LanguageNotFoundException();
         } else if (istream != null) {
            TrainerData data = new TrainerData(name);
            JsonObject json = (new JsonParser()).parse(new InputStreamReader((InputStream)istream, StandardCharsets.UTF_8)).getAsJsonObject();
            if (json.has("data")) {
               JsonObject object = JsonUtils.func_151210_l(json.get("data"), "data");
               String trainerType = object.get("trainerType").getAsString();
               data.trainerType = this.get(trainerType);
               data.minLevel = JsonUtils.func_151203_m(object, "minLevel");
               data.maxLevel = JsonUtils.func_151203_m(object, "maxLevel");
               data.minPartyPokemon = JsonUtils.func_151203_m(object, "minPartyPokemon");
               data.maxPartyPokemon = JsonUtils.func_151203_m(object, "maxPartyPokemon");
               data.winnings = JsonUtils.func_151203_m(object, "winnings");
            }

            JsonObject jsonelement1;
            String opening;
            JsonArray jsonarray;
            int i;
            if (json.has("names")) {
               jsonarray = JsonUtils.func_151214_t(json, "names");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  opening = jsonelement1.get("name").getAsString();
                  data.addName(opening);
               }
            }

            if (json.has("pokemon")) {
               jsonarray = JsonUtils.func_151214_t(json, "pokemon");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  opening = jsonelement1.get("name").getAsString();
                  EnumSpecies poke = EnumSpecies.getFromNameAnyCase(opening);
                  if (poke != null) {
                     PokemonForm pokemonForm = new PokemonForm(poke);
                     String formKey = "form";
                     if (jsonelement1.has(formKey)) {
                        pokemonForm.form = jsonelement1.get(formKey).getAsInt();
                     }

                     data.addPokemon(pokemonForm);
                  }
               }
            }

            if (json.has("chat")) {
               jsonarray = JsonUtils.func_151214_t(json, "chat");

               for(i = 0; i < jsonarray.size(); ++i) {
                  jsonelement1 = jsonarray.get(i).getAsJsonObject();
                  opening = jsonelement1.get("opening").getAsString();
                  String win = jsonelement1.get("win").getAsString();
                  String lose = jsonelement1.get("lose").getAsString();
                  data.addChat(opening, win, lose);
               }
            }

            List list = (List)thisData.trainers.get(data.trainerType);
            if (list == null) {
               list = new ArrayList();
            }

            ((List)list).add(data);
            thisData.trainers.put(data.trainerType, list);
         }
      } catch (LanguageNotFoundException var16) {
         throw var16;
      } catch (Exception var17) {
         throw new Exception("Error in trainer " + name + "_" + langCode.toLowerCase(), var17);
      }
   }

   public boolean has(String trainerName) {
      NPCRegistryData trainerData = (NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us);
      if (trainerData != null) {
         Iterator var3 = ((NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us)).trainerTypes.iterator();

         while(var3.hasNext()) {
            BaseTrainer trainer = (BaseTrainer)var3.next();
            if (trainer.name.equalsIgnoreCase(trainerName)) {
               return true;
            }
         }
      }

      return false;
   }

   public BaseTrainer get(String trainerName) {
      Iterator var2 = ((NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us)).trainerTypes.iterator();

      BaseTrainer trainer;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         trainer = (BaseTrainer)var2.next();
      } while(!trainer.name.equalsIgnoreCase(trainerName));

      return trainer;
   }

   public BaseTrainer getById(int id) {
      NPCRegistryData englishData = (NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us);
      if (englishData != null) {
         Iterator var3 = englishData.trainerTypes.iterator();

         while(var3.hasNext()) {
            BaseTrainer trainer = (BaseTrainer)var3.next();
            if (trainer.id == id) {
               return trainer;
            }
         }
      }

      return Steve;
   }

   public BaseTrainer getRandomBase() {
      return this.getById(RandomHelper.getRandomNumberBetween(0, BaseTrainer._index - 1));
   }

   public TrainerData getRandomData(BaseTrainer trainer) {
      List list = (List)((NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us)).trainers.get(trainer);
      if (list == null) {
         return null;
      } else {
         TrainerData td = (TrainerData)RandomHelper.getRandomElementFromList(list);
         return td;
      }
   }

   public TrainerData getRandomData(String name) {
      BaseTrainer trainer = this.get(name);
      return this.getRandomData(trainer);
   }

   public BaseTrainer getRandomBaseWithData() {
      TrainerData data = null;

      BaseTrainer t;
      for(t = null; data == null; data = this.getRandomData(t.name)) {
         t = this.getRandomBase();
      }

      return t;
   }

   public static BaseTrainer getByName(String name) {
      NPCRegistryData data = (NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us);
      if (data != null) {
         Iterator var2 = data.trainerTypes.iterator();

         while(var2.hasNext()) {
            BaseTrainer trainer = (BaseTrainer)var2.next();
            if (trainer.name.equalsIgnoreCase(name)) {
               return trainer;
            }
         }
      }

      return Steve;
   }

   public TrainerData getTranslatedData(String langCode, BaseTrainer baseTrainer, String id) {
      String lowerLangCode = langCode.toLowerCase();
      if (!ServerNPCRegistry.data.containsKey(lowerLangCode)) {
         try {
            ServerNPCRegistry.registerNPCS(lowerLangCode);
         } catch (LanguageNotFoundException var6) {
            ServerNPCRegistry.data.put(lowerLangCode, ServerNPCRegistry.data.get(ServerNPCRegistry.en_us));
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

      TrainerData trainer = this.findTrainerData(lowerLangCode, baseTrainer, id);
      return trainer != null ? trainer : this.findTrainerData(ServerNPCRegistry.en_us, baseTrainer, id);
   }

   private TrainerData findTrainerData(String langCode, BaseTrainer baseTrainer, String id) {
      NPCRegistryData npcData = (NPCRegistryData)ServerNPCRegistry.data.get(langCode);
      if (npcData != null) {
         List trainerData = (List)npcData.trainers.get(baseTrainer);
         if (trainerData != null) {
            Iterator var6 = trainerData.iterator();

            while(var6.hasNext()) {
               TrainerData trainer = (TrainerData)var6.next();
               if (trainer.id.equals(id)) {
                  return trainer;
               }
            }
         }
      }

      return null;
   }

   public String getTranslatedRandomName(String langCode, BaseTrainer baseTrainer, String id) {
      try {
         ArrayList names = this.getTranslatedData(langCode.toLowerCase(), baseTrainer, id).names;
         return (String)RandomHelper.getRandomElementFromList(names);
      } catch (NullPointerException var5) {
         return "Steve";
      }
   }

   public ArrayList getTypes() {
      return ServerNPCRegistry.data != null && ServerNPCRegistry.data.get(ServerNPCRegistry.en_us) != null ? ((NPCRegistryData)ServerNPCRegistry.data.get(ServerNPCRegistry.en_us)).trainerTypes : new ArrayList();
   }
}
