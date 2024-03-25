package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PrizeSelectorToServerPacket implements IMessage {
   private BlockPos blockPos;
   private int index;

   public PrizeSelectorToServerPacket() {
   }

   public PrizeSelectorToServerPacket(BlockPos blockPos, int index) {
      this.blockPos = blockPos;
      this.index = index;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.index = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeInt(this.index);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(PrizeSelectorToServerPacket msg, MessageContext ctx) {
         EntityPlayerMP e = ctx.getServerHandler().field_147369_b;
         e.func_71121_q().func_152344_a(() -> {
            WorldServer w = e.func_71121_q();
            TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
            b.setPrizeSelection(e, msg.index);
         });
         return null;
      }
   }
}
