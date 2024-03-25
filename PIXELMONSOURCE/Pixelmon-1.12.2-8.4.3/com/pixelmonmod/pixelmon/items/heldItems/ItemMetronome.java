package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemMetronome extends ItemHeld {
   public ItemMetronome() {
      super(EnumHeldItems.metronome, "metronome");
   }

   public void onAttackUsed(PixelmonWrapper user, Attack attack) {
      if (!user.bc.simulateMode) {
         if (attack.equals(user.choiceLocked)) {
            user.metronomeBoost = Math.min(user.metronomeBoost + 20, 100);
         } else {
            user.choiceLocked = attack;
            user.metronomeBoost = 0;
         }
      }

   }

   public double preProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      return attacker == target ? damage : damage + damage / 100.0 * (double)attacker.metronomeBoost;
   }

   public void onMiss(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack) {
      if (!attacker.bc.simulateMode) {
         this.reset(attacker);
      }

   }

   public void applySwitchOutEffect(PixelmonWrapper pw) {
      if (!pw.bc.simulateMode) {
         this.reset(pw);
      }

   }

   private void reset(PixelmonWrapper pw) {
      pw.metronomeBoost = 0;
      pw.choiceLocked = null;
   }
}
