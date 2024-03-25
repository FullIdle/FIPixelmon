package com.pixelmonmod.pixelmon.battles.rules.clauses.tiers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RulesRegistry {
   private static final String ASSETS_FOLDER;
   private static final String JSON_FOLDER;
   private static final String RULES_FOLDER;
   private static final String TIER_FOLDER;
   private static final String CUSTOM_FOLDER;
   private static final String RULESET_FOLDER;
   private static final String JSON_EXTENSION = ".json";
   private static final String BATTLE_SPOT_FILE;

   private RulesRegistry() {
   }

   public static void registerRules() {
      Pixelmon.LOGGER.info("Registering battle rules.");
      File rulesDir = new File("./pixelmon/rules/");
      if (PixelmonConfig.useExternalJSONFilesRules && !rulesDir.exists()) {
         Pixelmon.LOGGER.info("Creating rules directory.");
         rulesDir.mkdirs();
      }

      registerTiers();
      registerRulesets();
   }

   private static void registerTiers() {
      File tierDir = new File("./pixelmon/rules/tiers");
      File customTiers = new File(tierDir, "custom");
      if (PixelmonConfig.useExternalJSONFilesRules && !customTiers.exists()) {
         customTiers.mkdirs();
         extractTierDirs(tierDir);
      }

      EnumTier[] var2 = EnumTier.values();
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         EnumTier tier = var2[var4];
         if (tier != EnumTier.PU) {
            try {
               registerTier(tierDir, tier, PixelmonConfig.useExternalJSONFilesRules);
            } catch (IOException var11) {
               printError(tier.name().toLowerCase() + ".json", var11);
            }
         }
      }

      BattleClauseRegistry.getTierRegistry().removeCustomClauses();
      if (PixelmonConfig.useExternalJSONFilesRules) {
         File[] fileList = customTiers.listFiles();
         if (fileList == null) {
            fileList = new File[0];
         }

         File[] var13 = fileList;
         var4 = fileList.length;

         for(int var14 = 0; var14 < var4; ++var14) {
            File file = var13[var14];
            String fileName = file.getName();
            if (fileName.endsWith(".json")) {
               try {
                  registerTier(customTiers, fileName, PixelmonConfig.useExternalJSONFilesRules);
               } catch (IOException var10) {
                  printError(fileName, var10);
               }
            }
         }
      } else {
         try {
            registerTier(tierDir, BATTLE_SPOT_FILE, PixelmonConfig.useExternalJSONFilesRules);
         } catch (IOException var9) {
            printError(BATTLE_SPOT_FILE, var9);
         }
      }

   }

   private static void registerRulesets() {
      File rulesetDir = new File("./pixelmon/rules/rulesets");
      boolean useExternal = PixelmonConfig.useExternalJSONFilesRules;
      if (useExternal && !rulesetDir.isDirectory()) {
         rulesetDir.mkdir();
      }

   }

   private static void printError(String tierFile, Exception e) {
      Pixelmon.LOGGER.error("Error occurred when reading tier file: " + tierFile + ".");
      e.printStackTrace();
   }

   private static void registerTier(File tierDir, String tierFileName, boolean useExternal) throws IOException {
      registerTier(tierDir, (EnumTier)null, tierFileName, true, useExternal);
   }

   private static void registerTier(File tierDir, EnumTier tier, boolean useExternal) throws IOException {
      registerTier(tierDir, tier, tier.name().toLowerCase() + ".json", false, useExternal);
   }

   private static void registerTier(File tierDir, EnumTier tier, String tierFile, boolean isCustom, boolean useExternal) throws IOException {
      InputStream iStream = null;

      try {
         if (useExternal) {
            iStream = new FileInputStream(new File(tierDir, tierFile));
         } else {
            iStream = ServerNPCRegistry.class.getResourceAsStream("/assets/pixelmon/rules/tiers/" + tierFile.replace(File.separator, "/"));
         }

         JsonObject json = (new JsonParser()).parse(new InputStreamReader((InputStream)iStream, StandardCharsets.UTF_8)).getAsJsonObject();
         if (isCustom) {
            String id = tierFile;
            int lastPeriod = tierFile.lastIndexOf(46);
            if (lastPeriod > -1) {
               id = tierFile.substring(0, lastPeriod);
            }

            loadCustomTier(id, json);
         } else if (useExternal && !json.get("usecustom").getAsBoolean()) {
            registerTier(tierDir, tier, tierFile, isCustom, false);
         } else {
            loadDefaultTier(tier, json);
         }
      } catch (IOException var13) {
         throw var13;
      } catch (Exception var14) {
         printError(tierFile, var14);
         if (useExternal && !isCustom) {
            Pixelmon.LOGGER.info("Loading internal tier file instead of external tier file.");
            registerTier(tierDir, tier, tierFile, isCustom, false);
         }
      } finally {
         if (iStream != null) {
            ((InputStream)iStream).close();
         }

      }

   }

   private static void loadDefaultTier(EnumTier tier, JsonObject json) {
      TierSet tierClause = (TierSet)BattleClauseRegistry.getTierRegistry().getClause(tier.getTierID());
      tierClause.setPokemon(getPokemonSet(json));
   }

   private static void loadCustomTier(String id, JsonObject json) {
      TierAllowedSet tier = new TierAllowedSet(id, getPokemonSet(json), json.get("banlist").getAsBoolean());
      BattleClauseRegistry.getTierRegistry().registerCustomClause(tier);
   }

   private static Set getPokemonSet(JsonObject json) {
      Set pokemonSet = new HashSet();
      JsonArray pokemonJSON = json.get("pokemon").getAsJsonArray();

      for(int i = 0; i < pokemonJSON.size(); ++i) {
         String pokemon = pokemonJSON.get(i).getAsString();
         Optional form = PokemonForm.getFromName(pokemon);
         if (form.isPresent()) {
            pokemonSet.add(form.get());
         } else {
            Pixelmon.LOGGER.warn("Pokémon not found when registering tier: " + pokemon + ".");
         }
      }

      return pokemonSet;
   }

   private static void extractTierDirs(File tierDir) {
      EnumTier[] var1 = EnumTier.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumTier tier = var1[var3];
         if (tier != EnumTier.PU) {
            extractTierDir(tierDir, tier.name().toLowerCase() + ".json");
         }
      }

      extractTierDir(tierDir, BATTLE_SPOT_FILE);
   }

   private static void extractTierDir(File tierDir, String fileName) {
      ServerNPCRegistry.extractFile("/assets/pixelmon/rules/tiers/" + fileName, tierDir, fileName);
   }

   static {
      ASSETS_FOLDER = File.separator + "assets";
      JSON_FOLDER = File.separator + "pixelmon";
      RULES_FOLDER = File.separator + "rules";
      TIER_FOLDER = File.separator + "tiers";
      CUSTOM_FOLDER = File.separator + "custom";
      RULESET_FOLDER = File.separator + "rulesets";
      BATTLE_SPOT_FILE = CUSTOM_FOLDER.substring(1) + File.separator + "battlespot" + ".json";
   }
}
