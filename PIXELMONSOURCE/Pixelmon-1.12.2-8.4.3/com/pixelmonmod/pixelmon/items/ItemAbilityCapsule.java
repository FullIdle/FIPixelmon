package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayer;

public class ItemAbilityCapsule extends PixelmonItem {
   public ItemAbilityCapsule() {
      super("ability_capsule");
   }

   public boolean useOnEntity(EntityPixelmon pixelmon, EntityPlayer player) {
      int slot = pixelmon.getPokemonData().getAbilitySlot();
      if (slot != 2 && pixelmon.getBaseStats().getAbilitiesArray()[1] != null && pixelmon.getSpecies() != EnumSpecies.Zygarde) {
         int newSlot = slot == 1 ? 0 : 1;
         pixelmon.getPokemonData().setAbility(pixelmon.getBaseStats().getAbilitiesArray()[newSlot]);
         pixelmon.update(new EnumUpdateType[]{EnumUpdateType.Ability});
         ChatHandler.sendChat(player, "pixelmon.interaction.abilitycapsule", pixelmon.getNickname(), pixelmon.getPokemonData().getAbility().getTranslatedName());
         return true;
      } else {
         ChatHandler.sendChat(player, "pixelmon.interaction.noeffect");
         return false;
      }
   }
}
