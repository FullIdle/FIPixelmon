package com.pixelmonmod.pixelmon.comm.packetHandlers.chooseMoveset;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;

public class ChoosingMovesetData {
   EntityPlayerMP player;
   public List pokemonList;

   public ChoosingMovesetData(EntityPlayerMP player, List pokemonList) {
      this.player = player;
      this.pokemonList = pokemonList;
   }

   public void next() {
      Pokemon pokemon = (Pokemon)this.pokemonList.get(0);
      int slot = Pixelmon.storageManager.getParty(this.player).getPosition(pokemon).order;
      OpenScreen.open(this.player, EnumGuiScreen.ChooseMoveset, slot);
      this.pokemonList.remove(0);
   }
}
