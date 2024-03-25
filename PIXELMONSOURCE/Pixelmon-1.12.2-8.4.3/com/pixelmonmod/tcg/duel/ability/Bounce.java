package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.helper.LogicHelper;
import java.util.Iterator;
import java.util.List;

public class Bounce extends BaseAbilityEffect {
   public Bounce() {
      super("Bounce");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      return LogicHelper.canEvolve(pokemon.getTurn(), client.getTurnCount());
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState server) {
      return true;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      Iterator var4 = player.getActiveAndBenchCards().iterator();

      while(true) {
         while(true) {
            PokemonCardState card;
            List cards;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               card = (PokemonCardState)var4.next();
               cards = card.getAttachments();
            } while(pokemon != card);

            if (player.isInBench(card)) {
               for(int i = 0; i < player.getBenchCards().length; ++i) {
                  if (player.getBenchCards()[i] == pokemon) {
                     player.getHand().add(player.getBenchCards()[i].getData());
                     Iterator var11 = cards.iterator();

                     while(var11.hasNext()) {
                        CommonCardState attach = (CommonCardState)var11.next();
                        player.getDiscardPile().add(attach.getData());
                        player.getBenchCards()[i].getAttachments().remove(attach);
                     }

                     player.getBenchCards()[i] = null;
                  }
               }
            } else if (player.getActiveCard() == pokemon) {
               player.getHand().add(player.getActiveCard().getData());
               Iterator var7 = cards.iterator();

               while(var7.hasNext()) {
                  CommonCardState attach = (CommonCardState)var7.next();
                  player.getDiscardPile().add(attach.getData());
               }

               player.getActiveCard().getAttachments().clear();
               player.setActiveCard((PokemonCardState)null);
            }
         }
      }
   }
}
