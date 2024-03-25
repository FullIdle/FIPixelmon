package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class SweetVeil extends AbilityBase {
   public boolean allowsStatusTeammate(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper target, PixelmonWrapper user) {
      if (status != StatusType.Sleep && status != StatusType.Yawn) {
         return true;
      } else {
         if (user != target && user.attack != null && user.attack.getAttackCategory() == AttackCategory.STATUS) {
            user.bc.sendToAll("pixelmon.abilities.sweetveil", pokemon.getNickname(), user.getNickname());
         }

         return false;
      }
   }
}
