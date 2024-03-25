package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiEditedPlayer;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetEditingPlayer implements IMessage {
   UUID editingPlayerUUID;
   String editingPlayerName;

   public SetEditingPlayer() {
   }

   public SetEditingPlayer(UUID editingPlayerUUID, String editingPlayerName) {
      this.editingPlayerUUID = editingPlayerUUID;
      this.editingPlayerName = editingPlayerName;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.editingPlayerUUID);
      ByteBufUtils.writeUTF8String(buf, this.editingPlayerName);
   }

   public void fromBytes(ByteBuf buf) {
      this.editingPlayerUUID = PixelmonMethods.fromBytesUUID(buf);
      this.editingPlayerName = ByteBufUtils.readUTF8String(buf);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetEditingPlayer message, MessageContext ctx) {
         GuiEditedPlayer.editingPlayerUUID = message.editingPlayerUUID;
         GuiEditedPlayer.editingPlayerName = message.editingPlayerName;
         return null;
      }
   }
}
