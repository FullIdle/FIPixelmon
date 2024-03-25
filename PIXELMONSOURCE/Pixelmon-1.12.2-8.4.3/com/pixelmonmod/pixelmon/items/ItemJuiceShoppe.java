package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumBerry;

public class ItemJuiceShoppe extends PixelmonItem {
   public final StatsType type;

   public ItemJuiceShoppe(StatsType type, String itemName) {
      super(itemName);
      this.type = type;
      this.func_77625_d(16);
      this.func_77637_a(PixelmonCreativeTabs.restoration);
      this.canRepair = false;
   }

   public static int getJuiceTag(EnumBerry essence, EnumBerry solvent) {
      if (essence == solvent) {
         return essence.juiceGroup * 4;
      } else {
         if (essence.juiceGroup == 1) {
            switch (solvent.juiceGroup) {
               case 1:
                  return 8;
               case 2:
                  return 12;
               case 3:
                  return 16;
            }
         }

         if (essence.juiceGroup == 2) {
            switch (solvent.juiceGroup) {
               case 1:
                  return 12;
               case 2:
                  return 16;
               case 3:
                  return 32;
            }
         }

         if (essence.juiceGroup == 3) {
            switch (solvent.juiceGroup) {
               case 1:
                  return 16;
               case 2:
               case 3:
                  return 32;
            }
         }

         return 10;
      }
   }

   public boolean adjustEVs(EntityPixelmon entityPixelmon, int metadata) {
      int amount = 10;
      switch (metadata) {
         case 4:
            amount = 4;
            break;
         case 8:
            amount = 8;
            break;
         case 12:
            amount = 12;
            break;
         case 16:
            amount = 16;
            break;
         case 32:
            amount = 32;
      }

      boolean success = entityPixelmon.getPokemonData().getEVs().addEVsOfType(this.type, amount, 255);
      if (success) {
         entityPixelmon.updateStats();
         entityPixelmon.update(new EnumUpdateType[]{EnumUpdateType.Stats});
      }

      return success;
   }
}
