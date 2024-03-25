package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.tools.Quadstate;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DynamaxMegaRule implements IMessage {
   Quadstate oldGen;

   public DynamaxMegaRule() {
   }

   public DynamaxMegaRule(Quadstate oldGen) {
      this.oldGen = oldGen;
   }

   public void toBytes(ByteBuf buf) {
      this.oldGen.writeToBuffer(buf);
   }

   public void fromBytes(ByteBuf buf) {
      this.oldGen = Quadstate.readFromBuffer(buf);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(DynamaxMegaRule message, MessageContext ctx) {
         ClientProxy.battleManager.oldGen = message.oldGen;
         return null;
      }
   }
}
