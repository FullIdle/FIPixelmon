package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiTrainerEditor;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClearTrainerPokemon implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ClearTrainerPokemon message, MessageContext ctx) {
         GuiTrainerEditor.pokemonList.clear();
         return null;
      }
   }
}
