package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class LevelUpEvent extends Event {
   public final EntityPlayerMP player;
   public final PokemonLink pokemon;
   public final int newLevel;

   public LevelUpEvent(EntityPlayerMP player, PokemonLink pokemon, int newLevel) {
      this.player = player;
      this.pokemon = pokemon;
      this.newLevel = newLevel;
   }
}
