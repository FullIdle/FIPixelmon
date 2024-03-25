package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemPokemonEditor;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RandomizePokemon implements IMessage {
   UUID playerID;

   public RandomizePokemon() {
   }

   public RandomizePokemon(UUID playerUUID) {
      this.playerID = playerUUID;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.playerID);
   }

   public void fromBytes(ByteBuf buf) {
      this.playerID = PixelmonMethods.fromBytesUUID(buf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(RandomizePokemon message, MessageContext ctx) {
         if (ItemPokemonEditor.checkPermission(ctx.getServerHandler().field_147369_b)) {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(message.playerID);

            for(int i = 0; i < 6; ++i) {
               party.set(i, Pixelmon.pokemonFactory.create(EnumSpecies.randomPoke()));
            }

            ItemPokemonEditor.updateEditedPlayer(ctx.getServerHandler().field_147369_b, message.playerID);
         }
      }
   }
}
