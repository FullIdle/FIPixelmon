package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;

public class MagnetRise extends StatusBase {
   private transient int duration = 5;

   public MagnetRise() {
      super(StatusType.MagnetRise);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.hasStatus(StatusType.MagnetRise, StatusType.Ingrain) && !user.bc.globalStatusController.hasStatus(StatusType.Gravity)) {
         if (user.addStatus(new MagnetRise(), user)) {
            user.bc.sendToAll("pixelmon.effect.magnetrise", user.getNickname());
         }
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.duration <= 0) {
         pw.bc.sendToAll("pixelmon.status.magicroomend");
         pw.removeStatus((StatusBase)this);
      }

   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return user.attack.getType() == EnumType.Ground && !user.attack.isAttack("Sand-attack", "Thousand Arrows") && !pokemon.isGrounded();
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.battletext.noeffect", pokemon.getNickname());
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (MoveChoice.hasOffensiveAttackType(MoveChoice.getTargetedChoices(pw, bestOpponentChoices), EnumType.Ground)) {
         userChoice.raiseWeight(30.0F);
      }

   }
}
