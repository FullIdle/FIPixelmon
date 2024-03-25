package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.items;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class SpawnActionItem extends SpawnAction {
   public ItemStack stack = null;

   public SpawnActionItem(SpawnInfoItem spawnInfo, SpawnLocation spawnLocation) {
      super(spawnInfo, spawnLocation);
      if (spawnInfo.itemStack != null) {
         this.stack = spawnInfo.itemStack.func_77946_l();
         if (spawnInfo.item.quantity == null) {
            this.stack.func_190920_e(RandomHelper.getRandomNumberBetween(spawnInfo.minQuantity, spawnInfo.maxQuantity));
         }
      }

   }

   protected EntityItem createEntity() {
      return this.stack != null ? new EntityItem(this.spawnLocation.location.world, (double)this.spawnLocation.location.pos.func_177958_n(), (double)this.spawnLocation.location.pos.func_177956_o(), (double)this.spawnLocation.location.pos.func_177952_p(), this.stack) : null;
   }
}
