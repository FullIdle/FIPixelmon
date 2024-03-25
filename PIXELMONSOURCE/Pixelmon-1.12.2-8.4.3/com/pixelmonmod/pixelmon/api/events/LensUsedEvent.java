package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.ItemHirokusLens;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class LensUsedEvent extends Event {
   public boolean shouldItemBeDamaged = true;
   public final EntityPlayerMP player;
   public final EntityPixelmon pixelmon;
   public final ItemStack stack;
   public final ItemHirokusLens lens;

   public LensUsedEvent(EntityPlayerMP player, EntityPixelmon pixelmon, ItemStack stack) {
      this.player = player;
      this.pixelmon = pixelmon;
      this.stack = stack;
      this.lens = (ItemHirokusLens)stack.func_77973_b();
   }

   public void setCanceled(boolean cancelled) {
      super.setCanceled(cancelled);
      if (cancelled && this.shouldItemBeDamaged) {
         this.shouldItemBeDamaged = false;
      }

   }
}
