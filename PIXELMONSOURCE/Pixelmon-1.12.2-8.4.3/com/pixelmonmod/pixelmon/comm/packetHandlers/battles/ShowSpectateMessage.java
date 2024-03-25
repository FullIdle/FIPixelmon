package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ShowSpectateMessage implements IMessage {
   UUID uuid;

   public ShowSpectateMessage() {
   }

   public ShowSpectateMessage(UUID uuid) {
      this.uuid = uuid;
   }

   public void fromBytes(ByteBuf buffer) {
      this.uuid = new UUID(buffer.readLong(), buffer.readLong());
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.uuid.getMostSignificantBits());
      buffer.writeLong(this.uuid.getLeastSignificantBits());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ShowSpectateMessage message, MessageContext ctx) {
         GuiPixelmonOverlay.showSpectateMessage(message.uuid);
         return null;
      }
   }
}
