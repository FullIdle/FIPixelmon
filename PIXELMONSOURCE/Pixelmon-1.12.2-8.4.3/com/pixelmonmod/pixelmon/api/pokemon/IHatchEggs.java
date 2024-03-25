package com.pixelmonmod.pixelmon.api.pokemon;

import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IHatchEggs {
   List BONUS_ABILITIES = new ArrayList(Arrays.asList("MagmaArmor", "FlameBody", "SteamEngine"));

   void tick(EntityPlayerMP var1, PlayerPartyStorage var2);

   default int getHatchBonus(PlayerPartyStorage party) {
      Iterator var2 = party.getTeam().iterator();

      Pokemon pokemon;
      do {
         if (!var2.hasNext()) {
            return 1;
         }

         pokemon = (Pokemon)var2.next();
      } while(!BONUS_ABILITIES.contains(pokemon.getAbilityName()));

      return 2;
   }
}
