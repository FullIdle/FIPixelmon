package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonCenter extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return true;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return true;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      if (me.getActiveCard() != null) {
         this.apply(me.getActiveCard());
      }

      PokemonCardState[] var4 = me.getBenchCards();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         PokemonCardState card = var4[var6];
         if (card != null) {
            this.apply(card);
         }
      }

   }

   private void apply(PokemonCardState card) {
      if (card.getStatus().getDamage() > 0) {
         card.getStatus().setDamage(0);
         List energies = (List)card.getAttachments().stream().filter((attachment) -> {
            return attachment.isEnergyEquivalence();
         }).collect(Collectors.toList());
         card.getAttachments().removeAll(energies);
      }

   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}
