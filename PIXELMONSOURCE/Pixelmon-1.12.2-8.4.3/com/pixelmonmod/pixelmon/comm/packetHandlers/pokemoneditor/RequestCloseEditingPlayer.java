package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestCloseEditingPlayer implements IMessage {
   UUID editingPlayer;

   public RequestCloseEditingPlayer() {
   }

   public RequestCloseEditingPlayer(UUID editingPlayer) {
      this.editingPlayer = editingPlayer;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.editingPlayer);
   }

   public void fromBytes(ByteBuf buf) {
      this.editingPlayer = PixelmonMethods.fromBytesUUID(buf);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RequestCloseEditingPlayer message, MessageContext ctx) {
         EntityPlayerMP editingPlayer = ctx.getServerHandler().field_147369_b.func_184102_h().func_184103_al().func_177451_a(message.editingPlayer);
         if (editingPlayer != null) {
            ChatHandler.sendChat(editingPlayer, I18n.func_74838_a("gui.pokemoneditor.cancelediting"), ctx.getServerHandler().field_147369_b.getDisplayNameString());
            Pixelmon.network.sendTo(new CloseEditingPlayer(), editingPlayer);
         }

         return null;
      }
   }
}
