package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.ItemJuiceShoppe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionBerryJuice implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && entityPixelmon.func_70902_q() == player) {
         Item item = itemstack.func_77973_b();
         if (item instanceof ItemJuiceShoppe) {
            ItemJuiceShoppe vitamin = (ItemJuiceShoppe)item;
            String nickname = entityPixelmon.getNickname();
            if (vitamin.adjustEVs(entityPixelmon, itemstack.func_77978_p() != null ? itemstack.func_77978_p().func_74762_e("Stats_Boost") : 10)) {
               ChatHandler.sendChat(player, "pixelmon.interaction.juice", nickname, vitamin.type.getTranslatedName());
               if (!player.field_71075_bZ.field_75098_d) {
                  player.func_184586_b(hand).func_190918_g(1);
               }
            } else {
               ChatHandler.sendChat(player, "pixelmon.interaction.juice", nickname, vitamin.type.getTranslatedName());
            }

            return true;
         }
      }

      return false;
   }
}
