package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;

public class Powder extends StatusBase {
   public Powder() {
      super(StatusType.Powder);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.isImmuneToPowder() && target.addStatus(new Powder(), user)) {
         user.bc.sendToAll("pixelmon.status.powder", target.getNickname());
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public boolean stopsIncomingAttackUser(PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (user.attack.getType() == EnumType.Fire) {
         user.bc.sendToAll("pixelmon.status.powderexplode", user.getNickname());
         user.doBattleDamage(user, (float)user.getPercentMaxHealth(25.0F), DamageTypeEnum.STATUS);
         return true;
      } else {
         return false;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (MoveChoice.hasOffensiveAttackType(bestOpponentChoices, EnumType.Fire)) {
         userChoice.raiseWeight(25.0F);
      }

   }
}
