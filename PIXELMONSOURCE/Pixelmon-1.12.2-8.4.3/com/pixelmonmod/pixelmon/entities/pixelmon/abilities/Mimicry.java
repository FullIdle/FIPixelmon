package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Mimicry extends AbilityBase {
   public void onTerrainSwitch(BattleControllerBase bc, PixelmonWrapper user, Terrain terrain) {
      EnumType type = terrain == null ? null : terrain.getTypingForTerrain();
      if (type != null) {
         user.setTempType(type);
         user.bc.sendToAll("pixelmon.abilities.mimicry", user.getNickname());
      } else {
         user.setTempType(user.getInitialType());
         user.bc.sendToAll("pixelmon.abilities.mimicry.revert", user.getNickname());
      }

   }
}
