package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionColorfulShake implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && entityPixelmon.func_70902_q() == player && hand == EnumHand.MAIN_HAND && itemstack.func_77973_b() == PixelmonItems.colorfulShake) {
         if (entityPixelmon.getPokemonData().getFriendship() >= 255) {
            ChatHandler.sendChat(player, "pixelmon.interaction.shake.fail", entityPixelmon.getNickname());
            return false;
         } else {
            entityPixelmon.getPokemonData().increaseFriendship(itemstack.func_77978_p() != null ? itemstack.func_77978_p().func_74762_e("Stats_Boost") : 10);
            if (!player.field_71075_bZ.field_75098_d) {
               player.func_184586_b(hand).func_190918_g(1);
            }

            ChatHandler.sendChat(player, "pixelmon.interaction.shake", entityPixelmon.getNickname());
            return true;
         }
      } else {
         return false;
      }
   }
}
