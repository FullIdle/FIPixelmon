package com.pixelmonmod.pixelmon.comm.packetHandlers.custom.drops;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.gui.custom.GuiCustomDrops;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CustomDropsOpenPacket implements IMessage {
   private ITextComponent title;
   private List items;
   private String[] buttons;

   public CustomDropsOpenPacket() {
   }

   public CustomDropsOpenPacket(ITextComponent title, List items, String[] buttons) {
      this.title = title;
      this.items = items;
      this.buttons = buttons;
   }

   public void fromBytes(ByteBuf buf) {
      PacketBuffer buffer = new PacketBuffer(buf);

      try {
         this.title = buffer.func_179258_d();
         this.items = Lists.newArrayList();
         int size = buffer.readByte();

         int i;
         for(i = 0; i < size; ++i) {
            this.items.add(buffer.func_150791_c());
         }

         this.buttons = new String[3];

         for(i = 0; i < 3; ++i) {
            this.buttons[i] = buffer.func_150789_c(32767);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void toBytes(ByteBuf buf) {
      PacketBuffer buffer = new PacketBuffer(buf);
      buffer.func_179256_a(this.title);
      buffer.writeByte(this.items.size());

      int i;
      for(i = 0; i < this.items.size(); ++i) {
         buffer.func_150788_a((ItemStack)this.items.get(i));
      }

      for(i = 0; i < 3; ++i) {
         buffer.func_180714_a(this.buttons[i]);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(CustomDropsOpenPacket message, MessageContext ctx) {
         onClient(message, ctx);
      }

      @SideOnly(Side.CLIENT)
      private static void onClient(CustomDropsOpenPacket message, MessageContext ctx) {
         Minecraft.func_71410_x().func_147108_a(new GuiCustomDrops(message.title, (ItemStack[])message.items.toArray(new ItemStack[0]), message.buttons));
      }
   }
}
