package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class Pickpocket extends AbilityBase {
   public void applyEffectOnContactTargetLate(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.inMultipleHit && !target.hasHeldItem() && user.hasHeldItem() && user.isItemRemovable(target) && target.isItemGivable(user.getHeldItem()) && !target.isFainted()) {
         ItemHeld userItem = user.getHeldItem();
         user.bc.sendToAll("pixelmon.abilities.pickpocket", target.getNickname(), user.getNickname(), userItem.getLocalizedName());
         target.setNewHeldItem(userItem);
         user.removeHeldItem();
         user.bc.enableReturnHeldItems(target, user);
      }

   }
}
