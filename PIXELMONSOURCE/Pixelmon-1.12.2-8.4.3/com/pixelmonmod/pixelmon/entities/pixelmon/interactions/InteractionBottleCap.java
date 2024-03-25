package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.BottleCapEvent;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.items.EnumBottleCap;
import com.pixelmonmod.pixelmon.items.ItemBottleCap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionBottleCap implements IInteraction {
   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (!player.field_70170_p.field_72995_K && hand != EnumHand.OFF_HAND && itemstack.func_77973_b() instanceof ItemBottleCap) {
         Pokemon data = pixelmon.getPokemonData();
         if (data.getOwnerPlayer() != player) {
            return false;
         } else if (data.getLevel() < PixelmonConfig.maxLevel) {
            ChatHandler.sendChat(player, "pixelmon.interaction.bottlecap.level", pixelmon.getNickname());
            return true;
         } else {
            IVStore ivs = data.getIVs();
            boolean isMax = ivs.getStat(StatsType.HP) + ivs.getStat(StatsType.Attack) + ivs.getStat(StatsType.Defence) + ivs.getStat(StatsType.SpecialAttack) + ivs.getStat(StatsType.SpecialDefence) + ivs.getStat(StatsType.Speed) == 186;
            boolean isHt = ivs.isHyperTrained(StatsType.HP) && ivs.isHyperTrained(StatsType.Attack) && ivs.isHyperTrained(StatsType.Defence) && ivs.isHyperTrained(StatsType.SpecialAttack) && ivs.isHyperTrained(StatsType.SpecialDefence) && ivs.isHyperTrained(StatsType.Speed);
            if (!isMax && !isHt) {
               ItemBottleCap bottleCap = (ItemBottleCap)itemstack.func_77973_b();
               if (Pixelmon.EVENT_BUS.post(new BottleCapEvent(pixelmon, player, bottleCap.type, itemstack))) {
                  return false;
               } else {
                  if (bottleCap.type == EnumBottleCap.GOLD) {
                     ivs.setHyperTrained((StatsType)null, true);
                     data.getStats().setLevelStats(data.getNature(), data.getBaseStats(), data.getLevel());
                     data.markDirty(EnumUpdateType.HP, EnumUpdateType.Stats);
                     ChatHandler.sendChat(player, "pixelmon.interaction.bottlecap.goldcap", pixelmon.getNickname());
                     itemstack.func_190918_g(1);
                  } else {
                     StatsType[] types = StatsType.getStatValues();
                     int[] screenData = new int[types.length + 1];

                     for(int i = 0; i < types.length; ++i) {
                        screenData[i] = !ivs.isHyperTrained(types[i]) && ivs.getStat(types[i]) != 31 ? getHTValue(types[i], data) : 0;
                     }

                     screenData[6] = pixelmon.func_145782_y();
                     OpenScreen.open(player, EnumGuiScreen.BottleCap, screenData);
                  }

                  return true;
               }
            } else {
               ChatHandler.sendChat(player, "pixelmon.interaction.bottlecap.full", pixelmon.getNickname());
               return true;
            }
         }
      } else {
         return false;
      }
   }

   private static int getHTValue(StatsType type, Pokemon pokemon) {
      IVStore store = pokemon.getIVs();
      boolean isHT = store.isHyperTrained(type);
      store.setHyperTrained(type, true);
      int stat = type == StatsType.HP ? pokemon.getStats().calculateHP(pokemon.getBaseStats(), pokemon.getLevel()) : pokemon.getStats().calculateStat(type, pokemon.getNature(), pokemon.getBaseStats(), pokemon.getLevel());
      store.setHyperTrained(type, isHT);
      return stat;
   }
}
