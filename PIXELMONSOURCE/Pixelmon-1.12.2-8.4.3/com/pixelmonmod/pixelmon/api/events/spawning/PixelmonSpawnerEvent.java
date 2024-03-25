package com.pixelmonmod.pixelmon.api.events.spawning;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.blocks.spawning.TileEntityPixelmonSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PixelmonSpawnerEvent extends Event {
   public final TileEntityPixelmonSpawner spawner;
   public final PokemonSpec spec;
   public final BlockPos.MutableBlockPos pos;

   public PixelmonSpawnerEvent(TileEntityPixelmonSpawner spawner, PokemonSpec spec, BlockPos.MutableBlockPos pos) {
      this.spawner = spawner;
      this.spec = spec;
      this.pos = pos;
   }
}
