package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.SetNicknameEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ClientSet;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RenamePokemon implements IMessage {
   private StoragePosition position;
   private UUID pokemonUUID;
   private String name;

   public RenamePokemon() {
   }

   public RenamePokemon(StoragePosition position, UUID pokemonUUID, String name) {
      this.position = position;
      this.pokemonUUID = pokemonUUID;
      this.name = name;
   }

   public void fromBytes(ByteBuf buffer) {
      this.position = new StoragePosition(buffer.readShort(), buffer.readByte());
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.name = ByteBufUtils.readUTF8String(buffer);
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.position.box);
      buffer.writeByte(this.position.order);
      buffer.writeLong(this.pokemonUUID.getMostSignificantBits());
      buffer.writeLong(this.pokemonUUID.getLeastSignificantBits());
      ByteBufUtils.writeUTF8String(buffer, this.name);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(RenamePokemon message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         PokemonStorage storage = message.position.box == -1 ? Pixelmon.storageManager.getParty(player) : Pixelmon.storageManager.getPCForPlayer(player);
         Pokemon pokemon = ((PokemonStorage)storage).get(message.position);
         if (pokemon != null && pokemon.getUUID().equals(message.pokemonUUID)) {
            message.name = message.name.replace("§", "&");
            if (message.name.replaceAll("[&§][0-9a-fk-orA-FK-OR]", "").length() > 16) {
               message.name = message.name.substring(0, 16);
            }

            SetNicknameEvent event = new SetNicknameEvent(player, pokemon, message.name);
            if (Pixelmon.EVENT_BUS.post(event)) {
               Pixelmon.network.sendTo(new ClientSet((PokemonStorage)storage, message.position, pokemon, new EnumUpdateType[]{EnumUpdateType.Nickname}), player);
            } else {
               pokemon.setNickname(event.nickname);
            }

         }
      }
   }
}
