package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.raids.RegisterRaidEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.util.Tuple;

public class RaidSpawningRegistry {
   public static ArrayList raids = new ArrayList();
   public static HashMap map = new HashMap();

   public static void registerRaidSpawning() throws FileNotFoundException {
      Pixelmon.LOGGER.info("Registering raid spawning.");
      String path = Pixelmon.modDirectory + "/pixelmon/raids/";
      File raidsDir = new File(path);
      if (PixelmonConfig.useExternalJSONFilesSpawning && !raidsDir.isDirectory()) {
         File baseDir = new File(Pixelmon.modDirectory + "/pixelmon");
         if (!baseDir.isDirectory()) {
            baseDir.mkdir();
         }

         Pixelmon.LOGGER.info("Creating raids directory.");
         raidsDir.mkdir();
         extractRaidsDir(raidsDir);
      }

      Object iStream;
      if (!PixelmonConfig.useExternalJSONFilesSpawning) {
         iStream = RaidSpawningRegistry.class.getResourceAsStream("/assets/pixelmon/raids/raids.json");
      } else {
         iStream = new FileInputStream(new File(raidsDir, "raids.json"));
      }

      JsonObject json = (new JsonParser()).parse(new InputStreamReader((InputStream)iStream, StandardCharsets.UTF_8)).getAsJsonObject();
      registerRaidSpawns(json);
      remap();
   }

   private static void registerRaidSpawns(JsonObject json) {
      raids.clear();
      Iterator var1 = json.entrySet().iterator();

      while(true) {
         JsonArray species;
         ArrayList finalBiomes;
         HashMap finalSpecies;
         RegisterRaidEvent.Register event;
         do {
            Map.Entry entry;
            do {
               if (!var1.hasNext()) {
                  return;
               }

               entry = (Map.Entry)var1.next();
            } while(!((JsonElement)entry.getValue()).isJsonObject());

            JsonObject inner = (JsonObject)entry.getValue();
            JsonArray biomes = inner.getAsJsonArray("biomes");
            species = inner.getAsJsonArray("species");
            finalBiomes = new ArrayList();
            Iterator var7 = biomes.iterator();

            while(var7.hasNext()) {
               JsonElement element = (JsonElement)var7.next();
               finalBiomes.add(element.getAsString());
            }

            finalSpecies = new HashMap();

            for(int i = 1; i <= 5; ++i) {
               finalSpecies.put(i, new ArrayList());
            }

            event = new RegisterRaidEvent.Register(finalSpecies);
            Pixelmon.EVENT_BUS.post(event);
         } while(!event.shouldUseDefaults());

         Iterator var9 = species.iterator();

         while(var9.hasNext()) {
            JsonElement element = (JsonElement)var9.next();
            String str = element.getAsString();

            try {
               String[] split = str.split("-");
               String[] starStrs = split[0].split(",");
               EnumSpecies pokemon = EnumSpecies.valueOf(split[1]);
               if ((PixelmonConfig.raidHaveLegendaries || !pokemon.isLegendary()) && (PixelmonConfig.raidHaveUltraBeasts || !pokemon.isUltraBeast()) && !PixelmonConfig.raidBlacklist.contains(pokemon.getPokemonName()) && PixelmonConfig.isGenerationEnabled(pokemon.getGeneration())) {
                  IEnumForm form = null;
                  if (split.length > 2) {
                     form = pokemon.getFormEnum(split[2]);
                  }

                  String[] var16 = starStrs;
                  int var17 = starStrs.length;

                  for(int var18 = 0; var18 < var17; ++var18) {
                     String starStr = var16[var18];
                     int star = Integer.parseInt(starStr);
                     RegisterRaidEvent.AddDefault e1 = new RegisterRaidEvent.AddDefault(finalSpecies, star, new Tuple(pokemon, Optional.ofNullable(form)));
                     if (!Pixelmon.EVENT_BUS.post(e1)) {
                        ((ArrayList)finalSpecies.get(e1.getStars())).add(e1.getRaid());
                     }
                  }
               }
            } catch (Exception var22) {
               Pixelmon.LOGGER.error("Failed to parse raid string: " + str);
            }
         }

         raids.add(new Tuple(finalBiomes, finalSpecies));
      }
   }

   public static void remap() {
      map.clear();
      Iterator var0 = raids.iterator();

      while(var0.hasNext()) {
         Tuple group = (Tuple)var0.next();
         Iterator var2 = ((ArrayList)group.func_76341_a()).iterator();

         while(var2.hasNext()) {
            String biome = (String)var2.next();
            biome = biome.toLowerCase(Locale.ROOT).replace("_", " ");
            if (!map.containsKey(biome)) {
               map.put(biome, new HashMap());

               for(int i = 1; i <= 5; ++i) {
                  ((HashMap)map.get(biome)).put(i, new ArrayList());
               }
            }

            Iterator var6 = ((HashMap)group.func_76340_b()).entrySet().iterator();

            while(var6.hasNext()) {
               Map.Entry entry = (Map.Entry)var6.next();
               ((ArrayList)((HashMap)map.get(biome)).get(entry.getKey())).addAll((Collection)entry.getValue());
            }
         }
      }

   }

   private static void extractRaidsDir(File raidsDir) {
      ServerNPCRegistry.extractFile("/assets/pixelmon/raids/raids.json", raidsDir, "raids.json");
   }
}
