package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ThrowPokeballEvent extends Event {
   public EntityPlayer player;
   public ItemStack itemStack;
   public EnumPokeballs type;
   public boolean usedInBattle;

   public ThrowPokeballEvent(EntityPlayer player, ItemStack itemStack, EnumPokeballs type, boolean usedInBattle) {
      this.player = player;
      this.itemStack = itemStack;
      this.type = type;
      this.usedInBattle = usedInBattle;
   }

   public boolean isCancelable() {
      return !this.usedInBattle;
   }

   public void setCanceled(boolean cancel) {
      if (this.usedInBattle && cancel) {
         throw new IllegalArgumentException("You cannot cancel this event when it is from in battle");
      } else {
         super.setCanceled(cancel);
      }
   }
}
