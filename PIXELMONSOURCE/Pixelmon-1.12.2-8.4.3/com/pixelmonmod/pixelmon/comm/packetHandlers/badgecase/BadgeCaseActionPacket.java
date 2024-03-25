package com.pixelmonmod.pixelmon.comm.packetHandlers.badgecase;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.screens.BadgeCaseEvent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.items.ItemBadgeCase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BadgeCaseActionPacket implements IMessage {
   Action action;
   int index1;
   int index2;
   boolean custom;

   public BadgeCaseActionPacket() {
   }

   public BadgeCaseActionPacket(Action action, boolean custom) {
      this.action = action;
      this.custom = custom;
   }

   public BadgeCaseActionPacket(int index1, boolean custom) {
      this.action = BadgeCaseActionPacket.Action.REMOVE;
      this.index1 = index1;
      this.custom = custom;
   }

   public BadgeCaseActionPacket(int index1, int index2, boolean custom) {
      this.action = BadgeCaseActionPacket.Action.SWAP;
      this.index1 = index1;
      this.index2 = index2;
      this.custom = custom;
   }

   public void fromBytes(ByteBuf buf) {
      this.action = BadgeCaseActionPacket.Action.values()[buf.readByte()];
      if (this.action == BadgeCaseActionPacket.Action.REMOVE || this.action == BadgeCaseActionPacket.Action.SWAP) {
         this.index1 = buf.readInt();
      }

      if (this.action == BadgeCaseActionPacket.Action.SWAP) {
         this.index2 = buf.readInt();
      }

      this.custom = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.action.ordinal());
      if (this.action == BadgeCaseActionPacket.Action.REMOVE || this.action == BadgeCaseActionPacket.Action.SWAP) {
         buf.writeInt(this.index1);
      }

      if (this.action == BadgeCaseActionPacket.Action.SWAP) {
         buf.writeInt(this.index2);
      }

      buf.writeBoolean(this.custom);
   }

   public static enum Action {
      REGISTER,
      SWAP,
      REMOVE;
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(BadgeCaseActionPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         ItemStack hand = player.func_184614_ca();
         boolean isVaild = !message.custom && hand.func_77973_b() instanceof ItemBadgeCase;
         if (message.action == BadgeCaseActionPacket.Action.REGISTER) {
            if (!Pixelmon.EVENT_BUS.post(new BadgeCaseEvent.Register(player, message.custom)) && isVaild) {
               ItemBadgeCase.registerBadgeCase(hand, player);
            }
         } else if (message.action == BadgeCaseActionPacket.Action.REMOVE) {
            if (!Pixelmon.EVENT_BUS.post(new BadgeCaseEvent.Remove(player, message.custom, message.index1)) && isVaild) {
               ItemStack stack = ItemBadgeCase.removeBadge(hand, player, message.index1);
               DropItemHelper.giveItemStack(player, stack, false);
            }
         } else if (message.action == BadgeCaseActionPacket.Action.SWAP && !Pixelmon.EVENT_BUS.post(new BadgeCaseEvent.Swap(player, message.custom, message.index1, message.index2)) && isVaild) {
            ItemBadgeCase.swampBadge(hand, player, message.index1, message.index2);
         }

         if (isVaild) {
            ItemBadgeCase.openBadgeCase(hand, player);
         }

      }
   }
}
