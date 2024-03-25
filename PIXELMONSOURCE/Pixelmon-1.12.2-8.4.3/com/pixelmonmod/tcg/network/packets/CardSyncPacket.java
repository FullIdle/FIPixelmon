package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CardSyncPacket implements IMessage {
   private int slot;
   private int id;
   private boolean holo;

   public CardSyncPacket() {
   }

   public CardSyncPacket(int slot, int id, boolean holo) {
      this.slot = slot;
      this.id = id;
      this.holo = holo;
   }

   public void fromBytes(ByteBuf buf) {
      this.slot = buf.readInt();
      this.id = buf.readInt();
      this.holo = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.slot);
      buf.writeInt(this.id);
      buf.writeBoolean(this.holo);
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(CardSyncPacket message, MessageContext ctx) {
         Minecraft mc = Minecraft.func_71410_x();
         EntityPlayer e = mc.field_71439_g;
         if (e != null) {
            ItemStack i = e.field_71071_by.func_70301_a(message.slot);
            if (i != ItemStack.field_190927_a && i.func_77973_b() == TCG.itemCard) {
               if (i.func_77978_p() == null) {
                  i.func_77982_d(new NBTTagCompound());
               }

               ImmutableCard card = CardRegistry.fromId(message.id);
               if (card != null) {
                  card.write(i.func_77978_p());
               }
            }
         }

         return null;
      }
   }
}
