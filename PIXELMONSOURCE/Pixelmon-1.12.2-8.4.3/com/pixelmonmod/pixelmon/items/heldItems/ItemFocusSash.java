package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemFocusSash extends ItemHeld {
   public ItemFocusSash() {
      super(EnumHeldItems.focussash, "focus_sash");
   }

   public double modifyDamageIncludeFixed(double damage, PixelmonWrapper attacker, PixelmonWrapper target, Attack attack) {
      if (target != null && attacker != null && damage >= (double)target.getHealth() && target.hasFullHealth()) {
         if (attacker.bc != null) {
            attacker.bc.sendToAll("pixelmon.helditems.focussash", target.getNickname());
         }

         target.consumeItem();
         return attacker.bc.simulateMode && damage > 0.0 ? (double)Math.max(target.getHealth() - 1, 1) : (double)(target.getHealth() - 1);
      } else {
         return damage;
      }
   }
}
