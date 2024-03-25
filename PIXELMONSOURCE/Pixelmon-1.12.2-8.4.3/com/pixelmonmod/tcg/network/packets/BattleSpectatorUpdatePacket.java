package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleSpectator;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BattleSpectatorUpdatePacket implements IMessage {
   private BlockPos pos;
   private BlockPos controllerPosition;
   private int playerIndex;

   public BattleSpectatorUpdatePacket() {
   }

   public BattleSpectatorUpdatePacket(TileEntityBattleSpectator spectator) {
      this.pos = spectator.func_174877_v();
      this.controllerPosition = spectator.getControllerPosition();
      this.playerIndex = spectator.getPlayerIndex();
   }

   public void fromBytes(ByteBuf buf) {
      this.pos = ByteBufTCG.readBlockPos(buf);
      this.controllerPosition = ByteBufTCG.readBlockPos(buf);
      this.playerIndex = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.pos);
      ByteBufTCG.writeBlockPos(buf, this.controllerPosition);
      buf.writeInt(this.playerIndex);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(BattleSpectatorUpdatePacket message, MessageContext ctx) {
         FMLCommonHandler.instance().getMinecraftServerInstance().func_152344_a(() -> {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            TileEntityBattleSpectator tileEntity = (TileEntityBattleSpectator)player.field_70170_p.func_175625_s(message.pos);
            if (tileEntity != null) {
               tileEntity.setControllerPosition(message.controllerPosition);
               tileEntity.setPlayerIndex(message.playerIndex);
               tileEntity.func_70296_d();
               player.func_184102_h().func_130014_f_().func_175684_a(message.pos, player.func_71121_q().func_180495_p(message.pos).func_177230_c(), 0);
            }

         });
         return null;
      }
   }
}
