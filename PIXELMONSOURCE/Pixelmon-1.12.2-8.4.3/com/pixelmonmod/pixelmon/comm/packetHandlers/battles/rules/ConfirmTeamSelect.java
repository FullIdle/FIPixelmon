package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelection;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelectionList;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ConfirmTeamSelect implements IMessage {
   int teamSelectID;
   int[] selection;
   boolean force;

   public ConfirmTeamSelect() {
   }

   public ConfirmTeamSelect(int teamSelectID, int[] selection, boolean force) {
      this.teamSelectID = teamSelectID;
      this.selection = selection;
      this.force = force;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.teamSelectID);
      int[] var2 = this.selection;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int s = var2[var4];
         buf.writeInt(s);
      }

      buf.writeBoolean(this.force);
   }

   public void fromBytes(ByteBuf buf) {
      this.teamSelectID = buf.readInt();
      this.selection = new int[6];

      for(int i = 0; i < this.selection.length; ++i) {
         this.selection[i] = buf.readInt();
      }

      this.force = buf.readBoolean();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ConfirmTeamSelect message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         TeamSelection ts = TeamSelectionList.getTeamSelection(message.teamSelectID);
         if (ts == null) {
            Pixelmon.network.sendTo(new CancelTeamSelect(), player);
         } else {
            ts.registerTeamSelect(player, message.selection, message.force);
         }

      }
   }
}
