package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;

public class Unburden extends AbilityBase {
   private boolean itemUsed = false;

   public void onItemChanged(PixelmonWrapper pw, ItemHeld newItem) {
      if (!pw.bc.simulateMode) {
         if (newItem == NoItem.noItem) {
            this.itemUsed = true;
         } else {
            this.itemUsed = false;
         }
      }

   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (this.itemUsed) {
         int var10001 = StatsType.Speed.getStatIndex();
         stats[var10001] *= 2;
      }

      return stats;
   }

   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (target.isFainted() && !user.bc.simulateMode) {
         this.itemUsed = false;
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (!newPokemon.bc.simulateMode) {
         this.itemUsed = false;
      }

   }

   public boolean needNewInstance() {
      return true;
   }
}
