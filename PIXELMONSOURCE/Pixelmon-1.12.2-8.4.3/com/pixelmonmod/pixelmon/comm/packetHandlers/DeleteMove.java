package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DeleteMove implements IMessage {
   UUID pokemonUUID;
   int removeIndex;

   public DeleteMove() {
   }

   public DeleteMove(UUID pokemonUUID, int removeIndex) {
      this.pokemonUUID = pokemonUUID;
      this.removeIndex = removeIndex;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.removeIndex = buffer.readInt();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.pokemonUUID.getMostSignificantBits());
      buffer.writeLong(this.pokemonUUID.getLeastSignificantBits());
      buffer.writeInt(this.removeIndex);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(DeleteMove message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         Pokemon pokemon = party.find(message.pokemonUUID);
         if (message.removeIndex > -1 && message.removeIndex < 4) {
            if (pokemon == null) {
               pokemon = Pixelmon.storageManager.getPCForPlayer(player).find(message.pokemonUUID);
            }

            if (pokemon != null) {
               if (pokemon.getMoveset().size() > 1) {
                  Attack removed = pokemon.getMoveset().remove(message.removeIndex);
                  if (removed != null) {
                     ChatHandler.sendChat(player, "deletemove.forgot", pokemon.getSpecies().getLocalizedName(), removed.getActualMove().getTranslatedName());
                  }

               }
            }
         }
      }
   }
}
