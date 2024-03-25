package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class BadDreams extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.bc.getAdjacentPokemon(pw).stream().filter((pokemon) -> {
         return pokemon.hasStatus(StatusType.Sleep) && !(pokemon.getBattleAbility() instanceof MagicGuard);
      }).forEach((pokemon) -> {
         pw.bc.sendToAll("pixelmon.abilities.baddreams", pokemon.getNickname(), pw.getNickname());
         pokemon.doBattleDamage(pw, (float)pokemon.getPercentMaxHealth(12.5F), DamageTypeEnum.ABILITY);
      });
   }
}
