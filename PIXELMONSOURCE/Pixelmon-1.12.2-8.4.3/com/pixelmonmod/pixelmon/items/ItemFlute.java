package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemFlute extends PixelmonItem {
   private final int type;

   public ItemFlute(String name, int type) {
      super(name);
      this.type = type;
      this.func_77637_a(CreativeTabs.field_78040_i);
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand handIn) {
      if (!world.field_72995_K) {
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
         switch (this.type) {
            case 0:
               pps.transientData.useBlackFlute(System.currentTimeMillis());
               break;
            case 1:
               pps.transientData.useWhiteFlute(System.currentTimeMillis());
         }

         player.func_145747_a(new TextComponentTranslation("pixelmon.abilities.fluteself", new Object[]{this.getLocalizedName()}));
         return new ActionResult(EnumActionResult.SUCCESS, player.func_184586_b(handIn));
      } else {
         return new ActionResult(EnumActionResult.PASS, player.func_184586_b(handIn));
      }
   }
}
