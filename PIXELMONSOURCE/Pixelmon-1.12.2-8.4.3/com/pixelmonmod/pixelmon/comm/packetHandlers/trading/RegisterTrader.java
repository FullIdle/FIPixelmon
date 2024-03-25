package com.pixelmonmod.pixelmon.comm.packetHandlers.trading;

import com.pixelmonmod.pixelmon.client.gui.ClientTradingManager;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RegisterTrader implements IMessage {
   UUID uuid;
   boolean targetPartyIsMoreThanOnePokemon;

   public RegisterTrader() {
   }

   public RegisterTrader(UUID uuid, boolean targetPartyIsMoreThanOnePokemon) {
      this.uuid = uuid;
      this.targetPartyIsMoreThanOnePokemon = targetPartyIsMoreThanOnePokemon;
   }

   public void toBytes(ByteBuf buffer) {
      if (this.uuid == null) {
         ByteBufUtils.writeUTF8String(buffer, "");
      } else {
         ByteBufUtils.writeUTF8String(buffer, this.uuid.toString());
      }

      buffer.writeBoolean(this.targetPartyIsMoreThanOnePokemon);
   }

   public void fromBytes(ByteBuf buffer) {
      String uuidString = ByteBufUtils.readUTF8String(buffer);
      if (uuidString != null && !uuidString.isEmpty()) {
         this.uuid = UUID.fromString(uuidString);
      } else {
         this.uuid = null;
      }

      this.targetPartyIsMoreThanOnePokemon = buffer.readBoolean();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RegisterTrader message, MessageContext ctx) {
         ClientTradingManager.findTradePartner(message.uuid);
         ClientTradingManager.targetPartyIsMoreThanOnePokemon = message.targetPartyIsMoreThanOnePokemon;
         return null;
      }
   }
}
