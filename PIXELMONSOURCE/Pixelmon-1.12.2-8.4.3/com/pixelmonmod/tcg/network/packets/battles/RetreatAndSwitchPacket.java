package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RetreatAndSwitchPacket implements IMessage {
   private BlockPos blockPos;
   private boolean[] energySelection;
   private int benchIndex;

   public RetreatAndSwitchPacket() {
   }

   public RetreatAndSwitchPacket(BlockPos blockPos, boolean[] energySelection, int benchIndex) {
      this.blockPos = blockPos;
      this.energySelection = energySelection;
      this.benchIndex = benchIndex;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.energySelection = new boolean[buf.readInt()];

      for(int i = 0; i < this.energySelection.length; ++i) {
         this.energySelection[i] = buf.readBoolean();
      }

      this.benchIndex = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeInt(this.energySelection.length);
      boolean[] var2 = this.energySelection;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         boolean selection = var2[var4];
         buf.writeBoolean(selection);
      }

      buf.writeInt(this.benchIndex);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RetreatAndSwitchPacket msg, MessageContext ctx) {
         EntityPlayerMP e = ctx.getServerHandler().field_147369_b;
         e.func_71121_q().func_152344_a(() -> {
            WorldServer w = e.func_71121_q();
            TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
            GameServerState server = b.getGameServer();
            PlayerServerState player = b.getGameServer().getPlayer(b.getGameServer().getCurrentTurn());
            if ((b.getGameServer().getGamePhase() == GamePhase.NormalTurn || b.getGameServer().getGamePhase() == GamePhase.FirstTurn) && player != null && player.getEntityPlayer() == e && server.isCurrentTurn(player)) {
               List attachments = new ArrayList();
               List energies = (List)player.getActiveCard().getAttachments().stream().filter(CommonCardState::isEnergyEquivalence).collect(Collectors.toList());

               for(int i = 0; i < energies.size(); ++i) {
                  if (msg.energySelection[i]) {
                     attachments.add(player.getActiveCard().getAttachments().get(i));
                  }
               }

               b.requestRetreatAndSwitch(player, attachments, msg.benchIndex);
            }

         });
         return null;
      }
   }
}
