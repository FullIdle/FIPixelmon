package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.EchoedVoiceStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class EchoedVoice extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      EchoedVoiceStatus echoedVoice = (EchoedVoiceStatus)user.bc.globalStatusController.getGlobalStatus(StatusType.EchoedVoice);
      if (echoedVoice == null) {
         user.bc.globalStatusController.addGlobalStatus(new EchoedVoiceStatus(user.bc.battleTurn));
      } else {
         user.attack.getMove().setBasePower(echoedVoice.power);
         if (!user.bc.simulateMode) {
            if (echoedVoice.power < 200 && echoedVoice.turnInc != user.bc.battleTurn) {
               echoedVoice.power += 40;
            }

            echoedVoice.turnInc = user.bc.battleTurn;
         }
      }

      return AttackResult.proceed;
   }
}
