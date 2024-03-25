package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class EvolveEvent extends Event {
   public final EntityPlayerMP player;
   public final EntityPixelmon preEvo;
   public PokemonSpec postEvo;
   public final Evolution evo;

   private EvolveEvent(EntityPlayerMP player, EntityPixelmon preEvo, Evolution evo) {
      this.player = player;
      this.preEvo = preEvo;
      this.postEvo = evo.to.copy();
      this.evo = evo;
   }

   // $FF: synthetic method
   EvolveEvent(EntityPlayerMP x0, EntityPixelmon x1, Evolution x2, Object x3) {
      this(x0, x1, x2);
   }

   public static class PostEvolve extends EvolveEvent {
      public final EntityPixelmon pokemon;

      public PostEvolve(EntityPlayerMP player, EntityPixelmon preEvo, Evolution evo, EntityPixelmon pokemon) {
         super(player, preEvo, evo, null);
         this.pokemon = pokemon;
      }
   }

   @Cancelable
   public static class PreEvolve extends EvolveEvent {
      public PreEvolve(EntityPlayerMP player, EntityPixelmon preEvo, Evolution evo) {
         super(player, preEvo, evo, null);
      }
   }
}
