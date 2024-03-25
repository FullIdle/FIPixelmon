package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.heldItems.HeldItem;
import com.pixelmonmod.pixelmon.listener.SpinListener;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MilcerySweet extends HeldItem {
   private static boolean bypass = false;

   public MilcerySweet(EnumHeldItems heldItem, String name) {
      super(heldItem, name);
      this.setNoRepair();
      this.func_77637_a(CreativeTabs.field_78026_f);
   }

   public boolean interact(EntityPixelmon pokemon, ItemStack itemstack, EntityPlayer player) {
      if (pokemon.getSpecies() == EnumSpecies.Milcery && pokemon.func_70902_q() == player && !bypass) {
         SpinListener.registerListener(player, (pl) -> {
            if (!pokemon.field_70128_L && !itemstack.func_190926_b() && pl.field_71071_by.func_70431_c(itemstack) && pokemon.testInteractEvolution(itemstack)) {
               itemstack.func_190918_g(1);
            }

         });
         ChatHandler.sendChat(player, "pixelmon.interaction.milcerysweet", pokemon.func_145748_c_());
         return true;
      } else {
         return false;
      }
   }
}
