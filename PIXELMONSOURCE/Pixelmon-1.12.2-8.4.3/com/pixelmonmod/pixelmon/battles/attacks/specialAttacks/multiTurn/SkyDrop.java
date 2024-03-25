package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.SkyDropped;
import com.pixelmonmod.pixelmon.battles.status.SkyDropping;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;

public class SkyDrop extends MultiTurnSpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.hasStatus(StatusType.Substitute) && !user.bc.globalStatusController.hasStatus(StatusType.Gravity)) {
         if (!this.doesPersist(user)) {
            this.setPersists(user, true);
            this.setTurnCount(user, 2);
         }

         this.decrementTurnCount(user);
         if (this.getTurnCount(user) == 1) {
            user.bc.sendToAll("pixelmon.effect.skydrop", user.getNickname(), target.getNickname());
            user.addStatus(new SkyDropping(), user);
            target.addStatus(new SkyDropped(user), user);
            target.removeStatus(StatusType.FollowMe);
            return AttackResult.charging;
         } else {
            boolean succeeded = user.removeStatus(StatusType.SkyDropping);
            target.removeStatus(StatusType.SkyDropped);
            this.setPersists(user, false);
            return !succeeded && !user.bc.simulateMode ? AttackResult.failed : AttackResult.proceed;
         }
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void removeEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasStatus(StatusType.SkyDropping)) {
         user.removeStatus(StatusType.SkyDropping);
         target.removeStatus(StatusType.SkyDropped);
      }

      this.setPersists(user, false);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (pw.getUsableHeldItem().getHeldItemType() != EnumHeldItems.powerHerb) {
         userChoice.weight *= 0.9F;
         if (pw.hasStatus(StatusType.Confusion, StatusType.Infatuated, StatusType.Paralysis)) {
            userChoice.weight /= 2.0F;
         }

      }
   }

   public boolean shouldNotLosePP(PixelmonWrapper user) {
      return this.doesPersist(user);
   }
}
