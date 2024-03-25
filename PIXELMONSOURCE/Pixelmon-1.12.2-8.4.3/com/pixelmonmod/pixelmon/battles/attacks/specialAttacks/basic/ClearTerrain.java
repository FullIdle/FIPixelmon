package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.NoTerrain;
import com.pixelmonmod.pixelmon.battles.status.Terrain;

public class ClearTerrain extends EffectBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      GlobalStatusController global = user.bc.globalStatusController;
      Terrain terrain = user.bc.globalStatusController.getTerrain();
      if (!(terrain instanceof NoTerrain) && !user.bc.simulateMode) {
         global.removeGlobalStatus((GlobalStatusBase)terrain);
         global.bc.sendToAll(terrain.langEnd);
      }

   }

   public boolean cantMiss(PixelmonWrapper user) {
      return false;
   }
}
