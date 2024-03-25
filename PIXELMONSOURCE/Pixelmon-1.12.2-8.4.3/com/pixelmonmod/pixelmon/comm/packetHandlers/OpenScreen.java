package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpenScreen implements IMessage {
   private EnumGuiScreen gui;
   private int[] data;

   public OpenScreen() {
   }

   private OpenScreen(EnumGuiScreen gui, int... data) {
      this.gui = gui;
      this.data = data;
   }

   public static void open(EntityPlayer player, EnumGuiScreen gui, int... data) {
      if (player instanceof EntityPlayerMP) {
         Pixelmon.network.sendTo(new OpenScreen(gui, data), (EntityPlayerMP)player);
      } else {
         openClient(player, gui, data);
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.gui = EnumGuiScreen.getFromOrdinal(buf.readByte());
      this.data = new int[buf.readByte()];

      for(int i = 0; i < this.data.length; ++i) {
         this.data[i] = buf.readInt();
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.gui.getIndex());
      buf.writeByte(this.data.length);
      int[] var2 = this.data;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int i = var2[var4];
         buf.writeInt(i);
      }

   }

   @SideOnly(Side.CLIENT)
   private static void openClient(EntityPlayer player, EnumGuiScreen gui, int[] data) {
      GuiScreen screen = ClientProxy.createScreen(player, gui, data);
      Minecraft.func_71410_x().func_147108_a(screen);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(OpenScreen message, MessageContext ctx) {
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(OpenScreen message) {
         OpenScreen.openClient(Minecraft.func_71410_x().field_71439_g, message.gui, message.data);
      }
   }
}
