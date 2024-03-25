package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import io.netty.buffer.ByteBuf;

public class TrainerCardState extends CommonCardState {
   public TrainerCardState(ImmutableCard data) {
      super(data);
   }

   public TrainerCardState(ByteBuf buf) {
      super(buf);
   }

   public void handleEndTurn(PokemonCardState card, PlayerServerState player, GameServerState server) {
      super.handleEndTurn(card, player, server);
      if (this.data.getEffect() != null) {
         this.data.getEffect().handleEndTurn(this, card, player, server);
      }

   }
}
