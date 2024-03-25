package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.blocks.BlockScroll;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityScroll;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemScroll extends Item {
   public final BlockScroll.Type type;

   public ItemScroll(BlockScroll.Type type) {
      this.field_77777_bU = 1;
      this.func_77637_a(CreativeTabs.field_78031_c);
      this.func_77656_e(0);
      this.type = type;
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      IBlockState iblockstate = worldIn.func_180495_p(pos);
      boolean flag = iblockstate.func_177230_c().func_176200_f(worldIn, pos);
      if (facing == EnumFacing.DOWN || !iblockstate.func_185904_a().func_76220_a() && !flag || flag && facing != EnumFacing.UP) {
         return EnumActionResult.FAIL;
      } else {
         pos = pos.func_177972_a(facing);
         ItemStack itemstack = player.func_184586_b(hand);
         NBTTagCompound tag = itemstack.func_190925_c("BlockEntityTag");
         tag.func_74774_a("Type", (byte)this.type.ordinal());
         if (player.func_175151_a(pos, facing, itemstack) && PixelmonBlocks.standingScroll.func_176196_c(worldIn, pos)) {
            if (!worldIn.field_72995_K) {
               pos = flag ? pos.func_177977_b() : pos;
               if (facing == EnumFacing.UP) {
                  int i = MathHelper.func_76128_c((double)((player.field_70177_z + 180.0F) * 16.0F / 360.0F) + 0.5) & 15;
                  worldIn.func_180501_a(pos, PixelmonBlocks.standingScroll.func_176223_P().func_177226_a(BlockStandingSign.field_176413_a, i), 3);
               } else {
                  worldIn.func_180501_a(pos, PixelmonBlocks.hangingScroll.func_176223_P().func_177226_a(BlockWallSign.field_176412_a, facing), 3);
               }

               TileEntity tileentity = worldIn.func_175625_s(pos);
               if (tileentity instanceof TileEntityScroll) {
                  TileEntityScroll scroll = (TileEntityScroll)tileentity;
                  scroll.setItemValues(itemstack);
                  if (player instanceof EntityPlayerMP) {
                     EntityPlayerMP playerMP = (EntityPlayerMP)player;
                     CriteriaTriggers.field_193137_x.func_193173_a(playerMP, pos, itemstack);
                  }
               }

               worldIn.func_184138_a(pos, iblockstate, iblockstate, 2);
               itemstack.func_190918_g(1);
            }

            return EnumActionResult.SUCCESS;
         } else {
            return EnumActionResult.FAIL;
         }
      }
   }

   public String func_77653_i(ItemStack stack) {
      return I18n.func_74838_a("item." + this.type.getName() + ".name");
   }

   public static ItemStack makeScroll(BlockScroll.Type type, boolean displayOnly, @Nullable String resourceLocation) {
      ItemStack itemstack = new ItemStack(type == BlockScroll.Type.Waters ? PixelmonItems.scrollOfWaters : PixelmonItems.scrollOfDarkness, 1);
      NBTTagCompound tag = itemstack.func_190925_c("BlockEntityTag");
      tag.func_74778_a("Resource", resourceLocation == null ? type.getDefaultResource() : resourceLocation);
      tag.func_74774_a("Type", (byte)type.ordinal());
      tag.func_74757_a("DisplayOnly", displayOnly);
      return itemstack;
   }

   public CreativeTabs func_77640_w() {
      return CreativeTabs.field_78031_c;
   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }
}
