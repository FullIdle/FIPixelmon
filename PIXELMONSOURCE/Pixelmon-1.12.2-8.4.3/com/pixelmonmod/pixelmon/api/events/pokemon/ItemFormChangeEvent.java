package com.pixelmonmod.pixelmon.api.events.pokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ItemFormChangeEvent extends Event {
   public final EntityPlayerMP player;
   public final ItemStack item;
   public final EntityPixelmon pokemon;
   public final boolean isFusion;
   @Nullable
   public Pokemon fusion;

   public ItemFormChangeEvent(EntityPlayerMP player, ItemStack item, EntityPixelmon pokemon, @Nullable Pokemon fusion) {
      this.player = player;
      this.item = item;
      this.pokemon = pokemon;
      if (fusion == null) {
         this.isFusion = false;
         this.fusion = null;
      } else {
         this.isFusion = true;
         this.fusion = fusion;
      }

   }
}
