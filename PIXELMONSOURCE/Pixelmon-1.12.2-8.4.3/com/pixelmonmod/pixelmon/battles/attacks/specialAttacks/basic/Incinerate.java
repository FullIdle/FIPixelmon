package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.StickyHold;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.ArrayList;
import java.util.Iterator;

public class Incinerate extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.canDestroyBerry(user, target)) {
         target.bc.sendToAll("pixelmon.effect.incinerate", target.getNickname(), target.getHeldItem().getLocalizedName());
         target.setNewHeldItem((ItemHeld)null);
      }

      return AttackResult.proceed;
   }

   private boolean canDestroyBerry(PixelmonWrapper user, PixelmonWrapper target) {
      return target.getHeldItem().isBerry() && !(target.getBattleAbility(user) instanceof StickyHold) && !target.hasStatus(StatusType.Substitute);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         if (this.canDestroyBerry(pw, target)) {
            userChoice.raiseWeight(20.0F);
         }
      }

   }
}
