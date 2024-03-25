package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.network.PacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EssenceSyncRequestPacket implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(EssenceSyncRequestPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         player.func_71121_q().func_152344_a(() -> {
            PacketHandler.net.sendTo(new EssenceSyncPacket(player), player);
         });
         return null;
      }
   }
}
