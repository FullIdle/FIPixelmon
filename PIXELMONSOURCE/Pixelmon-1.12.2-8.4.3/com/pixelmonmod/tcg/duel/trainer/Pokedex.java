package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Pokedex extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return client.getMe().getDeckSize() > 0;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      int count = Math.min(5, server.getPlayer(server.getCurrentTurn()).getDeck().size());
      int skip = trainer.getParameters().size();
      List top = new ArrayList();

      for(int i = 0; i < count; ++i) {
         top.add(new CommonCardState((ImmutableCard)server.getPlayer(server.getCurrentTurn()).getDeck().get(i)));
      }

      Iterator var9 = trainer.getParameters().iterator();

      while(var9.hasNext()) {
         CommonCardState selected = (CommonCardState)var9.next();
         CommonCardState match = (CommonCardState)top.stream().filter((c) -> {
            return c.getData() == selected.getData();
         }).findFirst().get();
         top.remove(match);
      }

      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "trainer.pokedex.selector." + Integer.toString(skip + 1));
      selector.getCardList().addAll((Collection)top.stream().map((c) -> {
         return new CardWithLocation(c, true, BoardLocation.Deck, 0);
      }).collect(Collectors.toList()));
      return selector;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == Math.min(5, server.getPlayer(server.getCurrentTurn()).getDeck().size());
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      int index = 0;
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());

      for(Iterator var5 = trainer.getParameters().iterator(); var5.hasNext(); ++index) {
         CommonCardState card = (CommonCardState)var5.next();
         player.getDeck().remove(index);
         player.getDeck().add(index, card.getData());
      }

   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}
