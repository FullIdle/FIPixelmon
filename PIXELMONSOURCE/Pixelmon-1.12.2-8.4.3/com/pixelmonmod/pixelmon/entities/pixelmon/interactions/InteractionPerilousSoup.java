package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

public class InteractionPerilousSoup implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && entityPixelmon.func_70902_q() == player && hand == EnumHand.MAIN_HAND && itemstack.func_77973_b() == PixelmonItems.perilousSoup) {
         if (entityPixelmon.getPokemonData().getEVs().getTotal() > 0) {
            entityPixelmon.getPokemonData().getEVs().fillFromArray(new int[]{0, 0, 0, 0, 0, 0});
            player.func_145747_a(new TextComponentTranslation("pixelmon.interaction.periloussoup.success", new Object[]{entityPixelmon.getNickname()}));
            if (!player.func_184812_l_()) {
               player.func_184586_b(hand).func_190918_g(1);
            }
         } else {
            player.func_145747_a(new TextComponentTranslation("pixelmon.interaction.periloussoup.failure", new Object[]{entityPixelmon.getNickname()}));
         }

         return true;
      } else {
         return false;
      }
   }
}
