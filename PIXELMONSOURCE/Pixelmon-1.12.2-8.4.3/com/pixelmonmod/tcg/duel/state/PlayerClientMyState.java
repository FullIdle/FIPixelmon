package com.pixelmonmod.tcg.duel.state;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PlayerClientMyState extends PlayerCommonState {
   private int deckSize = 0;
   private AvailableActions availableActions = new AvailableActions();
   private CardSelectorState cardSelectorState = null;
   private String customGUI;

   public PlayerClientMyState(PlayerServerState player, GamePhase gamePhase, GameServerState server, boolean isSpectating) {
      super(player, server, true);
      this.deckSize = player.getDeck().size();
      this.availableActions = player.getAvailableActions();
      this.cardSelectorState = player.getCardSelectorState();
      this.customGUI = player.getCustomGUI() == null ? null : player.getCustomGUI().id;
      this.pendingPrizeCount = player.getPendingPrizeCount();
      this.pendingPrizePlayerIndex = player.getPendingPrizePlayerIndex();
      this.showPokemonsInClient = !isSpectating || gamePhase.after(GamePhase.PreMatch);
   }

   public PlayerClientMyState(ByteBuf buf, GameServerState server) {
      super(buf, server, true);
      this.deckSize = buf.readInt();
      this.availableActions = new AvailableActions(buf);
      if (buf.readBoolean()) {
         this.cardSelectorState = new CardSelectorState(buf);
      }

      if (buf.readBoolean()) {
         this.customGUI = ByteBufUtils.readUTF8String(buf);
      }

   }

   public void write(ByteBuf buf) {
      super.write(buf);
      buf.writeInt(this.deckSize);
      this.availableActions.write(buf);
      if (this.isSpectating) {
         buf.writeBoolean(false);
      } else {
         buf.writeBoolean(this.cardSelectorState != null);
         if (this.cardSelectorState != null) {
            this.cardSelectorState.write(buf);
         }
      }

      if (this.isSpectating) {
         buf.writeBoolean(false);
      } else {
         buf.writeBoolean(this.customGUI != null);
         if (this.customGUI != null) {
            ByteBufUtils.writeUTF8String(buf, this.customGUI);
         }
      }

   }

   public int getDeckSize() {
      return this.deckSize;
   }

   public AvailableActions getAvailableActions() {
      return this.availableActions;
   }

   public CardSelectorState getCardSelectorState() {
      return this.cardSelectorState;
   }

   public void setCardSelectorState(CardSelectorState state) {
      this.cardSelectorState = state;
   }

   public int getPendingPrizeCount() {
      return this.pendingPrizeCount;
   }

   public String getCustomGUI() {
      return this.customGUI;
   }
}
