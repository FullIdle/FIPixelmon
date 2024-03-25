package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;

public class MatBlock extends StatusBase {
   public MatBlock() {
      super(StatusType.MatBlock);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.isFirstTurn() && !user.bc.isLastMover() && user.addTeamStatus(new MatBlock(), user)) {
         user.bc.sendToAll("pixelmon.status.matblock", user.getNickname());
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed", user.getNickname());
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeTeamStatus((StatusBase)this);
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return user.attack.getAttackCategory() != AttackCategory.STATUS;
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.status.matblocked", pokemon.attack.getMove().getTranslatedName());
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = MoveChoice.splitChoices(pw.getOpponentPokemon(), bestOpponentChoices).iterator();

      ArrayList choices;
      do {
         if (!var7.hasNext()) {
            Protect protect = new Protect();
            protect.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
            if (pw.bc.rules.battleType.numPokemon > 1) {
               userChoice.raiseWeight((float)(20 * pw.bc.rules.battleType.numPokemon));
            }

            return;
         }

         choices = (ArrayList)var7.next();
      } while(!MoveChoice.canOutspeed(choices, pw, bestUserChoices));

   }
}
