package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;

public class HappyHour extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (PixelmonConfig.allowHappyHour) {
         user.bc.sendToAll("pixelmon.effect.happyhour");
         BattleParticipant participant = user.getParticipant();
         if (!user.bc.simulateMode && participant.getType() == ParticipantType.Player) {
            PlayerParticipant player = (PlayerParticipant)participant;
            player.hasHappyHour = true;
         }
      }

      return AttackResult.succeeded;
   }
}
