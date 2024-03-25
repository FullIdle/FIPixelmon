package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateWeather implements IMessage {
   private StatusType weather;

   public UpdateWeather() {
   }

   public UpdateWeather(StatusType weather) {
      this.weather = weather;
   }

   public void fromBytes(ByteBuf buf) {
      if (buf.readBoolean()) {
         this.weather = StatusType.values()[buf.readInt()];
      } else {
         this.weather = null;
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.weather != null);
      if (this.weather != null) {
         buf.writeInt(this.weather.ordinal());
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UpdateWeather message, MessageContext ctx) {
         ClientProxy.battleManager.weather = message.weather;
      }
   }
}
