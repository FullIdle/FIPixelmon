package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class LeafGuard extends AbilityBase {
   public boolean allowsStatus(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (user.bc == null || !(user.bc.globalStatusController.getWeather() instanceof Sunny) || !StatusType.isPrimaryStatus(status) && !status.equals(StatusType.Yawn)) {
         return true;
      } else {
         if (user != pokemon && user.attack != null && user.attack.getAttackCategory() == AttackCategory.STATUS) {
            user.bc.sendToAll("pixelmon.abilities.leafguard", pokemon.getNickname());
         }

         return false;
      }
   }
}
