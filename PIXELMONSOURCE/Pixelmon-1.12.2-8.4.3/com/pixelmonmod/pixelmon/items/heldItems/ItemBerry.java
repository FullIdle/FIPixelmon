package com.pixelmonmod.pixelmon.items.heldItems;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BerryEvent;
import com.pixelmonmod.pixelmon.blocks.BlockBerryTree;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBerryTree;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemBerry extends ItemHeld implements IPlantable {
   private EnumBerry berry;

   public ItemBerry(EnumHeldItems heldItemType, EnumBerry berry, String itemName) {
      super(heldItemType, itemName);
      this.berry = berry;
   }

   public String getTooltipText() {
      String ret = "";
      String fd = ChatFormatting.GOLD + I18n.func_74837_a("berry.flavor.spicy.name", new Object[0]) + ": " + this.berry.spicy + "\n" + ChatFormatting.BLUE + I18n.func_74837_a("berry.flavor.sweet.name", new Object[0]) + ": " + this.berry.sweet + "\n" + ChatFormatting.RED + I18n.func_74837_a("berry.flavor.sour.name", new Object[0]) + ": " + this.berry.sour + "\n" + ChatFormatting.GREEN + I18n.func_74837_a("berry.flavor.bitter.name", new Object[0]) + ": " + this.berry.bitter + "\n" + ChatFormatting.YELLOW + I18n.func_74837_a("berry.flavor.dry.name", new Object[0]) + ": " + this.berry.dry + "\n" + ChatFormatting.RESET;
      ret = ret + fd;
      if (I18n.func_94522_b(this.func_77658_a() + ".tooltip")) {
         ret = ret + I18n.func_74838_a(this.func_77658_a() + ".tooltip");
      }

      return ret;
   }

   public EnumActionResult func_180614_a(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      ItemStack stack = playerIn.func_184586_b(hand);
      if (!worldIn.field_72995_K && this.berry != null) {
         if (!PixelmonConfig.allowPlanting) {
            return EnumActionResult.FAIL;
         } else if (!this.berry.isImplemented) {
            return EnumActionResult.FAIL;
         } else {
            IBlockState groundBlock = worldIn.func_180495_p(pos);
            if (facing != EnumFacing.UP || groundBlock.func_185904_a() != Material.field_151577_b && groundBlock.func_185904_a() != Material.field_151578_c && groundBlock.func_177230_c() != Blocks.field_150407_cf && groundBlock.func_177230_c() != Blocks.field_150391_bh) {
               return EnumActionResult.FAIL;
            } else if (!worldIn.func_175623_d(pos.func_177984_a())) {
               return EnumActionResult.FAIL;
            } else {
               int count = BlockHelper.countTileEntitiesOfType(worldIn, new ChunkPos(pos), TileEntityApricornTree.class);
               count += BlockHelper.countTileEntitiesOfType(worldIn, new ChunkPos(pos), TileEntityBerryTree.class);
               if (count >= PixelmonConfig.maximumPlants) {
                  ChatHandler.sendChat(playerIn, "pixelmon.blocks.maxPlants");
                  return EnumActionResult.FAIL;
               } else {
                  pos = pos.func_177984_a();
                  Block berryTreeBlock = this.berry.getTreeBlock();
                  if (!playerIn.func_175151_a(pos, facing, stack)) {
                     return EnumActionResult.FAIL;
                  } else if (stack.func_190916_E() <= 0) {
                     return EnumActionResult.FAIL;
                  } else if (worldIn.func_190527_a(berryTreeBlock, pos, false, facing, (Entity)null)) {
                     BerryEvent.BerryPlanted plantEvent = new BerryEvent.BerryPlanted(this.berry, pos, (EntityPlayerMP)playerIn);
                     if (Pixelmon.EVENT_BUS.post(plantEvent)) {
                        return EnumActionResult.FAIL;
                     } else {
                        IBlockState state = berryTreeBlock.func_176223_P().func_177226_a(BlockBerryTree.BLOCKPOS, EnumBlockPos.BOTTOM);
                        worldIn.func_180501_a(pos, state, 3);
                        state = worldIn.func_180495_p(pos);
                        if (state.func_177230_c() == berryTreeBlock) {
                           ItemBlock.func_179224_a(worldIn, playerIn, pos, stack);
                           state.func_177230_c().func_180633_a(worldIn, pos, state, playerIn, stack);
                        }

                        worldIn.func_184148_a((EntityPlayer)null, (double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5, SoundEvents.field_187579_bV, SoundCategory.PLAYERS, 0.5F, 1.0F);
                        if (!playerIn.func_184812_l_()) {
                           if (stack.func_190916_E() <= 1) {
                              playerIn.func_184611_a(hand, ItemStack.field_190927_a);
                           } else {
                              stack.func_190918_g(1);
                           }
                        }

                        return EnumActionResult.SUCCESS;
                     }
                  } else {
                     return EnumActionResult.PASS;
                  }
               }
            }
         }
      } else {
         return EnumActionResult.SUCCESS;
      }
   }

   public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
      return EnumPlantType.Plains;
   }

   public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
      Block berryTreeBlock = this.berry.getTreeBlock();
      return berryTreeBlock.func_176223_P();
   }

   public EnumBerry getBerry() {
      return this.berry;
   }
}
