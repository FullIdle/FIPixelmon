package com.pixelmonmod.pixelmon.comm.packetHandlers;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestUpdatedPokemonList implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(RequestUpdatedPokemonList message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
      }
   }
}
