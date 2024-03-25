package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CheckRulesVersionFixed extends CheckRulesVersion {
   public CheckRulesVersionFixed() {
   }

   public CheckRulesVersionFixed(int clauseVersion, ShowTeamSelect packet) {
      super(clauseVersion, packet);
   }

   protected void readPacket(ByteBuf buf) {
      this.packet = new ShowTeamSelect();
      ((ShowTeamSelect)this.packet).fromBytes(buf);
   }

   public void processPacket(MessageContext ctx) {
      (new ShowTeamSelect.Handler()).onMessage(this.packet, ctx);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CheckRulesVersionFixed message, MessageContext ctx) {
         message.onMessage(ctx);
         return null;
      }
   }
}
