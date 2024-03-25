package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Focus;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;

public class FocusPunch extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.hasStatus(StatusType.Focus) && !user.bc.simulateMode) {
         user.bc.sendToAll("pixelmon.effect.lostfocus", user.getNickname());
         return AttackResult.failed;
      } else {
         user.bc.sendToAll("pixelmon.battletext.used", user.getNickname(), user.attack.getMove().getTranslatedName());
         user.removeStatus(StatusType.Focus);
         return AttackResult.proceed;
      }
   }

   public void applyEarlyEffect(PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.effect.focuspunch", user.getNickname());
      user.addStatus(new Focus(), user);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!pw.hasStatus(StatusType.Substitute)) {
         Iterator var7 = pw.getOpponentPokemon().iterator();

         while(var7.hasNext()) {
            PixelmonWrapper opponent = (PixelmonWrapper)var7.next();
            if (opponent.lastAttack != null && opponent.lastAttack.moveResult.target == pw && opponent.lastAttack.getAttackCategory() != AttackCategory.STATUS) {
               userChoice.lowerTier(2);
               userChoice.weight = 0.0F;
            }
         }

      }
   }
}
