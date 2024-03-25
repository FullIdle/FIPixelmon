package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Set;

public class PowerOfAlchemy extends AbilityBase {
   private static final Set invalidAbilities = Sets.newHashSet(new String[]{"PowerOfAlchemy", "Receiver", "Trace", "Forecast", "AsOne", "GulpMissile", "HungerSwitch", "FlowerGift", "Multitype", "Illusion", "WonderGuard", "ZenMode", "Imposter", "StanceChange", "PowerConstruct", "Schooling", "Comatose", "ShieldsDown", "Disguise", "RKSSystem", "BattleBond", "IceFace", "NeutralizingGas"});

   public void onAllyFaint(PixelmonWrapper pokemon, PixelmonWrapper fainted, PixelmonWrapper source) {
      if (!invalidAbilities.contains(fainted.getBattleAbility(false).getName())) {
         pokemon.bc.sendToAll("pixelmon.effect.entrainment", pokemon.getNickname(), pokemon.getBattleAbility(true).getName(), fainted.getBattleAbility(false).getName());
         pokemon.setTempAbility(fainted.getBattleAbility(false));
      }

   }
}
