package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Stockpile;

public class SpitUp extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      Stockpile stockpile = (Stockpile)user.getStatus(StatusType.Stockpile);
      if (stockpile == null) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         user.attack.getMove().setBasePower(100 * stockpile.numStockpiles);
         stockpile.removeStockpile(user);
         return AttackResult.proceed;
      }
   }
}
