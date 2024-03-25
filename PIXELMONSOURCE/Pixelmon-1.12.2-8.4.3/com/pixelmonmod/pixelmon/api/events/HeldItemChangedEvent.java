package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class HeldItemChangedEvent extends Event {
   public final EntityPlayerMP player;
   public final Pokemon pokemon;
   @Nonnull
   public ItemStack newHeldItem;

   public HeldItemChangedEvent(@Nullable EntityPlayerMP player, Pokemon pokemon, ItemStack newHeldItem) {
      this.player = player;
      this.pokemon = pokemon;
      this.newHeldItem = newHeldItem;
   }

   public EnumSpecies getSpecies() {
      return this.pokemon.getSpecies();
   }
}
