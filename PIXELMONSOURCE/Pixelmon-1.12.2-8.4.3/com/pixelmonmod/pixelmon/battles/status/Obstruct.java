package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;

public class Obstruct extends Protect {
   public Obstruct() {
      super(StatusType.Obstruct);
   }

   protected boolean addStatus(PixelmonWrapper user) {
      return user.addStatus(new Obstruct(), user);
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      super.stopsIncomingAttackMessage(pokemon, user);
      if (user.attack.getMove().getMakesContact()) {
         user.getBattleStats().modifyStat(-2, StatsType.Defence, user, false);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      super.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      if (userChoice.weight != -1.0F) {
         Iterator var7 = MoveChoice.splitChoices(pw.getOpponentPokemon(), bestOpponentChoices).iterator();

         while(true) {
            while(var7.hasNext()) {
               ArrayList choices = (ArrayList)var7.next();
               Iterator var9 = choices.iterator();

               while(var9.hasNext()) {
                  MoveChoice choice = (MoveChoice)var9.next();
                  if (choice.isAttack() && choice.attack.getMove().getMakesContact()) {
                     userChoice.raiseWeight(12.5F);
                     break;
                  }
               }
            }

            return;
         }
      }
   }
}
