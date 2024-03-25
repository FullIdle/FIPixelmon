package com.pixelmonmod.pixelmon.battles.controller.log;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.UUID;

public class SwitchAction extends BattleActionBase {
   public UUID switchingTo;

   public SwitchAction(int turn, int pokemonPosition, PixelmonWrapper pokemon, UUID switchingTo) {
      super(turn, pokemonPosition, pokemon);
      this.switchingTo = switchingTo;
   }

   public BattleActionBase.ActionType actionType() {
      return BattleActionBase.ActionType.SWITCH;
   }
}
