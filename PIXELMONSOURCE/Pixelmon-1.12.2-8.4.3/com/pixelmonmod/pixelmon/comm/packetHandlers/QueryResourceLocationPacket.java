package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class QueryResourceLocationPacket implements IMessage {
   UUID query;
   ResourceLocation resource;

   public QueryResourceLocationPacket() {
   }

   public QueryResourceLocationPacket(UUID query, ResourceLocation resource) {
      this.query = query;
      this.resource = resource;
   }

   public void fromBytes(ByteBuf buf) {
      this.query = UUIDHelper.readUUID(buf);
      this.resource = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
   }

   public void toBytes(ByteBuf buf) {
      UUIDHelper.writeUUID(this.query, buf);
      ByteBufUtils.writeUTF8String(buf, this.resource.toString());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(QueryResourceLocationPacket message, MessageContext ctx) {
         return new QueryResourceLocationResult(message.query, Pixelmon.proxy.resourceLocationExists(message.resource));
      }
   }
}
