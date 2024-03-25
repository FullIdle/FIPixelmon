package com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiItemDrops;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BattleGuiClosed;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DroppedItem;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.ITextComponent.Serializer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDropPacket implements IMessage {
   public DroppedItem[] items;
   public boolean hasCustomTitle;
   public ItemDropMode mode;
   public TextComponentTranslation customTitle;

   public ItemDropPacket() {
      this.hasCustomTitle = false;
   }

   public ItemDropPacket(ItemDropMode mode, ArrayList givenDrops) {
      this.hasCustomTitle = false;
      this.items = (DroppedItem[])givenDrops.toArray(new DroppedItem[0]);
      this.mode = mode;
   }

   public ItemDropPacket(ItemDropMode mode, TextComponentTranslation customTitle, ArrayList drops) {
      this(mode, drops);
      this.hasCustomTitle = true;
      this.customTitle = customTitle;
   }

   public ItemDropPacket(TextComponentTranslation customTitle, ArrayList drops) {
      this(ItemDropMode.Other, customTitle, drops);
   }

   public void fromBytes(ByteBuf buffer) {
      this.mode = ItemDropMode.values()[buffer.readInt()];
      this.items = new DroppedItem[buffer.readShort()];
      this.hasCustomTitle = buffer.readBoolean();
      int actualLength = this.items.length;

      for(int i = 0; i < this.items.length; ++i) {
         this.items[i] = DroppedItem.fromBytes(buffer);
         if (this.items[i] == null) {
            --actualLength;
         }
      }

      if (actualLength != this.items.length) {
         DroppedItem[] newItems = new DroppedItem[actualLength];
         int newItemIndex = 0;

         for(int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null && newItemIndex < actualLength) {
               newItems[newItemIndex++] = this.items[i];
            }
         }

         this.items = newItems;
      }

      if (this.hasCustomTitle) {
         PacketBuffer pb = new PacketBuffer(buffer);

         try {
            this.customTitle = (TextComponentTranslation)Serializer.func_150699_a(pb.func_150789_c(32767));
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.mode.ordinal());
      buffer.writeShort(this.items.length);
      buffer.writeBoolean(this.hasCustomTitle);
      DroppedItem[] var2 = this.items;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         DroppedItem item = var2[var4];
         item.toBytes(buffer);
      }

      if (this.hasCustomTitle) {
         PacketBuffer pb = new PacketBuffer(buffer);

         try {
            pb.func_180714_a(Serializer.func_150696_a(this.customTitle));
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ItemDropPacket message, MessageContext ctx) {
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(ItemDropPacket message) {
         if (!PixelmonConfig.useDropGUI && message.mode == ItemDropMode.NormalPokemon) {
            Pixelmon.network.sendToServer(new ServerItemDropPacket(ServerItemDropPacket.PacketMode.TakeAllItems));
            Pixelmon.network.sendToServer(new BattleGuiClosed());
         } else {
            ServerStorageDisplay.bossDrops = message;
            if (ClientProxy.battleManager.battleEnded && !ClientProxy.battleManager.hasMoreMessages()) {
               Minecraft.func_71410_x().func_147108_a(new GuiItemDrops());
            }
         }

      }
   }
}
