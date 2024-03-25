package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class Sweetness extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      Iterator var3 = user.getParticipant().getActiveUnfaintedPokemon().iterator();

      while(var3.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var3.next();
         pw.removePrimaryStatus();
      }

      return AttackResult.proceed;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int numStatus = 0;
      Iterator var8 = pw.getParticipant().getActiveUnfaintedPokemon().iterator();

      while(var8.hasNext()) {
         PixelmonWrapper other = (PixelmonWrapper)var8.next();
         if (other.hasPrimaryStatus(false)) {
            ++numStatus;
         }
      }

      userChoice.raiseWeight((float)(numStatus * 30));
   }
}
