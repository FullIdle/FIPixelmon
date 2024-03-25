package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.SmackedDown;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Levitate;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;

public class SmackDown extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.attack.isAttack("Thousand Arrows")) {
         if (target.hasStatus(StatusType.Substitute)) {
            return AttackResult.proceed;
         }

         if (target.hasStatus(StatusType.Flying)) {
            target.canAttack = false;
         }

         if (target.removeStatuses(StatusType.Flying, StatusType.MagnetRise, StatusType.Telekinesis) || !target.hasStatus(StatusType.SmackedDown) && (target.hasType(EnumType.Flying) || target.getBattleAbility() instanceof Levitate)) {
            user.bc.sendToAll("pixelmon.effect.smackdown", target.getNickname());
         }

         target.addStatus(new SmackedDown(), user);
      }

      return AttackResult.proceed;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.attack.isAttack("Thousand Arrows") && target.hasType(EnumType.Flying)) {
         if (target.hasStatus(StatusType.Substitute)) {
            return;
         }

         if (target.hasStatus(StatusType.Flying)) {
            target.canAttack = false;
         }

         if (target.removeStatuses(StatusType.Flying, StatusType.MagnetRise, StatusType.Telekinesis) || !target.hasStatus(StatusType.SmackedDown) && (target.hasType(EnumType.Flying) || target.getBattleAbility() instanceof Levitate)) {
            user.bc.sendToAll("pixelmon.effect.smackdown", target.getNickname());
         }

         target.addStatus(new SmackedDown(), user);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            if (target.hasStatus(StatusType.Flying) && pw.bc.getFirstMover(pw, target) == pw) {
               userChoice.raiseWeight(50.0F);
            }

            if (target.isAirborne() && pw.getMoveset().hasOffensiveAttackType(EnumType.Ground)) {
               userChoice.raiseWeight(25.0F);
            }
         }

      }
   }
}
