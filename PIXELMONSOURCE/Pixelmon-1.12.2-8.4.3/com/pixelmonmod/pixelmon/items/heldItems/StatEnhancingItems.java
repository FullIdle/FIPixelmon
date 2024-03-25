package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class StatEnhancingItems extends ItemHeld {
   private StatsType[] raisedStats;
   private float statMultiplier;
   private EnumSpecies[] affected;

   public StatEnhancingItems(EnumHeldItems type, String itemName, StatsType[] raisedStats, float statMultiplier, EnumSpecies... affected) {
      super(type, itemName);
      this.raisedStats = raisedStats;
      this.statMultiplier = statMultiplier;
      this.affected = affected;
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (this.canAffect(user)) {
         StatsType[] var3 = this.raisedStats;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            StatsType stat = var3[var5];
            int var10001 = stat.getStatIndex();
            stats[var10001] = (int)((float)stats[var10001] * this.statMultiplier);
         }
      }

      return stats;
   }

   protected boolean canAffect(PixelmonWrapper pokemon) {
      EnumSpecies species = pokemon.getSpecies();
      EnumSpecies[] var3 = this.affected;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumSpecies p = var3[var5];
         if (p == species) {
            return true;
         }
      }

      return false;
   }
}
