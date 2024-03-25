package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FormBattleUpdate implements IMessage {
   UUID uuid;
   short form;

   public FormBattleUpdate() {
   }

   public FormBattleUpdate(UUID uuid, int form) {
      this.uuid = uuid;
      this.form = (short)form;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.uuid);
      buf.writeShort(this.form);
   }

   public void fromBytes(ByteBuf buf) {
      this.uuid = new UUID(buf.readLong(), buf.readLong());
      this.form = buf.readShort();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(FormBattleUpdate message, MessageContext ctx) {
         PixelmonInGui pokemon = ClientProxy.battleManager.getPokemon(message.uuid);
         if (pokemon != null && !pokemon.isSwitchingOut && message.form != -1) {
            pokemon.form = message.form;
         }

         return null;
      }
   }
}
