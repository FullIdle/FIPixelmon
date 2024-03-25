package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class Magician extends AbilityBase {
   public void tookDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (!user.hasHeldItem() && target.hasHeldItem() && target.isItemRemovable(user) && user.isItemGivable(target.getHeldItem())) {
         ItemHeld targetItem = target.getHeldItem();
         user.bc.sendToAll("pixelmon.abilities.magician", user.getNickname(), target.getNickname(), targetItem.getLocalizedName());
         user.setNewHeldItem(targetItem);
         target.removeHeldItem();
         user.bc.enableReturnHeldItems(user, target);
      }

   }
}
