package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.WrapperLink;

public abstract class ItemPPRestore extends PixelmonItem {
   protected boolean allPP;

   public ItemPPRestore(String itemName, boolean allPP) {
      super(itemName);
      this.func_77625_d(16);
      this.func_77637_a(PixelmonCreativeTabs.restoration);
      this.canRepair = false;
      this.allPP = allPP;
   }

   public static boolean restorePP(PokemonLink userPokemon, int moveIndex, boolean allPP) {
      if (moveIndex == -1) {
         moveIndex = 0;
      }

      return restorePP(userPokemon, userPokemon.getMoveset().get(moveIndex), 10, allPP);
   }

   public static boolean restorePP(PokemonLink userPokemon, Attack m, int pp, boolean allPP) {
      if (m != null && m.pp < m.getMaxPP()) {
         if (allPP) {
            m.pp = m.getMaxPP();
         } else {
            m.pp = Math.min(m.pp + pp, m.getMaxPP());
         }

         if (userPokemon instanceof WrapperLink) {
            PixelmonWrapper wp = ((WrapperLink)userPokemon).getPokemonWrapper();
            wp.setTemporaryMoveset(wp.temporaryMoveset);
         } else if (userPokemon instanceof DelegateLink) {
            userPokemon.getPokemon().markDirty(EnumUpdateType.Moveset);
         }

         return true;
      } else {
         return false;
      }
   }

   protected String getMessage(boolean success) {
      return success ? "pixelmon.interaction.pprestore" : "pixelmon.interaction.ppfail";
   }
}
