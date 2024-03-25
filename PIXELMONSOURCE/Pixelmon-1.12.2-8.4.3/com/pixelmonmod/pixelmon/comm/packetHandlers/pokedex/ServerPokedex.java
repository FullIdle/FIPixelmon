package com.pixelmonmod.pixelmon.comm.packetHandlers.pokedex;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerPokedex implements IMessage {
   private boolean launch;
   private int entityID;

   public ServerPokedex(boolean launch, int entityID) {
      this.launch = launch;
      this.entityID = entityID;
   }

   public ServerPokedex(boolean launch) {
      this(launch, -1);
   }

   public ServerPokedex() {
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.launch);
      if (this.launch) {
         buf.writeInt(this.entityID);
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.launch = buf.readBoolean();
      if (this.launch) {
         this.entityID = buf.readInt();
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ServerPokedex message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         Pokedex pokedex = party.pokedex;
         boolean hasChanged = false;
         int id = 1;
         if (message.launch && message.entityID >= 0) {
            Entity entity = player.field_70170_p.func_73045_a(message.entityID);
            if (entity instanceof EntityPixelmon && entity.func_70032_d(player) < 20.0F) {
               Pokemon pokemon = ((EntityPixelmon)entity).getPokemonData();
               if (!Pixelmon.EVENT_BUS.post(new PokedexEvent(player.func_110124_au(), pokemon, EnumPokedexRegisterStatus.seen, "pokedexKey"))) {
                  hasChanged = pokedex.set(pokemon, EnumPokedexRegisterStatus.seen);
                  id = pokemon.getSpecies().getNationalPokedexInteger();
               }
            }
         }

         if (hasChanged) {
            pokedex.update();
         }

         if (message.launch) {
            OpenScreen.open(player, EnumGuiScreen.Pokedex, id);
         }

      }
   }
}
