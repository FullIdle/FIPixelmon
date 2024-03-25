package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateServerCardRecordPacket implements IMessage {
   private BlockPos blockPos;
   private BoardLocation location;
   private int locationSubIndex;
   private ImmutableCard card;
   private int handIndex;

   public UpdateServerCardRecordPacket() {
   }

   public UpdateServerCardRecordPacket(BlockPos blockPos, BoardLocation location, int locationSubIndex, ImmutableCard card, int handIndex) {
      this.blockPos = blockPos;
      this.location = location;
      this.locationSubIndex = locationSubIndex;
      this.card = card;
      this.handIndex = handIndex;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.location = BoardLocation.values()[buf.readInt()];
      this.locationSubIndex = buf.readInt();
      this.card = ByteBufTCG.readCard(buf);
      this.handIndex = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeInt(this.location.ordinal());
      buf.writeInt(this.locationSubIndex);
      ByteBufTCG.writeCard(buf, this.card);
      buf.writeInt(this.handIndex);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UpdateServerCardRecordPacket msg, MessageContext ctx) {
         EntityPlayerMP e = ctx.getServerHandler().field_147369_b;
         e.func_71121_q().func_152344_a(() -> {
            WorldServer w = e.func_71121_q();
            TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
            PlayerServerState player = null;
            PlayerServerState[] var5 = b.getGameServer().getPlayers();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               PlayerServerState p = var5[var7];
               if (p != null && p.getEntityPlayer() == e) {
                  player = p;
               }
            }

            if (player != null) {
               switch (msg.location) {
                  case Active:
                     b.playCardFromHandToActive(player, msg.handIndex, msg.card);
                     return;
                  case Bench:
                     b.playPokemonCardToBench(player, msg.handIndex, msg.card, msg.locationSubIndex);
                     return;
                  case Trainer:
                     b.playTrainerCard(player, msg.handIndex, msg.card);
                     return;
                  case Stadium:
                     b.playStadiumCard(player, msg.handIndex, msg.card);
                     return;
                  default:
               }
            }
         });
         return null;
      }
   }
}
