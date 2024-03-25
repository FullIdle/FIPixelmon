package com.pixelmonmod.pixelmon.api.spawning;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import com.pixelmonmod.pixelmon.api.spawning.util.SetLoader;
import com.pixelmonmod.pixelmon.api.spawning.util.SpawnConditionTypeAdapter;
import com.pixelmonmod.pixelmon.api.spawning.util.SpawnInfoTypeAdapter;
import com.pixelmonmod.pixelmon.api.spawning.util.SpawnSetTypeAdapter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class SpawnSet implements Iterable {
   private static Gson GSON = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(SpawnSet.class, new SpawnSetTypeAdapter()).registerTypeAdapter(SpawnInfo.class, new SpawnInfoTypeAdapter()).registerTypeAdapter(SpawnCondition.class, new SpawnConditionTypeAdapter()).create();
   public String id;
   public Float setSpecificShinyRate = null;
   public Float setSpecificPokerusRate = null;
   public ArrayList spawnInfos = new ArrayList();

   public void export(String dir) {
      if (!dir.endsWith("/")) {
         dir = dir + "/";
      }

      try {
         File file = new File(dir);
         file.mkdirs();
         file = new File(dir + this.id + ".set.json");
         if (!file.exists()) {
            file.createNewFile();
         }

         PrintWriter pw = new PrintWriter(file);
         String json = GSON.toJson(this);
         pw.write(json);
         pw.flush();
         pw.close();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void onImport() {
      SpawnInfo spawnInfo;
      for(Iterator var1 = this.spawnInfos.iterator(); var1.hasNext(); spawnInfo.onImport()) {
         spawnInfo = (SpawnInfo)var1.next();

         try {
            spawnInfo.set = this;
         } catch (NullPointerException var4) {
            System.out.println(this.id);
         }
      }

   }

   public ArrayList suitableSpawnsFor(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      ArrayList suitableSpawns = new ArrayList();
      Iterator var4 = this.spawnInfos.iterator();

      while(true) {
         SpawnInfo spawnInfo;
         do {
            if (!var4.hasNext()) {
               return suitableSpawns;
            }

            spawnInfo = (SpawnInfo)var4.next();
         } while(!(spawnInfo.rarity >= 0.0F) && spawnInfo.percentage == null);

         if (spawnInfo.fits(spawner, spawnLocation)) {
            suitableSpawns.add(spawnInfo);
         }
      }
   }

   public static SpawnSet deserialize(Reader reader) {
      return deserialize(reader, SetLoader.targetedSpawnSetClass);
   }

   public static SpawnSet deserialize(Reader reader, Class targetedSpawnSetClass) {
      return (SpawnSet)GSON.fromJson(reader, targetedSpawnSetClass);
   }

   public static SpawnSet deserialize(String contents) {
      return deserialize(contents, SetLoader.targetedSpawnSetClass);
   }

   public static SpawnSet deserialize(String contents, Class targetedSpawnSetClass) {
      return (SpawnSet)GSON.fromJson(contents, targetedSpawnSetClass);
   }

   public Iterator iterator() {
      return new Iterator() {
         int index = 0;

         public boolean hasNext() {
            return this.index < SpawnSet.this.spawnInfos.size();
         }

         public SpawnInfo next() {
            return (SpawnInfo)SpawnSet.this.spawnInfos.get(this.index++);
         }
      };
   }
}
