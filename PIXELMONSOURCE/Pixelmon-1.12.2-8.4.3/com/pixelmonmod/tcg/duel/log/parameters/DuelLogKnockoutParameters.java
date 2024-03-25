package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import io.netty.buffer.ByteBuf;

public class DuelLogKnockoutParameters extends DuelLogCardParameters {
   public DuelLogKnockoutParameters(PokemonCardState pokemon) {
      super((CommonCardState)pokemon);
   }

   public DuelLogKnockoutParameters(ByteBuf buf) {
      super(buf);
   }

   public void write(ByteBuf buf, GamePhase gamePhase, int itemPlayerIndex, int receiverIndex, boolean isMyTurn) {
      super.write(buf, gamePhase, itemPlayerIndex, receiverIndex, isMyTurn);
   }
}
