package com.pixelmonmod.pixelmon.entities;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.text.translation.I18n;

public enum SpawnLocationType {
   Land,
   LandNPC,
   LandVillager,
   Water,
   UnderGround,
   Air,
   AirPersistent,
   Legendary,
   Boss;

   public static SpawnLocationType[] getSpawnLocations(ArrayList list) {
      SpawnLocationType[] locations = new SpawnLocationType[list.size()];
      int i = 0;
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         String s = (String)var3.next();
         SpawnLocationType[] var5 = values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            SpawnLocationType sp = var5[var7];
            if (sp.toString().equalsIgnoreCase(s)) {
               locations[i++] = sp;
            }
         }
      }

      return locations;
   }

   public static SpawnLocationType getSpawnLocation(String location) {
      SpawnLocationType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SpawnLocationType sp = var1[var3];
         if (sp.toString().equalsIgnoreCase(location)) {
            return sp;
         }
      }

      return null;
   }

   public static SpawnLocationType getFromIndex(int integer) {
      try {
         return values()[integer];
      } catch (IndexOutOfBoundsException var2) {
         return null;
      }
   }

   public static SpawnLocationType nextLocation(SpawnLocationType spawnLocation) {
      switch (spawnLocation) {
         case Land:
            return Water;
         case AirPersistent:
            return Land;
         default:
            return getFromIndex((spawnLocation.ordinal() + 1) % values().length);
      }
   }

   public static boolean contains(SpawnLocationType[] locations, SpawnLocationType location) {
      SpawnLocationType[] var2 = locations;
      int var3 = locations.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         SpawnLocationType l = var2[var4];
         if (l == location) {
            return true;
         }
      }

      return false;
   }

   public static boolean containsOnly(SpawnLocationType[] locations, SpawnLocationType location) {
      return locations.length == 1 ? contains(locations, location) : false;
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("enum.spawnlocation." + this.toString().toLowerCase());
   }
}
