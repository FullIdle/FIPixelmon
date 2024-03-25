package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class PlusPower extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return true;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return true;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      server.getPlayer(server.getCurrentTurn()).getActiveCard().getAttachments().add(trainer);
   }

   public int modifyDamage(int damage, TrainerCardState trainer, GameServerState server) {
      return server.getPlayer(server.getCurrentTurn()).getActiveCard().getAttachments().contains(trainer) && damage > 0 ? damage + 10 : damage;
   }

   public boolean preventDiscard() {
      return true;
   }

   public void handleEndTurn(TrainerCardState trainer, PokemonCardState card, PlayerServerState player, GameServerState server) {
      card.getAttachments().remove(trainer);
      player.getDiscardPile().add(trainer.getData());
   }

   public boolean canPlaceOn(CardWithLocation card) {
      return card != null && card.getCard() != null && card.isMine() && card.getLocation() == BoardLocation.Active;
   }

   public boolean canSkipSelector() {
      return true;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
      server.getPlayer(server.getCurrentTurn()).getActiveCard().getAttachments().add(trainer);
   }
}
