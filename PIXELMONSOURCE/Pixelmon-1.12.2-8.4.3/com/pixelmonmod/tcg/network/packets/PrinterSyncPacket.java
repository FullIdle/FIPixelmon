package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.TileEntityPrinter;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PrinterSyncPacket implements IMessage {
   private BlockPos blockPos;
   private short printTime;

   public PrinterSyncPacket() {
   }

   public PrinterSyncPacket(BlockPos blockPos, short printTime) {
      this.blockPos = blockPos;
      this.printTime = printTime;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.printTime = buf.readShort();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeShort(this.printTime);
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PrinterSyncPacket msg, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            if (mc.field_71441_e != null) {
               World w = mc.field_71441_e;
               TileEntityPrinter b = (TileEntityPrinter)w.func_175625_s(msg.blockPos);
               if (b != null) {
                  b.setPrintTime(msg.printTime);
               }
            }
         }

         return null;
      }
   }
}
