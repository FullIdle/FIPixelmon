package com.pixelmonmod.pixelmon.api.events.pokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EVsGainedEvent extends Event {
   public final Pokemon pokemon;
   public final EVStore evStore;
   public final int[] evs;

   public EVsGainedEvent(Pokemon pokemon, EVStore evStore, int[] evs) {
      this.pokemon = pokemon;
      this.evStore = evStore;
      this.evs = evs;
   }

   @Cancelable
   public static class Item extends EVsGainedEvent {
      private final ItemStack itemStack;

      public Item(Pokemon pokemon, EVStore evStore, int ev, StatsType type, ItemStack itemStack) {
         super(pokemon, evStore, createArray(ev, type));
         this.itemStack = itemStack;
      }

      public ItemStack getItemStack() {
         return this.itemStack;
      }

      private static int[] createArray(int amount, StatsType type) {
         return new int[]{type == StatsType.HP ? amount : 0, type == StatsType.Attack ? amount : 0, type == StatsType.Defence ? amount : 0, type == StatsType.SpecialAttack ? amount : 0, type == StatsType.SpecialDefence ? amount : 0, type == StatsType.Speed ? amount : 0};
      }
   }
}
