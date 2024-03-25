package com.pixelmonmod.pixelmon.api.events.raids;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class JoinRaidEvent extends Event {
   private final RaidData raid;
   private final EntityPlayerMP player;
   private final Pokemon pokemon;

   public JoinRaidEvent(RaidData raid, EntityPlayerMP player, Pokemon pokemon) {
      this.raid = raid;
      this.player = player;
      this.pokemon = pokemon;
   }

   public RaidData getRaid() {
      return this.raid;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public Pokemon getPokemon() {
      return this.pokemon;
   }
}
