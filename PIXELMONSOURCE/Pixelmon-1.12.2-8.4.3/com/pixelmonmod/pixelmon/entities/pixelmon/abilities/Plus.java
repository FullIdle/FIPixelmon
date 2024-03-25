package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.Iterator;

public class Plus extends AbilityBase {
   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      Iterator var3 = user.bc.getTeamPokemon(user.getParticipant()).iterator();

      PixelmonWrapper pw;
      do {
         do {
            if (!var3.hasNext()) {
               return stats;
            }

            pw = (PixelmonWrapper)var3.next();
         } while(pw == user);
      } while(!(pw.getBattleAbility() instanceof Plus) && !(pw.getBattleAbility() instanceof Minus));

      int var10001 = StatsType.SpecialAttack.getStatIndex();
      stats[var10001] = (int)((double)stats[var10001] * 1.5);
      return stats;
   }
}
