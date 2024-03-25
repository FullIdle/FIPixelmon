package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PixelmonTradeEvent extends Event {
   public final EntityPlayer player1;
   public final EntityPlayer player2;
   public final Pokemon pokemon1;
   public final Pokemon pokemon2;

   public PixelmonTradeEvent(EntityPlayer player1, EntityPlayer player2, Pokemon pokemon1, Pokemon pokemon2) {
      this.player1 = player1;
      this.player2 = player2;
      this.pokemon1 = pokemon1;
      this.pokemon2 = pokemon2;
   }
}
