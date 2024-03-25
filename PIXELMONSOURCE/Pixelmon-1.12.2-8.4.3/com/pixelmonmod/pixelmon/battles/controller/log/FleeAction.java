package com.pixelmonmod.pixelmon.battles.controller.log;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class FleeAction extends BattleActionBase {
   public FleeAction(int turn, int pokemonPosition, PixelmonWrapper pokemon) {
      super(turn, pokemonPosition, pokemon);
   }

   public BattleActionBase.ActionType actionType() {
      return BattleActionBase.ActionType.FLEE;
   }
}
