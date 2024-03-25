package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;

public class Ingrain extends StatusBase {
   public Ingrain() {
      super(StatusType.Ingrain);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasStatus(StatusType.Ingrain)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         user.removeStatuses(StatusType.MagnetRise, StatusType.Telekinesis);
         user.addStatus(new Ingrain(), user);
         user.addStatus(new SmackedDown(), user);
         user.bc.sendToAll("pixelmon.status.applyingrain", user.getNickname());
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (!pw.hasStatus(StatusType.HealBlock) && !pw.hasFullHealth()) {
         pw.bc.sendToAll("pixelmon.status.ingrain", pw.getNickname());
         int healAmount = pw.getPercentMaxHealth(6.25F);
         if (pw.getUsableHeldItem().getHeldItemType() == EnumHeldItems.bigRoot) {
            healAmount = (int)((double)healAmount * 1.3);
         }

         pw.healEntityBy(healAmount);
      }

   }

   public boolean stopsSwitching() {
      return true;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      AquaRing aquaRing = new AquaRing();
      aquaRing.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }
}
