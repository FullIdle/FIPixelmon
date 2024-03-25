package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.ArrayList;

public class Bestow extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      ItemHeld userItem = user.getHeldItem();
      if (user.hasHeldItem() && !target.hasHeldItem() && user.isItemRemovable(user) && target.isItemGivable(userItem)) {
         user.bc.sendToAll("pixelmon.effect.bestow", user.getNickname(), userItem.getLocalizedName(), target.getNickname());
         target.setNewHeldItem(userItem);
         user.setNewHeldItem((ItemHeld)null);
         user.bc.enableReturnHeldItems(user, target);
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      ItemHeld itemHeld = pw.getHeldItem();
      boolean negative = itemHeld != null && itemHeld.hasNegativeEffect();
      boolean isAlly = userChoice.hitsAlly();
      if (isAlly ^ negative) {
         userChoice.raiseWeight(25.0F);
      }

   }
}
