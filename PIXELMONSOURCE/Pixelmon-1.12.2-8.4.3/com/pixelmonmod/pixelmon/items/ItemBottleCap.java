package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.items.EnumBottleCap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;

public class ItemBottleCap extends PixelmonItem {
   public final EnumBottleCap type;

   public ItemBottleCap(EnumBottleCap type) {
      super(type.toString().toLowerCase() + "_bottle_cap");
      this.type = type;
      this.func_77625_d(16);
      this.func_77637_a(CreativeTabs.field_78026_f);
      this.canRepair = false;
   }

   public static boolean onSilverSelection(EntityPlayerMP player, Pokemon pokemon, StatsType type) {
      IVStore store = pokemon.getIVs();
      if (!store.isHyperTrained(type)) {
         store.setHyperTrained(type, true);
         pokemon.getStats().setLevelStats(pokemon.getNature(), pokemon.getBaseStats(), pokemon.getLevel());
         pokemon.markDirty(EnumUpdateType.HP, EnumUpdateType.Stats);
         ChatHandler.sendChat(player, "pixelmon.interaction.bottlecap.silvercap", pokemon.getDisplayName(), type.getTranslatedName());
         return true;
      } else {
         return false;
      }
   }
}
