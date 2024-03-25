package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.blocks.IBlockHasOwner;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
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
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PixelmonFloatItemBlock extends ItemBlock {
   private CreativeTabs tabToDisplayOn;

   public PixelmonFloatItemBlock(Block block) {
      super(block);
   }

   public PixelmonFloatItemBlock(Block block, String name) {
      super(block);
      this.func_77655_b(name);
      this.setRegistryName(name);
   }

   public ActionResult func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
      ItemStack itemstack = playerIn.func_184586_b(handIn);
      RayTraceResult raytraceresult = this.func_77621_a(worldIn, playerIn, true);
      if (raytraceresult == null) {
         return new ActionResult(EnumActionResult.PASS, itemstack);
      } else if (raytraceresult.field_72313_a != Type.BLOCK) {
         return new ActionResult(EnumActionResult.PASS, itemstack);
      } else {
         BlockPos blockpos = raytraceresult.func_178782_a();
         boolean flag1 = worldIn.func_180495_p(blockpos).func_177230_c().func_176200_f(worldIn, blockpos);
         BlockPos blockpos1 = flag1 && raytraceresult.field_178784_b == EnumFacing.UP ? blockpos : blockpos.func_177972_a(raytraceresult.field_178784_b);
         if (!playerIn.func_175151_a(blockpos1, raytraceresult.field_178784_b, itemstack)) {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
         } else if (this.tryPlaceFloat(playerIn, worldIn, blockpos1)) {
            playerIn.func_71029_a(StatList.func_188057_b(this));
            if (!playerIn.func_184812_l_()) {
               itemstack.func_190918_g(1);
            }

            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
         } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
         }
      }
   }

   protected RayTraceResult func_77621_a(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
      float f = playerIn.field_70125_A;
      float f1 = playerIn.field_70177_z;
      double d0 = playerIn.field_70165_t;
      double d1 = playerIn.field_70163_u + (double)playerIn.func_70047_e();
      double d2 = playerIn.field_70161_v;
      Vec3d vec3d = new Vec3d(d0, d1, d2);
      float f2 = MathHelper.func_76134_b(-f1 * 0.017453292F - 3.1415927F);
      float f3 = MathHelper.func_76126_a(-f1 * 0.017453292F - 3.1415927F);
      float f4 = -MathHelper.func_76134_b(-f * 0.017453292F);
      float f5 = MathHelper.func_76126_a(-f * 0.017453292F);
      float f6 = f3 * f4;
      float f7 = f2 * f4;
      double d3 = playerIn.func_110148_a(EntityPlayer.REACH_DISTANCE).func_111126_e();
      Vec3d vec3d1 = vec3d.func_72441_c((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
      return worldIn.func_147447_a(vec3d, vec3d1, useLiquids, !useLiquids, false);
   }

   public boolean tryPlaceFloat(@Nullable EntityPlayer player, World worldIn, BlockPos posIn) {
      IBlockState iblockstate = worldIn.func_180495_p(posIn);
      Material material = iblockstate.func_185904_a();
      boolean flag = !material.func_76220_a();
      boolean flag1 = iblockstate.func_177230_c().func_176200_f(worldIn, posIn);
      int i = MathHelper.func_76128_c((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5) & 3;
      EnumFacing rot = EnumFacing.func_176731_b(i);
      if (!worldIn.func_175623_d(posIn) && !flag && !flag1) {
         return false;
      } else {
         MultiBlock mb = (MultiBlock)this.field_150939_a;
         if (!PixelmonItemBlock.canPlace(posIn.func_177982_a(0, 1, 0), rot, worldIn, mb, player, player.func_184586_b(EnumHand.MAIN_HAND), worldIn.func_180495_p(posIn).func_177230_c())) {
            return false;
         } else {
            IBlockState iblockstate1 = this.field_150939_a.getStateForPlacement(worldIn, posIn.func_177982_a(0, 1, 0), player.func_174811_aO(), 0.0F, 0.0F, 0.0F, 0, player, EnumHand.MAIN_HAND);
            this.placeBlock(player.func_184586_b(EnumHand.MAIN_HAND), iblockstate1, player, player.field_70170_p, posIn.func_177982_a(0, 1, 0));
            PixelmonItemBlock.setMultiBlocksWidth(posIn.func_177982_a(0, 1, 0), rot, worldIn, mb, this.field_150939_a, player);
            return true;
         }
      }
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
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
               if (!PixelmonItemBlock.canPlace(pos, rot, worldIn, mb, player, stack, placedOn)) {
                  return EnumActionResult.FAIL;
               }

               iblockstate1 = this.field_150939_a.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, player, hand);
               this.placeBlock(stack, iblockstate1, player, worldIn, pos);
               PixelmonItemBlock.setMultiBlocksWidth(pos, rot, worldIn, mb, this.field_150939_a, player);
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
         return PixelmonItemBlock.canPlace(pos2, player.func_174811_aO(), worldIn, mb, player, stack, worldIn.func_180495_p(pos2).func_177230_c());
      } else {
         return super.func_179222_a(worldIn, pos, side, player, stack);
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
