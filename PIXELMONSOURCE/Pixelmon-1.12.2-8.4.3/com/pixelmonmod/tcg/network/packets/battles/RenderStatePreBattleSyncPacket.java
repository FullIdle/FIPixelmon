package com.pixelmonmod.tcg.network.packets.battles;

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

public class RenderStatePreBattleSyncPacket implements IMessage {
   private BlockPos blockPos;
   private boolean isShadowGame;

   public RenderStatePreBattleSyncPacket() {
   }

   public RenderStatePreBattleSyncPacket(BlockPos blockPos, ServerBattleController bc) {
      this.blockPos = blockPos;
      this.isShadowGame = bc.isShadowGame();
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.isShadowGame = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeBoolean(this.isShadowGame);
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(RenderStatePreBattleSyncPacket msg, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            EntityPlayer e = mc.field_71439_g;
            if (mc.field_71441_e != null) {
               World w = mc.field_71441_e;
               TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
               if (b != null) {
                  b.setShadowGame(msg.isShadowGame);
               }
            }
         }

         return null;
      }
   }
}
