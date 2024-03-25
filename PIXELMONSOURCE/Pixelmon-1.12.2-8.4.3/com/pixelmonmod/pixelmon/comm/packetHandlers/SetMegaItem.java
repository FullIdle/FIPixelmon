package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetMegaItem implements IMessage {
   EnumMegaItem megaItem;

   public SetMegaItem() {
   }

   public SetMegaItem(EnumMegaItem megaItem) {
      this.megaItem = megaItem;
   }

   public void fromBytes(ByteBuf buffer) {
      this.megaItem = EnumMegaItem.values()[buffer.readInt()];
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.megaItem.ordinal());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetMegaItem message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
         if (storage.canEquipMegaItem() && (message.megaItem.canEvolve() && storage.getMegaItemsUnlocked().canMega() || message.megaItem.canDynamax() && storage.getMegaItemsUnlocked().canDynamax() || message.megaItem == EnumMegaItem.None)) {
            storage.setMegaItem(message.megaItem, false);
         }

         return null;
      }
   }
}
