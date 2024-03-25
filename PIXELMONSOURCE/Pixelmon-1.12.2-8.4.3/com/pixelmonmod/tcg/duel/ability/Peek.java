package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.dto.CustomGUI;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.CustomGUIResult;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class Peek extends BaseAbilityEffect {
   public Peek() {
      super("Peek");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      return super.isEnabled(pokemon, client) && pokemon.getParameters().isEmpty();
   }

   public CustomGUI getCustomGUI(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      if (player.getCustomGUIResult() != null && player.getCustomGUIResult().getResult() != null && player.getCustomGUIResult().getResult().length != 0) {
         return null;
      } else {
         CustomGUIResult defaultResult = new CustomGUIResult();
         defaultResult.setOpened(true);
         defaultResult.setResult(new int[]{1});
         return new CustomGUI("GUI_PEEK", defaultResult);
      }
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      if (player.getCustomGUIResult() != null && player.getCustomGUIResult().getResult() != null && player.getCustomGUIResult().getResult().length != 0) {
         player.setCustomGUI((CustomGUI)null);
         int buttonIndex = player.getCustomGUIResult().getResult()[0];
         if (buttonIndex != 4 && buttonIndex != 5) {
            pokemon.getParameters().add(pokemon);
            return true;
         } else {
            int prizePlayerIndex = buttonIndex == 4 ? server.getCurrentTurn() : server.getNextTurn();
            if (player.getOpeningPrizeIndex() >= 0) {
               pokemon.getParameters().add(new CommonCardState(server.getPlayer(prizePlayerIndex).getPrizeCards()[player.getOpeningPrizeIndex()]));
               player.setOpeningPrizeIndex(-1);
               player.setPendingPrizeCount(prizePlayerIndex, 0);
               return true;
            } else {
               player.setPendingPrizeCount(prizePlayerIndex, 1);
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
   }

   public void cleanUp(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      CardSelectorState selector = new CardSelectorState(0, 0, CardSelectorDisplay.Reveal, false, "ability.JG55.name");
      int[] result = me.getCustomGUIResult().getResult();
      switch (result[0]) {
         case 1:
            selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)me.getDeck().get(0)), true, BoardLocation.Deck, 0));
            break;
         case 2:
            selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)opp.getDeck().get(0)), false, BoardLocation.Deck, 0));
            break;
         case 3:
            if (!opp.getHand().isEmpty()) {
               int index = RandomHelper.rand.nextInt(opp.getHand().size());
               selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)opp.getHand().get(index)), false, BoardLocation.Hand, index));
            }
            break;
         case 4:
         case 5:
            selector.getCardList().add(new CardWithLocation((CommonCardState)pokemon.getParameters().get(0), true, BoardLocation.Prize, 0));
      }

      me.setCardSelectorState(selector);
      me.setCustomGUIResult((CustomGUIResult)null);
   }
}
