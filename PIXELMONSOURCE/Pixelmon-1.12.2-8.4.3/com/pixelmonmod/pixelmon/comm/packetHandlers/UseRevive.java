package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.items.ItemRevive;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UseRevive implements IMessage {
   UUID pokemonUUID;

   public UseRevive() {
   }

   public UseRevive(UUID pokemonUUID) {
      this.pokemonUUID = pokemonUUID;
   }

   public void toBytes(ByteBuf buffer) {
      PixelmonMethods.toBytesUUID(buffer, this.pokemonUUID);
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UseRevive message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         ItemStack itemStack = player.func_184614_ca();
         if (itemStack != null) {
            Item item = itemStack.func_77973_b();
            if (item instanceof ItemRevive) {
               ItemRevive revive = (ItemRevive)item;
               PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
               Pokemon pokemon = party.find(message.pokemonUUID);
               if (pokemon != null && revive.useMedicine(new DelegateLink(pokemon), 0) && !player.field_71075_bZ.field_75098_d) {
                  player.field_71071_by.func_174925_a(item, itemStack.func_77960_j(), 1, itemStack.func_77978_p());
               }
            }
         }

      }
   }
}
