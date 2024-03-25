package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Absorb extends AbilityBase {
   private EnumType type;
   private String langString;

   protected Absorb(EnumType type, String langString) {
      this.type = type;
      this.langString = langString;
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.getType() == this.type && !pokemon.hasStatus(StatusType.HealBlock)) {
         if (pokemon.hasFullHealth()) {
            pokemon.bc.sendToAll(this.langString + "2", pokemon.getNickname());
         } else {
            int healAmount = pokemon.getPercentMaxHealth(25.0F);
            float healPercent = pokemon.getHealPercent((float)healAmount);
            pokemon.healEntityBy(healAmount);
            pokemon.bc.sendToAll(this.langString, pokemon.getNickname());
            MoveResults var10000 = a.moveResult;
            var10000.weightMod -= healPercent;
         }

         return false;
      } else {
         return true;
      }
   }
}
