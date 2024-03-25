package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class InnardsOut extends AbilityBase {
   public void onSelfFaint(PixelmonWrapper pokemon, PixelmonWrapper source) {
      source.doBattleDamage(pokemon, (float)pokemon.lastHP, DamageTypeEnum.ABILITY);
      source.bc.sendToAll("pixelmon.abilities.innardsout", source.getNickname(), pokemon.getNickname());
   }
}
