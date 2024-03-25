package com.pixelmonmod.pixelmon.api.item;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class JsonItemStack {
   public String itemID = "minecraft:fish";
   public Integer quantity = null;
   public Integer meta = null;
   public String nbt = null;
   public Float percentChance = null;
   private transient ItemStack stack = null;

   public JsonItemStack() {
   }

   public JsonItemStack(Item item) {
      this.itemID = item.getRegistryName().toString();
   }

   public ItemStack getItemStack() {
      if (this.stack == null) {
         Item item = null;
         if (this.itemID.contains(":")) {
            ResourceLocation resourceLocation = new ResourceLocation(this.itemID.split(":")[0], this.itemID.split(":")[1]);
            item = (Item)Item.field_150901_e.func_82594_a(resourceLocation);
            if (item == null) {
               Pixelmon.LOGGER.error("Invalid item ID found in SpawnInfo: " + this.itemID + " is not a recognized item.");
               return ItemStack.field_190927_a;
            }
         } else {
            ArrayList matchingItems = new ArrayList();
            Item.field_150901_e.forEach((testItem) -> {
               if (testItem.getRegistryName().func_110623_a().equalsIgnoreCase(this.itemID)) {
                  matchingItems.add(testItem);
               }

            });
            if (matchingItems.isEmpty()) {
               Pixelmon.LOGGER.error("Invalid item ID found in SpawnInfo: " + this.itemID + " is not a recognized item.");
               return ItemStack.field_190927_a;
            }

            if (matchingItems.size() > 1) {
               Pixelmon.LOGGER.error("Duplicate items found for item ID in SpawnInfo: " + this.itemID + ". Prefix this id with the resource domain, such as: 'minecraft:'");
               return ItemStack.field_190927_a;
            }

            item = (Item)matchingItems.get(0);
         }

         if (this.meta != null && this.meta != -1) {
            this.stack = new ItemStack(item, this.quantity == null ? 1 : this.quantity, this.meta);
         } else {
            this.stack = new ItemStack(item, this.quantity == null ? 1 : this.quantity);
         }

         NBTTagCompound deserializedNBT = null;
         if (this.nbt != null) {
            try {
               deserializedNBT = JsonToNBT.func_180713_a(this.nbt);
            } catch (NBTException var4) {
               Pixelmon.LOGGER.error("Bad NBT: " + this.nbt);
               var4.printStackTrace();
            }
         }

         if (deserializedNBT != null) {
            if (this.stack.func_77978_p() == null) {
               this.stack.func_77982_d(new NBTTagCompound());
            }

            this.stack.func_77982_d(deserializedNBT);
         }
      }

      return this.stack.func_77946_l();
   }

   public static ItemStack choose(List stacks) {
      float percentSum = 0.0F;
      Iterator var2 = stacks.iterator();

      while(var2.hasNext()) {
         JsonItemStack stack = (JsonItemStack)var2.next();
         if (stack.percentChance != null && stack.percentChance > 0.0F) {
            if (percentSum + stack.percentChance > 100.0F) {
               Pixelmon.LOGGER.warn("Itemstack percent chance is above 100%. Overflowed to: " + (percentSum + stack.percentChance));
               break;
            }

            percentSum += stack.percentChance;
         }
      }

      float chosenPercentage = RandomHelper.getRandomNumberBetween(0.0F, 100.0F);
      if (chosenPercentage > percentSum) {
         return null;
      } else {
         percentSum = 0.0F;
         Iterator var6 = stacks.iterator();

         while(var6.hasNext()) {
            JsonItemStack stack = (JsonItemStack)var6.next();
            if (stack.percentChance != null && stack.percentChance > 0.0F) {
               percentSum += stack.percentChance;
               if (percentSum >= chosenPercentage) {
                  return stack.getItemStack();
               }
            }
         }

         return null;
      }
   }
}
