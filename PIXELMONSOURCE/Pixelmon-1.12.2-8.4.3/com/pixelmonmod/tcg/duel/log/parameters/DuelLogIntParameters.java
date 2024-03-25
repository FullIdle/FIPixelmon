package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.duel.state.GamePhase;
import io.netty.buffer.ByteBuf;

public class DuelLogIntParameters extends DuelLogParameters {
   private final int number;

   public DuelLogIntParameters(int number) {
      this.number = number;
   }

   public DuelLogIntParameters(ByteBuf buf) {
      this.number = buf.readInt();
   }

   public void write(ByteBuf buf, GamePhase gamePhase, int itemPlayerIndex, int receiverIndex, boolean isMyTurn) {
      buf.writeInt(this.number);
   }

   public int getNumber() {
      return this.number;
   }
}
