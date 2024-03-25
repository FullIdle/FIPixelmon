package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class AromaVeil extends AbilityBase {
   public boolean allowsStatusTeammate(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper target, PixelmonWrapper user) {
      if (status.isStatus(StatusType.Disable, StatusType.Encore, StatusType.HealBlock, StatusType.Infatuated, StatusType.Taunt, StatusType.Torment)) {
         user.bc.sendToAll("pixelmon.ability.aromaveil", pokemon.getNickname(), target.getNickname());
         return false;
      } else {
         return true;
      }
   }
}
