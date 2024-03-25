package com.pixelmonmod.pixelmon.api.events.blocks;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class CloningCompleteEvent extends Event {
   private final Pokemon mew;
   private final EntityPixelmon resultPokemon;
   private final EntityPlayer player;
   private final BlockPos blockPos;

   public CloningCompleteEvent(Pokemon mew, EntityPixelmon resultPokemon, EntityPlayer player, BlockPos blockPos) {
      this.mew = mew;
      this.resultPokemon = resultPokemon;
      this.player = player;
      this.blockPos = blockPos;
   }

   public Pokemon getMew() {
      return this.mew;
   }

   public EntityPixelmon getResultPokemon() {
      return this.resultPokemon;
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }
}
