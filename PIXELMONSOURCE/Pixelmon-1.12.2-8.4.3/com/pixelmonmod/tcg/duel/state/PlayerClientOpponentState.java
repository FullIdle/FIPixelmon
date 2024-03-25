package com.pixelmonmod.tcg.duel.state;

import io.netty.buffer.ByteBuf;

public class PlayerClientOpponentState extends PlayerCommonState {
   private int deckSize;

   public PlayerClientOpponentState(PlayerServerState player, GamePhase gamePhase, GameServerState server, boolean isOwnData) {
      super(player, server, false);
      this.setDeckSize(player.getDeck().size());
      this.showPokemonsInClient = isOwnData || gamePhase.after(GamePhase.PreMatch);
   }

   public PlayerClientOpponentState(ByteBuf buf, GameServerState server) {
      super(buf, server, false);
      this.deckSize = buf.readInt();
   }

   public void write(ByteBuf buf) {
      super.write(buf);
      buf.writeInt(this.deckSize);
   }

   public int getDeckSize() {
      return this.deckSize;
   }

   public void setDeckSize(int deckSize) {
      this.deckSize = deckSize;
   }
}
