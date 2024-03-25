package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Flinch;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SereneGrace;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemRazorFang extends ItemHeld {
   protected int randomInt = 0;

   public ItemRazorFang() {
      super(EnumHeldItems.razorFang, "razor_fang");
   }

   public void postProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, float damage) {
      this.randomInt = RandomHelper.rand.nextInt(9) + 1;
      attack.getMove().effects.stream().filter((effect) -> {
         return effect instanceof Flinch;
      }).forEach((effect) -> {
         this.randomInt = 0;
      });
      if (attacker.getBattleAbility() instanceof SereneGrace && this.randomInt == 4) {
         this.randomInt = 5;
      }

      if (this.randomInt == 5) {
         Flinch.flinch(attacker, target);
      }

   }
}
