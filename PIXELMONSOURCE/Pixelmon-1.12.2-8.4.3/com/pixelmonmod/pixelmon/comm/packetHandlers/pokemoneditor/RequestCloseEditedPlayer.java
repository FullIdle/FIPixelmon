package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.items.ItemPokemonEditor;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestCloseEditedPlayer implements IMessage {
   UUID editedPlayer;

   public RequestCloseEditedPlayer() {
   }

   public RequestCloseEditedPlayer(UUID editedPlayer) {
      this.editedPlayer = editedPlayer;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.editedPlayer);
   }

   public void fromBytes(ByteBuf buf) {
      this.editedPlayer = PixelmonMethods.fromBytesUUID(buf);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RequestCloseEditedPlayer message, MessageContext ctx) {
         if (!ItemPokemonEditor.checkPermission(ctx.getServerHandler().field_147369_b)) {
            return null;
         } else {
            EntityPlayerMP editedPlayer = ctx.getServerHandler().field_147369_b.func_184102_h().func_184103_al().func_177451_a(message.editedPlayer);
            if (editedPlayer != null) {
               Pixelmon.network.sendTo(new CloseEditedPlayer(), editedPlayer);
            }

            return null;
         }
      }
   }
}
