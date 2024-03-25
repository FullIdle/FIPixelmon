package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.items.ItemMedicine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionPotion implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && entityPixelmon.func_70902_q() == player && itemstack.func_77973_b() instanceof ItemMedicine) {
         if (((ItemMedicine)itemstack.func_77973_b()).useMedicine(new DelegateLink(entityPixelmon.getPokemonData()), 0) && !player.field_71075_bZ.field_75098_d) {
            player.func_184586_b(hand).func_190918_g(1);
         }

         return true;
      } else {
         return false;
      }
   }
}
