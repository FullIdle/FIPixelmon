package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class CottonDown extends AbilityBase {
   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      target.bc.sendToAll("pixelmon.abilities.cottondown", target.getNickname());
      target.bc.getActivePokemon().forEach((pw) -> {
         if (pw != target) {
            pw.getBattleStats().modifyStat(-1, (StatsType)StatsType.Speed);
         }

      });
   }
}
