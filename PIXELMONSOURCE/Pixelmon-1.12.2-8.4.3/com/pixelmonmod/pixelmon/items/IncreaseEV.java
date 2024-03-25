package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.EVsGainedEvent;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumIncreaseEV;
import net.minecraft.item.ItemStack;

public class IncreaseEV extends PixelmonItem {
   public EnumIncreaseEV type;

   public IncreaseEV(EnumIncreaseEV type, String itemName) {
      super(itemName);
      this.type = type;
      this.func_77625_d(16);
      this.func_77637_a(PixelmonCreativeTabs.restoration);
      this.canRepair = false;
   }

   public boolean adjustEVs(EntityPixelmon entityPixelmon, ItemStack itemStack) {
      if (Pixelmon.EVENT_BUS.post(new EVsGainedEvent.Item(entityPixelmon.getPokemonData(), entityPixelmon.getPokemonData().getEVs(), 1, this.type.statAffected, itemStack))) {
         return false;
      } else {
         boolean success = this.type.isFeather ? entityPixelmon.getPokemonData().getEVs().wingEVs(this.type.statAffected) : entityPixelmon.getPokemonData().getEVs().vitaminEVs(this.type.statAffected);
         if (success) {
            if (entityPixelmon.getPokemonData().getFriendship() < 100) {
               entityPixelmon.getPokemonData().increaseFriendship(5);
            } else if (entityPixelmon.getPokemonData().getFriendship() < 200) {
               entityPixelmon.getPokemonData().increaseFriendship(3);
            } else {
               entityPixelmon.getPokemonData().increaseFriendship(2);
            }

            entityPixelmon.updateStats();
            entityPixelmon.update(new EnumUpdateType[]{EnumUpdateType.Stats});
         }

         return success;
      }
   }
}
