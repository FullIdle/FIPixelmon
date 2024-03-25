package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.duel.state.GamePhase;
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

public class SwitchPacket implements IMessage {
   private BlockPos blockPos;
   private int switchingIn;

   public SwitchPacket() {
   }

   public SwitchPacket(BlockPos blockPos, int switchingIn) {
      this.blockPos = blockPos;
      this.switchingIn = switchingIn;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.switchingIn = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeInt(this.switchingIn);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SwitchPacket msg, MessageContext ctx) {
         EntityPlayerMP e = ctx.getServerHandler().field_147369_b;
         e.func_71121_q().func_152344_a(() -> {
            WorldServer w = e.func_71121_q();
            TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
            GameServerState server = b.getGameServer();
            PlayerServerState player = server.getPlayer(e);
            if (b.getGameServer().getGamePhase().after(GamePhase.PreMatch) && player != null && player.getEntityPlayer() == e) {
               b.requestSwitch(player, msg.switchingIn);
            }

         });
         return null;
      }
   }
}
