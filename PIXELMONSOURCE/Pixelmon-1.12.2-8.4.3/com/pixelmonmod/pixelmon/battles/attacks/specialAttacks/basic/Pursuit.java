package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.controller.CalcPriority;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.tasks.EnforcedSwitchTask;
import java.util.ArrayList;
import java.util.Iterator;

public class Pursuit extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.isSwitching || target.nextSwitchIsMove) {
         user.attack.getMove().setBasePower(user.attack.getMove().getBasePower() * 2);
      }

      return AttackResult.proceed;
   }

   public void switchOutRetreatingTarget(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.attack.getActualMove().hasEffect(SwitchOut.class) || target.attack.getActualMove().hasEffect(PartingShot.class)) {
         int targetIndex = user.bc.turnList.indexOf(target);
         int userIndex = user.bc.turnList.indexOf(user);
         if (targetIndex < userIndex) {
            PixelmonWrapper nextAttacker = null;
            if (userIndex + 1 < user.bc.turnList.size()) {
               nextAttacker = (PixelmonWrapper)user.bc.turnList.get(userIndex + 1);
            }

            if ((nextAttacker == null || !nextAttacker.attack.isAttack("Pursuit") || !nextAttacker.targets.contains(target)) && target.isAlive()) {
               target.wait = true;
               Pixelmon.network.sendTo(new EnforcedSwitchTask(target.bc.getPositionOfPokemon(target, target.getParticipant())), target.getPlayerOwner());
            }
         }
      }

   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.isSwitching && target.isFainted()) {
         for(int i = user.bc.turn + 1; i < user.bc.turnList.size(); ++i) {
            PixelmonWrapper pw = (PixelmonWrapper)user.bc.turnList.get(i);
            if (pw.attack != null && pw.attack.isAttack("Pursuit")) {
               CalcPriority.recalculateMoveSpeed(user.bc, user.bc.turn + 1);
               break;
            }
         }
      }

      if (target.isSwitching || target.nextSwitchIsMove) {
         this.switchOutRetreatingTarget(user, target);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         if (userChoice.tier >= 3) {
            userChoice.raiseWeight(1.0F);
         } else {
            Iterator var7 = userChoice.targets.iterator();

            while(var7.hasNext()) {
               PixelmonWrapper target = (PixelmonWrapper)var7.next();
               if (target.getHealthPercent(userChoice.weight) >= 50.0F) {
                  userChoice.raiseWeight(userChoice.weight);
               }
            }
         }
      }

   }
}
