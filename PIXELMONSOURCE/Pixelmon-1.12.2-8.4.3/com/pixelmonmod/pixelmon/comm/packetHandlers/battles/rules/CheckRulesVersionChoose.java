package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CheckRulesVersionChoose extends CheckRulesVersion {
   public CheckRulesVersionChoose() {
   }

   public CheckRulesVersionChoose(int clauseVersion, DisplayBattleQueryRules packet) {
      super(clauseVersion, packet);
   }

   protected void readPacket(ByteBuf buf) {
      this.packet = new DisplayBattleQueryRules();
      ((DisplayBattleQueryRules)this.packet).fromBytes(buf);
   }

   public void processPacket(MessageContext ctx) {
      (new DisplayBattleQueryRules.Handler()).onMessage(this.packet, ctx);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CheckRulesVersionChoose message, MessageContext ctx) {
         message.onMessage(ctx);
         return null;
      }
   }
}
