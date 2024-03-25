package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientSet implements IMessage {
   private static final EnumUpdateType[] ALL_TYPES = EnumUpdateType.values();
   UUID storageUUID;
   StoragePosition position;
   Pokemon pokemon;
   EnumUpdateType[] dataTypes;
   ByteBuf pokemonData;

   public ClientSet() {
   }

   public ClientSet(PokemonStorage storage, StoragePosition position, Pokemon pokemon, EnumUpdateType... dataTypes) {
      this.storageUUID = storage.uuid;
      this.position = position;
      this.pokemon = pokemon;
      if (dataTypes == null || dataTypes.length == 0) {
         dataTypes = EnumUpdateType.CLIENT;
      }

      this.dataTypes = dataTypes;
   }

   public void fromBytes(ByteBuf buf) {
      this.storageUUID = new UUID(buf.readLong(), buf.readLong());
      this.position = StoragePosition.decode(buf);
      boolean deleting = buf.readBoolean();
      if (!deleting) {
         this.dataTypes = new EnumUpdateType[buf.readByte()];

         for(int i = 0; i < this.dataTypes.length; ++i) {
            this.dataTypes[i] = ALL_TYPES[buf.readByte()];
         }

         this.pokemonData = buf.copy(buf.readerIndex(), buf.readableBytes());
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.storageUUID.getMostSignificantBits());
      buf.writeLong(this.storageUUID.getLeastSignificantBits());
      this.position.encode(buf);
      if (this.pokemon == null) {
         buf.writeBoolean(true);
      } else {
         buf.writeBoolean(false);
         buf.writeByte(this.dataTypes.length);
         EnumUpdateType[] var2 = this.dataTypes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            EnumUpdateType type = var2[var4];
            buf.writeByte((byte)type.ordinal());
         }

         this.pokemon.writeToByteBuffer(buf, this.dataTypes);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ClientSet message, MessageContext ctx) {
         try {
            PokemonStorage storage = ClientStorageManager.getStorage(message.storageUUID, message.position);
            if (storage == null) {
               Pixelmon.LOGGER.error("No storage on the client side with UUID " + message.storageUUID.toString());
               return;
            }

            if (message.pokemonData == null) {
               storage.set(message.position, (Pokemon)null);
            } else {
               boolean putting = false;
               Pokemon pokemon = storage.get(message.position);
               if (pokemon == null) {
                  pokemon = Pixelmon.pokemonFactory.create(UUID.randomUUID());
                  putting = true;
               }

               pokemon.readFromByteBuffer(message.pokemonData, message.dataTypes);
               if (putting) {
                  storage.set(message.position, pokemon);
               }
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }

      }
   }
}
