package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.GrassyTerrain;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class GrassySurge extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      Terrain terrain = newPokemon.bc.globalStatusController.getTerrain();
      if (!(terrain instanceof GrassyTerrain)) {
         if (terrain != null) {
            newPokemon.bc.globalStatusController.removeGlobalStatus((GlobalStatusBase)terrain);
         }

         GrassyTerrain et = new GrassyTerrain();
         if (newPokemon.getUsableHeldItem().getHeldItemType() == EnumHeldItems.terrainExtender) {
            et.setTurns(8);
         }

         newPokemon.bc.sendToAll(et.langStart);
         newPokemon.bc.globalStatusController.addGlobalStatus(et);
      }

   }
}
