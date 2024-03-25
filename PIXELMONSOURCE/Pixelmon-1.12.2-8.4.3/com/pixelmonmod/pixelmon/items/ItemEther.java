package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.WrapperLink;
import net.minecraft.util.text.TextComponentTranslation;

public class ItemEther extends ItemPPRestore {
   public ItemEther(String itemName, boolean allPP) {
      super(itemName, allPP);
   }

   public boolean useEther(PokemonLink pokemon, int selectedMove) {
      boolean restorePP = restorePP(pokemon, selectedMove, this.allPP);
      String message = this.getMessage(restorePP);
      if (pokemon.getPlayerOwner() != null) {
         pokemon.getPlayerOwner().func_145747_a(new TextComponentTranslation(message, new Object[]{pokemon.getMoveset().get(selectedMove).getMove().getTranslatedName()}));
      }

      return restorePP;
   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper, int selectedMove) {
      this.useEther(new WrapperLink(targetWrapper), selectedMove);
      return super.useFromBag(userWrapper, targetWrapper);
   }
}
