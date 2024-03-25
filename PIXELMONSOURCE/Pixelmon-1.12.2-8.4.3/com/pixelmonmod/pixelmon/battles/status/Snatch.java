package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class Snatch extends StatusBase {
   public Snatch() {
      super(StatusType.Snatch);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.effect.snatch", user.getNickname());
      user.addStatus(new Snatch(), user);
   }

   public boolean stopsSelfStatusMove(PixelmonWrapper user, PixelmonWrapper opponent, Attack attack) {
      if (attack.getAttackCategory() == AttackCategory.STATUS && !attack.isAttack("Helping Hand", "Metronome", "Snatch")) {
         user.bc.sendToAll("pixelmon.effect.snatched", user.getNickname(), opponent.getNickname());
         attack.applySelfStatusMove(user, attack.moveResult);
         user.removeStatus((StatusBase)this);
         return true;
      } else {
         return false;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }
}
