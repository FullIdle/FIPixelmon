package com.pixelmonmod.pixelmon.comm.packetHandlers.custom.overlays;

import com.pixelmonmod.pixelmon.client.gui.custom.overlays.CustomScoreboardOverlay;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.ScoreboardLocation;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CustomScoreboardDisplayPacket implements IMessage {
   ScoreboardLocation location;
   boolean enabled;

   public CustomScoreboardDisplayPacket() {
   }

   public CustomScoreboardDisplayPacket(ScoreboardLocation location, boolean enabled) {
      this.location = location;
      this.enabled = enabled;
   }

   public void fromBytes(ByteBuf buf) {
      this.location = ScoreboardLocation.values()[buf.readByte()];
      this.enabled = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.location.ordinal());
      buf.writeBoolean(this.enabled);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CustomScoreboardDisplayPacket message, MessageContext ctx) {
         CustomScoreboardOverlay.setLocation(message.location);
         CustomScoreboardOverlay.setEnabled(message.enabled);
         return null;
      }
   }
}
