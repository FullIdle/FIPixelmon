package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.ServerBattleController;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderStateSyncPacket implements IMessage {
   private BlockPos blockPos;
   private GamePhase gamePhase;
   private int currentTurn;
   private PlayerClientOpponentState[] players;
   private GameServerState server;
   private boolean isShadowGame;

   public RenderStateSyncPacket() {
   }

   public RenderStateSyncPacket(BlockPos blockPos, GameServerState server, EntityPlayer entityPlayer, ServerBattleController bc) {
      this.blockPos = blockPos;
      this.gamePhase = server.getGamePhase();
      this.currentTurn = server.getCurrentTurn();
      this.isShadowGame = bc.isShadowGame();
      this.players = new PlayerClientOpponentState[server.getPlayers().length];

      for(int i = 0; i < server.getPlayers().length; ++i) {
         PlayerServerState player = server.getPlayer(i);
         if (player != null) {
            this.players[i] = new PlayerClientOpponentState(player, server.getGamePhase(), server, player.getEntityPlayer() == entityPlayer);
         }
      }

      this.server = server;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.gamePhase = GamePhase.values()[buf.readInt()];
      this.currentTurn = buf.readInt();
      this.isShadowGame = buf.readBoolean();
      int size = buf.readInt();
      this.players = new PlayerClientOpponentState[size];

      for(int i = 0; i < size; ++i) {
         if (buf.readBoolean()) {
            this.players[i] = new PlayerClientOpponentState(buf, this.server);
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeInt(this.gamePhase.ordinal());
      buf.writeInt(this.currentTurn);
      buf.writeBoolean(this.isShadowGame);
      buf.writeInt(this.players.length);
      PlayerClientOpponentState[] var2 = this.players;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PlayerClientOpponentState player = var2[var4];
         buf.writeBoolean(player != null);
         if (player != null) {
            player.write(buf);
         }
      }

   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(RenderStateSyncPacket msg, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            EntityPlayer e = mc.field_71439_g;
            if (mc.field_71441_e != null) {
               World w = mc.field_71441_e;
               TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
               if (b != null) {
                  b.setRenderClientSideValues(msg.gamePhase, msg.currentTurn, msg.players);
                  b.setShadowGame(msg.isShadowGame);
               }
            }
         }

         return null;
      }
   }
}
