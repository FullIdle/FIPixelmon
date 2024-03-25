package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityZygardeCell;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.spawning.ZygardeCellsSpawner;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockZygardeCell extends GenericModelBlock {
   public static final PropertyEnum ORIENTATION_PROPERTY = PropertyEnum.func_177709_a("orientation", EnumFacing.class);
   public static final PropertyEnum ROTATION_PROPERTY = PropertyEnum.func_177709_a("rotation", EnumFacing.class);
   private static final AxisAlignedBB[] AABB_DOWN_ROTATE = new AxisAlignedBB[]{new AxisAlignedBB(0.26, 0.0, 0.1, 0.74, 0.05, 0.85), new AxisAlignedBB(0.15, 0.0, 0.26, 0.8, 0.05, 0.74), new AxisAlignedBB(0.26, 0.0, 0.15, 0.74, 0.05, 0.8), new AxisAlignedBB(0.1, 0.0, 0.26, 0.85, 0.05, 0.74)};
   private static final AxisAlignedBB[] AABB_UP_ROTATE = new AxisAlignedBB[]{new AxisAlignedBB(0.26, 0.95, 0.1, 0.74, 1.0, 0.85), new AxisAlignedBB(0.15, 0.95, 0.26, 0.8, 1.0, 0.74), new AxisAlignedBB(0.26, 0.95, 0.15, 0.74, 1.0, 0.8), new AxisAlignedBB(0.1, 0.95, 0.26, 0.85, 1.0, 0.74)};
   private static final AxisAlignedBB[] AABB_SIDES = new AxisAlignedBB[]{new AxisAlignedBB(0.26, 0.1, 1.0, 0.74, 0.9, 0.95), new AxisAlignedBB(0.0, 0.1, 0.26, 0.05, 0.9, 0.74), new AxisAlignedBB(0.26, 0.1, 0.0, 0.74, 0.9, 0.05), new AxisAlignedBB(1.0, 0.1, 0.26, 0.95, 0.9, 0.74)};

   public BlockZygardeCell() {
      super(Material.field_151584_j);
      this.func_149711_c(0.0F);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(ORIENTATION_PROPERTY, EnumFacing.DOWN).func_177226_a(ROTATION_PROPERTY, EnumFacing.SOUTH));
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{ORIENTATION_PROPERTY, ROTATION_PROPERTY});
   }

   public int func_176201_c(IBlockState state) {
      EnumFacing orientation = (EnumFacing)state.func_177229_b(ORIENTATION_PROPERTY);
      EnumFacing rotation = (EnumFacing)state.func_177229_b(ROTATION_PROPERTY);
      return orientation.func_176740_k() == Axis.Y ? (rotation.func_176740_k() != Axis.Y ? rotation.func_176736_b() : 0) + (orientation == EnumFacing.UP ? 4 : 0) : 8 + orientation.func_176736_b() * 2 + (rotation.func_176740_k() == Axis.Y ? rotation.func_176745_a() : 0);
   }

   public IBlockState func_176203_a(int meta) {
      EnumFacing orientation;
      EnumFacing rotation;
      if (meta < 4) {
         orientation = EnumFacing.DOWN;
         rotation = EnumFacing.func_176731_b(meta);
      } else if (meta < 8) {
         orientation = EnumFacing.UP;
         rotation = EnumFacing.func_176731_b(meta - 4);
      } else {
         meta -= 8;
         orientation = EnumFacing.func_176731_b(meta / 2);
         rotation = EnumFacing.field_82609_l[meta % 2];
      }

      return this.func_176223_P().func_177226_a(ORIENTATION_PROPERTY, orientation).func_177226_a(ROTATION_PROPERTY, rotation);
   }

   public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
      EnumFacing orientation = facing.func_176734_d();
      EnumFacing rotation;
      if (orientation.func_176740_k() == Axis.Y) {
         rotation = placer.func_174811_aO();
      } else {
         rotation = placer.field_70125_A > 0.0F ? EnumFacing.UP : EnumFacing.DOWN;
      }

      IBlockState state = this.func_176203_a(meta);
      return state.func_177226_a(ORIENTATION_PROPERTY, facing.func_176734_d()).func_177226_a(ROTATION_PROPERTY, rotation);
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      if (placer instanceof EntityPlayer) {
         ItemBlock.func_179224_a(worldIn, (EntityPlayer)placer, pos, stack);
      }

      if (stack.func_77960_j() == 1) {
         TileEntityZygardeCell cell = (TileEntityZygardeCell)BlockHelper.getTileEntity(TileEntityZygardeCell.class, worldIn, pos);
         if (cell != null) {
            cell.setPermanent(true);
         }
      }

      super.func_180633_a(worldIn, pos, state, placer, stack);
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return new TileEntityZygardeCell();
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      EnumFacing orientation = (EnumFacing)state.func_177229_b(ORIENTATION_PROPERTY);
      if (orientation.func_176740_k() == Axis.Y) {
         return orientation == EnumFacing.UP ? AABB_UP_ROTATE[((EnumFacing)state.func_177229_b(ROTATION_PROPERTY)).func_176736_b()] : AABB_DOWN_ROTATE[((EnumFacing)state.func_177229_b(ROTATION_PROPERTY)).func_176736_b()];
      } else {
         return AABB_SIDES[((EnumFacing)state.func_177229_b(ORIENTATION_PROPERTY)).func_176736_b()];
      }
   }

   @Nullable
   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return field_185506_k;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
      return !player.func_175149_v() && !player.func_184812_l_() && !ZygardeCellsSpawner.clientHasCube() ? field_185505_j : super.func_180640_a(state, worldIn, pos);
   }

   public boolean func_176200_f(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public int func_149745_a(Random random) {
      return 0;
   }

   public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
      return false;
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
      if (!this.canBlockStay(worldIn, state, pos)) {
         worldIn.func_175698_g(pos);
      }

   }

   public boolean canBlockStay(World worldIn, IBlockState state, BlockPos pos) {
      TileEntityZygardeCell cell = (TileEntityZygardeCell)BlockHelper.getTileEntity(TileEntityZygardeCell.class, worldIn, pos);
      if (cell != null && cell.isPermanent()) {
         return true;
      } else {
         pos = pos.func_177972_a((EnumFacing)state.func_177229_b(ORIENTATION_PROPERTY));
         Material mat = worldIn.func_180495_p(pos).func_185904_a();
         return mat == Material.field_151584_j || mat == Material.field_151577_b || mat == Material.field_151575_d;
      }
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }
}
