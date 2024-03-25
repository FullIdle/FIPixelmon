package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.battles.BattleQuery;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProposeBattleRules implements IMessage {
   int queryID;
   BattleRules rules;

   public ProposeBattleRules() {
   }

   public ProposeBattleRules(int queryID, BattleRules rules) {
      this.queryID = queryID;
      this.rules = rules;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.queryID);
      this.rules.encodeInto(buf);
   }

   public void fromBytes(ByteBuf buf) {
      this.queryID = buf.readInt();
      this.rules = new BattleRules(buf);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ProposeBattleRules message, MessageContext ctx) {
         BattleQuery query = BattleQuery.getQuery(message.queryID);
         if (query == null) {
            return null;
         } else {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            query.proposeRules(player, message.rules);
            return null;
         }
      }
   }
}
