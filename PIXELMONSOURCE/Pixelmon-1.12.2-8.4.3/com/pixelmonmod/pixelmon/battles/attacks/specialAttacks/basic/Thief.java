package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.IronBarbs;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.RoughSkin;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.StickyHold;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import java.util.ArrayList;
import java.util.Iterator;

public class Thief extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.isFainted() || !target.getBattleAbility().isAbility(RoughSkin.class) && !target.getBattleAbility().isAbility(IronBarbs.class) && target.getHeldItem().getHeldItemType() != EnumHeldItems.rockyHelmet) {
         if (!user.hasHeldItem() && target.hasHeldItem() && target.isItemRemovable(user) && user.isItemGivable(target.getHeldItem())) {
            ItemHeld targetItem = target.getHeldItem();
            user.bc.sendToAll("pixelmon.effect.thief", user.getNickname(), target.getNickname(), targetItem.getLocalizedName());
            user.setNewHeldItem(targetItem);
            target.setNewHeldItem(NoItem.noItem);
            user.bc.enableReturnHeldItems(user, target);
         }

      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            if (!pw.hasHeldItem() && target.hasHeldItem() && !target.getHeldItem().hasNegativeEffect() && !(target.getBattleAbility() instanceof StickyHold)) {
               userChoice.raiseWeight(25.0F);
            }
         }

      }
   }
}
