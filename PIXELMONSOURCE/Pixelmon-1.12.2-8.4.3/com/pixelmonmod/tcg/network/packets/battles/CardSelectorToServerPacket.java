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

public class CardSelectorToServerPacket implements IMessage {
   private BlockPos blockPos;
   private boolean isOpened;
   private boolean[] isSelected;

   public CardSelectorToServerPacket() {
   }

   public CardSelectorToServerPacket(BlockPos blockPos, boolean isOpened, boolean[] isSelected) {
      this.blockPos = blockPos;
      this.isOpened = isOpened;
      this.isSelected = isSelected;
   }

   public void fromBytes(ByteBuf buf) {
      this.blockPos = ByteBufTCG.readBlockPos(buf);
      this.isOpened = buf.readBoolean();
      if (buf.readBoolean()) {
         this.isSelected = new boolean[buf.readInt()];

         for(int i = 0; i < this.isSelected.length; ++i) {
            this.isSelected[i] = buf.readBoolean();
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      ByteBufTCG.writeBlockPos(buf, this.blockPos);
      buf.writeBoolean(this.isOpened);
      buf.writeBoolean(this.isSelected != null);
      if (this.isSelected != null) {
         buf.writeInt(this.isSelected.length);
         boolean[] var2 = this.isSelected;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            boolean anIsSelected = var2[var4];
            buf.writeBoolean(anIsSelected);
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CardSelectorToServerPacket msg, MessageContext ctx) {
         EntityPlayerMP e = ctx.getServerHandler().field_147369_b;
         e.func_71121_q().func_152344_a(() -> {
            WorldServer w = e.func_71121_q();
            TileEntityBattleController b = (TileEntityBattleController)w.func_175625_s(msg.blockPos);
            b.setCardSelection(e, msg.isOpened, msg.isSelected);
         });
         return null;
      }
   }
}
