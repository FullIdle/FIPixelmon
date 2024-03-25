package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCurryDex extends PixelmonItem {
   public ItemCurryDex(String name) {
      super(name);
   }

   public ActionResult func_77659_a(World world, EntityPlayer playerIn, EnumHand hand) {
      if (!world.field_72995_K) {
         OpenScreen.open(playerIn, EnumGuiScreen.CurryDex, Pixelmon.storageManager.getParty((EntityPlayerMP)playerIn).getCurryData());
      }

      return new ActionResult(EnumActionResult.SUCCESS, playerIn.func_184586_b(hand));
   }
}
