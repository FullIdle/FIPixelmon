package com.pixelmonmod.tcg.network.packets.battles;

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

public class ForceEndBattlePacket implements IMessage {
   private BlockPos blockPos;

   public ForceEndBattlePacket() {
   }

   public ForceEndBattlePacket(BlockPos blockPos) {
      this.blockPos = blockPos;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ForceEndBattlePacket msg, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         player.func_71121_q().func_152344_a(() -> {
            WorldServer w = player.func_71121_q();
            TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
            if (player.func_70003_b(4, "tcg.endbattle.others") && b != null && b.getServer() != null && b.getGameServer().getPlayers() != null && b.getGameServer().getPlayers().length == 2) {
               PlayerServerState[] players = b.getGameServer().getPlayers();
               PlayerServerState winner = players[0] != null && players[0].isInGUI() ? players[0] : (players[1] != null && players[1].isInGUI() ? players[1] : null);
               PlayerServerState loser = players[0] != null && !players[0].isInGUI() ? players[0] : (players[1] != null && !players[1].isInGUI() ? players[1] : null);
               if (winner == null || loser == null) {
                  winner = null;
                  loser = null;
               }

               b.endGame(winner, loser, false);
            }

         });
         return null;
      }
   }
}
