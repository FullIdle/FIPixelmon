package com.pixelmonmod.pixelmon.blocks.decorative;

import com.pixelmonmod.pixelmon.blocks.enums.EnumAxis;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityDecorativeBase;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockContainerPlus extends BlockContainer {
   public static final PropertyEnum AXIS = PropertyEnum.func_177709_a("axis", EnumAxis.class);
   protected String iconName = "quartzblock_bottom";
   public Class modelClass;
   protected int renderType;
   protected int amountDropped = 1;
   protected boolean opaqueCube = false;
   protected boolean renderNormalBlock = false;
   protected Class tileClass = TileEntityDecorativeBase.class;
   public float invScale = 1.0F;
   public float[] invOffsets = new float[]{0.0F, 0.0F, 0.0F};

   public BlockContainerPlus(Material mat) {
      super(mat);
   }

   public BlockContainerPlus setRenderOptions(int renderType, boolean opaqueCube, boolean renderNormal) {
      this.renderType = renderType;
      this.opaqueCube = opaqueCube;
      this.renderNormalBlock = renderNormal;
      return this;
   }

   public BlockContainerPlus setModelClass(Class modelClass) {
      this.modelClass = modelClass;
      return this;
   }

   public BlockContainerPlus setAmountDropped(int amount) {
      this.amountDropped = amount;
      return this;
   }

   public BlockContainerPlus setIconName(String name) {
      this.iconName = name;
      return this;
   }

   public BlockContainerPlus setTileEntityClass(Class tileClass) {
      this.tileClass = tileClass;
      return this;
   }

   public int func_149745_a(Random random) {
      return this.amountDropped;
   }

   public boolean func_149662_c(IBlockState state) {
      return this.opaqueCube;
   }

   public IBlockState func_176203_a(int meta) {
      IBlockState iblockstate = this.func_176223_P().func_177226_a(AXIS, EnumAxis.fromMeta(meta));
      return iblockstate;
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumAxis)state.func_177229_b(AXIS)).ordinal();
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{AXIS});
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.func_176223_P().func_177226_a(AXIS, EnumAxis.fromFacingAxis(facing.func_176740_k()));
   }

   public boolean isAnotherWithSameOrientationOnSide(IBlockAccess world, BlockPos pos, EnumAxis orientation, EnumFacing dir) {
      BlockPos loc = pos.func_177972_a(dir);
      IBlockState blockstate = world.func_180495_p(loc);
      if (blockstate.func_177230_c() != this) {
         return false;
      } else {
         EnumAxis otherorientation = (EnumAxis)blockstate.func_177229_b(AXIS);
         return orientation == otherorientation;
      }
   }

   public boolean isSameOrientationAndType(Class blockClass, int thisMeta, int thatMeta) {
      boolean flag = false;
      flag = (thisMeta & 7) / 2 == (thatMeta & 7) / 2;
      return blockClass == this.getClass() && flag;
   }

   public TileEntity func_149915_a(World world, int var2) {
      try {
         return (TileEntity)this.tileClass.newInstance();
      } catch (Exception var4) {
         return null;
      }
   }

   public static int rotate(int coordbaseMode, Block block, int meta) {
      return meta;
   }
}
