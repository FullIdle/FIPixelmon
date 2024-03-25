package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.items.ItemZygardeCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionZygardeCube implements IInteraction {
   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && pixelmon.func_70902_q() == player && itemstack.func_77973_b() instanceof ItemZygardeCube) {
         StoragePosition position = pixelmon.getPokemonData().getPosition();
         if (position != null && position.box == -1) {
            OpenScreen.open(player, EnumGuiScreen.ZygardeCube, hand.ordinal(), position.order);
            return true;
         }
      }

      return false;
   }
}
