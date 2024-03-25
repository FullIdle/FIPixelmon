package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class Telepathy extends AbilityBase {
   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a != null && a.getAttackCategory() != AttackCategory.STATUS && user.bc.getTeamPokemon(user.getParticipant()).contains(pokemon)) {
         user.bc.sendToAll("pixelmon.abilities.telepathy", pokemon.getNickname());
         return false;
      } else {
         return true;
      }
   }
}
