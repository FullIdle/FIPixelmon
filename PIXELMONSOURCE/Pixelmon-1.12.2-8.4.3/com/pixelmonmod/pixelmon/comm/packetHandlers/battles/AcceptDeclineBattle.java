package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.battles.BattleQuery;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AcceptDeclineBattle implements IMessage {
   EnumBattleQueryResponse response;
   int queryID;

   public AcceptDeclineBattle() {
   }

   public AcceptDeclineBattle(int queryID, EnumBattleQueryResponse response) {
      this.queryID = queryID;
      this.response = response;
   }

   public void fromBytes(ByteBuf buffer) {
      this.queryID = buffer.readInt();
      this.response = EnumBattleQueryResponse.getFromOrdinal(buffer.readInt());
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.queryID);
      buffer.writeInt(this.response.ordinal());
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(AcceptDeclineBattle message, MessageContext ctx) {
         BattleQuery query = BattleQuery.getQuery(message.queryID);
         if (query != null) {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            player.func_143004_u();
            switch (message.response) {
               case Decline:
                  query.declineQuery(player);
                  break;
               case Accept:
               case Rules:
                  query.acceptQuery(player, message.response);
                  break;
               case Change:
                  query.changeRules(player);
            }

         }
      }
   }
}
