package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.ItemInteractionEvent;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.ItemAbilityPatch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionAbilityPatch implements IInteraction {
   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemStack) {
      if (player instanceof EntityPlayerMP && pixelmon.func_70902_q() == player) {
         Item item = itemStack.func_77973_b();
         if (item instanceof ItemAbilityPatch) {
            ItemAbilityPatch patch = (ItemAbilityPatch)item;
            if (Pixelmon.EVENT_BUS.post(new ItemInteractionEvent(player, pixelmon, itemStack))) {
               return false;
            }

            if (patch.useOnEntity(pixelmon, player) && !player.field_71075_bZ.field_75098_d) {
               player.func_184586_b(hand).func_190918_g(1);
            }

            return true;
         }
      }

      return false;
   }
}
