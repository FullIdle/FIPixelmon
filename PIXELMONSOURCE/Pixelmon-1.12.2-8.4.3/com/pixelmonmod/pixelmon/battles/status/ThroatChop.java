package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class ThroatChop extends StatusBase {
   private transient int turnsToGo = 2;

   public ThroatChop() {
      super(StatusType.ThroatChop);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.checkChance()) {
         target.addStatus(new ThroatChop(), user);
      }

   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (!a.isZ && !a.isMax && a.isSoundBased()) {
         user.bc.sendToAll("pixelmon.status.throatchop.cannot", user.getNickname(), a.getMove().getTranslatedName());
         return false;
      } else {
         return true;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.turnsToGo <= 0) {
         pw.removeStatus((StatusBase)this);
      }

   }
}
