package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class Healer extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      ArrayList allies = pokemon.getTeamPokemonExcludeSelf();
      Iterator var3 = allies.iterator();

      while(var3.hasNext()) {
         PixelmonWrapper ally = (PixelmonWrapper)var3.next();
         if (RandomHelper.getRandomChance(30) && ally.hasPrimaryStatus(false)) {
            pokemon.bc.sendToAll(pokemon.getNickname(), this.getTranslatedName());
            ally.removePrimaryStatus();
         }
      }

   }
}
