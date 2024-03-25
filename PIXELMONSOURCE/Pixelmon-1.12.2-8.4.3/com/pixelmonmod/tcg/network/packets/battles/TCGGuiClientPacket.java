package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.gui.duel.GuiTCG;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCGGuiClientPacket implements IMessage {
   private int playerIndex;
   private BlockPos blockPos;
   private boolean isSpectating;

   public TCGGuiClientPacket() {
   }

   public TCGGuiClientPacket(BlockPos blockPos, int playerIndex, boolean isSpectating) {
      this.blockPos = blockPos;
      this.playerIndex = playerIndex;
      this.isSpectating = isSpectating;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.playerIndex = buf.readInt();
      this.isSpectating = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeInt(this.playerIndex);
      buf.writeBoolean(this.isSpectating);
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(TCGGuiClientPacket msg, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            if (mc.field_71441_e != null) {
               World w = mc.field_71441_e;
               TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
               if (b != null && !(mc.field_71462_r instanceof GuiTCG)) {
                  TCGConfig.getInstance().savedUIScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
                  Minecraft.func_71410_x().field_71474_y.field_74335_Z = 4;
                  GuiTCG gui = new GuiTCG(b, msg.playerIndex, msg.isSpectating);
                  b.setGui(gui);
                  mc.func_147108_a(gui);
               }
            }
         }

         return null;
      }
   }
}
