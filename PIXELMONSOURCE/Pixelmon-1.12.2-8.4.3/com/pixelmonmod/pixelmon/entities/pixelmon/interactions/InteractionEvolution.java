package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.MilcerySweet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionEvolution implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemStack) {
      if (player instanceof EntityPlayerMP && hand == EnumHand.MAIN_HAND && entityPixelmon.func_70902_q() == player) {
         if (itemStack.func_77973_b() instanceof MilcerySweet) {
            MilcerySweet item = (MilcerySweet)itemStack.func_77973_b();
            if (item.interact(entityPixelmon, itemStack, player)) {
               return true;
            }
         }

         if (entityPixelmon.testInteractEvolution(itemStack)) {
            player.func_184586_b(hand).func_190918_g(1);
            return true;
         }
      }

      return false;
   }

   public boolean processOnEmptyHand(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      return pixelmon.getSpecies() == EnumSpecies.Kubfu;
   }
}
