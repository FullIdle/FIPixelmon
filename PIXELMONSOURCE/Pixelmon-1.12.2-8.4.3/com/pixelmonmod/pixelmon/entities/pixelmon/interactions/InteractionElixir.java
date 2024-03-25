package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.items.ItemElixir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionElixir implements IInteraction {
   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && pixelmon.func_70902_q() == player) {
         Item item = itemstack.func_77973_b();
         if (item instanceof ItemElixir) {
            if (((ItemElixir)item).useElixir(new DelegateLink(pixelmon.getPokemonData())) && !player.field_71075_bZ.field_75098_d) {
               player.func_184586_b(hand).func_190918_g(1);
            }

            return true;
         }
      }

      return false;
   }
}
