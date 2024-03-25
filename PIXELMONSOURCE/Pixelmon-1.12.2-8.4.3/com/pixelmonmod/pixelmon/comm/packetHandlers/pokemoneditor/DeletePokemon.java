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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DeletePokemon implements IMessage {
   UUID playerID;
   UUID pokemonUUID;

   public DeletePokemon() {
   }

   public DeletePokemon(UUID pokemonUUID) {
      this.playerID = GuiPokemonEditorParty.editedPlayerUUID;
      this.pokemonUUID = pokemonUUID;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.playerID);
      PixelmonMethods.toBytesUUID(buf, this.pokemonUUID);
   }

   public void fromBytes(ByteBuf buf) {
      this.playerID = PixelmonMethods.fromBytesUUID(buf);
      this.pokemonUUID = new UUID(buf.readLong(), buf.readLong());
   }

   public static void deletePokemon(UUID playerID, int slot, MessageContext ctx) {
      EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(playerID);
      party.set(slot, (Pokemon)null);
      ItemPokemonEditor.updateSinglePokemon(player, playerID, slot);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(DeletePokemon message, MessageContext ctx) {
         if (ItemPokemonEditor.checkPermission(ctx.getServerHandler().field_147369_b)) {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(message.playerID);
            int slot = party.getSlot(message.pokemonUUID);
            if (slot != -1) {
               DeletePokemon.deletePokemon(message.playerID, slot, ctx);
            }
         }

      }
   }
}
