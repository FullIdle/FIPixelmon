package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.api.pokemon.EnumInitializeCategory;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPokemonEditorParty;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemPokemonEditor;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangePokemon implements IMessage {
   UUID playerID;
   UUID pokemonUUID;
   EnumSpecies newPokemon;

   public ChangePokemon() {
   }

   public ChangePokemon(UUID pokemonUUID, EnumSpecies newPokemon) {
      this.playerID = GuiPokemonEditorParty.editedPlayerUUID;
      this.pokemonUUID = pokemonUUID;
      this.newPokemon = newPokemon;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.playerID);
      PixelmonMethods.toBytesUUID(buf, this.pokemonUUID);
      buf.writeInt(this.newPokemon.getNationalPokedexInteger());
   }

   public void fromBytes(ByteBuf buf) {
      this.playerID = PixelmonMethods.fromBytesUUID(buf);
      this.pokemonUUID = new UUID(buf.readLong(), buf.readLong());
      this.newPokemon = EnumSpecies.getFromDex(buf.readInt());
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ChangePokemon message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (ItemPokemonEditor.checkPermission(player)) {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(message.playerID);
            Pokemon pokemon = party.find(message.pokemonUUID);
            if (pokemon == null) {
               party.add(pokemon = Pixelmon.pokemonFactory.create(message.newPokemon).initialize(EnumInitializeCategory.SPECIES, EnumInitializeCategory.INTRINSIC_FORCEFUL).setUUID(message.pokemonUUID));
            } else {
               pokemon.setSpecies(message.newPokemon);
               pokemon.initialize(EnumInitializeCategory.SPECIES, EnumInitializeCategory.INTRINSIC_FORCEFUL);
            }

            EntityPlayerMP editingPlayer = ctx.getServerHandler().field_147369_b;
            int slot = party.getSlot(pokemon);
            ItemPokemonEditor.updateSinglePokemon(editingPlayer, message.playerID, slot);
         }

      }
   }
}
