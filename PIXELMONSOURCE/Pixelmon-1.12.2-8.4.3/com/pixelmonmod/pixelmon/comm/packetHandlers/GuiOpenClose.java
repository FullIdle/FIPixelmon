package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GuiOpenClose implements IMessage {
   private boolean open;

   public GuiOpenClose() {
   }

   public GuiOpenClose(boolean open) {
      this.open = open;
   }

   public void fromBytes(ByteBuf buffer) {
      this.open = buffer.readBoolean();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBoolean(this.open);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(GuiOpenClose message, MessageContext ctx) {
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(ctx.getServerHandler().field_147369_b);
         party.guiOpened = message.open;
      }
   }
}
