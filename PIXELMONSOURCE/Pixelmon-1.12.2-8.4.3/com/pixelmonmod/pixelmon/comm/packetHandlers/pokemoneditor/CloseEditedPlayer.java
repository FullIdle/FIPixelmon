package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiEditedPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CloseEditedPlayer implements IMessage {
   public void toBytes(ByteBuf buf) {
   }

   public void fromBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CloseEditedPlayer message, MessageContext ctx) {
         GuiScreen currentScreen = Minecraft.func_71410_x().field_71462_r;
         if (currentScreen instanceof GuiEditedPlayer) {
            ((GuiEditedPlayer)currentScreen).markForceClose();
            GuiHelper.closeScreen();
         }

         return null;
      }
   }
}
