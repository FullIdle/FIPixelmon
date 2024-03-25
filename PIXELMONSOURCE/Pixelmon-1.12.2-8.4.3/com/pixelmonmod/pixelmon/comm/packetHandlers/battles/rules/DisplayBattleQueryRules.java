package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiBattleRulesPlayer;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DisplayBattleQueryRules implements IMessage {
   int queryIndex;
   boolean isProposing;

   public DisplayBattleQueryRules() {
   }

   public DisplayBattleQueryRules(int queryIndex, boolean isProposing) {
      this.queryIndex = queryIndex;
      this.isProposing = isProposing;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.queryIndex);
      buf.writeBoolean(this.isProposing);
   }

   public void fromBytes(ByteBuf buf) {
      this.queryIndex = buf.readInt();
      this.isProposing = buf.readBoolean();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(DisplayBattleQueryRules message, MessageContext ctx) {
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(DisplayBattleQueryRules message) {
         Minecraft.func_71410_x().func_147108_a(new GuiBattleRulesPlayer(message.queryIndex, message.isProposing));
      }
   }
}
