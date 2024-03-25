package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.attacks.ValueType;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import java.util.ArrayList;

public class Synthesis extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasFullHealth()) {
         user.bc.sendToAll("pixelmon.effect.healfailed", user.getNickname());
         return AttackResult.failed;
      } else if (user.hasStatus(StatusType.HealBlock)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         double heal = (double)((float)user.getMaxHealth() * this.getHealMultiplier(user));
         user.bc.sendToAll("pixelmon.effect.washealed", user.getNickname());
         user.healEntityBy((int)heal);
         return AttackResult.proceed;
      }
   }

   private float getHealMultiplier(PixelmonWrapper user) {
      Weather weather = user.bc.globalStatusController.getWeather();
      if (weather instanceof Sunny) {
         return 0.6666667F;
      } else {
         return weather != null ? 0.25F : 0.5F;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Recover heal = new Recover(new Value[]{new Value((int)(this.getHealMultiplier(pw) * 100.0F), ValueType.WholeNumber)});
      heal.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }
}
