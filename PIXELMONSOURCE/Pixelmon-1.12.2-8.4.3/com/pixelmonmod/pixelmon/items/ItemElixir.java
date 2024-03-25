package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.WrapperLink;

public class ItemElixir extends ItemPPRestore {
   public ItemElixir(String itemName, boolean allPP) {
      super(itemName, allPP);
   }

   public boolean useElixir(PokemonLink pokemon) {
      boolean success = false;

      for(int i = 0; i < pokemon.getMoveset().size(); ++i) {
         success = restorePP(pokemon, i, this.allPP) || success;
      }

      String message = this.getMessage(success);
      String data = pokemon.getNickname();
      pokemon.sendMessage(message, data);
      return success;
   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper, int selectedMove) {
      this.useElixir(new WrapperLink(targetWrapper));
      return super.useFromBag(userWrapper, targetWrapper);
   }
}
