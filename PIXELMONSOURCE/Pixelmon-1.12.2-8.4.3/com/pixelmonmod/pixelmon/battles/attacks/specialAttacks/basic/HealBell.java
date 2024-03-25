package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class HealBell extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0 || user.bc.simulateMode) {
         if (user.attack.isAttack("Heal Bell")) {
            user.bc.sendToAll("pixelmon.effect.healbellstart");
         } else if (user.attack.isAttack("Aromatherapy")) {
            user.bc.sendToAll("pixelmon.effect.aromatherapy");
         }

         boolean healedSomething = false;
         PixelmonWrapper[] var4 = user.getParticipant().allPokemon;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PixelmonWrapper pw = var4[var6];
            if (pw.removePrimaryStatus() != null) {
               healedSomething = true;
            }
         }

         if (!healedSomething) {
            return AttackResult.failed;
         }
      }

      return AttackResult.succeeded;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int numStatus = 0;
      PixelmonWrapper[] var8 = pw.getParticipant().allPokemon;
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         PixelmonWrapper other = var8[var10];
         if (other.hasPrimaryStatus(false)) {
            ++numStatus;
         }
      }

      userChoice.raiseWeight((float)(numStatus * 30));
   }
}
