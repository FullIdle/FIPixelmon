package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class BellyDrum extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.getHealthPercent() > 50.0F && user.getBattleStats().getStage(StatsType.Attack) < 6) {
         user.doBattleDamage(user, (float)user.getPercentMaxHealth(50.0F), DamageTypeEnum.SELF);
         user.bc.sendToAll("pixelmon.effect.bellydrum", user.getNickname());
         user.getBattleStats().modifyStat(12, (StatsType)StatsType.Attack);
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!(MoveChoice.getMaxDamagePercent(pw, bestOpponentChoices) > pw.getHealthPercent() - 50.0F)) {
         StatsEffect effect = new StatsEffect(StatsType.Attack, 12, true);
         effect.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      }
   }
}
