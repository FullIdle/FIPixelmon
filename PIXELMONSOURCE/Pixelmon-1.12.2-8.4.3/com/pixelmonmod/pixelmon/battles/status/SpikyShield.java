package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class SpikyShield extends Protect {
   public SpikyShield() {
      super(StatusType.SpikyShield);
   }

   protected boolean addStatus(PixelmonWrapper user) {
      return user.addStatus(new SpikyShield(), user);
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      super.stopsIncomingAttackMessage(pokemon, user);
      if (user.attack.getMove().getMakesContact()) {
         user.bc.sendToAll("pixelmon.effect.hurt", user.getNickname());
         user.doBattleDamage(pokemon, (float)user.getPercentMaxHealth(12.5F), DamageTypeEnum.STATUS);
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
