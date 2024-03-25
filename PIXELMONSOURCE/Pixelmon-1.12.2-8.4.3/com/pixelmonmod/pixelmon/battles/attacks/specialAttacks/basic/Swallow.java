package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Stockpile;
import java.util.ArrayList;

public class Swallow extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      Stockpile stockpile = (Stockpile)user.getStatus(StatusType.Stockpile);
      if (stockpile != null && !user.hasStatus(StatusType.HealBlock)) {
         if (user.hasFullHealth()) {
            user.bc.sendToAll("pixelmon.effect.healfailed", user.getNickname());
            return AttackResult.failed;
         } else {
            user.healEntityBy(this.getHealAmount(user, stockpile));
            user.bc.sendToAll("pixelmon.effect.washealed", target.getNickname());
            stockpile.removeStockpile(user);
            return AttackResult.succeeded;
         }
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   private int getHealAmount(PixelmonWrapper user, Stockpile stockpile) {
      return user.getPercentMaxHealth(100.0F / (float)(1 << 3 - stockpile.numStockpiles));
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Stockpile stockpile = (Stockpile)pw.getStatus(StatusType.Stockpile);
      if (stockpile != null) {
         float heal = (float)Math.min(this.getHealAmount(pw, stockpile), pw.getHealthDeficit());
         userChoice.raiseWeight(pw.getHealthPercent(heal));
      }

   }
}
