package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.List;
import java.util.Set;

public class Trace extends AbilityBase {
   private static final Set invalidAbilities = Sets.newHashSet(new String[]{"AsOne", "BattleBond", "Comatose", "Disguise", "FlowerGift", "Forecast", "GulpMissile", "HungerSwitch", "IceFace", "Illusion", "Imposter", "Multitype", "NeutralizingGas", "PowerConstruct", "PowerOfAlchemy", "Receiver", "RKSSystem", "Schooling", "ShieldsDown", "StanceChange", "Trace", "ZenMode"});

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      List opponents = newPokemon.getOpponentPokemon();

      while(!opponents.isEmpty()) {
         PixelmonWrapper opponent = (PixelmonWrapper)RandomHelper.getRandomElementFromList(opponents);
         AbilityBase targetAbility = opponent.getBattleAbility(false);
         if (!invalidAbilities.contains(targetAbility.getName()) && !opponent.isFainted()) {
            newPokemon.bc.sendToAll("pixelmon.abilities.trace", newPokemon.getNickname(), opponent.getNickname(), targetAbility.getTranslatedName());
            newPokemon.setTempAbility(targetAbility);
            return;
         }

         opponents.remove(opponent);
      }

   }
}
