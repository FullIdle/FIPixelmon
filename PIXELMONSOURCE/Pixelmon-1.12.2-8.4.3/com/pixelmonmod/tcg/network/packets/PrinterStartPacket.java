package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.tileentity.TileEntityPrinter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PrinterStartPacket implements IMessage {
   private BlockPos pos;

   public PrinterStartPacket() {
   }

   public PrinterStartPacket(BlockPos pos) {
      this.pos = pos;
   }

   public void fromBytes(ByteBuf buf) {
      int x = buf.readInt();
      int y = buf.readInt();
      int z = buf.readInt();
      this.pos = new BlockPos(x, y, z);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.pos.func_177958_n());
      buf.writeInt(this.pos.func_177956_o());
      buf.writeInt(this.pos.func_177952_p());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(PrinterStartPacket message, MessageContext ctx) {
         FMLCommonHandler.instance().getMinecraftServerInstance().func_152344_a(() -> {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            TileEntityPrinter tileEntity = (TileEntityPrinter)player.field_70170_p.func_175625_s(message.pos);
            tileEntity.startPrinting();
         });
         return null;
      }
   }
}
