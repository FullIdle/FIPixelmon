package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.ArrayList;
import java.util.Iterator;

public class Embargo extends StatusBase {
   transient int turnsRemaining = 5;

   public Embargo() {
      super(StatusType.Embargo);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.addStatus(new Embargo(), target)) {
         user.bc.sendToAll("pixelmon.status.embargo", target.getNickname());
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.turnsRemaining <= 0) {
         pw.bc.sendToAll("pixelmon.status.embargoend", pw.getNickname());
         pw.removeStatus((StatusBase)this);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         ItemHeld heldItem = target.getUsableHeldItem();
         if (heldItem != null && !heldItem.hasNegativeEffect()) {
            userChoice.raiseWeight(15.0F);
         }
      }

   }
}
