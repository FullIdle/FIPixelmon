package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.StickyHold;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.ArrayList;
import java.util.Iterator;

public class BugBite extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.isDynamax()) {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         if (!user.bc.simulateMode && (!user.inParentalBond || target.isFainted()) && this.canEatBerry(user, target)) {
            ItemHeld targetItem = target.getHeldItem();
            user.bc.sendToAll("pixelmon.effect.bugbite", user.getNickname(), target.getNickname(), targetItem.getLocalizedName());
            ItemHeld tempItem = user.getHeldItem();
            ItemHeld tempConsumedItem = user.getConsumedItem();
            user.setHeldItem(targetItem);
            target.setConsumedItem(targetItem);
            user.getHeldItem().eatBerry(user);
            user.setHeldItem(tempItem);
            user.setConsumedItem(tempConsumedItem);
            target.removeHeldItem();
         }

      }
   }

   private boolean canEatBerry(PixelmonWrapper user, PixelmonWrapper target) {
      return target.getHeldItem().isBerry() && !target.hasStatus(StatusType.Substitute) && !(target.getBattleAbility(user) instanceof StickyHold);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         if (this.canEatBerry(pw, target)) {
            userChoice.raiseWeight(30.0F);
         }
      }

   }
}
