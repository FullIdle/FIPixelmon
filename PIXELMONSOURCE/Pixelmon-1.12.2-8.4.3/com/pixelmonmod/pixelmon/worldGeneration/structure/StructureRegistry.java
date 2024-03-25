package com.pixelmonmod.pixelmon.worldGeneration.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.GymInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.WorldGymData;
import com.pixelmonmod.pixelmon.worldGeneration.structure.standalone.StandaloneStructureInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.towns.ComponentTownPart;
import com.pixelmonmod.pixelmon.worldGeneration.structure.towns.NPCPlacementInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.towns.TownStructureInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.towns.VillagePartHandler;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class StructureRegistry {
   public static ArrayList townStructures = new ArrayList();
   public static ArrayList standaloneStructures = new ArrayList();
   public static ArrayList gyms = new ArrayList();
   public static HashMap structuresByBiome = new HashMap();
   public static ArrayList structureIds = new ArrayList();

   public static void registerStructures() throws Exception {
      Pixelmon.LOGGER.info("Registering structures.");
      String path = Pixelmon.modDirectory + "/pixelmon/structures/";
      File structuresDir = new File(path);
      if (PixelmonConfig.useExternalJSONFilesStructures && !structuresDir.isDirectory()) {
         Pixelmon.LOGGER.info("Creating structures directory.");
         File baseDir = new File(Pixelmon.modDirectory + "/pixelmon");
         if (!baseDir.isDirectory()) {
            baseDir.mkdir();
         }

         structuresDir.mkdir();
         extractStructuresDir(structuresDir);
      }

      Object istream;
      if (!PixelmonConfig.useExternalJSONFilesStructures) {
         istream = StructureRegistry.class.getResourceAsStream("/assets/pixelmon/structures/structures.json");
      } else {
         istream = new FileInputStream(new File(structuresDir, "structures.json"));
      }

      JsonObject json = (new JsonParser()).parse(new InputStreamReader((InputStream)istream, StandardCharsets.UTF_8)).getAsJsonObject();
      JsonArray jsonarray;
      int i;
      JsonObject strucel;
      if (json.has("towns")) {
         jsonarray = JsonUtils.func_151214_t(json, "towns");

         for(i = 0; i < jsonarray.size(); ++i) {
            strucel = jsonarray.get(i).getAsJsonObject();
            String structureName = strucel.get("id").getAsString();
            if (PixelmonConfig.spawnPokemarts || !structureName.equals("townmart1")) {
               try {
                  registerTownStructure(strucel, path + "towns/");
               } catch (Exception var11) {
                  throw new Exception("Failed to load town structure " + strucel.get("id").getAsString() + ".", var11);
               }
            }
         }

         MapGenStructureIO.func_143031_a(ComponentTownPart.class, "TownPart");
         VillagerRegistry.instance().registerVillageCreationHandler(new VillagePartHandler());
      }

      if (json.has("gyms")) {
         jsonarray = JsonUtils.func_151214_t(json, "gyms");

         for(i = 0; i < jsonarray.size(); ++i) {
            strucel = jsonarray.get(i).getAsJsonObject();

            try {
               registerGym(strucel, path + "gyms/");
            } catch (Exception var10) {
               throw new Exception("Failed to load gym: " + strucel.get("id").getAsString() + ".", var10);
            }
         }
      }

      if (json.has("standalone")) {
         jsonarray = JsonUtils.func_151214_t(json, "standalone");

         for(i = 0; i < jsonarray.size(); ++i) {
            strucel = jsonarray.get(i).getAsJsonObject();

            try {
               registerStandaloneStructure(strucel, path + "standAlone/");
            } catch (Exception var9) {
               throw new Exception("Failed to load standalone structure " + strucel.get("id").getAsString() + ".", var9);
            }
         }
      }

   }

   private static void extractStructuresDir(File structuresDir) {
      InputStream istream = StructureRegistry.class.getResourceAsStream("/assets/pixelmon/structures/structures.json");
      extractFile("/assets/pixelmon/structures/structures.json", structuresDir, "structures.json");
      JsonObject json = (new JsonParser()).parse(new InputStreamReader(istream, StandardCharsets.UTF_8)).getAsJsonObject();
      File standAloneDir;
      JsonArray jsonarray;
      int i;
      JsonObject strucel;
      String filename;
      if (json.has("towns")) {
         standAloneDir = new File(structuresDir, "towns");
         standAloneDir.mkdir();
         jsonarray = JsonUtils.func_151214_t(json, "towns");

         for(i = 0; i < jsonarray.size(); ++i) {
            strucel = jsonarray.get(i).getAsJsonObject();
            filename = strucel.get("filename").getAsString();
            extractFile("/assets/pixelmon/structures/towns/" + filename, standAloneDir, filename);
         }
      }

      if (json.has("gyms")) {
         standAloneDir = new File(structuresDir, "gyms");
         standAloneDir.mkdir();
         jsonarray = JsonUtils.func_151214_t(json, "gyms");

         for(i = 0; i < jsonarray.size(); ++i) {
            strucel = jsonarray.get(i).getAsJsonObject();
            filename = strucel.get("filename").getAsString();
            extractFile("/assets/pixelmon/structures/gyms/" + filename, standAloneDir, filename);
            String npcfilename = strucel.get("npcdata").getAsString();
            extractFile("/assets/pixelmon/structures/gyms/" + npcfilename, standAloneDir, npcfilename);
         }
      }

      if (json.has("standalone")) {
         standAloneDir = new File(structuresDir, "standAlone");
         standAloneDir.mkdir();
         jsonarray = JsonUtils.func_151214_t(json, "standalone");

         for(i = 0; i < jsonarray.size(); ++i) {
            strucel = jsonarray.get(i).getAsJsonObject();
            filename = strucel.get("filename").getAsString();
            extractFile("/assets/pixelmon/structures/standAlone/" + filename, standAloneDir, filename);
         }
      }

   }

   private static void extractFile(String resourceName, File dir, String filename) {
      try {
         File file = new File(dir, filename);
         if (!file.exists()) {
            InputStream link = ServerNPCRegistry.class.getResourceAsStream(resourceName);
            InputStream in = new BufferedInputStream(link);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[2048];

            while(true) {
               int nBytes = in.read(buffer);
               if (nBytes <= 0) {
                  out.flush();
                  out.close();
                  in.close();
                  break;
               }

               out.write(buffer, 0, nBytes);
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   private static void registerStandaloneStructure(JsonObject strucObj, String path) throws IOException {
      StandaloneStructureInfo info = new StandaloneStructureInfo();
      String id = strucObj.get("id").getAsString();
      if (PixelmonConfig.spawnBirdShrines || !id.contains("shrine")) {
         info.setId(id);
         structureIds.add(id);
         if (strucObj.has("depth")) {
            int depth = strucObj.get("depth").getAsInt();
            info.setDepth(depth);
         }

         String filename = strucObj.get("filename").getAsString();
         if (!PixelmonConfig.useExternalJSONFilesStructures) {
            info.setSnapshot(loadSnapshot(StructureRegistry.class.getResourceAsStream("/assets/pixelmon/structures/standAlone/" + filename)));
         } else {
            NBTTagCompound compound = loadSnapshot(path + filename);
            info.setSnapshot(compound);
            if (compound.func_74771_c("formatVersion") == 0) {
               NBTTagCompound comp = new NBTTagCompound();
               info.snapshot.writeToNBT(comp);

               try {
                  DataOutputStream dataStream = new DataOutputStream(new FileOutputStream(new File(path + filename)));
                  Throwable var8 = null;

                  try {
                     CompressedStreamTools.func_74800_a(comp, dataStream);
                  } catch (Throwable var20) {
                     var8 = var20;
                     throw var20;
                  } finally {
                     if (dataStream != null) {
                        if (var8 != null) {
                           try {
                              dataStream.close();
                           } catch (Throwable var19) {
                              var8.addSuppressed(var19);
                           }
                        } else {
                           dataStream.close();
                        }
                     }

                  }
               } catch (IOException var22) {
                  var22.printStackTrace();
               }
            }
         }

         int rarity = strucObj.get("rarity").getAsInt();
         info.setRarity(rarity);
         JsonArray npcsArray;
         int j;
         if (strucObj.has("biomes")) {
            npcsArray = strucObj.get("biomes").getAsJsonArray();

            for(j = 0; j < npcsArray.size(); ++j) {
               Set categoryBiomes = Sets.newHashSet();
               SpawnCondition.cacheRegistry(Biome.class, Lists.newArrayList(new String[]{npcsArray.get(j).getAsString()}), categoryBiomes, BetterSpawnerConfig.INSTANCE.cachedBiomeCategories);
               Iterator var9 = categoryBiomes.iterator();

               while(var9.hasNext()) {
                  Biome biome = (Biome)var9.next();
                  info.addBiome(biome);
               }
            }
         }

         if (strucObj.has("npcs")) {
            npcsArray = strucObj.getAsJsonArray("npcs");

            for(j = 0; j < npcsArray.size(); ++j) {
               JsonObject npcsObj = npcsArray.get(j).getAsJsonObject();
               EnumNPCType type = EnumNPCType.getFromString(npcsObj.get("type").getAsString());
               int x = npcsObj.get("x").getAsInt();
               int y = npcsObj.get("y").getAsInt();
               int z = npcsObj.get("z").getAsInt();
               NPCPlacementInfo npcInfo = new NPCPlacementInfo(type, x, y, z);
               if (npcsObj.has("data")) {
                  npcInfo.setData(npcsObj.get("data").getAsString());
               }

               info.addNPC(npcInfo);
            }
         }

         Iterator var26 = info.getBiomes().iterator();

         while(var26.hasNext()) {
            Biome biome = (Biome)var26.next();
            ArrayList structures;
            if (structuresByBiome.containsKey(biome)) {
               structures = (ArrayList)structuresByBiome.get(biome);
            } else {
               structures = new ArrayList();
            }

            structures.add(info);
            structuresByBiome.put(biome, structures);
         }

         standaloneStructures.add(info);
      }
   }

   private static void registerGym(JsonObject strucObj, String path) throws Exception {
      GymInfo info = new GymInfo();
      String id = strucObj.get("id").getAsString();
      info.setId(id);
      structureIds.add(id);
      if (strucObj.has("depth")) {
         int depth = strucObj.get("depth").getAsInt();
         info.setDepth(depth);
      }

      String filename = strucObj.get("filename").getAsString();
      String npcfilename = strucObj.get("npcdata").getAsString();
      if (!PixelmonConfig.useExternalJSONFilesStructures) {
         info.setSnapshot(loadSnapshot(StructureRegistry.class.getResourceAsStream("/assets/pixelmon/structures/gyms/" + filename)));
         info.setGymInfo(npcfilename, StructureRegistry.class.getResourceAsStream("/assets/pixelmon/structures/gyms/" + npcfilename));
      } else {
         NBTTagCompound compound = loadSnapshot(path + filename);
         info.setSnapshot(compound);
         info.setGymInfo(npcfilename, new FileInputStream(path + npcfilename));
         if (compound.func_74771_c("formatVersion") == 0) {
            NBTTagCompound comp = new NBTTagCompound();
            info.snapshot.writeToNBT(comp);

            try {
               DataOutputStream dataStream = new DataOutputStream(new FileOutputStream(new File(path + filename)));
               Throwable var9 = null;

               try {
                  CompressedStreamTools.func_74800_a(comp, dataStream);
               } catch (Throwable var20) {
                  var9 = var20;
                  throw var20;
               } finally {
                  if (dataStream != null) {
                     if (var9 != null) {
                        try {
                           dataStream.close();
                        } catch (Throwable var19) {
                           var9.addSuppressed(var19);
                        }
                     } else {
                        dataStream.close();
                     }
                  }

               }
            } catch (IOException var22) {
               var22.printStackTrace();
            }
         }
      }

      if (strucObj.has("npcs")) {
         JsonArray npcsArray = strucObj.getAsJsonArray("npcs");

         for(int j = 0; j < npcsArray.size(); ++j) {
            JsonObject npcsObj = npcsArray.get(j).getAsJsonObject();
            EnumNPCType type = EnumNPCType.getFromString(npcsObj.get("type").getAsString());
            int x = npcsObj.get("x").getAsInt();
            int y = npcsObj.get("y").getAsInt();
            int z = npcsObj.get("z").getAsInt();
            NPCPlacementInfo npcInfo = new NPCPlacementInfo(type, x, y, z);
            if (npcsObj.has("data")) {
               npcInfo.setData(npcsObj.get("data").getAsString());
            }

            info.addNPC(npcInfo);
         }
      }

      gyms.add(info);
   }

   private static void registerTownStructure(JsonObject townObj, String path) throws IOException {
      TownStructureInfo info = new TownStructureInfo();
      String id = townObj.get("id").getAsString();
      info.setId(id);
      structureIds.add(id);
      int weighting = townObj.get("weighting").getAsInt();
      info.setWeighting(weighting);
      int maxNum = townObj.get("maxnum").getAsInt();
      info.setMaxNum(maxNum);
      VillagePartHandler.weightSum += weighting;
      VillagePartHandler.numCompSum += maxNum;
      if (townObj.has("depth")) {
         int depth = townObj.get("depth").getAsInt();
         info.setDepth(depth);
      }

      String filename = townObj.get("filename").getAsString();
      if (!PixelmonConfig.useExternalJSONFilesStructures) {
         info.setSnapshot(loadSnapshot(StructureRegistry.class.getResourceAsStream("/assets/pixelmon/structures/towns/" + filename)));
      } else {
         NBTTagCompound compound = loadSnapshot(path + filename);
         info.setSnapshot(compound);
         if (compound.func_74771_c("formatVersion") == 0) {
            NBTTagCompound comp = new NBTTagCompound();
            info.snapshot.writeToNBT(comp);

            try {
               DataOutputStream dataStream = new DataOutputStream(new FileOutputStream(new File(path + filename)));
               Throwable var10 = null;

               try {
                  CompressedStreamTools.func_74800_a(comp, dataStream);
               } catch (Throwable var21) {
                  var10 = var21;
                  throw var21;
               } finally {
                  if (dataStream != null) {
                     if (var10 != null) {
                        try {
                           dataStream.close();
                        } catch (Throwable var20) {
                           var10.addSuppressed(var20);
                        }
                     } else {
                        dataStream.close();
                     }
                  }

               }
            } catch (IOException var23) {
               var23.printStackTrace();
            }
         }
      }

      if (townObj.has("npcs")) {
         JsonArray npcsArray = townObj.getAsJsonArray("npcs");

         for(int j = 0; j < npcsArray.size(); ++j) {
            JsonObject npcsObj = npcsArray.get(j).getAsJsonObject();
            EnumNPCType type = EnumNPCType.getFromString(npcsObj.get("type").getAsString());
            int x = npcsObj.get("x").getAsInt();
            int y = npcsObj.get("y").getAsInt();
            int z = npcsObj.get("z").getAsInt();
            NPCPlacementInfo npcInfo = new NPCPlacementInfo(type, x, y, z);
            if (npcsObj.has("data")) {
               npcInfo.setData(npcsObj.get("data").getAsString());
            }

            info.addNPC(npcInfo);
         }
      }

      townStructures.add(info);
   }

   private static NBTTagCompound loadSnapshot(String snapshotPath) throws IOException {
      return loadSnapshot((InputStream)(new FileInputStream(new File(snapshotPath))));
   }

   private static NBTTagCompound loadSnapshot(InputStream stream) throws IOException {
      return CompressedStreamTools.func_74794_a(new DataInputStream(stream));
   }

   public static StructureInfo getByID(String id) {
      Iterator var1 = townStructures.iterator();

      StructureInfo info;
      do {
         if (!var1.hasNext()) {
            var1 = standaloneStructures.iterator();

            do {
               if (!var1.hasNext()) {
                  var1 = gyms.iterator();

                  do {
                     if (!var1.hasNext()) {
                        return null;
                     }

                     info = (StructureInfo)var1.next();
                  } while(!id.equalsIgnoreCase(info.id));

                  return info;
               }

               info = (StructureInfo)var1.next();
            } while(!id.equalsIgnoreCase(info.id));

            return info;
         }

         info = (StructureInfo)var1.next();
      } while(!id.equalsIgnoreCase(info.id));

      return info;
   }

   public static StructureInfo getRandomTownPiece(Random random, List pieces) throws Exception {
      int weightSum = 0;
      Iterator var3 = townStructures.iterator();

      while(var3.hasNext()) {
         TownStructureInfo info = (TownStructureInfo)var3.next();
         if (info.canPlaceMore(pieces)) {
            weightSum += info.getWeight();
         }
      }

      int weight = random.nextInt(weightSum);
      int count = 0;
      Iterator var5 = townStructures.iterator();

      while(var5.hasNext()) {
         TownStructureInfo info = (TownStructureInfo)var5.next();
         if (info.canPlaceMore(pieces)) {
            count += info.getWeight();
            if (weight <= count) {
               return info;
            }
         }
      }

      throw new Exception("Couldn't get a random structure for town generation.");
   }

   public static StandaloneStructureInfo getScatteredStructureFromBiome(Random random, Biome biomeGenForCoords) {
      ArrayList possibleStructures = null;
      if ((possibleStructures = (ArrayList)structuresByBiome.get(biomeGenForCoords)) != null && !possibleStructures.isEmpty()) {
         int weightSum = 0;

         StandaloneStructureInfo info;
         for(Iterator var4 = possibleStructures.iterator(); var4.hasNext(); weightSum += info.getRarity()) {
            info = (StandaloneStructureInfo)var4.next();
         }

         if (weightSum == 0) {
            return null;
         }

         int weight = random.nextInt(weightSum);
         int count = 0;
         Iterator var6 = possibleStructures.iterator();

         while(var6.hasNext()) {
            StandaloneStructureInfo info = (StandaloneStructureInfo)var6.next();
            count += info.getRarity();
            if (weight <= count) {
               return info;
            }
         }
      }

      return null;
   }

   public static GymInfo getNextGym(WorldGymData data, Random random) {
      ArrayList gymList = data.getGymList();
      ArrayList reducedList = (ArrayList)gyms.clone();
      if (reducedList.size() == 0) {
         return null;
      } else {
         GymInfo gyminfo = (GymInfo)RandomHelper.getRandomElementFromList(reducedList);
         return gyminfo;
      }
   }
}
