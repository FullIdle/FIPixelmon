package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;

public class WideGuard extends ProtectVariationTeam {
   public WideGuard() {
      super(StatusType.WideGuard);
   }

   public ProtectVariationTeam getNewInstance() {
      return new WideGuard();
   }

   protected void displayMessage(PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.status.wideguard", user.getNickname());
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return super.stopsIncomingAttack(pokemon, user) && user.attack.getAttackCategory() != AttackCategory.STATUS && user.attack.getMove().getTargetingInfo().hitsAll && user.attack.getMove().getTargetingInfo().hitsAdjacentFoe;
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.status.wideguardprotect", pokemon.getNickname());
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!MoveChoice.canBreakProtect(pw.getOpponentPokemon(), bestOpponentChoices)) {
         if (pw.bc.rules.battleType.numPokemon > 1 && MoveChoice.hasSpreadMove(bestOpponentChoices)) {
            userChoice.raiseWeight(75.0F);
         }

      }
   }
}
