package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UseItem implements IMessage {
   public int itemID;

   public UseItem() {
   }

   public UseItem(Item item) {
      this.itemID = Item.func_150891_b(item);
   }

   public void fromBytes(ByteBuf buf) {
      this.itemID = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.itemID);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UseItem message, MessageContext ctx) {
         ItemStack item = new ItemStack(Item.func_150899_d(message.itemID));
         Minecraft.func_71410_x().field_71439_g.field_71071_by.func_174925_a(item.func_77973_b(), item.func_77960_j(), 1, item.func_77978_p());
         return null;
      }
   }
}
