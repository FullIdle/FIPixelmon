package com.pixelmonmod.pixelmon.comm.packetHandlers.evolution;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EvolvePokemon implements IMessage {
   UUID pokemonUUID;
   EvolutionStage stage;

   public EvolvePokemon() {
   }

   public EvolvePokemon(UUID pokemonUUID, EvolutionStage stage) {
      this.pokemonUUID = pokemonUUID;
      this.stage = stage;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.stage = EvolutionStage.values()[buffer.readInt()];
   }

   public void toBytes(ByteBuf buffer) {
      PixelmonMethods.toBytesUUID(buffer, this.pokemonUUID);
      buffer.writeInt(this.stage.ordinal());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(EvolvePokemon message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            this.evolvePokemon(message);
         });
         return null;
      }

      private void evolvePokemon(EvolvePokemon message) {
         EntityPixelmon pixelmon = GuiHelper.getEntity(message.pokemonUUID);
         if (pixelmon != null) {
            pixelmon.setEvolutionAnimationStage(message.stage);
            pixelmon.evoAnimTicks = 0;
         }

      }
   }
}
