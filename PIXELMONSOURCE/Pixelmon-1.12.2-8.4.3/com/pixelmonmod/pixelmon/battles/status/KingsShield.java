package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;

public class KingsShield extends Protect {
   public KingsShield() {
      super(StatusType.KingsShield);
   }

   protected boolean addStatus(PixelmonWrapper user) {
      return user.addStatus(new KingsShield(), user);
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return user.attack.getAttackCategory() != AttackCategory.STATUS && super.stopsIncomingAttack(pokemon, user);
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      super.stopsIncomingAttackMessage(pokemon, user);
      if (user.attack.getMove().getMakesContact()) {
         user.getBattleStats().modifyStat(-1, StatsType.Attack, user, false);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      super.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      if (userChoice.weight != -1.0F) {
         StatsEffect statsEffect = new StatsEffect(StatsType.Attack, -1, false);
         Iterator var8 = MoveChoice.splitChoices(pw.getOpponentPokemon(), bestOpponentChoices).iterator();

         while(true) {
            while(var8.hasNext()) {
               ArrayList choices = (ArrayList)var8.next();
               Iterator var10 = choices.iterator();

               while(var10.hasNext()) {
                  MoveChoice choice = (MoveChoice)var10.next();
                  if (choice.isAttack() && choice.attack.getMove().getMakesContact()) {
                     ArrayList saveTargets = userChoice.targets;
                     ArrayList newTargets = new ArrayList();
                     newTargets.add(choice.user);
                     userChoice.targets = newTargets;
                     statsEffect.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
                     userChoice.targets = saveTargets;
                     break;
                  }
               }
            }

            return;
         }
      }
   }
}
