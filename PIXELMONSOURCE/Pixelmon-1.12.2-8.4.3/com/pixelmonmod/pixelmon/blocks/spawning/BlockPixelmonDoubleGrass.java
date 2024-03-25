package com.pixelmonmod.pixelmon.blocks.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block.EnumOffsetType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPixelmonDoubleGrass extends Block implements IGrowable, IShearable, IPlantable {
   public static final PropertyEnum HALF = PropertyEnum.func_177709_a("half", EnumBlockHalf.class);

   public BlockPixelmonDoubleGrass() {
      super(Material.field_151585_k);
      this.func_149675_a(true);
      this.func_149672_a(SoundType.field_185850_c);
      this.func_149711_c(3.0F);
   }

   public String func_149739_a() {
      return super.func_149739_a().substring(5);
   }

   public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
      return EnumPlantType.Plains;
   }

   protected boolean canPlaceBlockOn(Block ground) {
      Material groundMaterial = ground.func_149688_o(ground.func_176194_O().func_177621_b());
      return groundMaterial == Material.field_151578_c || groundMaterial == Material.field_151595_p || groundMaterial == Material.field_151577_b;
   }

   public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entityIn) {
      if (entityIn instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)entityIn;
         if (BattleRegistry.getBattle(player) != null) {
            return;
         }

         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
         if (storage.getTicksTillEncounter() <= 1 && entityIn.field_70163_u == (double)pos.func_177956_o()) {
            BlockSpawningHandler.getInstance().performBattleStartCheck(world, pos, player, (EntityPixelmon)null, EnumBattleStartTypes.PUGRASSDOUBLE, state);
         }

         storage.updateTicksTillEncounter();
      }

   }

   public boolean func_176200_f(IBlockAccess world, BlockPos pos) {
      return true;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public int func_149679_a(int fortune, Random random) {
      return 1 + random.nextInt(fortune * 2 + 1);
   }

   public void func_180657_a(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
      super.func_180657_a(world, player, pos, state, te, stack);
   }

   public int func_180651_a(IBlockState state) {
      return state.func_177230_c().func_176201_c(state);
   }

   public boolean func_176473_a(World world, BlockPos pos, IBlockState state, boolean isClient) {
      return false;
   }

   public boolean func_180670_a(World world, Random rand, BlockPos pos, IBlockState state) {
      return true;
   }

   public void func_176474_b(World world, Random rand, BlockPos pos, IBlockState state) {
   }

   public int func_176201_c(IBlockState state) {
      return 0;
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P();
   }

   public IBlockState func_176221_a(IBlockState state, IBlockAccess world, BlockPos pos) {
      boolean grassBelow = world.func_180495_p(pos.func_177977_b()).func_177230_c() == this;
      EnumBlockHalf position = BlockPixelmonDoubleGrass.EnumBlockHalf.LOWER;
      if (grassBelow) {
         position = BlockPixelmonDoubleGrass.EnumBlockHalf.UPPER;
      }

      return state.func_177226_a(HALF, position);
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{HALF});
   }

   public List onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
      List ret = new ArrayList();
      ret.add(new ItemStack(PixelmonBlocks.pixelmonGrassBlock, 1, 0));
      return ret;
   }

   public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
      IBlockState state = world.func_180495_p(pos);
      return state.func_177229_b(HALF) == BlockPixelmonDoubleGrass.EnumBlockHalf.LOWER;
   }

   public boolean func_176196_c(World world, BlockPos pos) {
      return super.func_176196_c(world, pos) && world.func_180495_p(pos.func_177977_b()).func_177230_c().canSustainPlant(world.func_180495_p(pos), world, pos.func_177977_b(), EnumFacing.UP, this) && world.func_175623_d(pos.func_177984_a());
   }

   public void func_189540_a(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
      super.func_189540_a(state, world, pos, blockIn, fromPos);
      this.checkAndDropBlock(world, pos, state);
   }

   public void func_180650_b(World world, BlockPos pos, IBlockState state, Random rand) {
      this.checkAndDropBlock(world, pos, state);
   }

   protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
      if (!this.canBlockStay(world, pos, state)) {
         boolean flag = state.func_177229_b(HALF) == BlockPixelmonDoubleGrass.EnumBlockHalf.UPPER;
         BlockPos blockpos1 = flag ? pos : pos.func_177984_a();
         BlockPos blockpos2 = flag ? pos.func_177977_b() : pos;
         Object object = flag ? this : world.func_180495_p(blockpos1).func_177230_c();
         Object object1 = flag ? world.func_180495_p(blockpos2).func_177230_c() : this;
         if (!flag) {
            this.func_176226_b(world, pos, state, 0);
         }

         if (object == this) {
            world.func_180501_a(blockpos1, Blocks.field_150350_a.func_176223_P(), 3);
         }

         if (object1 == this) {
            world.func_180501_a(blockpos2, Blocks.field_150350_a.func_176223_P(), 3);
         }
      }

   }

   public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
      BlockPos down = pos.func_177977_b();
      Block soil = world.func_180495_p(down).func_177230_c();
      return state.func_177230_c() != this ? this.canPlaceBlockOn(soil) : soil.canSustainPlant(state, world, down, EnumFacing.UP, this);
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess world, BlockPos pos) {
      return null;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT;
   }

   public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
      IBlockState state = world.func_180495_p(pos);
      return state.func_177230_c() != this ? this.func_176223_P() : state;
   }

   public void placeAt(World world, BlockPos lowerPos, int flags) {
      world.func_180501_a(lowerPos, this.func_176223_P().func_177226_a(HALF, BlockPixelmonDoubleGrass.EnumBlockHalf.LOWER), flags);
      world.func_180501_a(lowerPos.func_177984_a(), this.func_176223_P().func_177226_a(HALF, BlockPixelmonDoubleGrass.EnumBlockHalf.UPPER), flags);
   }

   public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      world.func_180501_a(pos.func_177984_a(), this.func_176223_P().func_177226_a(HALF, BlockPixelmonDoubleGrass.EnumBlockHalf.UPPER), 2);
   }

   public void func_176208_a(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
      if (state.func_177229_b(HALF) == BlockPixelmonDoubleGrass.EnumBlockHalf.UPPER) {
         if (world.func_180495_p(pos.func_177977_b()).func_177230_c() == this) {
            if (!player.field_71075_bZ.field_75098_d) {
               if (!world.field_72995_K) {
                  if (player.func_184614_ca() != ItemStack.field_190927_a && player.func_184614_ca().func_77973_b() == Items.field_151097_aZ) {
                     world.func_175698_g(pos.func_177977_b());
                  } else {
                     world.func_175655_b(pos.func_177977_b(), true);
                  }
               } else {
                  world.func_175698_g(pos.func_177977_b());
               }
            } else {
               world.func_175698_g(pos.func_177977_b());
            }
         }
      } else if (player.field_71075_bZ.field_75098_d && world.func_180495_p(pos.func_177984_a()).func_177230_c() == this) {
         world.func_180501_a(pos.func_177984_a(), Blocks.field_150350_a.func_176223_P(), 2);
      }

      super.func_176208_a(world, pos, state, player);
   }

   @SideOnly(Side.CLIENT)
   public Block.EnumOffsetType func_176218_Q() {
      return EnumOffsetType.NONE;
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      if (state.func_177230_c() == this && state.func_177229_b(HALF) == BlockPixelmonDoubleGrass.EnumBlockHalf.LOWER && world.func_180495_p(pos.func_177984_a()).func_177230_c() == this) {
         world.func_175698_g(pos.func_177984_a());
      }

      return world.func_175698_g(pos);
   }

   static enum EnumBlockHalf implements IStringSerializable {
      UPPER,
      LOWER;

      public String toString() {
         return this.func_176610_l();
      }

      public String func_176610_l() {
         return this == UPPER ? "upper" : "lower";
      }
   }
}
