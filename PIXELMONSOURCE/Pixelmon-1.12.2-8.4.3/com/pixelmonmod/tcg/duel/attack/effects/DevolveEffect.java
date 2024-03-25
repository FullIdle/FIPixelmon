package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.Iterator;
import java.util.List;

public class DevolveEffect extends BaseAttackEffect {
   private static final String CODE = "DEVOLVE";
   private int mode;

   public DevolveEffect() {
      super("DEVOLVE");
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      return parameters.isEmpty() ? this.getCardSelectorStateForEvolutionCard(new GameClientState(server)) : null;
   }

   private CardSelectorState getCardSelectorStateForEvolutionCard(GameClientState client) {
      CardSelectorState selectorState = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "card.BS72.effect.selector.choosepokemon");
      PlayerClientMyState player = client.getMe();
      if (player.getActiveCard().getData().isEvolution() && this.hasLowerEvolution(player.getActiveCard().getData().getPrevEvoID(), player.getActiveCard().getAttachments())) {
         selectorState.getCardList().add(new CardWithLocation(player.getActiveCard(), true, BoardLocation.Active, 0));
      }

      for(int i = 0; i < player.getBenchCards().length; ++i) {
         if (player.getBenchCards()[i] != null && player.getBenchCards()[i].getData().isEvolution() && this.hasLowerEvolution(player.getBenchCards()[i].getData().getPrevEvoID(), player.getBenchCards()[i].getAttachments())) {
            selectorState.getCardList().add(new CardWithLocation(player.getBenchCards()[i], true, BoardLocation.Bench, 0));
         }
      }

      PlayerClientOpponentState opp = client.getOpponent();
      if (opp.getActiveCard().getData().isEvolution() && this.hasLowerEvolution(opp.getActiveCard().getData().getPrevEvoID(), opp.getActiveCard().getAttachments())) {
         selectorState.getCardList().add(new CardWithLocation(opp.getActiveCard(), false, BoardLocation.Active, 0));
      }

      for(int i = 0; i < opp.getBenchCards().length; ++i) {
         if (opp.getBenchCards()[i] != null && opp.getBenchCards()[i].getData().isEvolution() && this.hasLowerEvolution(opp.getBenchCards()[i].getData().getPrevEvoID(), opp.getBenchCards()[i].getAttachments())) {
            selectorState.getCardList().add(new CardWithLocation(opp.getBenchCards()[i], false, BoardLocation.Bench, 0));
         }
      }

      return selectorState;
   }

   private boolean hasLowerEvolution(int pokemonId, List attachments) {
      return this.getLowerEvolution(pokemonId, attachments) != null;
   }

   private PokemonCardState getLowerEvolution(int pokemonId, List attachments) {
      Iterator var3 = attachments.iterator();

      CommonCardState attachment;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         attachment = (CommonCardState)var3.next();
      } while(!(attachment instanceof PokemonCardState) || attachment.isEnergyEquivalence() || attachment.getData().getPokemonID() != pokemonId);

      return (PokemonCardState)attachment;
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      return !parameters.isEmpty() || this.getCardSelectorStateForEvolutionCard(new GameClientState(server)).getCardList().isEmpty();
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (parameters != null && !parameters.isEmpty()) {
         PlayerServerState[] var5 = server.getPlayers();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            PlayerServerState player = var5[var7];
            PokemonCardState active = player.getActiveCard();
            PokemonCardState cycle;
            if (parameters.contains(active)) {
               PokemonCardState prevEvo = this.getLowerEvolution(active.getData().getPrevEvoID(), active.getAttachments());
               if (prevEvo != null) {
                  cycle = new PokemonCardState(prevEvo.getData(), server.getCurrentTurn());
                  cycle.getAttachments().addAll(active.getAttachments());
                  cycle.getAttachments().remove(prevEvo);
                  cycle.setStatus(active.getStatus());
                  player.getHand().add(active.getData());
                  player.setActiveCard(cycle);
                  active.getStatus().getConditions().clear();
                  return;
               }
            }

            for(int i = 0; i < player.getBenchCards().length; ++i) {
               cycle = player.getBenchCards()[i];
               if (parameters.contains(cycle)) {
                  PokemonCardState prevEvo = this.getLowerEvolution(player.getBenchCards()[i].getData().getPrevEvoID(), player.getBenchCards()[i].getAttachments());
                  if (prevEvo != null) {
                     PokemonCardState newCard = new PokemonCardState(prevEvo.getData(), server.getCurrentTurn());
                     newCard.getAttachments().addAll(player.getBenchCards()[i].getAttachments());
                     newCard.getAttachments().remove(prevEvo);
                     newCard.setStatus(player.getBenchCards()[i].getStatus());
                     player.getHand().add(player.getBenchCards()[i].getData());
                     player.getBenchCards()[i] = newCard;
                     player.getBenchCards()[i].getStatus().getConditions().clear();
                     return;
                  }
               }
            }
         }

      }
   }

   public BaseAttackEffect parse(String... args) {
      this.mode = Integer.parseInt(args[1]);
      return super.parse(args);
   }
}
