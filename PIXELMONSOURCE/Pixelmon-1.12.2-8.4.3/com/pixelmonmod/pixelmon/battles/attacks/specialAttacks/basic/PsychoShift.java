package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.NoStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import java.util.ArrayList;

public class PsychoShift extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.hasPrimaryStatus(false)) {
         StatusPersist primaryStatus = user.getPrimaryStatus();
         if (primaryStatus != NoStatus.noStatus && target.addStatus(primaryStatus.copy(), user)) {
            user.bc.sendToAll("pixelmon.effect.psychoshift", user.getNickname(), target.getNickname());
            user.removeStatus((StatusBase)primaryStatus, false);
            return AttackResult.succeeded;
         }
      }

      user.bc.sendToAll("pixelmon.effect.effectfailed");
      return AttackResult.failed;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.raiseWeight(25.0F);
   }
}
