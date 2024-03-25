package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.network.ByteBufTCG;
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

public class EndGamePacket implements IMessage {
   private BlockPos blockPos;

   public EndGamePacket() {
   }

   public EndGamePacket(BlockPos blockPos) {
      this.blockPos = blockPos;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(EndGamePacket msg, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            EntityPlayer e = mc.field_71439_g;
            if (mc.field_71441_e != null) {
               World w = mc.field_71441_e;
               TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
               if (b != null) {
                  b.resetClientGame();
                  e.func_71053_j();
                  Minecraft.func_71410_x().field_71474_y.field_74335_Z = TCGConfig.getInstance().savedUIScale;
               }
            }
         }

         return null;
      }
   }
}
