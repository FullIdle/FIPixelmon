package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.items.ItemEther;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryLeppa;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UseEther implements IMessage {
   int moveIndex;
   UUID pokemonUUID;

   public UseEther() {
   }

   public UseEther(int moveIndex, UUID pokemonUUID) {
      this.moveIndex = moveIndex;
      this.pokemonUUID = pokemonUUID;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.moveIndex);
      PixelmonMethods.toBytesUUID(buf, this.pokemonUUID);
   }

   public void fromBytes(ByteBuf buf) {
      this.moveIndex = buf.readInt();
      this.pokemonUUID = new UUID(buf.readLong(), buf.readLong());
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UseEther message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         ItemStack itemStack = player.func_184614_ca();
         if (itemStack != null) {
            Item item = itemStack.func_77973_b();
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
            Pokemon pokemon = party.find(message.pokemonUUID);
            if (pokemon == null) {
               return;
            }

            Attack attack = pokemon.getMoveset().get(message.moveIndex);
            if (attack != null) {
               ItemEther ether;
               if (item instanceof ItemEther) {
                  ether = (ItemEther)item;
               } else {
                  if (!(item instanceof ItemBerryLeppa)) {
                     return;
                  }

                  ether = (ItemEther)PixelmonItems.ether;
               }

               if (ether.useEther(new DelegateLink(pokemon), message.moveIndex) && !player.field_71075_bZ.field_75098_d) {
                  itemStack.func_190918_g(1);
               }
            }
         }

      }
   }
}
