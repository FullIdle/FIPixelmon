package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import java.util.ArrayList;
import java.util.Iterator;

public class Trick extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      ItemHeld userItem = user.getHeldItem();
      ItemHeld targetItem = target.getHeldItem();
      if ((user.hasHeldItem() || target.hasHeldItem()) && user.isItemRemovable(user) && target.isItemRemovable(user) && target.isItemGivable(userItem)) {
         if (userItem.getHeldItemType() == EnumHeldItems.choiceItem && targetItem.getHeldItemType() == EnumHeldItems.choiceItem) {
            user.choiceSwapped = true;
            target.choiceSwapped = true;
         }

         userItem.applySwitchOutEffect(user);
         targetItem.applySwitchOutEffect(target);
         user.bc.sendToAll("pixelmon.effect.trick", user.getNickname(), target.getNickname());
         if (targetItem != NoItem.noItem) {
            user.bc.sendToAll("pixelmon.effect.trickitem", user.getNickname(), targetItem.getLocalizedName());
         }

         user.setNewHeldItem(targetItem);
         if (userItem != NoItem.noItem) {
            target.bc.sendToAll("pixelmon.effect.trickitem", target.getNickname(), userItem.getLocalizedName());
         }

         target.setNewHeldItem(userItem);
         user.bc.enableReturnHeldItems(user, target);
         target.bc.modifyStats(target);
         user.bc.modifyStats(user);
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         ItemHeld userItem = pw.getHeldItem();
         boolean userNegative = userItem == null || userItem.hasNegativeEffect();
         Iterator var9 = userChoice.targets.iterator();

         while(var9.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var9.next();
            ItemHeld targetItem = target.getHeldItem();
            boolean targetNegative = targetItem == null || targetItem.hasNegativeEffect();
            if (userNegative && !targetNegative) {
               userChoice.raiseWeight(40.0F);
            }
         }

      }
   }
}
