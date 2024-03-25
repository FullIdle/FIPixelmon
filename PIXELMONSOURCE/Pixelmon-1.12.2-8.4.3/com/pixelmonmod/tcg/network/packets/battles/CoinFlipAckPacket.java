package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CoinFlipAckPacket implements IMessage {
   private BlockPos blockPos;

   public CoinFlipAckPacket() {
   }

   public CoinFlipAckPacket(BlockPos blockPos) {
      this.blockPos = blockPos;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CoinFlipAckPacket msg, MessageContext ctx) {
         EntityPlayerMP e = ctx.getServerHandler().field_147369_b;
         e.func_71121_q().func_152344_a(() -> {
            WorldServer w = e.func_71121_q();
            TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
            GameServerState server = b.getGameServer();
            PlayerServerState player = server.getPlayer(e);
            if (player != null && player.getEntityPlayer() == e) {
               b.requestFlip(player);
            }

         });
         return null;
      }
   }
}
