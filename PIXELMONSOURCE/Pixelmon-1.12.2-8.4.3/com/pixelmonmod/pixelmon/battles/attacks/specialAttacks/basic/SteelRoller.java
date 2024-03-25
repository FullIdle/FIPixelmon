package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import java.util.ArrayList;

public class SteelRoller extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      boolean clearedTerrain = false;
      if (user.bc.globalStatusController.removeGlobalStatuses(StatusType.ElectricTerrain, StatusType.GrassyTerrain, StatusType.MistyTerrain, StatusType.PsychicTerrain)) {
         clearedTerrain = true;
      }

      if (clearedTerrain) {
         user.bc.sendToAll("pixelmon.effect.clearterrain", user.getNickname());
         return AttackResult.proceed;
      } else {
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int total = 0;
      StatusType[] entryTerrains = new StatusType[]{StatusType.ElectricTerrain, StatusType.MistyTerrain, StatusType.PsychicTerrain, StatusType.GrassyTerrain};
      total += pw.countStatuses(entryTerrains);
      total -= ((PixelmonWrapper)pw.getOpponentPokemon().get(0)).countStatuses(entryTerrains);
      userChoice.raiseWeight((float)(30 * total));
   }
}
