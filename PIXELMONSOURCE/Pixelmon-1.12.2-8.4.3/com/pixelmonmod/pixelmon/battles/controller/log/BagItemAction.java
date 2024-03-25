package com.pixelmonmod.pixelmon.battles.controller.log;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.items.PixelmonItem;

public class BagItemAction extends BattleActionBase {
   PixelmonItem item;
   String recipient;

   public BagItemAction(int turn, int pokemonPosition, PixelmonWrapper pokemon, String recipient, PixelmonItem item) {
      super(turn, pokemonPosition, pokemon);
      this.item = item;
      this.recipient = recipient;
   }

   public BattleActionBase.ActionType actionType() {
      return BattleActionBase.ActionType.BAG;
   }
}
