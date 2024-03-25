package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class JungleHealing extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      Iterator var3 = user.getTeamPokemon().iterator();

      while(var3.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var3.next();
         pw.healEntityBy((int)((double)pw.getMaxHealth() * 0.25));
         pw.removePrimaryStatus(false);
      }

      return AttackResult.proceed;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.raiseWeight(100.0F - pw.getHealthPercent());
      if (pw.hasPrimaryStatus()) {
         userChoice.raiseWeight(30.0F);
      }

   }
}
