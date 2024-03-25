package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class InnerFocus extends AbilityBase {
   public boolean allowsStatus(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper user) {
      AbilityBase userAbility = user.getBattleAbility();
      if (!(userAbility instanceof MoldBreaker) && !(userAbility instanceof Teravolt) && !(userAbility instanceof Turboblaze)) {
         return status != StatusType.Flinch;
      } else {
         return true;
      }
   }
}
