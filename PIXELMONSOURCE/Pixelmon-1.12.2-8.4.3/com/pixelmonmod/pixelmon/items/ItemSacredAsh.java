package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemSacredAsh extends ItemHeld {
   public ItemSacredAsh() {
      super(EnumHeldItems.other, "sacredash");
      this.func_77625_d(16);
      this.func_77637_a(PixelmonCreativeTabs.restoration);
      this.canRepair = false;
   }

   public static boolean fullyHeal(EntityPlayerMP player) {
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
      party.heal();
      player.func_145747_a(new TextComponentString(TextFormatting.GRAY + I18n.func_74838_a("pixelmon.effect.sacredash")));
      return true;
   }

   public ActionResult func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
      if (!worldIn.field_72995_K && handIn == EnumHand.MAIN_HAND) {
         fullyHeal((EntityPlayerMP)playerIn);
         playerIn.func_184586_b(handIn).func_190918_g(1);
      }

      return new ActionResult(EnumActionResult.PASS, playerIn.func_184586_b(handIn));
   }
}
