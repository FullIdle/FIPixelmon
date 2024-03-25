package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityScroll;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockScroll extends BlockContainer {
   public static final PropertyDirection FACING;
   public static final PropertyInteger ROTATION;
   protected static final AxisAlignedBB STANDING_AABB;

   public BlockScroll() {
      super(Material.field_151575_d);
      this.field_149762_H = SoundType.field_185848_a;
      this.field_149790_y = false;
   }

   public String func_149732_F() {
      return I18n.func_74838_a("tile.scroll.name");
   }

   @Nullable
   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return field_185506_k;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_176205_b(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_181623_g() {
      return true;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityScroll(BlockScroll.Type.Waters);
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Items.field_179564_cE;
   }

   private ItemStack getTileDataItemStack(World worldIn, BlockPos pos) {
      TileEntity te = worldIn.func_175625_s(pos);
      return te instanceof TileEntityScroll ? ((TileEntityScroll)te).getItem() : ItemStack.field_190927_a;
   }

   public ItemStack func_185473_a(World worldIn, BlockPos pos, IBlockState state) {
      ItemStack itemstack = this.getTileDataItemStack(worldIn, pos);
      return itemstack.func_190926_b() ? new ItemStack(Items.field_179564_cE) : itemstack;
   }

   public void func_180653_a(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      super.func_180653_a(worldIn, pos, state, chance, fortune);
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return !this.func_181087_e(worldIn, pos) && super.func_176196_c(worldIn, pos);
   }

   protected boolean func_181086_a(World worldIn, BlockPos pos, EnumFacing facing) {
      return worldIn.func_180495_p(pos.func_177972_a(facing)).func_177230_c() instanceof BlockScroll;
   }

   protected boolean func_181087_e(World worldIn, BlockPos pos) {
      return this.func_181086_a(worldIn, pos, EnumFacing.NORTH) || this.func_181086_a(worldIn, pos, EnumFacing.SOUTH) || this.func_181086_a(worldIn, pos, EnumFacing.WEST) || this.func_181086_a(worldIn, pos, EnumFacing.EAST);
   }

   public void func_180657_a(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
      if (te instanceof TileEntityScroll) {
         TileEntityScroll scroll = (TileEntityScroll)te;
         ItemStack itemstack = scroll.getItem();
         func_180635_a(worldIn, pos, itemstack);
      } else {
         super.func_180657_a(worldIn, player, pos, state, (TileEntity)null, stack);
      }

   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public void getDrops(NonNullList drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      TileEntity te = world.func_175625_s(pos);
      if (te instanceof TileEntityScroll) {
         TileEntityScroll scroll = (TileEntityScroll)te;
         ItemStack itemstack = scroll.getItem();
         drops.add(itemstack);
      } else {
         drops.add(new ItemStack(Items.field_179564_cE, 1, 0));
      }

   }

   static {
      FACING = BlockHorizontal.field_185512_D;
      ROTATION = PropertyInteger.func_177719_a("rotation", 0, 15);
      STANDING_AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
   }

   public static enum Type {
      Waters("scroll_of_waters", "pixelmon:textures/entity/scroll/scroll_of_waters.png"),
      Darkness("scroll_of_darkness", "pixelmon:textures/entity/scroll/scroll_of_darkness.png");

      private final String name;
      private final String defaultResource;

      private Type(String name, String defaultResource) {
         this.name = name;
         this.defaultResource = defaultResource;
      }

      public String getName() {
         return this.name;
      }

      public String getDefaultResource() {
         return this.defaultResource;
      }
   }

   public static class BlockScrollStanding extends BlockScroll {
      public BlockScrollStanding() {
         this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(ROTATION, 0));
      }

      public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
         return STANDING_AABB;
      }

      public IBlockState func_185499_a(IBlockState state, Rotation rot) {
         return state.func_177226_a(ROTATION, rot.func_185833_a((Integer)state.func_177229_b(ROTATION), 16));
      }

      public IBlockState func_185471_a(IBlockState state, Mirror mirrorIn) {
         return state.func_177226_a(ROTATION, mirrorIn.func_185802_a((Integer)state.func_177229_b(ROTATION), 16));
      }

      public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
         if (!worldIn.func_180495_p(pos.func_177977_b()).func_185904_a().func_76220_a()) {
            this.func_176226_b(worldIn, pos, state, 0);
            worldIn.func_175698_g(pos);
         }

         super.func_189540_a(state, worldIn, pos, blockIn, fromPos);
      }

      public IBlockState func_176203_a(int meta) {
         return this.func_176223_P().func_177226_a(ROTATION, meta);
      }

      public int func_176201_c(IBlockState state) {
         return (Integer)state.func_177229_b(ROTATION);
      }

      protected BlockStateContainer func_180661_e() {
         return new BlockStateContainer(this, new IProperty[]{ROTATION});
      }
   }

   public static class BlockScrollHanging extends BlockScroll {
      protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.875, 1.0, 0.78125, 1.0);
      protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.78125, 0.125);
      protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.875, 0.0, 0.0, 1.0, 0.78125, 1.0);
      protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.125, 0.78125, 1.0);

      public BlockScrollHanging() {
         this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(FACING, EnumFacing.NORTH));
      }

      public IBlockState func_185499_a(IBlockState state, Rotation rot) {
         return state.func_177226_a(FACING, rot.func_185831_a((EnumFacing)state.func_177229_b(FACING)));
      }

      public IBlockState func_185471_a(IBlockState state, Mirror mirrorIn) {
         return state.func_185907_a(mirrorIn.func_185800_a((EnumFacing)state.func_177229_b(FACING)));
      }

      public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
         switch ((EnumFacing)state.func_177229_b(FACING)) {
            case NORTH:
            default:
               return NORTH_AABB;
            case SOUTH:
               return SOUTH_AABB;
            case WEST:
               return WEST_AABB;
            case EAST:
               return EAST_AABB;
         }
      }

      public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
         EnumFacing enumfacing = (EnumFacing)state.func_177229_b(FACING);
         if (!worldIn.func_180495_p(pos.func_177972_a(enumfacing.func_176734_d())).func_185904_a().func_76220_a()) {
            this.func_176226_b(worldIn, pos, state, 0);
            worldIn.func_175698_g(pos);
         }

         super.func_189540_a(state, worldIn, pos, blockIn, fromPos);
      }

      public IBlockState func_176203_a(int meta) {
         EnumFacing enumfacing = EnumFacing.func_82600_a(meta);
         if (enumfacing.func_176740_k() == Axis.Y) {
            enumfacing = EnumFacing.NORTH;
         }

         return this.func_176223_P().func_177226_a(FACING, enumfacing);
      }

      public int func_176201_c(IBlockState state) {
         return ((EnumFacing)state.func_177229_b(FACING)).func_176745_a();
      }

      protected BlockStateContainer func_180661_e() {
         return new BlockStateContainer(this, new IProperty[]{FACING});
      }
   }
}
