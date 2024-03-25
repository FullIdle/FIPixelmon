package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Iterator;

public class CuriousMedicine extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      boolean first = true;
      Iterator var3 = newPokemon.getTeamPokemonExcludeSelf().iterator();

      while(var3.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var3.next();
         if (first) {
            pw.bc.sendToAll("pixelmon.abilities.curiousmedicine", newPokemon.getNickname());
            first = false;
         }

         pw.getBattleStats().resetLoweredBattleStats();
         pw.bc.sendToAll("pixelmon.effect.resetstats", pw.getNickname());
      }

   }
}
