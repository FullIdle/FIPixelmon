package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiImportExport;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPokemonEditorAdvanced;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPokemonEditorIndividual;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPokemonEditorParty;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CloseEditingPlayer implements IMessage {
   public void toBytes(ByteBuf buf) {
   }

   public void fromBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CloseEditingPlayer message, MessageContext ctx) {
         GuiScreen currentScreen = Minecraft.func_71410_x().field_71462_r;
         if (currentScreen instanceof GuiPokemonEditorParty || currentScreen instanceof GuiPokemonEditorIndividual || currentScreen instanceof GuiPokemonEditorAdvanced || currentScreen instanceof GuiImportExport) {
            GuiHelper.closeScreen();
         }

         return null;
      }
   }
}
