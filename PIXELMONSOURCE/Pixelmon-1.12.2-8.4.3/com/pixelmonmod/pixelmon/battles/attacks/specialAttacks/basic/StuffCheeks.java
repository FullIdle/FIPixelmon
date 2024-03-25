package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;

public class StuffCheeks extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      ItemHeld heldItem = user.getHeldItem();
      if (heldItem != null && heldItem != NoItem.noItem && heldItem.isBerry()) {
         heldItem.eatBerry(user);
         user.setConsumedItem(heldItem);
         user.getBattleStats().modifyStat(2, StatsType.Defence, user, true);
         user.removeHeldItem();
         return AttackResult.proceed;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }
}
