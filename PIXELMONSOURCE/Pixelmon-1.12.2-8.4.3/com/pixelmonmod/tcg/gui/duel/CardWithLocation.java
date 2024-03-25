package com.pixelmonmod.tcg.gui.duel;

import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import io.netty.buffer.ByteBuf;

public class CardWithLocation {
   protected CommonCardState card;
   protected boolean isMine;
   protected BoardLocation location;
   protected int locationSubIndex;

   public CardWithLocation() {
   }

   public CardWithLocation(CommonCardState card, boolean isMine, BoardLocation location, int locationSubIndex) {
      this.set(card, isMine, location, locationSubIndex);
   }

   public CardWithLocation(ByteBuf buf) {
      this.card = ByteBufTCG.readCardState(buf);
      this.isMine = buf.readBoolean();
      int locationIndex = buf.readInt();
      if (locationIndex == -1) {
         this.location = null;
      } else {
         this.location = BoardLocation.values()[locationIndex];
      }

      this.locationSubIndex = buf.readInt();
   }

   public void write(ByteBuf buf) {
      ByteBufTCG.writeCardState(buf, this.card);
      buf.writeBoolean(this.isMine);
      if (this.location == null) {
         buf.writeInt(-1);
      } else {
         buf.writeInt(this.location.ordinal());
      }

      buf.writeInt(this.locationSubIndex);
   }

   public void set(CommonCardState card, boolean isMine, BoardLocation location, int locationSubIndex) {
      this.isMine = isMine;
      this.card = card;
      this.location = location;
      this.locationSubIndex = locationSubIndex;
   }

   public boolean isMine() {
      return this.isMine;
   }

   public CommonCardState getCard() {
      return this.card;
   }

   public BoardLocation getLocation() {
      return this.location;
   }

   public int getLocationSubIndex() {
      return this.locationSubIndex;
   }
}
