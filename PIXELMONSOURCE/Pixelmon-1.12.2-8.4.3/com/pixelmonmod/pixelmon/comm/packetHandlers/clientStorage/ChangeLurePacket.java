package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangeLurePacket implements IMessage {
   public Change change;
   public ResourceLocation item;
   public int damage;

   public ChangeLurePacket() {
   }

   public ChangeLurePacket(Change change) {
      this.change = change;
   }

   public ChangeLurePacket(Change change, Item item, int damage) {
      this.change = change;
      this.item = item.getRegistryName();
      this.damage = damage;
   }

   public void fromBytes(ByteBuf buf) {
      this.change = ChangeLurePacket.Change.values()[buf.readByte()];
      if (buf.readBoolean()) {
         this.item = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
         this.damage = buf.readInt();
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.change.ordinal());
      buf.writeBoolean(this.item != null);
      if (this.item != null) {
         ByteBufUtils.writeUTF8String(buf, this.item.toString());
         buf.writeInt(this.damage);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ChangeLurePacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (player.field_71070_bA != null) {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
            Runnable sync = () -> {
               party.setLureStack(party.getLureStack());
               player.func_71113_k();
            };
            ItemStack held = player.field_71071_by.func_70445_o();
            if (player.field_71075_bZ.field_75098_d) {
               if (message.item == null) {
                  held = ItemStack.field_190927_a;
               } else {
                  Item item = (Item)Item.field_150901_e.func_82594_a(message.item);
                  if (!(item instanceof ItemLure)) {
                     return;
                  }

                  held = new ItemStack(item);
                  held.func_77964_b(message.damage);
               }
            }

            if (message.change == ChangeLurePacket.Change.PUT) {
               if (party.getLure() != null || held.func_190926_b() || !(held.func_77973_b() instanceof ItemLure)) {
                  sync.run();
                  return;
               }

               if (!player.field_71075_bZ.field_75098_d) {
                  player.field_71071_by.func_70437_b(ItemStack.field_190927_a);
               }

               party.setLureStack(held);
            } else if (message.change == ChangeLurePacket.Change.SWAP) {
               if (party.getLure() == null || held.func_190926_b() || !(held.func_77973_b() instanceof ItemLure)) {
                  sync.run();
                  return;
               }

               if (!player.field_71075_bZ.field_75098_d) {
                  player.field_71071_by.func_70437_b(party.getLureStack());
               }

               party.setLureStack(held);
            } else if (message.change == ChangeLurePacket.Change.TAKE) {
               if (party.getLure() == null || !held.func_190926_b()) {
                  sync.run();
                  return;
               }

               ItemStack lureStack = party.getLureStack();
               party.setLureStack((ItemStack)null);
               if (!player.field_71075_bZ.field_75098_d) {
                  player.field_71071_by.func_70437_b(lureStack);
               }
            }

         }
      }
   }

   public static enum Change {
      SWAP,
      PUT,
      TAKE;
   }
}
