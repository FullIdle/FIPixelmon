package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.items.ItemBottleCap;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SelectStatPacket implements IMessage {
   private int entityId;
   private StatsType type;

   public SelectStatPacket() {
   }

   public SelectStatPacket(int entityId, StatsType type) {
      this.entityId = entityId;
      this.type = type;
   }

   public void fromBytes(ByteBuf buf) {
      this.entityId = buf.readInt();
      this.type = StatsType.getStatValues()[buf.readByte()];
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeByte(this.type.ordinal() - 1);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SelectStatPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         Entity entity = player.field_70170_p.func_73045_a(message.entityId);
         ItemStack hand = player.func_184586_b(EnumHand.MAIN_HAND);
         if (entity instanceof EntityPixelmon && hand.func_77973_b() == PixelmonItems.silverBottleCap) {
            EntityPixelmon pixelmon = (EntityPixelmon)entity;
            if (pixelmon.getPokemonData().getOwnerPlayer() == player && ItemBottleCap.onSilverSelection(player, pixelmon.getPokemonData(), message.type) && !player.field_71075_bZ.field_75098_d) {
               hand.func_190918_g(1);
            }
         }

      }
   }
}
