package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiTeamSelect;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RejectTeamSelect implements IMessage {
   String clauseID;

   public RejectTeamSelect() {
   }

   public RejectTeamSelect(String clauseID) {
      this.clauseID = clauseID;
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.clauseID);
   }

   public void fromBytes(ByteBuf buf) {
      this.clauseID = ByteBufUtils.readUTF8String(buf);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RejectTeamSelect message, MessageContext ctx) {
         GuiScreen screen = Minecraft.func_71410_x().field_71462_r;
         if (screen instanceof GuiTeamSelect) {
            ((GuiTeamSelect)screen).rejectClause = message.clauseID;
         }

         return null;
      }
   }
}
