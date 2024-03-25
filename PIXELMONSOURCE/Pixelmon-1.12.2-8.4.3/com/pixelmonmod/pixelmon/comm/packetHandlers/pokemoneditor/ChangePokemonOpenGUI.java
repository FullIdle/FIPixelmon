package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPartyEditorBase;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangePokemonOpenGUI implements IMessage {
   int slot;

   public ChangePokemonOpenGUI() {
   }

   public ChangePokemonOpenGUI(int slot) {
      this.slot = slot;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.slot);
   }

   public void fromBytes(ByteBuf buf) {
      this.slot = buf.readInt();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ChangePokemonOpenGUI message, MessageContext ctx) {
         GuiPartyEditorBase.editPokemonPacket(message.slot);
         return null;
      }
   }
}
