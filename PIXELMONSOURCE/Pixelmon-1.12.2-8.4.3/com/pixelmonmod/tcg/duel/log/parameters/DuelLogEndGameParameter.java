package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.duel.state.GamePhase;
import io.netty.buffer.ByteBuf;

public class DuelLogEndGameParameter extends DuelLogParameters {
   private final int winnerIndex;

   public DuelLogEndGameParameter(int winnerIndex) {
      this.winnerIndex = winnerIndex;
   }

   public DuelLogEndGameParameter(ByteBuf buf) {
      this.winnerIndex = buf.readInt();
   }

   public void write(ByteBuf buf, GamePhase gamePhase, int itemPlayerIndex, int receiverIndex, boolean isMyTurn) {
      buf.writeInt(this.winnerIndex);
   }
}
