package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage;

import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumTrainerCardColor;
import com.pixelmonmod.pixelmon.storage.ClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateClientPlayerData implements IMessage {
   private ClientDataType type;
   private int playerMoney;
   private EnumMegaItem megaItem;
   private int giveChoice;
   private EnumTrainerCardColor color;
   private ItemStack lureStack;

   public UpdateClientPlayerData() {
   }

   public UpdateClientPlayerData(int playerMoney) {
      this.playerMoney = playerMoney;
      this.type = UpdateClientPlayerData.ClientDataType.Currency;
   }

   public UpdateClientPlayerData(EnumMegaItem megaItem, int openMegaItemGui) {
      this.megaItem = megaItem;
      this.type = UpdateClientPlayerData.ClientDataType.MegaItem;
      this.giveChoice = openMegaItemGui;
   }

   public UpdateClientPlayerData(EnumTrainerCardColor color) {
      this.type = UpdateClientPlayerData.ClientDataType.TrainerCardColor;
      this.color = color;
   }

   public UpdateClientPlayerData(ItemStack lureStack) {
      this.type = UpdateClientPlayerData.ClientDataType.Lure;
      this.lureStack = lureStack;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.type.ordinal());
      switch (this.type) {
         case Currency:
            buf.writeInt(this.playerMoney);
            break;
         case MegaItem:
            buf.writeInt(this.megaItem.ordinal());
            buf.writeInt(this.giveChoice);
            break;
         case TrainerCardColor:
            buf.writeInt(this.color.ordinal());
            break;
         case Lure:
            if (this.lureStack != null && !this.lureStack.func_190926_b()) {
               buf.writeBoolean(true);
               ByteBufUtils.writeItemStack(buf, this.lureStack);
            } else {
               buf.writeBoolean(false);
            }
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.type = UpdateClientPlayerData.ClientDataType.values()[buf.readInt()];
      switch (this.type) {
         case Currency:
            this.playerMoney = buf.readInt();
            break;
         case MegaItem:
            this.megaItem = EnumMegaItem.values()[buf.readInt()];
            this.giveChoice = buf.readInt();
            break;
         case TrainerCardColor:
            this.color = EnumTrainerCardColor.values()[buf.readInt()];
            break;
         case Lure:
            this.lureStack = buf.readBoolean() ? ByteBufUtils.readItemStack(buf) : ItemStack.field_190927_a;
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UpdateClientPlayerData message, MessageContext ctx) {
         switch (message.type) {
            case Currency:
               ClientData.playerMoney = message.playerMoney;
               break;
            case MegaItem:
               ClientData.openMegaItemGui = message.giveChoice;
               break;
            case TrainerCardColor:
               ClientData.color = message.color;
               break;
            case Lure:
               ClientStorageManager.party.setLureStack(message.lureStack);
         }

      }
   }

   public static enum ClientDataType {
      Currency,
      MegaItem,
      TrainerCardColor,
      Lure;
   }
}
