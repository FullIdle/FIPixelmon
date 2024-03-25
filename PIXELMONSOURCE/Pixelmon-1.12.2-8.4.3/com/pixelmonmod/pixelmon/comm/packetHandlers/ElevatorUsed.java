package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.blocks.machines.BlockElevator;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ElevatorUsed implements IMessage {
   public void toBytes(ByteBuf buffer) {
   }

   public void fromBytes(ByteBuf buffer) {
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ElevatorUsed message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         IBlockState state = player.func_71121_q().func_180495_p(new BlockPos((double)((int)Math.floor(player.field_70165_t)), (double)((int)Math.floor(player.field_70163_u) - 1), Math.floor(player.field_70161_v)));
         if (state.func_177230_c() instanceof BlockElevator) {
            ((BlockElevator)state.func_177230_c()).takeElevator(player.func_71121_q(), player.func_180425_c().func_177977_b(), player, false);
         }

      }
   }
}
