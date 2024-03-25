package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.HealerEvent;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.IBlockHasOwner;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityHealer;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHealer extends BlockContainer implements IBlockHasOwner {
   public BlockHealer() {
      super(Material.field_151576_e);
      this.func_149711_c(3.5F);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149663_c("healer");
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockProperties.FACING, EnumFacing.EAST));
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      ArrayList drops = new ArrayList();
      drops.add(new ItemStack(PixelmonItems.aluminiumPlate));
      return drops;
   }

   public int func_149745_a(Random random) {
      return 2;
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

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand != EnumHand.OFF_HAND) {
         ItemStack heldItem = player.func_184586_b(hand);
         TileEntityHealer healer = (TileEntityHealer)BlockHelper.getTileEntity(TileEntityHealer.class, world, pos);
         if (!heldItem.func_190926_b() && heldItem.func_77973_b() instanceof ItemDye) {
            if (healer != null) {
               if (!player.func_110124_au().equals(healer.getOwnerUUID()) && healer.getOwnerUUID() != null) {
                  ChatHandler.sendChat(player, "pixelmon.blocks.healer.ownership");
               } else {
                  EnumDyeColor dyeColor = EnumDyeColor.func_176766_a(heldItem.func_77952_i());
                  healer.setColor(dyeColor);
                  if (!player.field_71075_bZ.field_75098_d) {
                     heldItem.func_190918_g(1);
                  }
               }
            }
         } else if (!healer.beingUsed) {
            if (!Pixelmon.EVENT_BUS.post(new HealerEvent.Pre(player, pos, false))) {
               healer.use(player);
            }
         } else {
            ChatHandler.sendChat(player, "pixelmon.blocks.healer");
         }
      }

      return true;
   }

   public void setOwner(BlockPos pos, EntityPlayer playerIn) {
      UUID playerID = playerIn.func_110124_au();
      TileEntityHealer healer = (TileEntityHealer)BlockHelper.getTileEntity(TileEntityHealer.class, playerIn.func_130014_f_(), pos);
      healer.setOwner(playerID);
   }

   public TileEntity func_149915_a(World var1, int var2) {
      return new TileEntityHealer();
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING});
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta));
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState iblockstate = this.func_176223_P();
      if (facing.func_176740_k().func_176722_c()) {
         iblockstate = iblockstate.func_177226_a(BlockProperties.FACING, facing);
      }

      return iblockstate;
   }

   public void func_176208_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
      if (!worldIn.field_72995_K) {
         TileEntityHealer healer = (TileEntityHealer)BlockHelper.getTileEntity(TileEntityHealer.class, worldIn, pos);
         if (healer != null && healer.beingUsed && healer.player != null) {
            healer.player.func_71053_j();
         }

      }
   }

   public int func_149738_a(World world) {
      return 2;
   }

   public int func_180656_a(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      TileEntityHealer healer = (TileEntityHealer)BlockHelper.getTileEntity(TileEntityHealer.class, blockAccess, pos);
      return healer.beingUsed ? 15 : 0;
   }

   public int func_176211_b(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return 0;
   }

   public boolean func_149744_f(IBlockState state) {
      return true;
   }
}
