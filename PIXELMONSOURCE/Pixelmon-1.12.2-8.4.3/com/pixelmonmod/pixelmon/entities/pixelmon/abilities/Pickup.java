package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.PickupEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import net.minecraft.item.ItemStack;

public class Pickup extends AbilityBase {
   private ItemHeld consumedItem;
   private PixelmonWrapper consumer;

   public Pickup() {
      this.consumedItem = NoItem.noItem;
   }

   public void onItemConsumed(PixelmonWrapper pokemon, PixelmonWrapper consumer, ItemHeld heldItem) {
      if (pokemon != consumer && !pokemon.bc.simulateMode) {
         this.consumedItem = heldItem;
         this.consumer = consumer;
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon != this.consumer && !pokemon.bc.simulateMode) {
         if (!pokemon.hasHeldItem() && this.consumedItem != NoItem.noItem && this.consumer != null && this.consumer.getConsumedItem() != NoItem.noItem && pokemon.bc.getActivePokemon().contains(this.consumer)) {
            pokemon.setHeldItem(this.consumedItem);
            this.consumer.setConsumedItem((ItemHeld)null);
            pokemon.bc.sendToAll("pixelmon.abilities.pickup", pokemon.getNickname(), this.consumedItem.getLocalizedName());
            pokemon.enableReturnHeldItem();
         }

         this.consumedItem = null;
         this.consumer = null;
      }

   }

   public boolean needNewInstance() {
      return true;
   }

   public static void pickupItem(PlayerParticipant player) {
      if (PixelmonConfig.pickupRate > 0) {
         PixelmonWrapper[] var1 = player.allPokemon;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            PixelmonWrapper pw = var1[var3];
            if (pw.getAbility() instanceof Pickup && !pw.hasHeldItem() && RandomHelper.getRandomChance(1.0F / (float)PixelmonConfig.pickupRate)) {
               int level = pw.getLevelNum();
               ItemStack foundItem;
               if (level <= 50) {
                  foundItem = DropItemRegistry.getTier1Drop();
               } else {
                  int rand;
                  if (level <= 80) {
                     rand = RandomHelper.getRandomNumberBetween(1, 100);
                     if (rand < 91) {
                        foundItem = DropItemRegistry.getTier1Drop();
                     } else {
                        foundItem = DropItemRegistry.getTier2Drop();
                     }
                  } else {
                     rand = RandomHelper.getRandomNumberBetween(1, 100);
                     if (rand < 80) {
                        foundItem = DropItemRegistry.getTier1Drop();
                     } else if (rand < 100) {
                        foundItem = DropItemRegistry.getTier2Drop();
                     } else {
                        foundItem = DropItemRegistry.getTier3Drop();
                     }
                  }
               }

               if (foundItem != null) {
                  ItemStack itemCopy = foundItem.func_77946_l();
                  itemCopy.func_190920_e(1);
                  PickupEvent event = new PickupEvent(pw, player, itemCopy);
                  if (!Pixelmon.EVENT_BUS.post(event)) {
                     player.player.field_71071_by.func_70441_a(event.stack);
                  }
               }
            }
         }
      }

   }
}
