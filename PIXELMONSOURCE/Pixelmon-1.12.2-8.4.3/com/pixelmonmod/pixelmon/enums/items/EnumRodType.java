package com.pixelmonmod.pixelmon.enums.items;

import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.api.spawning.conditions.TriggerLocation;

public enum EnumRodType {
   OldRod("oldrod", 170, 32, LocationType.OLD_ROD, LocationType.OLD_ROD_LAVA),
   GoodRod("goodrod", 60, 64, LocationType.GOOD_ROD, LocationType.GOOD_ROD_LAVA),
   SuperRod("superrod", 0, 128, LocationType.SUPER_ROD, LocationType.SUPER_ROD_LAVA),
   OasRod("oasrod", 0, 256, LocationType.OAS_ROD, LocationType.OAS_ROD);

   public String textureName;
   public int rarityThreshold;
   public int maxDamage;
   public final TriggerLocation locationInWater;
   public final TriggerLocation locationInLava;

   private EnumRodType(String textureName, int rarityThreshold, int maxDamage, TriggerLocation locationInWater, TriggerLocation locationInLava) {
      this.textureName = textureName;
      this.rarityThreshold = rarityThreshold;
      this.maxDamage = maxDamage;
      this.locationInWater = locationInWater;
      this.locationInLava = locationInLava;
   }

   public static boolean hasRodType(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}
