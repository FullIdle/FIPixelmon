package com.pixelmonmod.pixelmon.items;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.AnvilEvent;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityAnvil;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTools;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHammer extends ItemTool {
   private static final Set effectiveBlocks;

   public ItemHammer(Item.ToolMaterial material, String itemName) {
      super(2.0F, 1.0F, material, effectiveBlocks);
      this.func_77655_b(itemName);
      this.setRegistryName(itemName);
   }

   public boolean func_150897_b(IBlockState block) {
      return block.func_177230_c() != PixelmonBlocks.anvil;
   }

   public boolean func_179218_a(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
      if ((double)state.func_185887_b(worldIn, pos) != 0.0) {
         stack.func_77972_a(1, entityLiving);
      }

      return true;
   }

   public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
      if (player.field_70170_p.func_180495_p(pos).func_177230_c() == PixelmonBlocks.anvil) {
         if (!player.field_70170_p.field_72995_K && ((TileEntityAnvil)player.field_70170_p.func_175625_s(pos)).blockHit((int)this.func_150893_a((ItemStack)null, PixelmonBlocks.anvil.func_176223_P()), (EntityPlayerMP)player)) {
            if (itemstack.func_77952_i() >= itemstack.func_77958_k()) {
               player.func_184611_a(EnumHand.MAIN_HAND, ItemStack.field_190927_a);
            } else if (!player.func_184812_l_()) {
               AnvilEvent.HammerDamage hammerDamageEvent = new AnvilEvent.HammerDamage((EntityPlayerMP)player, (TileEntityAnvil)player.field_70170_p.func_175625_s(pos), itemstack, 1);
               if (Pixelmon.EVENT_BUS.post(hammerDamageEvent)) {
                  return true;
               }

               itemstack.func_96631_a(hammerDamageEvent.damage, field_77697_d, (EntityPlayerMP)null);
            }
         }

         return true;
      } else {
         return super.onBlockStartBreak(itemstack, pos, player);
      }
   }

   public float func_150893_a(ItemStack stack, IBlockState state) {
      if (state.func_177230_c() == PixelmonBlocks.anvil) {
         if (this.field_77862_b == ToolMaterial.WOOD) {
            return 1.0F;
         }

         if (this.field_77862_b == ToolMaterial.STONE) {
            return 2.0F;
         }

         if (this.field_77862_b == PixelmonItemsTools.ALUMINIUM || this.field_77862_b == ToolMaterial.IRON) {
            return 3.0F;
         }

         if (this.field_77862_b == ToolMaterial.GOLD) {
            return 4.0F;
         }

         if (this.field_77862_b == ToolMaterial.DIAMOND) {
            return 5.0F;
         }
      }

      return 1.0F;
   }

   static {
      effectiveBlocks = Sets.newHashSet(new Block[]{PixelmonBlocks.anvil});
   }
}
