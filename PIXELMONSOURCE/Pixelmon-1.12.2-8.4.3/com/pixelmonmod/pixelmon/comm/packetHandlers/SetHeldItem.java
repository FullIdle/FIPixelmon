package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetHeldItem implements IMessage {
   private StoragePosition position;
   private UUID pokemon;
   private Item item;

   public SetHeldItem() {
   }

   public SetHeldItem(StoragePosition position, UUID pokemon) {
      this.position = position;
      this.pokemon = pokemon;
   }

   public void setItem(Item item) {
      this.item = item == null ? Items.field_190931_a : item;
   }

   public void toBytes(ByteBuf buf) {
      this.position.encode(buf);
      buf.writeBoolean(this.pokemon != null);
      if (this.pokemon != null) {
         buf.writeLong(this.pokemon.getMostSignificantBits());
         buf.writeLong(this.pokemon.getLeastSignificantBits());
      }

      buf.writeBoolean(this.item != null);
      if (this.item != null) {
         ByteBufUtils.writeUTF8String(buf, this.item.getRegistryName().toString());
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.position = StoragePosition.decode(buf);
      if (buf.readBoolean()) {
         this.pokemon = new UUID(buf.readLong(), buf.readLong());
      }

      this.item = buf.readBoolean() ? Item.func_111206_d(ByteBufUtils.readUTF8String(buf)) : null;
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SetHeldItem message, MessageContext ctx) {
         PokemonStorage storage = Pixelmon.storageManager.getStorage(ctx.getServerHandler().field_147369_b, message.position);
         if (storage.validate(message.position, message.pokemon)) {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            if (BattleRegistry.getBattle(player) != null) {
               return;
            }

            InventoryPlayer inventory = player.field_71071_by;
            Pokemon pokemon = storage.get(message.position);
            Preconditions.checkArgument(pokemon != null, "The pokémon cannot be null since we've already validated it");
            ItemStack newHeldItem = ItemStack.field_190927_a;
            ItemStack currentItem = inventory.func_70445_o().func_77946_l();
            ItemStack giveItem = null;
            if (player.func_184812_l_()) {
               newHeldItem = message.item == null ? ItemStack.field_190927_a : new ItemStack(message.item, 1);
            } else {
               ItemStack oldItem = pokemon.getHeldItem();
               ItemStack singleItem;
               if (oldItem.func_190926_b()) {
                  if (!currentItem.func_190926_b()) {
                     singleItem = currentItem.func_77946_l();
                     singleItem.func_190920_e(1);
                     newHeldItem = singleItem;
                     currentItem.func_190918_g(1);
                  }
               } else if (currentItem.func_190926_b()) {
                  currentItem = oldItem;
                  newHeldItem = ItemStack.field_190927_a;
               } else if (ItemStack.func_179545_c(oldItem, currentItem) && ItemStack.func_77970_a(oldItem, currentItem)) {
                  currentItem.func_190917_f(1);
               } else {
                  singleItem = currentItem.func_77946_l();
                  singleItem.func_190920_e(1);
                  newHeldItem = singleItem;
                  if (currentItem.func_190916_E() <= 1) {
                     currentItem = oldItem;
                  } else {
                     currentItem.func_190918_g(1);
                     giveItem = oldItem;
                  }
               }
            }

            if (newHeldItem.func_190926_b()) {
               newHeldItem = ItemStack.field_190927_a;
            }

            inventory.func_70437_b(currentItem);
            if (giveItem != null && !inventory.func_70441_a(giveItem)) {
               player.func_71019_a(giveItem, false);
            }

            pokemon.setHeldItem(newHeldItem);
         } else {
            storage.notifyListener(ctx.getServerHandler().field_147369_b, message.position, storage.get(message.position));
         }

      }
   }
}
