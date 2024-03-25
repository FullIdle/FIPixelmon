package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelection;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelectionList;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnconfirmTeamSelect implements IMessage {
   int teamSelectID;

   public UnconfirmTeamSelect() {
   }

   public UnconfirmTeamSelect(int teamSelectID) {
      this.teamSelectID = teamSelectID;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.teamSelectID);
   }

   public void fromBytes(ByteBuf buf) {
      this.teamSelectID = buf.readInt();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UnconfirmTeamSelect message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         player.func_184102_h().func_152344_a(() -> {
            TeamSelection ts = TeamSelectionList.getTeamSelection(message.teamSelectID);
            if (ts != null) {
               ts.unregisterTeamSelect(player);
            }
         });
         return null;
      }
   }
}
