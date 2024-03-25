package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class MrFuji extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return client.getMe().getActiveAndBenchCards().size() > 1;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 1;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      return SelectorHelper.generateSelectorForBench(server.getPlayer(server.getCurrentTurn()), "card.FOSS58.effect.selector");
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());

      for(int i = 0; i < me.getBenchCards().length; ++i) {
         if (me.getBenchCards()[i] == trainer.getParameters().get(0)) {
            me.getDeck().add(me.getBenchCards()[i].getData());
            me.getBenchCards()[i].getAttachments().stream().forEach((attach) -> {
               me.getDeck().add(attach.getData());
            });
            me.getBenchCards()[i].getAttachments().clear();
            me.getBenchCards()[i] = null;
            LogicHelper.shuffleCardList(me.getDeck());
         }
      }

   }

   public boolean canPlaceOn(CardWithLocation card) {
      return card != null && card.getCard() != null && card.isMine() && card.getLocation() == BoardLocation.Bench;
   }

   public boolean canSkipSelector() {
      return true;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());

      for(int i = 0; i < me.getBenchCards().length; ++i) {
         if (me.getBenchCards()[i] == pokemon) {
            me.getDeck().add(me.getBenchCards()[i].getData());
            me.getBenchCards()[i].getAttachments().stream().forEach((attach) -> {
               me.getDeck().add(attach.getData());
            });
            me.getBenchCards()[i].getAttachments().clear();
            me.getBenchCards()[i] = null;
            LogicHelper.shuffleCardList(me.getDeck());
         }
      }

   }
}
