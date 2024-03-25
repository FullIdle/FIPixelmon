package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Buzzap extends BaseAbilityEffect {
   public Buzzap() {
      super("Buzzap");
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      CardSelectorState selector;
      switch (pokemon.getParameters().size()) {
         case 0:
            selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "ability.buzzap.selector.chooseenergy");
            Iterator var6 = CardRegistry.getEnergyCards().iterator();

            while(var6.hasNext()) {
               ImmutableCard energyCard = (ImmutableCard)var6.next();
               selector.getCardList().add(new CardWithLocation(new CommonCardState(energyCard), false, (BoardLocation)null, 0));
            }

            return selector;
         case 1:
            selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "ability.buzzap.selector.chooseattach");
            PlayerServerState player = server.getPlayer(server.getCurrentTurn());
            if (player.getActiveCard() != null && player.getActiveCard() != pokemon) {
               selector.getCardList().add(new CardWithLocation(player.getActiveCard(), true, BoardLocation.Active, 0));
            }

            for(int i = 0; i < player.getBenchCards().length; ++i) {
               if (player.getBenchCards()[i] != null && player.getBenchCards()[i] != pokemon) {
                  selector.getCardList().add(new CardWithLocation(player.getBenchCards()[i], true, BoardLocation.Bench, i));
               }
            }

            return selector;
         default:
            return null;
      }
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return pokemon.getParameters().size() == 2;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      PokemonCardState attachTo = (PokemonCardState)pokemon.getParameters().get(1);
      pokemon.getStatus().setDamage(pokemon.getHP());
      pokemon.getParameters().remove(1);
      attachTo.getAttachments().add(pokemon);
   }

   public List getEnergyEquivalence(CommonCardState attachment) {
      if (attachment.getParameters().size() == 0) {
         return null;
      } else {
         List result = new ArrayList();

         for(int i = 0; i < 2; ++i) {
            result.add(attachment.getParameters().get(0));
         }

         return result;
      }
   }

   public void cleanUp(PokemonCardState pokemon, GameServerState server) {
   }

   public boolean holdParameters() {
      return true;
   }
}
