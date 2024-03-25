package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.enums.EnumShrine;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemShrineOrb extends Item {
   public static int full = 375;
   public String itemNames;

   public ItemShrineOrb(EnumShrine shrine, String itemName) {
      this.func_77637_a(CreativeTabs.field_78026_f);
      this.func_77625_d(1);
      this.func_77656_e(full);
      this.itemNames = itemName;
      this.setDamage(new ItemStack(this), 1);
      this.func_77655_b(itemName);
      this.setRegistryName(itemName);
   }

   public void func_77663_a(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
      int damage = par1ItemStack.func_77952_i();
      if (damage > full) {
         par1ItemStack.func_77964_b(full);
      }

   }

   public ActionResult func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand hand) {
      ItemStack stack = playerIn.func_184586_b(hand);
      int damage = stack.func_77952_i();
      if (damage >= full) {
         ChatHandler.sendChat(playerIn, "pixelmon.orb.isfull");
      } else {
         ChatHandler.sendChat(playerIn, "pixelmon.orb.amountfull", full - damage);
      }

      return new ActionResult(EnumActionResult.SUCCESS, stack);
   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }
}
