package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Flying;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Fly extends MultiTurnCharge {
   public Fly() {
      super("pixelmon.effect.flyup", Flying.class.getSimpleName(), StatusType.Flying);
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.hasStatus(StatusType.Gravity)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         if (!this.doesPersist(user)) {
            this.setPersists(user, true);
            this.setTurnCount(user, 2);
         }

         this.decrementTurnCount(user);
         boolean skipCharge = false;
         if (this.getTurnCount(user) == 1) {
            if (user.attack.isAttack("Fly")) {
               user.bc.sendToAll("pixelmon.effect.flyup", user.getNickname());
            } else if (user.attack.isAttack("Bounce")) {
               user.bc.sendToAll("pixelmon.effect.bounce", user.getNickname());
            }

            if (!user.getUsableHeldItem().affectMultiturnMove(user)) {
               user.addStatus(StatusBase.getNewInstance(this.statusClass), user);
               return AttackResult.charging;
            }

            skipCharge = true;
         }

         if (!user.bc.simulateMode && !skipCharge && !user.hasStatus(this.type)) {
            this.setPersists(user, false);
            return AttackResult.failed;
         } else {
            user.removeStatus(this.type);
            this.setPersists(user, false);
            return AttackResult.proceed;
         }
      }
   }
}
