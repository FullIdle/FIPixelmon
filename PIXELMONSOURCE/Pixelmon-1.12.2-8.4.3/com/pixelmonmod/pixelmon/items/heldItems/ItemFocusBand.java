package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemFocusBand extends ItemHeld {
   public ItemFocusBand() {
      super(EnumHeldItems.focusband, "focus_band");
   }

   public double modifyDamageIncludeFixed(double damage, PixelmonWrapper attacker, PixelmonWrapper target, Attack attack) {
      if (target != null && attacker != null && damage >= (double)target.getHealth() && RandomHelper.getRandomChance(10)) {
         if (attacker.bc != null) {
            attacker.bc.sendToAll("pixelmon.helditems.focusband", target.getNickname());
         }

         return (double)(target.getHealth() - 1);
      } else {
         return damage;
      }
   }
}
