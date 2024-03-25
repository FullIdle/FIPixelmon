package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemMail extends ItemHeld {
   public ItemMail(String name) {
      super(EnumHeldItems.mail, "pokemail_" + name);
      this.field_77777_bU = 1;
   }

   public boolean func_77636_d(ItemStack itemStack) {
      return this.isMailSealed(itemStack);
   }

   public int getItemStackLimit(ItemStack stack) {
      return this.isMailSealed(stack) ? 1 : 16;
   }

   private boolean isMailSealed(ItemStack itemStack) {
      return itemStack.func_77942_o() && itemStack.func_77978_p().func_74764_b("editable") && !itemStack.func_77978_p().func_74767_n("editable");
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      ItemStack stack = player.func_184586_b(hand);
      if (!world.field_72995_K && hand == EnumHand.MAIN_HAND) {
         OpenScreen.open(player, EnumGuiScreen.Mail, hand.ordinal());
      }

      return new ActionResult(EnumActionResult.SUCCESS, stack);
   }

   public String getTooltipText() {
      return I18n.func_74838_a("item.mail.tooltip");
   }
}
