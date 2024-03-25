package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CustomGUIChoiceToServerPacket implements IMessage {
   private BlockPos blockPos;
   private boolean isOpened;
   private int[] result;

   public CustomGUIChoiceToServerPacket() {
   }

   public CustomGUIChoiceToServerPacket(BlockPos blockPos, boolean isOpened, int[] result) {
      this.blockPos = blockPos;
      this.isOpened = isOpened;
      this.result = result;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.isOpened = buf.readBoolean();
      if (buf.readBoolean()) {
         this.result = new int[buf.readInt()];

         for(int i = 0; i < this.result.length; ++i) {
            this.result[i] = buf.readInt();
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeBoolean(this.isOpened);
      buf.writeBoolean(this.result != null);
      if (this.result != null) {
         buf.writeInt(this.result.length);
         int[] var2 = this.result;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            int item = var2[var4];
            buf.writeInt(item);
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CustomGUIChoiceToServerPacket msg, MessageContext ctx) {
         EntityPlayerMP e = ctx.getServerHandler().field_147369_b;
         e.func_71121_q().func_152344_a(() -> {
            WorldServer w = e.func_71121_q();
            TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
            b.setCustomGUIResult(e, msg.isOpened, msg.result);
         });
         return null;
      }
   }
}
