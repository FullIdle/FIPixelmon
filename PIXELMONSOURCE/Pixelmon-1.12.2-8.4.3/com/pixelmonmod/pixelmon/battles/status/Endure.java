package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class Endure extends ProtectVariation {
   public Endure() {
      super(StatusType.Endure);
   }

   protected boolean addStatus(PixelmonWrapper user) {
      return user.addStatus(new Endure(), user);
   }

   protected void displayMessage(PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.status.endure", user.getNickname());
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return false;
   }

   public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a, DamageTypeEnum damageType) {
      float targetHealth = (float)target.getHealth();
      if ((float)damage >= targetHealth) {
         target.bc.sendToAll("pixelmon.status.endurehit", target.getNickname());
         return (int)targetHealth - 1;
      } else {
         return damage;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (pw.getHealth() > 1 && pw.getMoveset().hasAttack("Flail", "Reversal") && MoveChoice.canOHKO(bestOpponentChoices, pw)) {
         userChoice.raiseWeight((float)(100 / (1 << pw.protectsInARow)));
      }

   }
}
