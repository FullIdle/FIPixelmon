package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.attacks.ValueType;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Recover;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class Wish extends StatusBase {
   private transient int turnsLeft;
   private transient int healAmount;
   private transient String userName;

   public Wish() {
      super(StatusType.Wish);
   }

   public Wish(PixelmonWrapper user) {
      this();
      this.turnsLeft = 2;
      this.healAmount = user.getPercentMaxHealth(50.0F);
      this.userName = user.getNickname();
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasStatus(this.type)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         user.addStatus(new Wish(user), user);
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public boolean isWholeTeamStatus() {
      return false;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.turnsLeft <= 0) {
         pw.removeStatus((StatusBase)this);
         if (this.turnsLeft == 0 && pw.isAlive()) {
            if (!pw.hasStatus(StatusType.HealBlock)) {
               pw.bc.sendToAll("pixelmon.effect.wish", this.userName);
               if (!pw.hasFullHealth()) {
                  pw.healEntityBy(this.healAmount);
               } else {
                  pw.bc.sendToAll("pixelmon.effect.healfailed", pw.getNickname());
               }
            } else {
               pw.bc.sendToAll("pixelmon.effect.effectfailed");
            }
         }
      }

   }

   public void applySwitchOutEffect(PixelmonWrapper outgoing, PixelmonWrapper incoming) {
      if (outgoing.isFainted() && this.turnsLeft == 0) {
         this.turnsLeft = -1;
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Recover heal = new Recover(new Value[]{new Value(50, ValueType.WholeNumber)});
      heal.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }
}
