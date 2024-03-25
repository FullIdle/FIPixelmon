package com.pixelmonmod.pixelmon.api.events.pokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.items.EnumBottleCap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BottleCapEvent extends Event {
   private final EntityPixelmon pixelmon;
   private final EntityPlayer player;
   private final EnumBottleCap bottleCap;
   private final ItemStack itemStack;

   public BottleCapEvent(EntityPixelmon pixelmon, EntityPlayer player, EnumBottleCap bottleCap, ItemStack itemStack) {
      this.pixelmon = pixelmon;
      this.player = player;
      this.bottleCap = bottleCap;
      this.itemStack = itemStack;
   }

   public EntityPixelmon getPixelmon() {
      return this.pixelmon;
   }

   public Pokemon getPokemon() {
      return this.pixelmon.getPokemonData();
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }

   public EnumBottleCap getBottleCap() {
      return this.bottleCap;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public Item getItem() {
      return this.itemStack.func_77973_b();
   }
}
