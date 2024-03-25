package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.old_gui.chooseMove;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BagPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.UseEther;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import java.util.UUID;

public class ChooseEther extends ChooseMove {
   public ChooseEther(GuiBattle parent) {
      super(parent, BattleMode.ChooseEther);
   }

   protected void clickMove(int moveIndex) {
      UUID pokemonUUID = this.bm.getCurrentPokemon().pokemonUUID;
      if (this.bm.battleEnded) {
         Pixelmon.network.sendToServer(new UseEther(moveIndex, pokemonUUID));
         this.bm.choosingPokemon = false;
      } else {
         this.bm.selectedActions.add(new BagPacket(pokemonUUID, this.bm.itemToUse.id, this.bm.battleControllerIndex, moveIndex));
         this.bm.selectedMove();
      }

   }
}
