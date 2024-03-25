package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BerryEvent;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBerryTree;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockBerryTree extends GenericRotatableModelBlock implements IGrowable, IPlantable {
   public static final PropertyEnum BLOCKPOS = PropertyEnum.func_177709_a("blockpos", EnumBlockPos.class);
   private static final AxisAlignedBB SeedStage = new AxisAlignedBB(0.4000000059604645, 0.0, 0.4000000059604645, 0.6000000238418579, 0.20000000298023224, 0.6000000238418579);
   private final byte ordinal;

   public BlockBerryTree(EnumBerry type) {
      super(Material.field_151585_k);
      this.ordinal = (byte)type.ordinal();
      this.func_149672_a(SoundType.field_185850_c);
      this.field_149787_q = false;
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess access, BlockPos pos) {
      return this.getBlockBounds(access, pos, blockState);
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return this.getBlockBounds(worldIn, pos, state).func_186670_a(pos);
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return this.getBlockBounds(worldIn, pos, state);
   }

   protected AxisAlignedBB getBlockBounds(IBlockAccess world, BlockPos pos, IBlockState state) {
      TileEntityBerryTree tile = (TileEntityBerryTree)BlockHelper.getTileEntity(TileEntityBerryTree.class, world, pos);
      if (tile != null) {
         double mul = tile.getStage() == 3 ? 0.75 : 1.0;
         double height = (double)tile.getType().height * mul;
         double width = (double)tile.getType().width * mul;
         if (tile.getStage() < 3) {
            return SeedStage;
         } else {
            return tile.getStage() == 3 ? new AxisAlignedBB(0.5 - width / 2.0, 0.0, 0.5 - width / 2.0, 0.5 + width / 2.0, height, 0.5 + width / 2.0) : new AxisAlignedBB(0.5 - width / 2.0, 0.0, 0.5 - width / 2.0, 0.5 + width / 2.0, height, 0.5 + width / 2.0);
         }
      } else {
         return Block.field_185505_j;
      }
   }

   public Material func_149688_o(IBlockState state) {
      return Material.field_151585_k;
   }

   public float func_176195_g(IBlockState state, World world, BlockPos pos) {
      if (state.func_177229_b(BLOCKPOS) == EnumBlockPos.TOP) {
         pos = pos.func_177977_b();
      }

      TileEntityBerryTree tile = (TileEntityBerryTree)BlockHelper.getTileEntity(TileEntityBerryTree.class, world, pos);
      return tile != null && tile.getStage() < 2 ? 1.0F : 2.0F;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(this.getType().getBerry());
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      List drops = new ArrayList();
      TileEntityBerryTree tile = (TileEntityBerryTree)BlockHelper.getTileEntity(TileEntityBerryTree.class, world, pos);
      if (tile == null) {
         return drops;
      } else {
         byte stage = tile.getStage();
         if (stage > 1 && stage < 5) {
            return drops;
         } else {
            if (world instanceof World) {
               ItemStack stack = new ItemStack(tile.getType().getBerry());
               stack.func_190920_e(stage == 1 ? 1 : tile.getProjectedYield());
               drops.add(stack);
            }

            return drops;
         }
      }
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return state.func_177229_b(BLOCKPOS) == EnumBlockPos.BOTTOM ? new TileEntityBerryTree(this.ordinal) : null;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return null;
   }

   public EnumBerry getType() {
      return EnumBerry.values()[this.ordinal];
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         if (state.func_177229_b(BLOCKPOS) == EnumBlockPos.TOP) {
            pos = pos.func_177977_b();
         }

         TileEntityBerryTree tile = (TileEntityBerryTree)BlockHelper.getTileEntity(TileEntityBerryTree.class, world, pos);
         if (tile == null) {
            return false;
         } else if (EnumBerry.values()[this.ordinal].getBerry() != null && tile.getStage() == 5) {
            BerryEvent.PickBerry pick = new BerryEvent.PickBerry(this.getType(), pos, (EntityPlayerMP)player, tile, new ItemStack(this.getType().getBerry(), tile.getProjectedYield()));
            if (!Pixelmon.EVENT_BUS.post(pick)) {
               Block.func_180635_a(world, pos, pick.getPickedStack());
               tile.setStage((byte)-1);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING, BLOCKPOS});
   }

   public int func_176201_c(IBlockState state) {
      byte b0 = 0;
      int i = b0 | ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
      i |= ((EnumBlockPos)state.func_177229_b(BLOCKPOS)).toMeta() << 2;
      return i;
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta & 3)).func_177226_a(BLOCKPOS, EnumBlockPos.fromMeta((meta & 15) >> 2));
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      worldIn.func_180501_a(pos, state.func_177226_a(BlockProperties.FACING, placer.func_174811_aO().func_176734_d()), 2);
      if (placer instanceof EntityPlayer) {
         ItemBlock.func_179224_a(worldIn, (EntityPlayer)placer, pos, stack);
      }

   }

   public boolean func_176473_a(World world, BlockPos pos, IBlockState state, boolean isClient) {
      EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
      BlockPos loc = pos;
      if (blockpos == EnumBlockPos.TOP) {
         loc = pos.func_177977_b();
      }

      TileEntityBerryTree tile = (TileEntityBerryTree)BlockHelper.getTileEntity(TileEntityBerryTree.class, world, loc);
      return tile != null && !tile.isGrowthBoosted() && tile.getStage() != 5;
   }

   public boolean func_180670_a(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      return this.func_176473_a(worldIn, pos, state, worldIn.field_72995_K);
   }

   public void func_176474_b(World world, Random rand, BlockPos pos, IBlockState state) {
      EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
      BlockPos loc = pos;
      if (blockpos == EnumBlockPos.TOP) {
         loc = pos.func_177977_b();
      }

      TileEntityBerryTree tile = (TileEntityBerryTree)BlockHelper.getTileEntity(TileEntityBerryTree.class, world, loc);
      if (tile != null) {
         tile.boostGrowth();
      }

   }

   public void growStage(World world, Random rand, BlockPos pos, IBlockState state) {
      EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
      if (blockpos == EnumBlockPos.TOP) {
         pos = pos.func_177977_b();
      }

      TileEntityBerryTree tile = (TileEntityBerryTree)BlockHelper.getTileEntity(TileEntityBerryTree.class, world, pos);
      if (tile != null) {
         int stage = tile.getStage();
         if (stage < 5) {
            ++stage;
            tile.setStage((byte)stage);
            if (stage == 5) {
               Pixelmon.EVENT_BUS.post(new BerryEvent.BerryReady(this.getType(), pos, tile));
            }

            ((WorldServer)world).func_184164_w().func_180244_a(pos);
         }

      }
   }

   public void replant(World worldIn, BlockPos pos, IBlockState state) {
      EnumBlockPos blockpos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
      if (blockpos == EnumBlockPos.TOP) {
         pos = pos.func_177977_b();
      }

      TileEntityBerryTree tile = (TileEntityBerryTree)BlockHelper.getTileEntity(TileEntityBerryTree.class, worldIn, pos);
      if (tile != null) {
         tile.setStage((byte)1);
         ((WorldServer)worldIn).func_184164_w().func_180244_a(pos);
      }
   }

   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
      BlockPos down = pos.func_177977_b();
      IBlockState soil = worldIn.func_180495_p(down);
      if (soil.func_177230_c() == this) {
         soil = worldIn.func_180495_p(down.func_177977_b());
      }

      Material material = soil.func_185904_a();
      if (material != Material.field_151577_b && material != Material.field_151578_c) {
         return soil.func_177230_c() == Blocks.field_150349_c || soil.func_177230_c() == Blocks.field_150346_d || soil.func_177230_c() == Blocks.field_150458_ak;
      } else {
         return true;
      }
   }

   protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
      if (!this.canBlockStay(worldIn, pos, state)) {
         this.func_176226_b(worldIn, pos, state, 0);
         worldIn.func_180501_a(pos, Blocks.field_150350_a.func_176223_P(), 3);
      }

   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      EnumBlockPos multiPos = (EnumBlockPos)state.func_177229_b(BLOCKPOS);
      if (player.func_184812_l_()) {
         return super.removedByPlayer(state, world, pos, player, willHarvest);
      } else {
         if (multiPos == EnumBlockPos.TOP) {
            pos = pos.func_177977_b();
         }

         this.func_176226_b(world, pos, state, 0);
         if (world.func_180495_p(pos).func_177230_c() != this) {
            world.func_180501_a(pos.func_177984_a(), Blocks.field_150350_a.func_176223_P(), 3);
            return super.removedByPlayer(state, world, pos, player, willHarvest);
         } else {
            return super.removedByPlayer(state, world, pos, player, willHarvest);
         }
      }
   }

   public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
      return EnumPlantType.Plains;
   }

   public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
      IBlockState state = world.func_180495_p(pos);
      return state.func_177230_c() != this ? this.func_176223_P() : state;
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
      this.checkAndDropBlock(worldIn, pos, state);
   }

   public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      this.checkAndDropBlock(worldIn, pos, state);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}
