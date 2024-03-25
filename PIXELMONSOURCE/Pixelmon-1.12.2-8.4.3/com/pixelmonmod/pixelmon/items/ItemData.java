package com.pixelmonmod.pixelmon.items;

import java.util.Objects;
import net.minecraft.item.Item;

public class ItemData {
   public int id;
   public int count;

   public ItemData(int id, int count) {
      this.id = id;
      this.count = count;
   }

   public ItemData(Item item, int count) {
      this.id = Item.func_150891_b(item);
      this.count = count;
   }

   public Item getItem() {
      return Item.func_150899_d(this.id);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ItemData itemData = (ItemData)o;
         return this.id == itemData.id && this.count == itemData.count;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.id, this.count});
   }
}
