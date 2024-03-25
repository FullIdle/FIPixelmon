package com.pixelmonmod.pixelmon.api.spawning;

import com.pixelmonmod.pixelmon.api.world.MutableLocation;
import com.pixelmonmod.pixelmon.enums.EnumBerryFlavor;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.biome.Biome;

public class SpawnLocation {
   public Entity cause;
   public MutableLocation location;
   public Set types;
   public Block baseBlock;
   public Set uniqueSurroundingBlocks;
   public Biome biome;
   public boolean seesSky;
   public int light;
   public int diameter;
   public EnumBerryFlavor flavor;

   public SpawnLocation(MutableLocation location, Set types, Block baseBlock, Set uniqueSurroundingBlocks, Biome biome, boolean seesSky, int diameter, int light) {
      this((Entity)null, location, types, baseBlock, uniqueSurroundingBlocks, biome, seesSky, diameter, light);
   }

   public SpawnLocation(Entity cause, MutableLocation location, Set types, Block baseBlock, Set uniqueSurroundingBlocks, Biome biome, boolean seesSky, int diameter, int light) {
      this.flavor = null;
      this.cause = cause;
      this.location = location;
      this.types = types;
      this.baseBlock = baseBlock;
      this.uniqueSurroundingBlocks = uniqueSurroundingBlocks;
      this.biome = biome;
      this.seesSky = seesSky;
      this.diameter = diameter;
      this.light = light;
   }

   public SpawnLocation clone() {
      return new SpawnLocation(this.cause, this.location, this.types, this.baseBlock, this.uniqueSurroundingBlocks, this.biome, this.seesSky, this.diameter, this.light);
   }
}
