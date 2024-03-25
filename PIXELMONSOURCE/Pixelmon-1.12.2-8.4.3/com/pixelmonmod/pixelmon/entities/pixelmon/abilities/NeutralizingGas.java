package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Iterator;
import java.util.List;

public class NeutralizingGas extends AbilityBase {
   private static final List invalidAbilities = Lists.newArrayList(new String[]{"NeutralizingGas", "Multitype", "StanceChange", "Schooling", "Comatose", "ShieldsDown", "Disguise", "RKSSystem", "BattleBond", "PowerConstruct", "AsOne", "ZenMode", "GulpMissile", "IceFace"});

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      newPokemon.bc.sendToAll("pixelmon.abilities.neutralizinggas");
   }

   public void onSelfFaint(PixelmonWrapper pokemon, PixelmonWrapper source) {
      this.triggerOtherSwitchInAbilities(pokemon, pokemon.bc);
   }

   public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
      this.triggerOtherSwitchInAbilities(oldPokemon, oldPokemon.bc);
   }

   private void triggerOtherSwitchInAbilities(PixelmonWrapper pw, BattleControllerBase bc) {
      Iterator var3 = bc.getActivePokemon().iterator();

      while(var3.hasNext()) {
         PixelmonWrapper opw = (PixelmonWrapper)var3.next();
         if (pw != opw) {
            opw.getBattleAbility(false).applySwitchInEffect(opw);
         }
      }

   }

   public static boolean isAbilityDisabled(AbilityBase ability) {
      return !invalidAbilities.contains(ability.getName());
   }
}
