package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class PreventStatDrop extends AbilityBase {
   private String langKey;

   public PreventStatDrop(String langKey) {
      this.langKey = langKey;
   }

   public boolean allowsStatChange(PixelmonWrapper pokemon, PixelmonWrapper user, StatsEffect e) {
      if (!e.getUser() && e.amount <= 0) {
         if (!Attack.dealsDamage(user.attack)) {
            user.bc.sendToAll(this.langKey, pokemon.getNickname());
         }

         return false;
      } else {
         return true;
      }
   }
}
