package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemThroatSpray extends ItemHeld {
   public ItemThroatSpray() {
      super(EnumHeldItems.throatSpray, "throat_spray");
   }

   public void postProcessAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack) {
      if (attack.isSoundBased() && !attacker.bc.battleEnded) {
         attacker.getBattleStats().modifyStat(1, (StatsType)StatsType.SpecialAttack);
         attacker.bc.sendToAll("pixelmon.helditems.throatspray", attacker.getNickname());
         attacker.consumeItem();
      }

   }
}
