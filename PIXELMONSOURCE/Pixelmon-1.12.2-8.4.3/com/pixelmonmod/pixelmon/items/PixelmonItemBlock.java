package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.IBlockHasOwner;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PixelmonItemBlock extends ItemBlock {
   private CreativeTabs tabToDisplayOn;

   public PixelmonItemBlock(Block block) {
      super(block);
   }

   public PixelmonItemBlock(Block block, String name) {
      super(block);
      this.func_77655_b(name);
      this.setRegistryName(name);
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (worldIn.field_72995_K) {
         return EnumActionResult.SUCCESS;
      } else {
         ItemStack stack = player.func_184586_b(hand);
         IBlockState iBlockState = worldIn.func_180495_p(pos);
         Block placedOn = iBlockState.func_177230_c();
         if (placedOn == Blocks.field_150431_aC && (Integer)iBlockState.func_177229_b(BlockSnow.field_176315_a) < 1) {
            facing = EnumFacing.UP;
         } else if (!placedOn.func_176200_f(worldIn, pos)) {
            pos = pos.func_177972_a(facing);
         }

         if (stack.func_190916_E() == 0) {
            return EnumActionResult.FAIL;
         } else if (!player.func_175151_a(pos, facing, stack)) {
            return EnumActionResult.FAIL;
         } else if (pos.func_177956_o() == 255 && iBlockState.func_185904_a().func_76220_a()) {
            return EnumActionResult.FAIL;
         } else {
            int i = MathHelper.func_76128_c((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5) & 3;
            EnumFacing rot = EnumFacing.func_176731_b(i);
            if (worldIn.func_175623_d(pos) && worldIn.func_180495_p(pos.func_177977_b()).func_177230_c() != this.field_150939_a) {
               IBlockState iblockstate1;
               if (this.field_150939_a instanceof MultiBlock) {
                  MultiBlock mb = (MultiBlock)this.field_150939_a;
                  if (!canPlace(pos, rot, worldIn, mb, player, stack, placedOn)) {
                     return EnumActionResult.FAIL;
                  }

                  iblockstate1 = this.field_150939_a.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, player, hand);
                  this.placeBlock(stack, iblockstate1, player, worldIn, pos);
                  setMultiBlocksWidth(pos, rot, worldIn, mb, this.field_150939_a, player);
               } else {
                  int meta = this.func_77647_b(stack.func_77960_j());
                  if (this.field_150939_a instanceof BlockStairs) {
                     rot = facing;
                  }

                  iblockstate1 = this.field_150939_a.getStateForPlacement(worldIn, pos, rot, hitX, hitY, hitZ, meta, player, hand);
                  BlockEvent.PlaceEvent placeEvent = new BlockEvent.PlaceEvent(new BlockSnapshot(worldIn, pos, iblockstate1), iBlockState, player, hand);
                  MinecraftForge.EVENT_BUS.post(placeEvent);
                  if (placeEvent.isCanceled()) {
                     return EnumActionResult.FAIL;
                  }

                  if (this.placeBlock(stack, iblockstate1, player, worldIn, pos)) {
                     SoundType soundtype = worldIn.func_180495_p(pos).func_177230_c().getSoundType(worldIn.func_180495_p(pos), worldIn, pos, player);
                     worldIn.func_184133_a(player, pos, soundtype.func_185841_e(), SoundCategory.BLOCKS, (soundtype.func_185843_a() + 1.0F) / 2.0F, soundtype.func_185847_b() * 0.8F);
                  }
               }

               if (this.field_150939_a instanceof IBlockHasOwner) {
                  ((IBlockHasOwner)this.field_150939_a).setOwner(pos, player);
               }

               if (!player.field_71075_bZ.field_75098_d) {
                  stack.func_190920_e(stack.func_190916_E() - 1);
               }

               return EnumActionResult.SUCCESS;
            } else {
               return EnumActionResult.FAIL;
            }
         }
      }
   }

   private boolean placeBlock(ItemStack stack, IBlockState newState, EntityPlayer player, World world, BlockPos pos) {
      if (!world.func_180501_a(pos, newState, 3)) {
         return false;
      } else {
         IBlockState state = world.func_180495_p(pos);
         if (state.func_177230_c() == this.field_150939_a) {
            func_179224_a(world, player, pos, stack);
            this.field_150939_a.func_180633_a(world, pos, state, player, stack);
         }

         return true;
      }
   }

   public boolean func_179222_a(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
      if (this.field_150939_a instanceof MultiBlock) {
         MultiBlock mb = (MultiBlock)this.field_150939_a;
         BlockPos pos2 = pos.func_177972_a(side);
         return canPlace(pos2, player.func_174811_aO(), worldIn, mb, player, stack, worldIn.func_180495_p(pos2).func_177230_c());
      } else {
         return super.func_179222_a(worldIn, pos, side, player, stack);
      }
   }

   public static boolean canPlace(BlockPos pos, EnumFacing rot, World world, MultiBlock mb, EntityPlayer player, ItemStack stack, Block block) {
      int l;
      if (rot == EnumFacing.EAST) {
         for(l = 0; l < mb.width; ++l) {
            if (!canPlaceLength(pos, 0, l, rot, world, mb, player, stack, block)) {
               return false;
            }
         }
      } else if (rot == EnumFacing.NORTH) {
         for(l = 0; l < mb.width; ++l) {
            if (!canPlaceLength(pos, l, 0, rot, world, mb, player, stack, block)) {
               return false;
            }
         }
      } else if (rot == EnumFacing.WEST) {
         for(l = 0; l < mb.width; ++l) {
            if (!canPlaceLength(pos, 0, -1 * l, rot, world, mb, player, stack, block)) {
               return false;
            }
         }
      } else {
         for(l = 0; l < mb.width; ++l) {
            if (!canPlaceLength(pos, -1 * l, 0, rot, world, mb, player, stack, block)) {
               return false;
            }
         }
      }

      return true;
   }

   private static boolean canPlaceLength(BlockPos pos, int xd, int zd, EnumFacing rot, World world, MultiBlock mb, EntityPlayer player, ItemStack stack, Block block) {
      int w;
      if (rot == EnumFacing.EAST) {
         for(w = 0; w < mb.length; ++w) {
            if (!canPlaceHeight(pos, xd + w, zd, rot, world, mb, player, stack, block)) {
               return false;
            }
         }
      } else if (rot == EnumFacing.NORTH) {
         for(w = 0; w < mb.length; ++w) {
            if (!canPlaceHeight(pos, xd, zd - w, rot, world, mb, player, stack, block)) {
               return false;
            }
         }
      } else if (rot == EnumFacing.WEST) {
         for(w = 0; w < mb.length; ++w) {
            if (!canPlaceHeight(pos, xd - w, zd, rot, world, mb, player, stack, block)) {
               return false;
            }
         }
      } else {
         for(w = 0; w < mb.length; ++w) {
            if (!canPlaceHeight(pos, xd, zd + w, rot, world, mb, player, stack, block)) {
               return false;
            }
         }
      }

      return true;
   }

   private static boolean canPlaceHeight(BlockPos pos, int xd, int zd, EnumFacing rot, World world, MultiBlock mb, EntityPlayer player, ItemStack stack, Block block) {
      for(int h = 0; (double)h < mb.height; ++h) {
         BlockPos p = new BlockPos(pos.func_177958_n() + xd, pos.func_177956_o() + h, pos.func_177952_p() + zd);
         IBlockState iblockstate1 = block.func_176194_O().func_177621_b();
         BlockEvent.PlaceEvent placeEvent = new BlockEvent.PlaceEvent(new BlockSnapshot(world, p, iblockstate1), world.func_180495_p(p.func_177977_b()), player, EnumHand.MAIN_HAND);
         MinecraftForge.EVENT_BUS.post(placeEvent);
         if (placeEvent.isCanceled()) {
            return false;
         }

         IBlockState iblockstate = world.func_180495_p(p);
         if (iblockstate.func_185904_a() != Material.field_151579_a) {
            return false;
         }

         if (iblockstate.func_185904_a() != Material.field_151597_y && iblockstate.func_185904_a() != Material.field_151577_b && iblockstate.func_185904_a() != Material.field_151579_a) {
            return false;
         }
      }

      return true;
   }

   public static void setMultiBlocksWidth(BlockPos pos, EnumFacing rot, World world, MultiBlock mb, Block block, EntityPlayer player) {
      int l;
      if (rot == EnumFacing.EAST) {
         for(l = 0; l < mb.width; ++l) {
            setMultiBlocksLength(pos, 0, l, rot, world, mb, block, player);
         }
      } else if (rot == EnumFacing.NORTH) {
         for(l = 0; l < mb.width; ++l) {
            setMultiBlocksLength(pos, l, 0, rot, world, mb, block, player);
         }
      } else if (rot == EnumFacing.WEST) {
         for(l = 0; l < mb.width; ++l) {
            setMultiBlocksLength(pos, 0, -1 * l, rot, world, mb, block, player);
         }
      } else {
         for(l = 0; l < mb.width; ++l) {
            setMultiBlocksLength(pos, -1 * l, 0, rot, world, mb, block, player);
         }
      }

   }

   private static void setMultiBlocksLength(BlockPos pos, int xd, int zd, EnumFacing rot, World world, MultiBlock mb, Block block, EntityPlayer player) {
      int w;
      if (rot == EnumFacing.EAST) {
         for(w = 0; w < mb.length; ++w) {
            setMultiBlocksHeight(pos, xd + w, zd, rot, world, mb, block, player);
         }
      } else if (rot == EnumFacing.NORTH) {
         for(w = 0; w < mb.length; ++w) {
            setMultiBlocksHeight(pos, xd, zd - w, rot, world, mb, block, player);
         }
      } else if (rot == EnumFacing.WEST) {
         for(w = 0; w < mb.length; ++w) {
            setMultiBlocksHeight(pos, xd - w, zd, rot, world, mb, block, player);
         }
      } else {
         for(w = 0; w < mb.length; ++w) {
            setMultiBlocksHeight(pos, xd, zd + w, rot, world, mb, block, player);
         }
      }

   }

   private static void setMultiBlocksHeight(BlockPos pos, int xd, int zd, EnumFacing rot, World world, MultiBlock mb, Block block, EntityPlayer player) {
      for(int h = 0; (double)h < mb.height; ++h) {
         BlockPos p = new BlockPos(pos.func_177958_n() + xd, pos.func_177956_o() + h, pos.func_177952_p() + zd);
         EnumFacing facing = EnumFacing.EAST;
         if (xd > 0) {
            facing = EnumFacing.EAST;
         } else if (xd < 0) {
            facing = EnumFacing.WEST;
         } else if (zd > 0) {
            facing = EnumFacing.SOUTH;
         } else if (zd < 0) {
            facing = EnumFacing.NORTH;
         } else {
            facing = rot;
         }

         EnumMultiPos multiPos = EnumMultiPos.BASE;
         if (xd == 0 && zd == 0 && h == 0) {
            multiPos = EnumMultiPos.BASE;
         } else if (h == 0) {
            multiPos = EnumMultiPos.BOTTOM;
         } else {
            multiPos = EnumMultiPos.TOP;
         }

         IBlockState iblockstate1 = block.getStateForPlacement(world, p, rot, 0.0F, 0.0F, 0.0F, 0, player, EnumHand.MAIN_HAND).func_177226_a(MultiBlock.MULTIPOS, multiPos).func_177226_a(BlockProperties.FACING, facing);
         world.func_180501_a(p, iblockstate1, 2);
      }

   }

   public Item func_77637_a(CreativeTabs tab) {
      this.tabToDisplayOn = tab;
      return super.func_77637_a(tab);
   }

   @SideOnly(Side.CLIENT)
   public CreativeTabs func_77640_w() {
      CreativeTabs tab = this.field_150939_a.func_149708_J();
      return tab == null ? this.tabToDisplayOn : tab;
   }

   @SideOnly(Side.CLIENT)
   public void func_77624_a(ItemStack stack, World world, List tooltip, ITooltipFlag advanced) {
      String tt = this.getTooltipText(stack);
      if (!tt.isEmpty()) {
         if (GameSettings.func_100015_a(Minecraft.func_71410_x().field_71474_y.field_74311_E)) {
            Collections.addAll(tooltip, tt.split("\n"));
         } else {
            tooltip.add(TextFormatting.GRAY + I18n.func_74838_a("gui.tooltip.collapsed"));
         }
      }

      super.func_77624_a(stack, world, tooltip, advanced);
   }

   public String getTooltipText(ItemStack stack) {
      NBTTagCompound nbt = stack.func_77978_p();
      return nbt != null && nbt.func_74764_b("tooltip") ? nbt.func_74779_i("tooltip") : this.getTooltipText();
   }

   public String getTooltipText() {
      return I18n.func_94522_b(this.func_77658_a() + ".tooltip") ? I18n.func_74838_a(this.func_77658_a() + ".tooltip") : "";
   }
}
