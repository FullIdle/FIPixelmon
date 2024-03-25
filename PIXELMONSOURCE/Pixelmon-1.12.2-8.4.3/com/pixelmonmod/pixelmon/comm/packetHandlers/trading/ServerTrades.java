package com.pixelmonmod.pixelmon.comm.packetHandlers.trading;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTradeMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerTrades implements IMessage {
   EnumServerTradesMode mode;
   int pos;
   boolean ready;
   BlockPos tradeMachine;

   public ServerTrades() {
      this.pos = 0;
      this.ready = false;
   }

   public ServerTrades(EnumServerTradesMode mode, BlockPos tradeMachine) {
      this.pos = 0;
      this.ready = false;
      this.mode = mode;
      this.tradeMachine = tradeMachine;
   }

   public static ServerTrades getTradePacket(BlockPos tradeMachine) {
      return new ServerTrades(EnumServerTradesMode.Trade, tradeMachine);
   }

   public static ServerTrades getDeRegisterPacket(BlockPos tradeMachine) {
      return new ServerTrades(EnumServerTradesMode.DeRegisterTrader, tradeMachine);
   }

   public ServerTrades(int pos, BlockPos tradeMachine) {
      this(EnumServerTradesMode.SelectPokemon, tradeMachine);
      this.pos = pos;
   }

   public ServerTrades(boolean ready, BlockPos tradeMachine) {
      this(EnumServerTradesMode.Ready, tradeMachine);
      this.ready = ready;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.mode.ordinal());
      buffer.writeInt(this.pos);
      buffer.writeBoolean(this.ready);
      buffer.writeLong(this.tradeMachine.func_177986_g());
   }

   public void fromBytes(ByteBuf buffer) {
      int ind = buffer.readShort();
      EnumServerTradesMode[] var3 = EnumServerTradesMode.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumServerTradesMode m = var3[var5];
         if (m.ordinal() == ind) {
            this.mode = m;
         }
      }

      this.pos = buffer.readInt();
      this.ready = buffer.readBoolean();
      this.tradeMachine = BlockPos.func_177969_a(buffer.readLong());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ServerTrades message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         BlockPos p = message.tradeMachine;
         if (player.func_70092_e((double)p.func_177958_n() + 0.5, (double)p.func_177956_o() + 0.5, (double)p.func_177952_p() + 0.5) <= 64.0) {
            player.func_71121_q().func_152344_a(() -> {
               TileEntity te = player.func_71121_q().func_175625_s(p);
               if (te != null && te instanceof TileEntityTradeMachine) {
                  TileEntityTradeMachine tradeMachine = (TileEntityTradeMachine)te;
                  if (message.mode == EnumServerTradesMode.SelectPokemon) {
                     if (tradeMachine.player1 == player) {
                        tradeMachine.setPos1(message.pos);
                     } else if (tradeMachine.player2 == player) {
                        tradeMachine.setPos2(message.pos);
                     }
                  } else if (message.mode == EnumServerTradesMode.DeRegisterTrader) {
                     tradeMachine.removePlayer(player);
                  } else if (message.mode == EnumServerTradesMode.Ready) {
                     tradeMachine.ready(player, message.ready);
                  } else if (message.mode == EnumServerTradesMode.Trade) {
                     tradeMachine.trade();
                  }
               }

            });
         }

         return null;
      }
   }
}
