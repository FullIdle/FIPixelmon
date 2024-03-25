package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.items.ItemPokemonEditor;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdatePlayerParty extends UpdateEditedParty {
   UUID playerID;

   public UpdatePlayerParty() {
   }

   public UpdatePlayerParty(List party) {
      super(party);
   }

   protected UpdateEditedPokemon createPokemonPacket(Pokemon data) {
      return new UpdatePlayerPokemon(data);
   }

   protected UpdateEditedPokemon readPokemonData(ByteBuf buf) {
      UpdatePlayerPokemon data = new UpdatePlayerPokemon();
      data.fromBytes(buf);
      this.playerID = data.playerUUID;
      return data;
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UpdatePlayerParty message, MessageContext ctx) {
         if (ItemPokemonEditor.checkPermission(ctx.getServerHandler().field_147369_b)) {
            UpdatePlayerPokemon.Handler handler = new UpdatePlayerPokemon.Handler();

            for(int i = 0; i < message.party.size(); ++i) {
               UpdatePlayerPokemon data = (UpdatePlayerPokemon)message.party.get(i);
               PlayerPartyStorage party = Pixelmon.storageManager.getParty(message.playerID);
               if (data == null) {
                  if (party.get(i) != null) {
                     DeletePokemon.deletePokemon(message.playerID, i, ctx);
                  }
               } else {
                  party.doWithoutSendingUpdates(() -> {
                     party.set(i, (Pokemon)null);
                  });
                  handler.onSyncMessage(data, ctx);
               }
            }

         }
      }
   }
}
