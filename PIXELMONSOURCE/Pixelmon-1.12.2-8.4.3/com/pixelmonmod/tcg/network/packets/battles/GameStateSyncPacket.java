package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.log.DuelLog;
import com.pixelmonmod.tcg.duel.state.CoinFlipState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GameStateSyncPacket implements IMessage {
   private boolean isSpectating;
   private BlockPos blockPos;
   private GamePhase gamePhase;
   private int playerIndex;
   private boolean isMyTurn;
   private PlayerClientMyState myState;
   private PlayerClientOpponentState oppState;
   private DuelLog log;
   private int turnCount;
   private CoinFlipState coinFlip;
   private GameServerState server;

   public GameStateSyncPacket() {
   }

   public GameStateSyncPacket(BlockPos blockPos, int playerIndex, boolean isSpectating, GameServerState server, EntityPlayer entityPlayer) {
      this.blockPos = blockPos;
      this.playerIndex = playerIndex;
      this.isSpectating = isSpectating;
      this.gamePhase = server.getGamePhase();
      this.isMyTurn = server.getCurrentTurn() == playerIndex;
      this.myState = new PlayerClientMyState(server.getPlayer(playerIndex), server.getGamePhase(), server, isSpectating);
      this.oppState = new PlayerClientOpponentState(server.getOpponent(playerIndex), server.getGamePhase(), server, false);
      this.log = server.getLog();
      this.turnCount = server.getTurnCount();
      if (server.getCoinFlip() != null && server.getRevealedCoinFlipResults() <= server.getCoinFlip().getResults().size()) {
         List truncatedResults = new ArrayList();

         for(int i = 0; i < server.getRevealedCoinFlipResults(); ++i) {
            truncatedResults.add(server.getCoinFlip().getResults().get(i));
         }

         this.coinFlip = new CoinFlipState(truncatedResults, server.getCoinFlip().getPlayerIndex());
      }

      this.server = server;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.gamePhase = GamePhase.values()[buf.readInt()];
      this.playerIndex = buf.readInt();
      this.isMyTurn = buf.readBoolean();
      this.myState = new PlayerClientMyState(buf, this.server);
      this.oppState = new PlayerClientOpponentState(buf, this.server);
      this.log = new DuelLog(buf);
      this.turnCount = buf.readInt();
      if (buf.readBoolean()) {
         List coinFlipResults = new ArrayList();
         int flipCount = buf.readInt();

         int playerIndex;
         for(playerIndex = 0; playerIndex < flipCount; ++playerIndex) {
            coinFlipResults.add(CoinSide.values()[buf.readInt()]);
         }

         playerIndex = buf.readInt();
         this.coinFlip = new CoinFlipState(coinFlipResults, playerIndex);
      }

   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeInt(this.gamePhase.ordinal());
      buf.writeInt(this.playerIndex);
      buf.writeBoolean(this.isMyTurn);
      this.myState.write(buf);
      this.oppState.write(buf);
      this.log.write(buf, this.gamePhase, this.isSpectating ? -1 : this.playerIndex, this.isMyTurn);
      buf.writeInt(this.turnCount);
      buf.writeBoolean(this.coinFlip != null);
      if (this.coinFlip != null) {
         buf.writeInt(this.coinFlip.getResults().size());
         Iterator var2 = this.coinFlip.getResults().iterator();

         while(var2.hasNext()) {
            CoinSide result = (CoinSide)var2.next();
            buf.writeInt(result.ordinal());
         }

         buf.writeInt(this.coinFlip.getPlayerIndex());
      }

   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(GameStateSyncPacket msg, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            EntityPlayer e = mc.field_71439_g;
            if (mc.field_71441_e != null) {
               World w = mc.field_71441_e;
               TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
               if (b != null) {
                  b.setClientSideValues(msg.gamePhase, msg.playerIndex, msg.isMyTurn, msg.myState, msg.oppState, msg.log, msg.turnCount, msg.coinFlip);
               }
            }
         }

         return null;
      }
   }
}
