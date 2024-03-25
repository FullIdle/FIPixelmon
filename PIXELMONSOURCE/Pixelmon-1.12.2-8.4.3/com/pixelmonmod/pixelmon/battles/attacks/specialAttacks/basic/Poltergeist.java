package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;

public class Poltergeist extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.getUsableHeldItem() == NoItem.noItem) {
         return AttackResult.failed;
      } else {
         user.bc.sendToAll("pixelmon.effect.poltergeist", target.getNickname(), target.getUsableHeldItem().getLocalizedName());
         return super.applyEffectDuring(user, target);
      }
   }
}
