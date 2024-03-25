package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Dynamax implements IMessage {
   UUID pokemonUUID;
   boolean gigantamax;

   public Dynamax() {
   }

   public Dynamax(UUID pokemonUUID, boolean gigantamax) {
      this.pokemonUUID = pokemonUUID;
      this.gigantamax = gigantamax;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.pokemonUUID);
      buf.writeBoolean(this.gigantamax);
   }

   public void fromBytes(ByteBuf buf) {
      this.pokemonUUID = new UUID(buf.readLong(), buf.readLong());
      this.gigantamax = buf.readBoolean();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(Dynamax message, MessageContext ctx) {
         ClientProxy.battleManager.gigantamax = message.gigantamax;
         ClientProxy.battleManager.dynamax = message.pokemonUUID;
         return null;
      }
   }
}
