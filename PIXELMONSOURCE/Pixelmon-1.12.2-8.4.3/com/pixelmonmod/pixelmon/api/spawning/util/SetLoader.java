package com.pixelmonmod.pixelmon.api.spawning.util;

import com.google.common.collect.ImmutableList;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.RCFileHelper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

public class SetLoader {
   private static HashMap loadedSets = new HashMap();
   public static Class targetedSpawnSetClass = SpawnSet.class;
   public static final String SPAWN_SET_ROOT = "pixelmon/spawning/";
   public static final String DEFAULT_SPAWN_SET_FOLDER = "default";

   public static ImmutableList getAllSets() {
      ArrayList loadedSetsT = new ArrayList();
      Iterator var1 = loadedSets.values().iterator();

      while(var1.hasNext()) {
         SpawnSet spawnSet = (SpawnSet)var1.next();
         loadedSetsT.add(spawnSet);
      }

      return ImmutableList.copyOf(loadedSetsT);
   }

   public static void clearAll() {
      loadedSets.clear();
   }

   public static Object getSet(String name) {
      return loadedSets.get(name);
   }

   public static void export(String dir, String name) {
      SpawnSet spawnSet = (SpawnSet)loadedSets.get(name);
      if (spawnSet != null) {
         spawnSet.export(dir);
      }

   }

   public static void exportAll(String dir) {
      Iterator var1 = loadedSets.values().iterator();

      while(var1.hasNext()) {
         SpawnSet spawnSet = (SpawnSet)var1.next();
         spawnSet.export(dir);
      }

   }

   public static ArrayList importSetsFrom(String dir) {
      ArrayList setList = new ArrayList();
      File file = new File(dir);
      if (file.exists()) {
         ArrayList jsons = new ArrayList();
         if (file.isDirectory()) {
            recursiveSetSearch(dir, jsons);
         } else if (dir.endsWith(".json")) {
            jsons.add(file);
         }

         while(!jsons.isEmpty()) {
            File json = (File)jsons.remove(0);

            try {
               SpawnSet spawnSet = SpawnSet.deserialize((Reader)(new FileReader(json)));
               if (spawnSet.spawnInfos == null) {
                  Pixelmon.LOGGER.log(Level.WARN, "There was a nulled spawnInfo list in set: " + spawnSet.id);
               } else {
                  spawnSet.spawnInfos.removeIf((spawnInfo) -> {
                     if (spawnInfo instanceof SpawnInfoPokemon) {
                        return !PixelmonConfig.isGenerationEnabled(((SpawnInfoPokemon)spawnInfo).getSpecies().getGeneration());
                     } else {
                        return false;
                     }
                  });
                  setList.add(spawnSet);
                  loadedSets.put(spawnSet.id, spawnSet);
               }
            } catch (Exception var6) {
               Pixelmon.LOGGER.error(json.getName() + " has JSON syntax problems! Here's a long error:");
               var6.printStackTrace();
            }
         }
      }

      return setList;
   }

   public static void recursiveSetSearch(String dir, ArrayList jsons) {
      File file = new File(dir);
      String[] var3 = file.list();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String name = var3[var5];
         File subFile = new File(dir + "/" + name);
         if (subFile.isFile() && name.endsWith(".json")) {
            jsons.add(subFile);
         } else if (subFile.isDirectory()) {
            recursiveSetSearch(dir + "/" + name, jsons);
         }
      }

   }

   public static void addSet(SpawnSet set) {
      loadedSets.put(set.id, set);
   }

   public static String getSpawnSetPath() {
      return "pixelmon/spawning/" + PixelmonConfig.spawnSetFolder;
   }

   public static void checkForMissingSpawnSets() {
      File file = new File("pixelmon/spawning/default/");
      file.mkdirs();
      retrieveSpawnSetsFromAssets("");
   }

   public static ArrayList retrieveSpawnSetsFromAssets(String folder) {
      ArrayList spawnSets = new ArrayList();

      try {
         Path path = RCFileHelper.pathFromResourceLocation(new ResourceLocation("pixelmon", "spawning/" + folder));
         List setPaths = RCFileHelper.listFilesRecursively(path, (entry) -> {
            return entry.getFileName().toString().endsWith(".json") && !entry.getFileName().toString().contains("BetterSpawnerConfig");
         }, true);
         Iterator var4 = setPaths.iterator();

         while(var4.hasNext()) {
            Path setPath = (Path)var4.next();

            try {
               Path lowerPath = setPath;
               String root = "/assets/pixelmon/spawning/";

               String assetPath;
               for(assetPath = ""; !lowerPath.getParent().endsWith("spawning"); lowerPath = lowerPath.getParent()) {
                  assetPath = lowerPath.getParent().getFileName().toString() + "/" + assetPath;
               }

               InputStream iStream = SetLoader.class.getResourceAsStream(root + assetPath + setPath.getFileName());
               if (iStream == null) {
                  Pixelmon.LOGGER.log(Level.WARN, "Couldn't find internal spawning JSON at " + root + assetPath + setPath.getFileName());
               } else {
                  String json = "";
                  SpawnSet set = null;

                  try {
                     Scanner s = new Scanner(iStream);
                     s.useDelimiter("\\A");
                     json = s.hasNext() ? s.next() : "";
                     s.close();
                     set = SpawnSet.deserialize(json);
                     if (set.spawnInfos != null && !set.spawnInfos.isEmpty()) {
                        set.spawnInfos.removeIf((spawnInfo) -> {
                           if (!(spawnInfo instanceof SpawnInfoPokemon)) {
                              return false;
                           } else {
                              SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;
                              EnumSpecies species = EnumSpecies.getFromNameAnyCaseNoTranslate(spawnInfoPokemon.getPokemonSpec().name);
                              if (species == null) {
                                 Pixelmon.LOGGER.warn("Bad pokemon name: " + spawnInfoPokemon.getPokemonSpec().name + " in " + spawnInfo.set.id + ".set.json");
                              }

                              return species == null || !PixelmonConfig.isGenerationEnabled(species.getGeneration());
                           }
                        });
                     }
                  } catch (Exception var17) {
                     Pixelmon.LOGGER.error("Couldn't load spawn JSON: " + root + assetPath + setPath.getFileName());
                     throw var17;
                  }

                  spawnSets.add(set);
                  if (PixelmonConfig.useExternalJSONFilesSpawning) {
                     String primaryPath = "./pixelmon/spawning/default";

                     String relevantPath;
                     for(relevantPath = ""; !setPath.getParent().endsWith("spawning"); setPath = setPath.getParent()) {
                        relevantPath = setPath.getParent().getFileName().toString() + "/" + relevantPath;
                     }

                     File file = new File(primaryPath + "/" + relevantPath + set.id + ".set.json");
                     if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        PrintWriter pw = new PrintWriter(file);
                        pw.write(json);
                        pw.flush();
                        pw.close();
                     }
                  } else {
                     loadedSets.put(set.id, set);
                  }

                  try {
                     iStream.close();
                  } catch (IOException var16) {
                     var16.printStackTrace();
                  }
               }
            } catch (Exception var18) {
               var18.printStackTrace();
            }
         }
      } catch (Exception var19) {
         var19.printStackTrace();
      }

      return spawnSets;
   }
}
