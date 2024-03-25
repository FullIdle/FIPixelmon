package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class QuickGuard extends ProtectVariationTeam {
   public QuickGuard() {
      super(StatusType.QuickGuard);
   }

   public ProtectVariationTeam getNewInstance() {
      return new QuickGuard();
   }

   protected void displayMessage(PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.status.quickguard", user.getNickname());
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return !user.attack.isAttack("Quick Guard") && user.attack.getMove().getPriority(user) >= 1 && super.stopsIncomingAttack(pokemon, user);
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.status.quickguardprotect", pokemon.getNickname());
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!MoveChoice.canBreakProtect(pw.getOpponentPokemon(), bestOpponentChoices)) {
         if (pw.bc.rules.battleType.numPokemon > 1 && !MoveChoice.canOutspeed(bestOpponentChoices, pw, userChoice.createList()) && MoveChoice.hasPriority(bestOpponentChoices)) {
            userChoice.raiseWeight(50.0F);
         }

      }
   }
}
