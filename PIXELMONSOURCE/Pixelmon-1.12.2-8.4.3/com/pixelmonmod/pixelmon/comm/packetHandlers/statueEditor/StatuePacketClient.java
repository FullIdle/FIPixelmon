package com.pixelmonmod.pixelmon.comm.packetHandlers.statueEditor;

import com.pixelmonmod.pixelmon.client.gui.statueEditor.GuiStatueEditor;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StatuePacketClient implements IMessage {
   UUID id;

   public StatuePacketClient() {
   }

   public StatuePacketClient(UUID id) {
      this.id = id;
   }

   public void fromBytes(ByteBuf buf) {
      this.id = new UUID(buf.readLong(), buf.readLong());
   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.id.getMostSignificantBits());
      buf.writeLong(this.id.getLeastSignificantBits());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(StatuePacketClient message, MessageContext ctx) {
         GuiStatueEditor.statueId = message.id;
         return null;
      }
   }
}
