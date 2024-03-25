package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class Recharge extends StatusBase {
   transient int turnWait;
   transient int turnsWaited = 0;

   public Recharge() {
      super(StatusType.Recharge);
      this.turnWait = 1;
   }

   public Recharge(int turnWait) {
      super(StatusType.Recharge);
      this.turnWait = turnWait;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.addStatus(new Recharge(1), user);
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (this.turnsWaited > this.turnWait) {
         user.removeStatus((StatusBase)this);
         return true;
      } else {
         user.bc.sendToAll("pixelmon.status.recharging", user.getNickname());
         return false;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (this.turnsWaited++ >= this.turnWait) {
         pw.removeStatus((StatusBase)this);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.weight /= 2.0F;
   }
}
