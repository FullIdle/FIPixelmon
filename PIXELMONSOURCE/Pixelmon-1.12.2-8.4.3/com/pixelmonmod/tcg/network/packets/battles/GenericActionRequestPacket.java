package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import com.pixelmonmod.tcg.network.packets.enums.PhaseAction;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GenericActionRequestPacket implements IMessage {
   private BlockPos blockPos;
   private PhaseAction action;
   private int playerIndex;
   private int actionIndex;
   private int actionSubindex;

   public GenericActionRequestPacket() {
   }

   public GenericActionRequestPacket(BlockPos blockPos, PhaseAction action, int playerIndex, int actionIndex, int actionSubindex) {
      this.blockPos = blockPos;
      this.action = action;
      this.playerIndex = playerIndex;
      this.actionIndex = actionIndex;
      this.actionSubindex = actionSubindex;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.action = PhaseAction.values()[buf.readInt()];
      this.playerIndex = buf.readInt();
      this.actionIndex = buf.readInt();
      this.actionSubindex = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeInt(this.action.ordinal());
      buf.writeInt(this.playerIndex);
      buf.writeInt(this.actionIndex);
      buf.writeInt(this.actionSubindex);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(GenericActionRequestPacket msg, MessageContext ctx) {
         EntityPlayerMP e = ctx.getServerHandler().field_147369_b;
         e.func_71121_q().func_152344_a(() -> {
            WorldServer w = e.func_71121_q();
            TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
            GameServerState server = b.getGameServer();
            PlayerServerState playerx;
            switch (msg.action) {
               case Exit:
                  return;
               case EndTurn:
                  playerx = server.getPlayer(msg.playerIndex);
                  if ((b.getGameServer().getGamePhase() == GamePhase.PreMatch || server.getCurrentTurn() == msg.playerIndex) && playerx != null && playerx.getEntityPlayer() == e) {
                     b.requestEndTurn(playerx);
                  }

                  return;
               case UseAbility:
                  if (server.getCurrentTurn() == msg.playerIndex && server.getPlayer(msg.playerIndex).getEntityPlayer() == e) {
                     b.requestAbility(msg.playerIndex, BoardLocation.values()[msg.actionIndex], msg.actionSubindex);
                  }

                  return;
               case UseAttack:
                  if (server.getCurrentTurn() == msg.playerIndex && server.getPlayer(msg.playerIndex).getEntityPlayer() == e) {
                     playerx = server.getPlayer(server.getCurrentTurn());
                     if (playerx.isChoosingOppAttack()) {
                        b.requestPickAttack(msg.playerIndex, msg.actionIndex);
                     } else {
                        b.requestAttack(msg.playerIndex, msg.actionIndex);
                     }
                  }

                  return;
               case Retreat:
                  if (server.getCurrentTurn() == msg.playerIndex && server.getPlayer(msg.playerIndex).getEntityPlayer() == e) {
                  }

                  return;
               case Discard:
                  if (server.getCurrentTurn() == msg.playerIndex && server.getPlayer(msg.playerIndex).getEntityPlayer() == e) {
                     BoardLocation location = BoardLocation.values()[msg.actionIndex];
                     PlayerServerState player = server.getPlayer(server.getCurrentTurn());
                     b.discard(player, location, msg.actionSubindex);
                  }
               default:
            }
         });
         return null;
      }
   }
}
