package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestExtrasDisplayData implements IMessage {
   UUID uuid;

   public RequestExtrasDisplayData() {
   }

   public RequestExtrasDisplayData(UUID uuid) {
      this.uuid = uuid;
   }

   public void fromBytes(ByteBuf buf) {
      this.uuid = new UUID(buf.readLong(), buf.readLong());
   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.uuid.getMostSignificantBits());
      buf.writeLong(this.uuid.getLeastSignificantBits());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RequestExtrasDisplayData message, MessageContext ctx) {
         if (PixelExtrasStorage.playerExtras.containsKey(message.uuid)) {
            PixelExtrasData data = (PixelExtrasData)PixelExtrasStorage.playerExtras.get(message.uuid);
            if (data.isReady() && data.hasData()) {
               return new PixelExtrasDisplayPacket(data);
            }
         }

         return null;
      }
   }
}
