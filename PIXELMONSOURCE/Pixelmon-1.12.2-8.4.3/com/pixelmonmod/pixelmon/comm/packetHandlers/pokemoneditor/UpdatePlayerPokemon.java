package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPokemonEditorParty;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.items.ItemPokemonEditor;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdatePlayerPokemon extends UpdateEditedPokemon {
   UUID playerUUID;

   public UpdatePlayerPokemon() {
   }

   public UpdatePlayerPokemon(Pokemon data) {
      super(data);
      this.playerUUID = GuiPokemonEditorParty.editedPlayerUUID;
   }

   public void toBytes(ByteBuf buffer) {
      super.toBytes(buffer);
      PixelmonMethods.toBytesUUID(buffer, this.playerUUID);
   }

   public void fromBytes(ByteBuf buffer) {
      super.fromBytes(buffer);
      this.playerUUID = PixelmonMethods.fromBytesUUID(buffer);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UpdatePlayerPokemon message, MessageContext ctx) {
         EntityPlayerMP sender = ctx.getServerHandler().field_147369_b;
         if (ItemPokemonEditor.checkPermission(sender)) {
            EntityPlayerMP player = sender.func_184102_h().func_184103_al().func_177451_a(message.playerUUID);
            if (player != null) {
               PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
               int slot = party.getSlot(message.data);
               if (slot == -1) {
                  party.add(message.data);
               } else {
                  party.set(party.getSlot(message.data), message.data);
               }

               UpdateEditedPokemon.updatePokemon(message, player, party);
               ItemPokemonEditor.updateSinglePokemon(sender, message.playerUUID, party.getSlot(message.data));
            }

         }
      }
   }
}
