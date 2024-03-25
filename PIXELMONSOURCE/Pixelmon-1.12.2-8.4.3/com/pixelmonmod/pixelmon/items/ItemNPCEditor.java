package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemNPCEditor extends PixelmonItem {
   public ItemNPCEditor() {
      super("trainer_editor");
      this.func_77637_a(CreativeTabs.field_78040_i);
      this.func_77625_d(1);
   }

   public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      if (side != EnumFacing.UP) {
         return EnumActionResult.FAIL;
      } else {
         if (!world.field_72995_K) {
            if (!checkPermission(player)) {
               return EnumActionResult.FAIL;
            }

            if (player.field_71075_bZ.field_75098_d) {
               OpenScreen.open(player, EnumGuiScreen.CreateNpc, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
               return EnumActionResult.SUCCESS;
            }
         }

         return EnumActionResult.PASS;
      }
   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (player.field_70170_p.field_72995_K) {
         return super.onLeftClickEntity(stack, player, entity);
      } else {
         if (entity instanceof EntityNPC && checkPermission(player)) {
            OpenScreen.open(player, EnumGuiScreen.DeleteNpc, entity.func_145782_y());
         }

         return super.onLeftClickEntity(stack, player, entity);
      }
   }

   public static boolean checkPermission(EntityPlayer player) {
      if (!PixelmonConfig.opToUseNpcEditor && player.func_184812_l_()) {
         return true;
      } else if (PixelmonConfig.opToUseNpcEditor && player.func_70003_b(4, "pixelmon.npceditor.use")) {
         return true;
      } else {
         ChatHandler.sendChat(player, "pixelmon.general.needop");
         return false;
      }
   }
}
