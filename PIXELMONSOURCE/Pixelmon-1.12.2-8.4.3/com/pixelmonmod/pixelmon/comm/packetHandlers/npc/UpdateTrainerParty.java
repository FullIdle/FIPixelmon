package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.UpdateEditedParty;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.UpdateEditedPokemon;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateTrainerParty extends UpdateEditedParty {
   int trainerID;

   public UpdateTrainerParty() {
   }

   public UpdateTrainerParty(List party) {
      super(party);
   }

   protected UpdateEditedPokemon createPokemonPacket(Pokemon data) {
      return new UpdateTrainerPokemon(data);
   }

   protected UpdateEditedPokemon readPokemonData(ByteBuf buf) {
      UpdateTrainerPokemon data = new UpdateTrainerPokemon();
      data.fromBytes(buf);
      this.trainerID = data.trainerID;
      return data;
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UpdateTrainerParty message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (!ItemNPCEditor.checkPermission(player)) {
            return null;
         } else {
            Optional entityNPCOptional = EntityNPC.locateNPCServer(ctx.getServerHandler().field_147369_b.field_70170_p, message.trainerID, NPCTrainer.class);
            if (!entityNPCOptional.isPresent()) {
               return null;
            } else {
               NPCTrainer t = (NPCTrainer)entityNPCOptional.get();
               TrainerPartyStorage storage = t.getPokemonStorage();

               for(int i = 0; i < 6; ++i) {
                  storage.set(i, (Pokemon)null);
               }

               UpdateTrainerPokemon.Handler handler = new UpdateTrainerPokemon.Handler();
               int numPokemon = message.party.size();

               for(int i = 0; i < 6; ++i) {
                  UpdateTrainerPokemon data = null;
                  if (i < numPokemon) {
                     data = (UpdateTrainerPokemon)message.party.get(i);
                  }

                  if (data == null) {
                     DeleteTrainerPokemon.deletePokemon(message.trainerID, numPokemon, ctx, i == 5);
                  } else {
                     handler.onMessage(data, ctx);
                  }
               }

               return null;
            }
         }
      }
   }
}
