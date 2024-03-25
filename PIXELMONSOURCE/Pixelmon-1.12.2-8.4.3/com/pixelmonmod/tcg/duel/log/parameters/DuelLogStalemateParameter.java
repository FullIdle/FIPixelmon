package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.duel.state.GamePhase;
import io.netty.buffer.ByteBuf;

public class DuelLogStalemateParameter extends DuelLogParameters {
   public DuelLogStalemateParameter() {
   }

   public DuelLogStalemateParameter(ByteBuf buf) {
   }

   public void write(ByteBuf buf, GamePhase gamePhase, int itemPlayerIndex, int receiverIndex, boolean isMyTurn) {
   }
}
