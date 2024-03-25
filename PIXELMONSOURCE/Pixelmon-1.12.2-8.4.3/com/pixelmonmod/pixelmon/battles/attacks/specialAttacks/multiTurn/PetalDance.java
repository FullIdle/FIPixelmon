package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Confusion;
import java.util.ArrayList;
import net.minecraft.util.text.TextComponentTranslation;

public class PetalDance extends MultiTurnSpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!this.doesPersist(user)) {
         this.setPersists(user, true);
         this.setTurnCount(user, RandomHelper.getRandomNumberBetween(2, 3));
      }

      this.decrementTurnCount(user);
      if (this.getTurnCount(user) == 0) {
         this.setPersists(user, false);
         TextComponentTranslation message = new TextComponentTranslation("pixelmon.effect.thrash", new Object[]{user.getNickname()});
         user.addStatus(new Confusion(), user, message);
      }

      return AttackResult.proceed;
   }

   public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
      this.setPersists(user, false);
   }

   public void removeEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.getTurnCount(user) == 0 && this.doesPersist(user) && user.addStatus(new Confusion(), user)) {
         user.bc.sendToAll("pixelmon.effect.thrash", user.getNickname());
      }

      this.setPersists(user, false);
   }

   public boolean shouldNotLosePP(PixelmonWrapper user) {
      return this.doesPersist(user);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (userChoice.tier >= 3) {
         userChoice.weight /= 2.0F;
      }

   }
}
