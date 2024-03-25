package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionHeldItem implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemStack) {
      if (player instanceof EntityPlayerMP && entityPixelmon.func_70902_q() == player && hand == EnumHand.MAIN_HAND) {
         Item item = itemStack.func_77973_b();
         if (item instanceof ItemHeld) {
            ItemHeld itemHeld = (ItemHeld)item;
            if (itemHeld.interact(entityPixelmon, itemStack, player)) {
               if (itemHeld.getHeldItemType() != EnumHeldItems.leppa) {
                  player.func_184586_b(hand).func_190918_g(1);
               }

               return true;
            }

            ItemStack currentItem = entityPixelmon.func_184614_ca();
            if (currentItem != null && currentItem != ItemStack.field_190927_a) {
               if (currentItem.func_77973_b() == item) {
                  return true;
               }

               if (!entityPixelmon.field_70170_p.field_72995_K) {
                  entityPixelmon.func_70099_a(entityPixelmon.getPokemonData().getHeldItem().func_77946_l(), 1.0F);
               }

               entityPixelmon.getPokemonData().setHeldItem(ItemStack.field_190927_a);
            }

            ItemStack itemStack1 = itemStack.func_77946_l();
            player.func_184586_b(hand).func_190918_g(1);
            itemStack1.func_190920_e(1);
            entityPixelmon.getPokemonData().setHeldItem(itemStack1);
            if (entityPixelmon.getPokemonData().getHeldItemAsItemHeld() == NoItem.noItem) {
               ChatHandler.sendChat(player, "Couldn't give item: " + itemStack1.func_82833_r());
            } else {
               entityPixelmon.update(new EnumUpdateType[]{EnumUpdateType.HeldItem});
               ChatHandler.sendChat(player, "pixelmon.interaction.helditem", entityPixelmon.getNickname(), itemHeld.getLocalizedName());
            }

            return true;
         }
      }

      return false;
   }
}
