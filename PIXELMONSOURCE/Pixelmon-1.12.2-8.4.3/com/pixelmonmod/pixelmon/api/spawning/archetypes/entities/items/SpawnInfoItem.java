package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.items;

import com.pixelmonmod.pixelmon.api.item.JsonItemStack;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import net.minecraft.item.ItemStack;

public class SpawnInfoItem extends SpawnInfo {
   public static final String TYPE_ID_ITEM = "item";
   public JsonItemStack item;
   public int minQuantity = 1;
   public int maxQuantity = 1;
   public transient ItemStack itemStack;

   public SpawnInfoItem() {
      super("item");
      this.itemStack = ItemStack.field_190927_a;
   }

   public void onImport() {
      super.onImport();
      if (this.item != null) {
         this.itemStack = this.item.getItemStack();
      }

   }

   public SpawnAction construct(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      return new SpawnActionItem(this, spawnLocation);
   }

   public String toString() {
      return this.itemStack.func_82833_r();
   }
}
