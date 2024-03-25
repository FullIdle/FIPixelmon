package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;

public class AquaRing extends StatusBase {
   public AquaRing() {
      super(StatusType.AquaRing);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.addStatus(new AquaRing(), user)) {
         user.bc.sendToAll("pixelmon.effect.surroundwithwater", user.getNickname());
      } else {
         user.bc.sendToAll("pixelmon.effect.surroundedbywater", user.getNickname());
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (!pw.hasStatus(StatusType.HealBlock)) {
         if (pw.getHealth() != pw.getMaxHealth()) {
            pw.bc.sendToAll("pixelmon.status.ringheal", pw.getNickname());
         }

         int healAmount = pw.getPercentMaxHealth(6.25F);
         if (pw.getUsableHeldItem().getHeldItemType() == EnumHeldItems.bigRoot) {
            healAmount = (int)((double)healAmount * 1.3);
         }

         pw.healEntityBy(healAmount);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!pw.hasStatus(StatusType.HealBlock)) {
         userChoice.raiseWeight(12.5F);
      }

   }
}
