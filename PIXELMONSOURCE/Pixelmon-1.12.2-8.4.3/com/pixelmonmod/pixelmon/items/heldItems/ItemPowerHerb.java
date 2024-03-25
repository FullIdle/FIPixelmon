package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemPowerHerb extends ItemHeld {
   public ItemPowerHerb() {
      super(EnumHeldItems.powerHerb, "power_herb");
   }

   public boolean affectMultiturnMove(PixelmonWrapper user) {
      user.consumeItem();
      user.bc.sendToAll("pixelmon.helditems.powerherb", user.getNickname());
      return true;
   }
}
