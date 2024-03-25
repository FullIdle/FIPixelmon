package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ExitBattle implements IMessage {
   public void toBytes(ByteBuf buffer) {
   }

   public void fromBytes(ByteBuf buffer) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ExitBattle message, MessageContext ctx) {
         ClientProxy.battleManager.battleEnded = true;
         return null;
      }
   }
}
