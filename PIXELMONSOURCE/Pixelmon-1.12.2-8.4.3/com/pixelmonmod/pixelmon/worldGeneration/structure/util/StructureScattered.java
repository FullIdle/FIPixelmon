package com.pixelmonmod.pixelmon.worldGeneration.structure.util;

import com.pixelmonmod.pixelmon.WorldHelper;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public abstract class StructureScattered extends StructureComponent implements IVillageStructure {
   public int scatteredFeatureSizeX;
   public int scatteredFeatureSizeY;
   public int scatteredFeatureSizeZ;
   protected int field_74936_d;
   protected boolean shouldSave;
   public ItemStack signItem;

   protected StructureScattered(Random par1Random, BlockPos pos, int width, int height, int length, boolean doRotation) {
      this(par1Random, pos, width, height, length, false, doRotation);
   }

   protected StructureScattered(Random par1Random, BlockPos pos, int width, int height, int length, boolean save, boolean doRotation) {
      super(0);
      this.field_74936_d = -1;
      this.scatteredFeatureSizeX = width;
      this.scatteredFeatureSizeY = height;
      this.scatteredFeatureSizeZ = length;
      if (doRotation) {
         this.func_186164_a(EnumFacing.values()[par1Random.nextInt(4)]);
      } else {
         this.func_186164_a(EnumFacing.DOWN);
      }

      switch (this.func_186165_e()) {
         case EAST:
         case WEST:
            this.field_74887_e = new StructureBoundingBox(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), pos.func_177958_n() + width - 1, pos.func_177956_o() + height - 1, pos.func_177952_p() + length - 1);
            break;
         default:
            this.field_74887_e = new StructureBoundingBox(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), pos.func_177958_n() + length - 1, pos.func_177956_o() + height - 1, pos.func_177952_p() + width - 1);
      }

      this.shouldSave = save;
   }

   protected StructureScattered() {
      this.field_74936_d = -1;
   }

   public abstract String getName();

   public void setShouldSave(boolean save) {
      this.shouldSave = save;
   }

   public final boolean generate(World world, Random random) {
      boolean generated = this.generateImpl(world, random);
      return generated;
   }

   public abstract boolean generateImpl(World var1, Random var2);

   protected abstract boolean canStructureFitAtCoords(World var1);

   public abstract int getX(int var1, int var2);

   public abstract int getY(int var1);

   public abstract int getZ(int var1, int var2);

   public int getTopSolidBlock(World world, int par1, int par2) {
      Chunk chunk = world.func_175726_f(new BlockPos(par1, 0, par2));
      int k = chunk.func_76625_h() + 15;
      par1 &= 15;

      for(par2 &= 15; k > 0; --k) {
         IBlockState block = chunk.func_186032_a(par1, k, par2);
         if (block.func_177230_c() != Blocks.field_150350_a && block.func_185904_a().func_76230_c() && block.func_185904_a() != Material.field_151584_j && block.func_185904_a() != Material.field_151575_d && !block.func_177230_c().isFoliage(world, new BlockPos(par1, k, par2))) {
            return k;
         }
      }

      return -1;
   }

   public void fixLighting(World world) {
      WorldHelper.fixLighting(world, this.field_74887_e);
   }

   public EnumFacing getFacing() {
      return this.func_186165_e();
   }

   public int getNPCXWithOffset(int x, int z) {
      return super.func_74865_a(x, z);
   }

   public int getNPCYWithOffset(int y) {
      return super.func_74862_a(y);
   }

   public int getNPCZWithOffset(int x, int z) {
      return super.func_74873_b(x, z);
   }
}
