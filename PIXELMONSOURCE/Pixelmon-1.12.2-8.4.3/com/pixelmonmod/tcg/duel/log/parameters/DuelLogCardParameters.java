package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import io.netty.buffer.ByteBuf;

public class DuelLogCardParameters extends DuelLogParameters {
   private final CommonCardState card;

   public DuelLogCardParameters(CommonCardState card) {
      this.card = card;
   }

   public DuelLogCardParameters(ByteBuf buf) {
      this.card = new CommonCardState(ByteBufTCG.readCard(buf));
   }

   public void write(ByteBuf buf, GamePhase gamePhase, int itemPlayerIndex, int receiverIndex, boolean isMyTurn) {
      if (!gamePhase.after(GamePhase.PreMatch) && itemPlayerIndex != receiverIndex) {
         ByteBufTCG.writeFaceDownCard(buf);
      } else {
         ByteBufTCG.writeCard(buf, this.card.getData());
      }

   }

   public CommonCardState getCard() {
      return this.card;
   }
}
