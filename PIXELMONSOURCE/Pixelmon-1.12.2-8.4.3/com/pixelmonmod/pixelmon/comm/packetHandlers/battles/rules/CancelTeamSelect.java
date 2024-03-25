package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiBattleRulesPlayer;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiTeamSelect;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CancelTeamSelect implements IMessage {
   public void toBytes(ByteBuf buf) {
   }

   public void fromBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CancelTeamSelect message, MessageContext ctx) {
         GuiScreen screen = Minecraft.func_71410_x().field_71462_r;
         if (screen instanceof GuiTeamSelect || screen instanceof GuiBattleRulesPlayer) {
            GuiHelper.closeScreen();
         }

         return null;
      }
   }
}
