package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PickupEvent extends Event {
   public final PixelmonWrapper pokemon;
   public final PlayerParticipant player;
   public ItemStack stack;

   public PickupEvent(PixelmonWrapper pokemon, PlayerParticipant player, ItemStack stack) {
      this.pokemon = pokemon;
      this.player = player;
      this.stack = stack;
   }
}
