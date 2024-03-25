package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ClientSet;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetDisplayRibbon implements IMessage {
   private StoragePosition position;
   private UUID pokemonUUID;
   private EnumRibbonType ribbon;

   public SetDisplayRibbon() {
   }

   public SetDisplayRibbon(StoragePosition position, UUID pokemonUUID, EnumRibbonType ribbon) {
      this.position = position;
      this.pokemonUUID = pokemonUUID;
      this.ribbon = ribbon;
   }

   public void fromBytes(ByteBuf buffer) {
      this.position = new StoragePosition(buffer.readShort(), buffer.readByte());
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.ribbon = EnumRibbonType.values()[buffer.readInt()];
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.position.box);
      buffer.writeByte(this.position.order);
      buffer.writeLong(this.pokemonUUID.getMostSignificantBits());
      buffer.writeLong(this.pokemonUUID.getLeastSignificantBits());
      buffer.writeInt(this.ribbon.ordinal());
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SetDisplayRibbon message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         PokemonStorage storage = message.position.box == -1 ? Pixelmon.storageManager.getParty(player) : Pixelmon.storageManager.getPCForPlayer(player);
         Pokemon pokemon = ((PokemonStorage)storage).get(message.position);
         if (pokemon != null && pokemon.getUUID().equals(message.pokemonUUID)) {
            pokemon.setDisplayedRibbon(message.ribbon);
            Pixelmon.network.sendTo(new ClientSet((PokemonStorage)storage, message.position, pokemon, new EnumUpdateType[]{EnumUpdateType.Ribbons}), player);
         }
      }
   }
}
