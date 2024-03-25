package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemLeftovers extends ItemHeld {
   public ItemLeftovers() {
      super(EnumHeldItems.leftovers, "leftovers");
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (!pw.hasFullHealth() && !pw.hasStatus(StatusType.HealBlock)) {
         int par1 = (int)((float)pw.getMaxHealth() * 0.0625F);
         pw.healEntityBy(par1);
         if (pw.bc != null) {
            pw.bc.sendToAll("pixelmon.helditems.leftovers", pw.getNickname());
         }
      }

   }
}
