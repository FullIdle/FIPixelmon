package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.item.ItemStack;

public class Frisk extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      ArrayList opponents = newPokemon.bc.getOpponentPokemon(newPokemon.getParticipant());
      Iterator var3 = opponents.iterator();

      while(var3.hasNext()) {
         PixelmonWrapper opponent = (PixelmonWrapper)var3.next();
         ItemStack item = opponent.pokemon.getHeldItem();
         if (!item.func_190926_b()) {
            newPokemon.bc.sendToAll("pixelmon.abilities.frisk", newPokemon.getNickname(), opponent.getNickname(), item.func_82833_r());
         }
      }

   }
}
