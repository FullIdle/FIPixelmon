package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MultiTurnCharge extends MultiTurnSpecialAttackBase {
   String message;
   String base;
   StatusType type;
   transient Class statusClass;
   private static final List INSTANT_IN_RAIDS = new ArrayList(Arrays.asList("Dig", "Fly", "Bounce", "Dive", "Phantom Force", "Sky Drop", "Shadow Force"));

   public MultiTurnCharge() {
      this.message = "";
      this.base = null;
      this.statusClass = null;
   }

   public MultiTurnCharge(String message) {
      this.message = "";
      this.base = null;
      this.statusClass = null;
      this.message = message;
   }

   public MultiTurnCharge(String message, String base, StatusType type) {
      this(message);
      this.base = base;

      try {
         this.statusClass = Class.forName("com.pixelmonmod.pixelmon.battles.status." + base);
      } catch (ClassNotFoundException var5) {
         var5.printStackTrace();
      }

      this.type = type;
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.isRaidPokemon() && user.attack.isAttack(INSTANT_IN_RAIDS)) {
         return AttackResult.proceed;
      } else {
         if (!this.doesPersist(user)) {
            this.setPersists(user, true);
            this.setTurnCount(user, 2);
         }

         this.decrementTurnCount(user);
         if (this.getTurnCount(user) == 1) {
            user.bc.sendToAll(this.message, user.getNickname());
            if (!user.getUsableHeldItem().affectMultiturnMove(user)) {
               if (this.base != null) {
                  user.addStatus(StatusBase.getNewInstance(this.statusClass), user);
               }

               return AttackResult.charging;
            }
         }

         if (this.type != null) {
            user.removeStatus(this.type);
         }

         this.setPersists(user, false);
         return AttackResult.proceed;
      }
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return this.getTurnCount(user) == -1;
   }

   public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
      this.removeEffect(user, target);
   }

   public void removeEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.type != null) {
         user.removeStatus(this.type);
      }

      this.setPersists(user, false);
   }

   public boolean isCharging(PixelmonWrapper user, PixelmonWrapper target) {
      return !this.doesPersist(user);
   }

   public boolean shouldNotLosePP(PixelmonWrapper user) {
      return !this.doesPersist(user) && user.getUsableHeldItem().getHeldItemType() != EnumHeldItems.powerHerb;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (pw.getUsableHeldItem().getHeldItemType() != EnumHeldItems.powerHerb) {
         if (this.base == null) {
            if (userChoice.tier >= 3) {
               userChoice.lowerTier(2);
            } else {
               userChoice.weight /= 2.0F;
            }
         } else {
            userChoice.weight *= 0.9F;
         }

         if (pw.hasStatus(StatusType.Confusion, StatusType.Infatuated, StatusType.Paralysis)) {
            userChoice.weight /= 2.0F;
         }

      }
   }
}
