package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ServerTrash;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionGaeBolg implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && hand == EnumHand.MAIN_HAND && player.func_184812_l_() && itemstack.func_77973_b() == PixelmonItems.gaeBolg) {
         if (entityPixelmon.func_70902_q() == player) {
            entityPixelmon.func_70106_y();
            StoragePosition position = new StoragePosition(-1, entityPixelmon.getPartyPosition());
            Pixelmon.network.sendToServer(new ServerTrash(position, entityPixelmon.getStoragePokemonData()));
            entityPixelmon.getStorage().set(position, (Pokemon)null);
         } else if (entityPixelmon.func_70902_q() == null) {
            entityPixelmon.func_70106_y();
         }

         return true;
      } else {
         return false;
      }
   }
}
