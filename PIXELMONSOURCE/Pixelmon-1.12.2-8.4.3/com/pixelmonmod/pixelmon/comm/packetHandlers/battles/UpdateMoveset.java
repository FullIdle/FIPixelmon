package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateMoveset implements IMessage {
   UUID uuid;
   Moveset moveset;

   public UpdateMoveset() {
   }

   public UpdateMoveset(PixelmonWrapper wrapper) {
      this.uuid = wrapper.getPokemonUUID();
      this.moveset = wrapper.getMoveset();
   }

   public void fromBytes(ByteBuf buf) {
      this.uuid = new UUID(buf.readLong(), buf.readLong());
      this.moveset = new Moveset();
      this.moveset.fromBytes(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.uuid.getMostSignificantBits()).writeLong(this.uuid.getLeastSignificantBits());
      this.moveset.toBytes(buf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UpdateMoveset message, MessageContext ctx) {
         Iterator var3 = ClientProxy.battleManager.fullOurPokemon.iterator();

         while(var3.hasNext()) {
            PixelmonInGui pig = (PixelmonInGui)var3.next();
            if (pig.pokemonUUID.equals(message.uuid)) {
               pig.moveset = message.moveset;
            }
         }

         PixelmonInGui[] var7 = ClientProxy.battleManager.displayedOurPokemon;
         int var8 = var7.length;

         for(int var5 = 0; var5 < var8; ++var5) {
            PixelmonInGui pig = var7[var5];
            if (pig.pokemonUUID.equals(message.uuid)) {
               pig.moveset = message.moveset;
            }
         }

      }
   }
}
