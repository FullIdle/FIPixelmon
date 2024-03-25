package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import java.util.Iterator;

public class LastResort extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      Moveset moveset = user.getMoveset();
      int movesetSize = moveset.size();
      if (movesetSize == 1 && moveset.get(0).isAttack("Last Resort")) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         Iterator var5 = moveset.iterator();

         Attack move;
         do {
            if (!var5.hasNext()) {
               return AttackResult.proceed;
            }

            move = (Attack)var5.next();
         } while(move == null || move.isAttack("Last Resort") || user.usedMoves.contains(move));

         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }
}
