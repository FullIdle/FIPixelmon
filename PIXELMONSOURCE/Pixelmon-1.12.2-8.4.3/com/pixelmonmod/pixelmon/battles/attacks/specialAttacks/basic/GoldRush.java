package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;

public class GoldRush extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (PixelmonConfig.allowGMaxGoldRush) {
         if (!user.bc.simulateMode && user.getParticipant() instanceof PlayerParticipant) {
            PlayerParticipant player = (PlayerParticipant)user.getParticipant();
            player.payDay = (int)((double)player.payDay + (double)user.getLevelNum() * PixelmonConfig.gMaxGoldRushMultiplier);
         }

         user.bc.sendToAll("pixelmon.effect.payday");
      }

      return AttackResult.proceed;
   }
}
