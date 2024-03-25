package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemExpAll extends PixelmonItem {
   public ItemExpAll(String name) {
      super(name);
      this.func_77625_d(1);
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      ItemStack itemStackIn = player.func_184586_b(hand);
      if (!world.field_72995_K) {
         boolean activated = false;
         NBTTagCompound nbt;
         if (itemStackIn.func_77942_o()) {
            nbt = itemStackIn.func_77978_p();
            activated = nbt.func_74767_n("Activated");
         } else {
            nbt = new NBTTagCompound();
            itemStackIn.func_77982_d(nbt);
         }

         nbt.func_74757_a("Activated", !activated);
         if (activated) {
            ChatHandler.sendChat(player, "pixelmon.items.expalloff");
         } else {
            ChatHandler.sendChat(player, "pixelmon.items.expallon");
         }
      }

      return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
   }

   public static boolean isActivated(ItemStack stack) {
      NBTTagCompound nbt = stack.func_77978_p();
      return nbt != null && nbt.func_74767_n("Activated");
   }
}
