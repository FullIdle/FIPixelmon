package com.pixelmonmod.pixelmon.api.events.pokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ItemInteractionEvent extends Event {
   private final EntityPlayer player;
   private final EntityPixelmon pixelmon;
   private final ItemStack item;

   public ItemInteractionEvent(EntityPlayer player, EntityPixelmon pixelmon, ItemStack item) {
      this.player = player;
      this.pixelmon = pixelmon;
      this.item = item;
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }

   public EntityPixelmon getPixelmon() {
      return this.pixelmon;
   }

   public Pokemon getPokemon() {
      return this.pixelmon.getPokemonData();
   }

   public ItemStack getItemStack() {
      return this.item;
   }

   public Item getItem() {
      return this.item.func_77973_b();
   }
}
