package com.pixelmonmod.pixelmon.api.trading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import com.pixelmonmod.pixelmon.util.helpers.RCFileHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

public class PossibleTradeList {
   private transient ArrayList filteredAllPossibleTrades = new ArrayList();
   private ArrayList allPossibleTrades = new ArrayList();
   private static PossibleTradeList INSTANCE;
   private static final String PATH = "npcs/trades.json";
   private static final Gson GSON;

   public static void registerTrades() {
      try {
         NPCTrades.UNTRADEABLE = new PokemonSpec("untradeable");
         File file = new File("pixelmon/npcs/trades.json");
         if (PixelmonConfig.useExternalJSONFilesNPCs && file.exists()) {
            file.getParentFile().mkdirs();
            INSTANCE = (PossibleTradeList)GSON.fromJson(new FileReader(file), PossibleTradeList.class);
            INSTANCE.allPossibleTrades = new ArrayList(INSTANCE.allPossibleTrades);
            filterDisabledGenerations();
            if (PixelmonConfig.allGenerationsEnabled()) {
               Pixelmon.LOGGER.info("Registered NPC trades.");
            }
         } else {
            Path path = RCFileHelper.pathFromResourceLocation(new ResourceLocation("pixelmon", "npcs/trades.json"));
            InputStream iStream = PossibleTradeList.class.getResourceAsStream("/assets/pixelmon/npcs/trades.json");
            if (iStream == null) {
               Pixelmon.LOGGER.log(Level.WARN, "Couldn't find the trade list JSON: " + path.toString());
            }

            Scanner s = new Scanner(iStream);
            s.useDelimiter("\\A");
            String json = s.hasNext() ? s.next() : "";
            INSTANCE = (PossibleTradeList)GSON.fromJson(json, PossibleTradeList.class);
            s.close();
            if (PixelmonConfig.useExternalJSONFilesNPCs) {
               file.getParentFile().mkdirs();
               file.createNewFile();
               PrintWriter pw = new PrintWriter(file);
               pw.write(json);
               pw.flush();
               pw.close();
            }

            filterDisabledGenerations();
         }
      } catch (IOException | URISyntaxException var6) {
         var6.printStackTrace();
         Pixelmon.LOGGER.error("Unable to register NPC trades.");
         INSTANCE = new PossibleTradeList();
         INSTANCE.allPossibleTrades.add(new TradePair(new PokemonSpec("Magikarp"), new PokemonSpec("Magikarp")));
         if (INSTANCE.filteredAllPossibleTrades.isEmpty()) {
            INSTANCE.filteredAllPossibleTrades.add(new TradePair(new PokemonSpec("Magikarp"), new PokemonSpec("Magikarp")));
         }
      }

   }

   private static void filterDisabledGenerations() {
      if (PixelmonConfig.allGenerationsEnabled()) {
         INSTANCE.filteredAllPossibleTrades = new ArrayList(INSTANCE.allPossibleTrades);
      } else {
         INSTANCE.filteredAllPossibleTrades = new ArrayList();
         Iterator var0 = (new ArrayList(INSTANCE.allPossibleTrades)).iterator();

         while(var0.hasNext()) {
            TradePair s = (TradePair)var0.next();

            try {
               EnumSpecies request = EnumSpecies.getFromNameAnyCase(s.exchange.name);
               if (request != null) {
                  EnumSpecies offer = EnumSpecies.getFromNameAnyCase(s.offer.name);
                  if (offer != null && PixelmonConfig.isGenerationEnabled(request.getGeneration()) && PixelmonConfig.isGenerationEnabled(offer.getGeneration())) {
                     INSTANCE.filteredAllPossibleTrades.add(s);
                  }
               }
            } catch (Exception var4) {
               Pixelmon.LOGGER.warn("Failed to register trade exchanging " + (s != null ? (s.exchange != null ? s.exchange.name : "null") : "null") + " for " + (s != null ? (s.offer != null ? s.offer.name : "null") : "null") + (s == null ? "" : (s.description != null && !s.description.isEmpty() ? " with description \"" + s.description + "\"" : "")), var4);
            }
         }

         if (INSTANCE.filteredAllPossibleTrades.isEmpty()) {
            INSTANCE.filteredAllPossibleTrades.add(new TradePair(new PokemonSpec("Magikarp"), new PokemonSpec("Magikarp")));
            String enabledGenerations = "";

            for(int i = 0; i <= 8; ++i) {
               if (PixelmonConfig.isGenerationEnabled(i)) {
                  if (!enabledGenerations.isEmpty()) {
                     enabledGenerations = enabledGenerations + ",";
                  }

                  enabledGenerations = enabledGenerations + "gen" + i;
               }
            }

            if (enabledGenerations.isEmpty()) {
               enabledGenerations = "No";
            }

            if (!PixelmonConfig.allGenerationsEnabled()) {
               Pixelmon.LOGGER.error("No valid trades could be registered with only " + enabledGenerations + " being enabled!");
            }
         } else if (!PixelmonConfig.allGenerationsEnabled()) {
            Pixelmon.LOGGER.info("Registered NPC trades. Filtered " + (INSTANCE.allPossibleTrades.size() - INSTANCE.filteredAllPossibleTrades.size()) + " trades containing disabled generation pokemon!");
         }
      }

   }

   public static void save() {
      try {
         (new File("pixelmon/npcs/trades.json")).getParentFile().mkdirs();
         PrintWriter pw = new PrintWriter(new File("pixelmon/npcs/trades.json"));
         String json = GSON.toJson(INSTANCE);
         pw.write(json);
         pw.flush();
         pw.close();
         Pixelmon.LOGGER.info("Saved NPC trades.");
      } catch (FileNotFoundException var2) {
         var2.printStackTrace();
      }

   }

   public static ArrayList getAllPossibleTrades() {
      return PixelmonConfig.allGenerationsEnabled() ? INSTANCE.allPossibleTrades : INSTANCE.filteredAllPossibleTrades;
   }

   public static TradePair getRandomTrade() {
      return (TradePair)CollectionHelper.getRandomElement((List)getAllPossibleTrades());
   }

   static {
      GSON = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(PokemonSpec.class, PokemonSpec.SPEC_ADAPTER).create();
   }
}
