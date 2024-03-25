package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ProphecyEffect extends BaseAttackEffect {
   private static final String CODE = "PROPHECY";
   private int count;

   public ProphecyEffect() {
      super("PROPHECY");
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      PlayerServerState player;
      if (parameters.size() == 0) {
         player = server.getPlayer(server.getCurrentTurn());
         PlayerServerState opp = server.getPlayer(server.getNextTurn());
         return SelectorHelper.generateSelectorForPlayer(player, opp, "Choose a player");
      } else if (parameters.size() < 1) {
         return null;
      } else {
         player = server.getPlayer(server.getCurrentTurn());
         CommonCardState playerChoice = (CommonCardState)parameters.get(0);
         if (player.getActiveCard() != playerChoice) {
            player = server.getPlayer(server.getNextTurn());
         }

         int count = Math.min(this.count, player.getDeck().size());
         int skip = parameters.size() - 1;
         List top = new ArrayList();

         int i;
         for(i = 0; i < count; ++i) {
            top.add(new CommonCardState((ImmutableCard)player.getDeck().get(i)));
         }

         for(i = 0; i < parameters.size(); ++i) {
            if (i != 0) {
               CommonCardState selected = (CommonCardState)parameters.get(i);
               CommonCardState match = (CommonCardState)top.stream().filter((c) -> {
                  return c.getData() == selected.getData();
               }).findFirst().get();
               top.remove(match);
            }
         }

         CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "trainer.pokedex.selector." + Integer.toString(skip + 1));
         selector.getCardList().addAll((Collection)top.stream().map((c) -> {
            return new CardWithLocation(c, true, BoardLocation.Deck, 0);
         }).collect(Collectors.toList()));
         return selector;
      }
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      return parameters.size() - 1 == Math.min(this.count, server.getPlayer(server.getCurrentTurn()).getDeck().size());
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      PokemonCardState playerChoice = (PokemonCardState)parameters.get(0);
      if (player.getActiveCard() != playerChoice) {
         player = server.getPlayer(server.getNextTurn());
      }

      parameters.remove(0);
      int index = 0;

      for(Iterator var9 = parameters.iterator(); var9.hasNext(); ++index) {
         Object cardObject = var9.next();
         CommonCardState cardData = (CommonCardState)cardObject;
         player.getDeck().remove(index);
         player.getDeck().add(index, cardData.getData());
      }

   }

   public BaseAttackEffect parse(String... args) {
      this.count = Integer.parseInt(args[1]);
      return super.parse(args);
   }
}
