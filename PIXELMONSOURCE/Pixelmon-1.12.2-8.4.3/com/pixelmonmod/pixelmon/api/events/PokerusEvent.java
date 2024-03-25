package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumPokerusType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class PokerusEvent extends Event {
   public abstract static class Spread extends PokerusEvent {
      public final PlayerPartyStorage party;
      public final PixelmonWrapper wrapper;
      public final EnumPokerusType donorType;

      public Spread(PlayerPartyStorage party, PixelmonWrapper wrapper, EnumPokerusType donorType) {
         this.party = party;
         this.wrapper = wrapper;
         this.donorType = donorType;
      }

      public static class Post extends Spread {
         public Post(PlayerPartyStorage party, PixelmonWrapper wrapper, EnumPokerusType donorType) {
            super(party, wrapper, donorType);
         }
      }

      @Cancelable
      public static class Pre extends Spread {
         public Pre(PlayerPartyStorage party, PixelmonWrapper wrapper, EnumPokerusType donorType) {
            super(party, wrapper, donorType);
         }
      }
   }

   public static class Cured extends PokerusEvent {
      public final PlayerPartyStorage party;
      public final EntityPlayerMP player;
      public final Pokemon pokemon;

      public Cured(EntityPlayerMP player, Pokemon pokemon, PlayerPartyStorage party) {
         this.player = player;
         this.pokemon = pokemon;
         this.party = party;
      }
   }
}
