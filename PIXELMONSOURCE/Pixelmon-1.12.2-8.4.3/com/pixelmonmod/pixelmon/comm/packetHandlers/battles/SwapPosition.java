package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SwapPosition implements IMessage {
   UUID user;
   UUID target;

   public SwapPosition() {
   }

   public SwapPosition(PixelmonWrapper user, PixelmonWrapper target) {
      this.user = user.getPokemonUUID();
      this.target = target.getPokemonUUID();
   }

   public void fromBytes(ByteBuf buf) {
      this.user = new UUID(buf.readLong(), buf.readLong());
      this.target = new UUID(buf.readLong(), buf.readLong());
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.user);
      PixelmonMethods.toBytesUUID(buf, this.target);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SwapPosition message, MessageContext ctx) {
         PixelmonInGui userPokemon = null;
         PixelmonInGui targetPokemon = null;
         PixelmonInGui[] var5 = ClientProxy.battleManager.displayedOurPokemon;
         int var6 = var5.length;

         int var7;
         PixelmonInGui pokemon;
         for(var7 = 0; var7 < var6; ++var7) {
            pokemon = var5[var7];
            if (message.user.equals(pokemon.pokemonUUID)) {
               userPokemon = pokemon;
            } else if (message.target.equals(pokemon.pokemonUUID)) {
               targetPokemon = pokemon;
            }
         }

         var5 = ClientProxy.battleManager.displayedEnemyPokemon;
         var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            pokemon = var5[var7];
            if (message.user.equals(pokemon.pokemonUUID)) {
               userPokemon = pokemon;
            } else if (message.target.equals(pokemon.pokemonUUID)) {
               targetPokemon = pokemon;
            }
         }

         if (userPokemon != null && targetPokemon != null) {
            int userPosition = userPokemon.position;
            userPokemon.position = targetPokemon.position;
            targetPokemon.position = userPosition;
         }

         return null;
      }
   }
}
