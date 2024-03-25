package com.pixelmonmod.pixelmon.customStarters;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SelectPokemonListPacket;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayerMP;

public class SelectPokemonController {
   private static HashMap hashPokemonArrays = new HashMap();

   public static boolean sendSelectPokemon(EntityPlayerMP p, EnumSpecies[] pokemonList) {
      if (isOnList(p)) {
         return false;
      } else {
         hashPokemonArrays.put(p.func_110124_au(), pokemonList);
         Pixelmon.network.sendTo(new SelectPokemonListPacket(getPokemonList(p)), p);
         return true;
      }
   }

   public static boolean removePlayer(EntityPlayerMP p) {
      if (isOnList(p)) {
         hashPokemonArrays.remove(p.func_110124_au());
         return true;
      } else {
         return false;
      }
   }

   public static EnumSpecies[] getPokemonList(EntityPlayerMP p) {
      return isOnList(p) ? (EnumSpecies[])hashPokemonArrays.get(p.func_110124_au()) : null;
   }

   public static boolean isOnList(EntityPlayerMP p) {
      return !hashPokemonArrays.isEmpty() && hashPokemonArrays.containsKey(p.func_110124_au());
   }
}
