package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;

public class FlowerShield extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      boolean hadEffect = false;
      Iterator var4 = user.bc.getActiveUnfaintedPokemon().iterator();

      while(var4.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var4.next();
         if (pw.hasType(EnumType.Grass)) {
            pw.getBattleStats().modifyStat(1, StatsType.Defence, user, true);
            hadEffect = true;
         }
      }

      if (hadEffect) {
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = pw.getOpponentPokemon().iterator();

      PixelmonWrapper other;
      while(var7.hasNext()) {
         other = (PixelmonWrapper)var7.next();
         if (other.hasType(EnumType.Grass)) {
            userChoice.raiseWeight(-10.0F);
         }
      }

      var7 = pw.getTeamPokemon().iterator();

      while(var7.hasNext()) {
         other = (PixelmonWrapper)var7.next();
         if (other.hasType(EnumType.Grass)) {
            userChoice.raiseWeight(10.0F);
         }
      }

   }
}
