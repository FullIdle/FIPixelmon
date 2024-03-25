package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.EvoConditionTypeAdapter;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.EvolutionTypeAdapter;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.EvoCondition;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

public class BaseStatsLoader {
   public static final transient Gson GSON;

   public static void loadAllBaseStats() {
      if (PixelmonConfig.useExternalJSONFilesStats) {
         (new File("./pixelmon/stats/")).mkdirs();
      }

      EnumSpecies[] var0 = EnumSpecies.values();
      int var1 = var0.length;

      BaseStats bs;
      for(int var2 = 0; var2 < var1; ++var2) {
         EnumSpecies species = var0[var2];

         try {
            if (!PixelmonConfig.useExternalJSONFilesStats) {
               BaseStats.allBaseStats.put(species, getBaseStatsFromAssets(species));
            } else {
               bs = null;

               try {
                  bs = getBaseStatsFromExternal(species, true);
               } catch (Exception var8) {
                  Pixelmon.LOGGER.error("Could not load external base stat file for {}", species.name());
                  var8.printStackTrace();
               }

               if (bs == null) {
                  Pixelmon.LOGGER.warn("Failed to load external base stat file for {}, Loading internal instead", species.name());
                  bs = getBaseStatsFromAssets(species);
               }

               BaseStats.allBaseStats.put(species, bs);
            }
         } catch (Exception var9) {
            Pixelmon.LOGGER.error("Could not load base stats for {}, file: {}.json, external: {}", species.getPokemonName(), PixelmonConfig.useExternalJSONFilesStats ? species.name() : species.getNationalPokedexNumber(), PixelmonConfig.useExternalJSONFilesStats);
            var9.printStackTrace();
         }
      }

      Iterator var10 = BaseStats.allBaseStats.values().iterator();

      while(var10.hasNext()) {
         BaseStats stat = (BaseStats)var10.next();
         List stats = Lists.newArrayList(new BaseStats[]{stat});
         if (stat.forms != null) {
            stats.addAll(stat.forms.values());
         }

         stat.initLevelupMoves();
         Iterator var13;
         if (stat.forms != null) {
            var13 = stat.forms.entrySet().iterator();

            while(var13.hasNext()) {
               Map.Entry entry = (Map.Entry)var13.next();
               ((BaseStats)entry.getValue()).initLevelupMoves();
               if (((BaseStats)entry.getValue()).form == 0) {
                  ((BaseStats)entry.getValue()).form = (Integer)entry.getKey();
               }
            }
         }

         var13 = stats.iterator();

         while(var13.hasNext()) {
            bs = (BaseStats)var13.next();
            Iterator var5 = bs.getTutorMoves().iterator();

            Attack a;
            AttackBase ab;
            while(var5.hasNext()) {
               a = (Attack)var5.next();
               ab = a.getActualMove();
               if (!NPCTutor.allTutorMoves.contains(ab)) {
                  NPCTutor.allTutorMoves.add(ab);
               }
            }

            var5 = bs.getTransferMoves().iterator();

            while(var5.hasNext()) {
               a = (Attack)var5.next();
               ab = a.getActualMove();
               if (!NPCTutor.allTransferMoves.contains(ab)) {
                  NPCTutor.allTransferMoves.add(ab);
               }
            }

            bs.calculateMinMaxLevels();
         }
      }

   }

   public static BaseStats getBaseStatsFromAssets(EnumSpecies species) throws IOException {
      String path = "/assets/pixelmon/stats/" + species.getNationalPokedexNumber() + ".json";

      try {
         Reader reader = new InputStreamReader(BaseStats.class.getResourceAsStream(path));
         Throwable var3 = null;

         BaseStats var5;
         try {
            BaseStats bs = (BaseStats)GSON.fromJson(reader, BaseStats.class);
            prepare(species, bs);
            var5 = bs;
         } catch (Throwable var15) {
            var3 = var15;
            throw var15;
         } finally {
            if (reader != null) {
               if (var3 != null) {
                  try {
                     reader.close();
                  } catch (Throwable var14) {
                     var3.addSuppressed(var14);
                  }
               } else {
                  reader.close();
               }
            }

         }

         return var5;
      } catch (Exception var17) {
         Pixelmon.LOGGER.error("Couldn't load internal stat JSON: " + path);
         throw var17;
      }
   }

   @Nullable
   public static BaseStats getBaseStatsFromExternal(EnumSpecies species, boolean exportIfMissing) throws IOException {
      File file = new File("./pixelmon/stats/" + species.name() + ".json");
      if (!file.exists()) {
         if (!exportIfMissing) {
            return null;
         }

         String path = "/assets/pixelmon/stats/" + species.getNationalPokedexNumber() + ".json";

         try {
            InputStream iStream = BaseStats.class.getResourceAsStream(path);
            Throwable var5 = null;

            try {
               Files.copy(iStream, file.toPath(), new CopyOption[0]);
            } catch (Throwable var33) {
               var5 = var33;
               throw var33;
            } finally {
               if (iStream != null) {
                  if (var5 != null) {
                     try {
                        iStream.close();
                     } catch (Throwable var32) {
                        var5.addSuppressed(var32);
                     }
                  } else {
                     iStream.close();
                  }
               }

            }
         } catch (IOException var37) {
            Pixelmon.LOGGER.error("Failed to export base stat file {}", path);
            var37.printStackTrace();
         }
      }

      if (file.exists()) {
         try {
            FileReader reader = new FileReader(file);
            Throwable var39 = null;

            BaseStats var6;
            try {
               BaseStats bs = (BaseStats)BaseStats.GSON.fromJson(reader, BaseStats.class);
               prepare(species, bs);
               var6 = bs;
            } catch (Throwable var31) {
               var39 = var31;
               throw var31;
            } finally {
               if (reader != null) {
                  if (var39 != null) {
                     try {
                        reader.close();
                     } catch (Throwable var30) {
                        var39.addSuppressed(var30);
                     }
                  } else {
                     reader.close();
                  }
               }

            }

            return var6;
         } catch (Exception var35) {
            throw var35;
         }
      } else {
         return null;
      }
   }

   private static void prepare(EnumSpecies species, BaseStats bs) {
      bs.nationalPokedexNumber = species.getNationalPokedexInteger();
      bs.preEvolutions = bs.preEvolutions == null ? new String[0] : bs.preEvolutions;
      if (bs.forms != null && !bs.forms.isEmpty()) {
         Iterator var2 = bs.forms.values().iterator();

         while(var2.hasNext()) {
            BaseStats form = (BaseStats)var2.next();
            form.expand(bs);
         }
      }

      List legacyPreEvolutions = new ArrayList();
      List specPreEvolutions = new ArrayList();
      String[] var4 = bs.preEvolutions;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String preEvolution = var4[var6];
         PokemonSpec spec = new PokemonSpec(preEvolution.split(" "));
         if (spec != null) {
            Optional s = EnumSpecies.getFromName(spec.name);
            if (s.isPresent() && s.get() != null) {
               legacyPreEvolutions.add(s.get());
               specPreEvolutions.add(spec);
            } else {
               Pixelmon.LOGGER.error("Found Invalid pokemonSpec in preEvolutions for " + species.name() + ".json matching \"" + spec.toString() + "\"");
            }
         }
      }

      bs.specPreEvolutions = new PokemonSpec[specPreEvolutions == null ? 0 : specPreEvolutions.size()];
      specPreEvolutions.toArray(bs.specPreEvolutions);
      bs.legacyPreEvolutions = new EnumSpecies[legacyPreEvolutions == null ? 0 : legacyPreEvolutions.size()];
      legacyPreEvolutions.toArray(bs.legacyPreEvolutions);
   }

   static {
      GSON = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(Evolution.class, new EvolutionTypeAdapter()).registerTypeAdapter(EvoCondition.class, new EvoConditionTypeAdapter()).registerTypeAdapter(Attack.class, new AttackTypeAdapter()).registerTypeAdapter(AttackBase.class, new AttackBaseAdapter()).registerTypeAdapter(PokemonSpec.class, PokemonSpec.SPEC_ADAPTER).registerTypeHierarchyAdapter(ITechnicalMove.class, new ITechnicalMove.Adapter()).create();
   }
}
