package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class Growth extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      int amount = user.bc.globalStatusController.getWeather() instanceof Sunny ? 2 : 1;
      user.getBattleStats().modifyStat(amount, StatsType.Attack, StatsType.SpecialAttack);
      StatsEffect.addStatChangeAnimation(user, target, StatsType.Attack, amount);
      StatsEffect.addStatChangeAnimation(user, target, StatsType.SpecialAttack, amount);
      return AttackResult.succeeded;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int amount = pw.bc.globalStatusController.getWeather() instanceof Sunny ? 2 : 1;
      StatsEffect attackBoost = new StatsEffect(StatsType.Attack, amount, true);
      StatsEffect specialAttackBoost = new StatsEffect(StatsType.SpecialAttack, amount, true);
      attackBoost.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      specialAttackBoost.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }
}
