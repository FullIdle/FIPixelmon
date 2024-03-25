package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import io.netty.buffer.ByteBuf;

public class DuelLogEvolveParameters extends DuelLogParameters {
   public final CommonCardState from;
   public final CommonCardState to;

   public DuelLogEvolveParameters(PokemonCardState from, PokemonCardState to) {
      this.from = from;
      this.to = to;
   }

   public DuelLogEvolveParameters(ByteBuf buf) {
      this.from = new CommonCardState(ByteBufTCG.readCard(buf));
      this.to = new CommonCardState(ByteBufTCG.readCard(buf));
   }

   public void write(ByteBuf buf, GamePhase gamePhase, int itemPlayerIndex, int receiverIndex, boolean isMyTurn) {
      ByteBufTCG.writeCard(buf, this.from.getData());
      ByteBufTCG.writeCard(buf, this.to.getData());
   }

   public CommonCardState getFrom() {
      return this.from;
   }

   public CommonCardState getTo() {
      return this.to;
   }
}
