package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayer;

public class ItemAbilityPatch extends PixelmonItem {
   public ItemAbilityPatch() {
      super("ability_patch");
   }

   public boolean useOnEntity(EntityPixelmon pixelmon, EntityPlayer player) {
      int slot = pixelmon.getPokemonData().getAbilitySlot();
      if (slot != 2 && pixelmon.getBaseStats().getAbilitiesArray()[2] != null && pixelmon.getSpecies() != EnumSpecies.Zygarde) {
         pixelmon.getPokemonData().setAbility(pixelmon.getBaseStats().getAbilitiesArray()[2]);
         pixelmon.update(new EnumUpdateType[]{EnumUpdateType.Ability});
         ChatHandler.sendChat(player, "pixelmon.interaction.abilitypatch", pixelmon.getNickname(), pixelmon.getPokemonData().getAbility().getTranslatedName());
         return true;
      } else {
         ChatHandler.sendChat(player, "pixelmon.interaction.noeffect");
         return false;
      }
   }
}
