package com.pixelmonmod.pixelmon.comm.packetHandlers.evolution;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQueryList;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EvolutionResponse implements IMessage {
   boolean accept;
   UUID pokemonUUID;
   EvolutionResponseMode mode;

   public EvolutionResponse() {
   }

   public EvolutionResponse(UUID pokemonUUID, boolean accept) {
      this.pokemonUUID = pokemonUUID;
      this.accept = accept;
      this.mode = EvolutionResponse.EvolutionResponseMode.Message;
   }

   public EvolutionResponse(UUID pokemonUUID) {
      this.mode = EvolutionResponse.EvolutionResponseMode.SpawnPokemon;
      this.pokemonUUID = pokemonUUID;
   }

   public void fromBytes(ByteBuf buffer) {
      this.mode = EvolutionResponse.EvolutionResponseMode.values()[buffer.readInt()];
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      if (this.mode == EvolutionResponse.EvolutionResponseMode.Message) {
         this.accept = buffer.readBoolean();
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.mode.ordinal());
      PixelmonMethods.toBytesUUID(buffer, this.pokemonUUID);
      if (this.mode == EvolutionResponse.EvolutionResponseMode.Message) {
         buffer.writeBoolean(this.accept);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(EvolutionResponse message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
            if (message.mode == EvolutionResponse.EvolutionResponseMode.Message) {
               if (message.accept) {
                  EvolutionQueryList.acceptQuery(ctx.getServerHandler().field_147369_b, message.pokemonUUID);
               } else {
                  EvolutionQueryList.declineQuery(ctx.getServerHandler().field_147369_b, message.pokemonUUID);
               }
            } else {
               EvolutionQueryList.spawnPokemon(ctx.getServerHandler().field_147369_b, message.pokemonUUID);
            }

         });
         return null;
      }
   }

   static enum EvolutionResponseMode {
      Message,
      SpawnPokemon;
   }
}
