package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class Stockpile extends StatusBase {
   public transient int numStockpiles = 1;

   public Stockpile() {
      super(StatusType.Stockpile);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      int stockpileIndex = user.getStatusIndex(StatusType.Stockpile);
      if (stockpileIndex == -1) {
         user.addStatus(new Stockpile(), target);
         user.bc.sendToAll("pixelmon.status.stockpile", user.getNickname(), 1);
         user.getBattleStats().modifyStat(1, (StatsType[])(StatsType.Defence, StatsType.SpecialDefence));
      } else {
         Stockpile stockpile = (Stockpile)user.getStatus(stockpileIndex);
         if (stockpile.numStockpiles < 3) {
            if (!user.bc.simulateMode) {
               ++stockpile.numStockpiles;
               user.bc.sendToAll("pixelmon.status.stockpile", user.getNickname(), stockpile.numStockpiles);
               user.getBattleStats().modifyStat(1, (StatsType[])(StatsType.Defence, StatsType.SpecialDefence));
            }
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            user.attack.moveResult.result = AttackResult.failed;
         }
      }

   }

   public void removeStockpile(PixelmonWrapper pokemon) {
      pokemon.bc.sendToAll("pixelmon.status.stockpileoff", pokemon.getNickname());
      pokemon.getBattleStats().modifyStat(-this.numStockpiles, StatsType.Defence, StatsType.SpecialDefence);
      pokemon.removeStatus((StatusBase)this);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      StatsEffect defense = new StatsEffect(StatsType.Defence, 1, true);
      StatsEffect spDefense = new StatsEffect(StatsType.SpecialDefence, 1, true);
      defense.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      spDefense.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      if (pw.getMoveset().hasAttack("Spit Up", "Swallow")) {
         userChoice.raiseWeight(15.0F);
      }

   }
}
