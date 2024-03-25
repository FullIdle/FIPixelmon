package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.PsychicTerrain;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class PsychicSurge extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      Terrain terrain = newPokemon.bc.globalStatusController.getTerrain();
      if (!(terrain instanceof PsychicTerrain)) {
         if (terrain != null) {
            newPokemon.bc.globalStatusController.removeGlobalStatus((GlobalStatusBase)terrain);
         }

         PsychicTerrain et = new PsychicTerrain();
         if (newPokemon.getUsableHeldItem().getHeldItemType() == EnumHeldItems.terrainExtender) {
            et.setTurns(8);
         }

         newPokemon.bc.sendToAll(et.langStart);
         newPokemon.bc.globalStatusController.addGlobalStatus(et);
      }

   }
}
