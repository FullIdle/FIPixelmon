package com.pixelmonmod.pixelmon.blocks.spawning;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPixelmonSpawner extends BlockContainer {
   public BlockPixelmonSpawner() {
      super(Material.field_151575_d);
      this.func_149722_s();
      this.func_149752_b(6000000.0F);
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
      this.func_149663_c("pixelmonSpawner");
   }

   public TileEntity func_149915_a(World world, int var1) {
      return new TileEntityPixelmonSpawner();
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (player instanceof EntityPlayerMP && hand == EnumHand.MAIN_HAND && checkPermission((EntityPlayerMP)player)) {
         TileEntityPixelmonSpawner spawner = (TileEntityPixelmonSpawner)BlockHelper.getTileEntity(TileEntityPixelmonSpawner.class, world, pos);
         ((EntityPlayerMP)player).field_71135_a.func_147359_a(spawner.func_189518_D_());
         spawner.onActivate();
         OpenScreen.open(player, EnumGuiScreen.PixelmonSpawner, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         return true;
      } else {
         return true;
      }
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      TileEntityPixelmonSpawner spawner = (TileEntityPixelmonSpawner)BlockHelper.getTileEntity(TileEntityPixelmonSpawner.class, worldIn, pos);
      if (spawner != null) {
         spawner.despawnAllPokemon();
      }

      super.func_180663_b(worldIn, pos, state);
   }

   public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
      return true;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.MODEL;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT_MIPPED;
   }

   public void func_189540_a(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
      boolean flag = world.func_175640_z(pos) || world.func_175640_z(new BlockPos(pos.func_177958_n(), pos.func_177956_o() + 1, pos.func_177952_p()));
      if (flag) {
         world.func_175684_a(pos, this, this.func_149738_a(world));
      }

   }

   public void func_180650_b(World world, BlockPos pos, IBlockState state, Random rand) {
      TileEntityPixelmonSpawner spawner = (TileEntityPixelmonSpawner)BlockHelper.getTileEntity(TileEntityPixelmonSpawner.class, world, pos);
      spawner.updateRedstone();
   }

   public int func_149738_a(World par1World) {
      return 4;
   }

   public static boolean checkPermission(EntityPlayerMP player) {
      if (!PixelmonConfig.opToUseSpawners && player.field_71075_bZ.field_75098_d) {
         return true;
      } else if (PixelmonConfig.opToUseSpawners && player.func_70003_b(4, "pixelmon.spawner.use")) {
         return true;
      } else {
         ChatHandler.sendChat(player, "pixelmon.general.needop");
         return false;
      }
   }
}
