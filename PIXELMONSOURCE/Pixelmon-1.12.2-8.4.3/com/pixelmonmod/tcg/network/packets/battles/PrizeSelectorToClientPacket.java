package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
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

public class PrizeSelectorToClientPacket implements IMessage {
   private BlockPos blockPos;
   private int prizeIndex;
   private ImmutableCard prize;

   public PrizeSelectorToClientPacket() {
   }

   public PrizeSelectorToClientPacket(BlockPos blockPos, int prizeIndex, ImmutableCard prize) {
      this.blockPos = blockPos;
      this.prizeIndex = prizeIndex;
      this.prize = prize;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.prizeIndex = buf.readInt();
      this.prize = ByteBufTCG.readCard(buf);
   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeInt(this.prizeIndex);
      ByteBufTCG.writeCard(buf, this.prize);
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PrizeSelectorToClientPacket msg, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            EntityPlayer e = mc.field_71439_g;
            if (mc.field_71441_e != null) {
               World w = mc.field_71441_e;
               TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
               if (b != null) {
                  b.revealPrize(msg.prizeIndex, msg.prize);
               }
            }
         }

         return null;
      }
   }
}
