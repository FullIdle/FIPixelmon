package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemExpertBelt extends ItemHeld {
   public ItemExpertBelt() {
      super(EnumHeldItems.expertBelt, "expert_belt");
   }

   public double preProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      if (attack.getTypeEffectiveness(attacker, target) >= 2.0) {
         damage *= 1.2;
      }

      return damage;
   }
}
