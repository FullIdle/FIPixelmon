package com.pixelmonmod.pixelmon.worldGeneration.structure.worldGen;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.PixelmonStructureGenerationEvent;
import com.pixelmonmod.pixelmon.worldGeneration.structure.GeneratorHelper;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureRegistry;
import com.pixelmonmod.pixelmon.worldGeneration.structure.standalone.StandaloneStructureInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.StructureScattered;
import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenScatteredFeature extends MapGenScatteredFeature implements IWorldGenerator {
   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (random.nextInt(12) == 1) {
         int xPos = random.nextInt(16) + chunkX * 16;
         int yPos = 64;
         int zPos = random.nextInt(16) + chunkZ * 16;
         BlockPos pos = new BlockPos(xPos, yPos, zPos);
         StandaloneStructureInfo structure = StructureRegistry.getScatteredStructureFromBiome(random, world.func_180494_b(pos));
         if (structure != null) {
            StructureScattered s = structure.createStructure(random, pos, true, false, (EnumFacing)null, false);
            if (this.canSpawnStructureAtCoords(world, random, s, structure, pos)) {
               PixelmonStructureGenerationEvent generationEvent = new PixelmonStructureGenerationEvent.Post(world, s, structure, pos, s.generate(world, random));
               Pixelmon.EVENT_BUS.post(generationEvent);
            }

         }
      }
   }

   protected boolean canSpawnStructureAtCoords(World world, Random random, StructureScattered s, StandaloneStructureInfo structure, BlockPos pos) {
      if ((GeneratorHelper.isOverworld(world) || GeneratorHelper.isUltraSpace(world)) && RandomHelper.useRandomForNumberBetween(random, 0, 40) == 0) {
         PixelmonStructureGenerationEvent generationEvent = new PixelmonStructureGenerationEvent.Pre(world, s, structure, pos);
         Pixelmon.EVENT_BUS.post(generationEvent);
         return !generationEvent.isCanceled();
      } else {
         return false;
      }
   }
}
