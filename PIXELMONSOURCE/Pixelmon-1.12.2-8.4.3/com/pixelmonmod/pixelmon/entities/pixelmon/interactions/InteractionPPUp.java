package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.items.ItemPPUp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionPPUp implements IInteraction {
   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && pixelmon.func_70902_q() == player) {
         Item item = itemstack.func_77973_b();
         StoragePosition position = pixelmon.getPokemonData().getPosition();
         if (item instanceof ItemPPUp && position != null && position.box == -1) {
            OpenScreen.open(player, EnumGuiScreen.SelectMove, 1, position.order);
            return true;
         }
      }

      return false;
   }
}
