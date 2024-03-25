package com.pixelmonmod.pixelmon.entities.pixelmon.drops;

import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class DroppedItem {
   public ItemStack itemStack;
   public EnumBossMode rarity;
   public int id;

   public DroppedItem(ItemStack item, int id) {
      this.rarity = EnumBossMode.NotBoss;
      this.itemStack = item;
      this.id = id;
   }

   public DroppedItem(ItemStack item, int id, EnumBossMode rarity) {
      this(item, id);
      this.rarity = rarity;
   }

   public void toBytes(ByteBuf buffer) {
      ByteBufUtils.writeItemStack(buffer, this.itemStack);
      buffer.writeInt(this.rarity.ordinal());
      buffer.writeInt(this.id);
   }

   public static DroppedItem fromBytes(ByteBuf buffer) {
      ItemStack itemStack = ByteBufUtils.readItemStack(buffer);
      EnumBossMode rarity = EnumBossMode.values()[buffer.readInt()];
      int id = buffer.readInt();
      return new DroppedItem(itemStack, id, rarity);
   }
}
