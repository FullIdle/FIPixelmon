package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.config.FormLogRegistry;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFishingLog extends PixelmonItem {
   public ItemFishingLog(String name) {
      super(name);
   }

   public ActionResult func_77659_a(World world, EntityPlayer playerIn, EnumHand hand) {
      if (!world.field_72995_K) {
         OpenScreen.open(playerIn, EnumGuiScreen.FishingLogMenu, FormLogRegistry.getMenuData((EntityPlayerMP)playerIn));
      }

      return new ActionResult(EnumActionResult.SUCCESS, playerIn.func_184586_b(hand));
   }
}
