package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.IronBarbs;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.RoughSkin;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Iterator;

public class KnockOff extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasHeldItem() && !target.hasSpecialItem(user)) {
         user.attack.getMove().setBasePower((int)((double)user.attack.getMove().getBasePower() * 1.5));
      }

      return AttackResult.proceed;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.isFainted() || !target.getBattleAbility().isAbility(RoughSkin.class) && !target.getBattleAbility().isAbility(IronBarbs.class) && target.getHeldItem().getHeldItemType() != EnumHeldItems.rockyHelmet) {
         if (!user.inParentalBond && target.hasHeldItem() && target.isItemRemovable(user)) {
            user.bc.sendToAll("pixelmon.effect.knockoff", user.getNickname(), target.getNickname(), target.getHeldItem().getLocalizedName());
            target.removeHeldItem();
            target.enableReturnHeldItem();
            user.bc.modifyStats(target);
            user.bc.modifyStats(user);
         }

      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            if (target.isItemRemovable(pw)) {
               userChoice.raiseWeight(25.0F);
            }
         }

      }
   }
}
