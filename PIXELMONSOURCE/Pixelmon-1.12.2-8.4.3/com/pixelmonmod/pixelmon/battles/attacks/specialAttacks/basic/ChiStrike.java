package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Iterator;

public class ChiStrike extends SpecialAttackBase {
   public void applyAfterEffect(PixelmonWrapper user) {
      Iterator var2 = user.bc.getTeamPokemon(user).iterator();

      while(var2.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var2.next();
         pw.getBattleStats().increaseCritStage(1, false);
      }

   }

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      return AttackResult.proceed;
   }
}
