package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.EnumDecreaseEV;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class DecreaseEV extends ItemBerry {
   public EnumDecreaseEV type;

   public DecreaseEV(EnumDecreaseEV type, EnumBerry berry, String itemName) {
      super(EnumHeldItems.berryEVReducing, berry, itemName);
      this.type = type;
      this.func_77637_a(PixelmonCreativeTabs.restoration);
      this.canRepair = false;
   }

   public boolean interact(EntityPixelmon pokemon, ItemStack itemstack, EntityPlayer player) {
      return this.berryEVs(pokemon);
   }

   public boolean berryEVs(EntityPixelmon entityPixelmon) {
      boolean success = false;
      EntityLivingBase owner = entityPixelmon.func_70902_q();
      String nickname = entityPixelmon.getNickname();
      if (entityPixelmon.getPokemonData().getEVs().berryEVs(this.type.statAffected)) {
         ChatHandler.sendChat(owner, "pixelmon.interaction.berry", nickname, this.type.statAffected.getTranslatedName());
         success = true;
      }

      boolean increasedFriendship = true;
      if (entityPixelmon.getPokemonData().getFriendship() >= 255) {
         increasedFriendship = false;
      } else if (entityPixelmon.getPokemonData().getFriendship() < 100) {
         entityPixelmon.getPokemonData().increaseFriendship(10);
      } else if (entityPixelmon.getPokemonData().getFriendship() < 200) {
         entityPixelmon.getPokemonData().increaseFriendship(5);
      } else {
         entityPixelmon.getPokemonData().increaseFriendship(2);
      }

      if (increasedFriendship) {
         ChatHandler.sendChat(owner, "pixelmon.interaction.berryfriend", nickname);
         success = true;
      }

      if (success) {
         entityPixelmon.updateStats();
         entityPixelmon.update(new EnumUpdateType[]{EnumUpdateType.Stats});
      } else {
         ChatHandler.sendChat(owner, "pixelmon.interaction.berryfail", nickname, this.type.statAffected.getTranslatedName());
      }

      return success;
   }
}
