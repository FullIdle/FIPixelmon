package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.enums.EnumUsed;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityShrine;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.EnumShrine;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockShrine extends GenericRotatableModelBlock {
   public static final PropertyEnum USED = PropertyEnum.func_177709_a("used", EnumUsed.class);
   public EnumShrine rockType;

   public BlockShrine(Material par2Material, EnumShrine rockType) {
      super(par2Material);
      this.rockType = rockType;
      this.func_149675_a(true);
      this.func_149722_s();
      this.func_149752_b(6000000.0F);
      if (this.rockType == EnumShrine.Articuno) {
         this.func_149663_c("shrine_uno");
      } else if (this.rockType == EnumShrine.Zapdos) {
         this.func_149663_c("shrine_dos");
      } else if (this.rockType == EnumShrine.Moltres) {
         this.func_149663_c("shrine_tres");
      }

      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(USED, EnumUsed.NO));
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING, USED});
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta & 3)).func_177226_a(USED, EnumUsed.fromMeta((meta & 15) >> 2));
   }

   public int func_176201_c(IBlockState state) {
      byte b0 = 0;
      int i = b0 | ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
      i |= ((EnumUsed)state.func_177229_b(USED)).getMeta() << 2;
      return i;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState iblockstate = this.func_176223_P();
      iblockstate = iblockstate.func_177226_a(BlockProperties.FACING, placer.func_174811_aO()).func_177226_a(USED, EnumUsed.NO);
      return iblockstate;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      if (this.rockType == EnumShrine.Articuno) {
         return new ItemStack(Item.func_150898_a(PixelmonBlocks.shrineUno));
      } else if (this.rockType == EnumShrine.Zapdos) {
         return new ItemStack(Item.func_150898_a(PixelmonBlocks.shrineDos));
      } else {
         return this.rockType == EnumShrine.Moltres ? new ItemStack(Item.func_150898_a(PixelmonBlocks.shrineTres)) : null;
      }
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public int func_149745_a(Random random) {
      return 0;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public TileEntity func_149915_a(World world, int var1) {
      return new TileEntityShrine();
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      ItemStack heldItem = player.func_184586_b(hand);
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         TileEntityShrine tile = (TileEntityShrine)BlockHelper.getTileEntity(TileEntityShrine.class, world, pos);
         if (tile != null) {
            tile.activate(player, this, state, heldItem);
            this.updateRedstoneOutput(world, pos);
            world.func_175666_e(pos, this);
         }

         return true;
      } else {
         return true;
      }
   }

   public void updateRedstoneOutput(World world, BlockPos pos) {
      world.func_175685_c(pos, this, true);
   }

   public boolean getIsUsed(TileEntityShrine tile) {
      EnumUsed used = (EnumUsed)tile.func_145831_w().func_180495_p(tile.func_174877_v()).func_177229_b(USED);
      return used == EnumUsed.YES;
   }

   public int func_149738_a(World world) {
      return 2;
   }

   public void func_176213_c(World world, BlockPos pos, IBlockState state) {
      this.updateRedstoneOutput(world, pos);
   }

   public void func_180663_b(World world, BlockPos pos, IBlockState state) {
      this.updateRedstoneOutput(world, pos);
   }

   public int func_180656_a(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      TileEntityShrine tile = (TileEntityShrine)BlockHelper.getTileEntity(TileEntityShrine.class, blockAccess, pos);
      return this.getIsUsed(tile) ? 15 : 0;
   }

   public int func_176211_b(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return 0;
   }

   public boolean func_149744_f(IBlockState state) {
      return true;
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      if (placer instanceof EntityPlayer) {
         ItemBlock.func_179224_a(worldIn, (EntityPlayer)placer, pos, stack);
      }

   }
}
