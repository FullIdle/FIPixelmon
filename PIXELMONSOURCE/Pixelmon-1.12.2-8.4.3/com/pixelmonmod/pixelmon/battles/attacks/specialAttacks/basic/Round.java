package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class Round extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      ArrayList allies = user.bc.getTeamPokemon(user.getParticipant());

      for(int i = 0; i < user.bc.turnList.size(); ++i) {
         PixelmonWrapper pw = (PixelmonWrapper)user.bc.turnList.get(i);
         if (pw != user && pw.attack != null && pw.attack.isAttack("Round") && allies.contains(pw)) {
            if (i > user.bc.turn) {
               if (!user.bc.simulateMode) {
                  user.bc.turnList.remove(i);
                  user.bc.turnList.add(user.bc.turn + 1, pw);
               }
               break;
            }

            if (pw.canAttack) {
               user.attack.getMove().setBasePower(120);
            }
         }
      }

      return AttackResult.proceed;
   }
}
