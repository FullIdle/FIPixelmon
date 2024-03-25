package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.blocks.BlockBerryTree;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.apricornTrees.BlockApricornTree;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBerryTree;
import com.pixelmonmod.pixelmon.config.PixelmonBlocksApricornTrees;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import com.pixelmonmod.pixelmon.worldGeneration.structure.GeneratorHelper;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenPixelmonTrees implements IWorldGenerator {
   private Block[] apricorns;

   public WorldGenPixelmonTrees() {
      this.apricorns = new Block[]{PixelmonBlocksApricornTrees.apricornTreeBlack, PixelmonBlocksApricornTrees.apricornTreeWhite, PixelmonBlocksApricornTrees.apricornTreePink, PixelmonBlocksApricornTrees.apricornTreeGreen, PixelmonBlocksApricornTrees.apricornTreeBlue, PixelmonBlocksApricornTrees.apricornTreeYellow, PixelmonBlocksApricornTrees.apricornTreeRed};
   }

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (world.field_73011_w.func_76569_d() || GeneratorHelper.isUltraSpace(world)) {
         Biome biome = world.func_180494_b(new BlockPos(chunkX * 16, 0, chunkZ * 16));
         if (BiomeDictionary.getTypes(biome).contains(Type.FOREST)) {
            int count = random.nextInt(4);

            for(int i = 0; i < count - 1; ++i) {
               int x = random.nextInt(16) + chunkX * 16;
               int z = random.nextInt(16) + chunkZ * 16;
               int y = world.func_175645_m(new BlockPos(x, 0, z)).func_177956_o();
               Material material = world.func_180495_p(new BlockPos(x, y - 1, z)).func_185904_a();
               if (material == Material.field_151577_b || material == Material.field_151578_c) {
                  if (random.nextBoolean()) {
                     this.generateApricorn(x, y, z, world);
                  } else {
                     this.generateBerry(x, y, z, world);
                  }
               }
            }
         }

      }
   }

   public void generateApricorn(int x, int y, int z, World world) {
      Block newBlock = (Block)RandomHelper.getRandomElementFromArray(this.apricorns);
      if (newBlock != null) {
         world.func_180501_a(new BlockPos(x, y, z), newBlock.func_176223_P().func_177226_a(BlockApricornTree.BLOCKPOS, EnumBlockPos.BOTTOM), 18);
         world.func_180501_a(new BlockPos(x, y + 1, z), newBlock.func_176223_P().func_177226_a(BlockApricornTree.BLOCKPOS, EnumBlockPos.TOP), 18);
      }

      TileEntityApricornTree tree = (TileEntityApricornTree)BlockHelper.getTileEntity(TileEntityApricornTree.class, world, new BlockPos(x, y, z));
      if (tree != null) {
         tree.setGenerated();
      }

   }

   public void generateBerry(int x, int y, int z, World world) {
      EnumBerry berry = EnumBerry.getImplementedBerry();
      Block newBlock = berry.getTreeBlock();
      if (newBlock != null) {
         EnumFacing facing = (EnumFacing)RandomHelper.getRandomElementFromArray(EnumFacing.field_176754_o);
         world.func_180501_a(new BlockPos(x, y, z), newBlock.func_176223_P().func_177226_a(BlockBerryTree.BLOCKPOS, EnumBlockPos.BOTTOM).func_177226_a(BlockProperties.FACING, facing), 18);
      }

      TileEntityBerryTree tree = (TileEntityBerryTree)BlockHelper.getTileEntity(TileEntityBerryTree.class, world, new BlockPos(x, y, z));
      if (tree != null) {
         tree.setGenerated();
      }

   }
}
