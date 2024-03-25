package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.client.render.blockReveal.BlockReveal;
import com.pixelmonmod.pixelmon.client.render.blockReveal.BlockRevealRenderer;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class IlluminatePositionsPacket implements IMessage {
   public int durationTicks;
   public HashMap locationColors = new HashMap();

   public IlluminatePositionsPacket() {
   }

   public IlluminatePositionsPacket(int durationTicks, HashMap locationColors) {
      this.durationTicks = durationTicks;
      this.locationColors = locationColors;
   }

   public void fromBytes(ByteBuf buf) {
      this.durationTicks = buf.readUnsignedShort();
      int locationCount = buf.readUnsignedShort();

      for(int i = 0; i < locationCount; ++i) {
         BlockPos pos = new BlockPos(buf.readInt(), buf.readShort(), buf.readInt());
         int color = buf.readInt();
         int pattern = buf.readInt();
         this.locationColors.put(pos, new Tuple(color, pattern));
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeShort(this.durationTicks);
      buf.writeShort(this.locationColors.size());
      Iterator var2 = this.locationColors.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry entry = (Map.Entry)var2.next();
         buf.writeInt(((BlockPos)entry.getKey()).func_177958_n());
         buf.writeShort(((BlockPos)entry.getKey()).func_177956_o());
         buf.writeInt(((BlockPos)entry.getKey()).func_177952_p());
         buf.writeInt((Integer)((Tuple)entry.getValue()).func_76341_a());
         buf.writeInt((Integer)((Tuple)entry.getValue()).func_76340_b());
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(IlluminatePositionsPacket message, MessageContext ctx) {
         Iterator var3 = message.locationColors.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry entry = (Map.Entry)var3.next();
            BlockReveal reveal = new BlockReveal(message.durationTicks, (BlockPos)entry.getKey(), (Integer)((Tuple)entry.getValue()).func_76341_a(), (Integer)((Tuple)entry.getValue()).func_76340_b());
            BlockRevealRenderer.revealedBlocks.put(entry.getKey(), reveal);
         }

         return null;
      }
   }
}
