package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Sturdy extends AbilityBase {
   public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper pokemon, Attack a) {
      if (AbilityBase.ignoreAbility(user, pokemon)) {
         return damage;
      } else if (pokemon != null && user != null && user != pokemon && damage >= pokemon.getHealth() && pokemon.getHealth() == pokemon.getMaxHealth()) {
         if (user.bc != null) {
            user.bc.sendToAll("pixelmon.abilities.sturdy", pokemon.getNickname());
         }

         return pokemon.getHealth() - 1;
      } else {
         return damage;
      }
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      return AbilityBase.ignoreAbility(user, pokemon) || !a.isAttack("Fissure", "Guillotine", "Horn Drill", "Sheer Cold");
   }

   public void allowsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (user.bc != null) {
         user.bc.sendToAll("pixelmon.abilities.sturdy2", pokemon.getNickname());
      }

   }
}
