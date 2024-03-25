package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemRockyHelmet extends ItemHeld {
   public ItemRockyHelmet() {
      super(EnumHeldItems.rockyHelmet, "rocky_helmet");
   }

   public void applyEffectOnContact(PixelmonWrapper user, PixelmonWrapper target) {
      if (!(user.getBattleAbility() instanceof MagicGuard) && user.isAlive()) {
         user.bc.sendToAll("pixelmon.helditems.rockyhelmet", user.getNickname(), target.getNickname());
         user.doBattleDamage(target, (float)user.getPercentMaxHealth(16.666666F), DamageTypeEnum.ITEM);
      }

   }
}
