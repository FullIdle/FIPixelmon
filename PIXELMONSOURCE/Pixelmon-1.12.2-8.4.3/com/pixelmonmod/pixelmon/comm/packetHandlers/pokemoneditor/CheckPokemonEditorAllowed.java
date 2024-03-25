package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CheckPokemonEditorAllowed implements IMessage {
   UUID editingPlayer;

   public CheckPokemonEditorAllowed() {
   }

   public CheckPokemonEditorAllowed(UUID editingPlayer) {
      this.editingPlayer = editingPlayer;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.editingPlayer);
   }

   public void fromBytes(ByteBuf buf) {
      this.editingPlayer = PixelmonMethods.fromBytesUUID(buf);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CheckPokemonEditorAllowed message, MessageContext ctx) {
         Pixelmon.network.sendToServer(new RespondPokemonEditorAllowed(message.editingPlayer, PixelmonConfig.allowPokemonEditors));
         return null;
      }
   }
}
