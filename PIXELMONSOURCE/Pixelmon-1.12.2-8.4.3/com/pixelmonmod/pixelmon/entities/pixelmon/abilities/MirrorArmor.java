package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.Objects;

public class MirrorArmor extends AbilityBase {
   public boolean allowsStatChange(PixelmonWrapper pokemon, PixelmonWrapper user, StatsEffect effect) {
      if (user.getBattleAbility() instanceof MirrorArmor && (pokemon.bc.turnList.size() <= pokemon.bc.battleTurn || Objects.equals(pokemon.getPokemonUUID(), ((PixelmonWrapper)pokemon.bc.turnList.get(pokemon.bc.battleTurn)).getPokemonUUID()))) {
         return false;
      } else {
         boolean allowed = true;
         if (user.attack != null && user.attack.getAttackCategory() == AttackCategory.STATUS && pokemon.getAbility() == this) {
            pokemon.bc.sendToAll("pixelmon.abilities.mirrorarmor", pokemon.getNickname());
            effect.applyStatEffect(pokemon, user, user.attack.getActualMove());
            allowed = false;
         }

         return allowed;
      }
   }
}
