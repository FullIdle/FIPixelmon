package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.StructureScattered;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PixelmonStructureGenerationEvent extends Event {
   private final World world;
   private final StructureScattered structureScattered;
   private final StructureInfo structureData;
   private final BlockPos pos;

   private PixelmonStructureGenerationEvent(World world, StructureScattered structureScattered, StructureInfo structure, BlockPos pos) {
      this.world = world;
      this.structureScattered = structureScattered;
      this.structureData = structure;
      this.pos = pos;
   }

   public World getWorld() {
      return this.world;
   }

   public StructureScattered getStructureScattered() {
      return this.structureScattered;
   }

   public StructureInfo getStructureData() {
      return this.structureData;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   // $FF: synthetic method
   PixelmonStructureGenerationEvent(World x0, StructureScattered x1, StructureInfo x2, BlockPos x3, Object x4) {
      this(x0, x1, x2, x3);
   }

   public static class Post extends PixelmonStructureGenerationEvent {
      public final boolean isGenerated;

      public Post(World world, StructureScattered structureScattered, StructureInfo structure, BlockPos pos, boolean isGenerated) {
         super(world, structureScattered, structure, pos, null);
         this.isGenerated = isGenerated;
      }
   }

   @Cancelable
   public static class Pre extends PixelmonStructureGenerationEvent {
      public Pre(World world, StructureScattered structureScattered, StructureInfo structure, BlockPos pos) {
         super(world, structureScattered, structure, pos, null);
      }
   }
}
