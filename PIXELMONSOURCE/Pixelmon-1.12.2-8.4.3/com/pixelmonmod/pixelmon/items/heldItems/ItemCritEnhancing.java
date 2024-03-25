package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ItemCritEnhancing extends ItemHeld {
   private final Set pokemon = new HashSet();

   public ItemCritEnhancing(EnumHeldItems type, String name, EnumSpecies... pokemon) {
      super(type, name);
      this.pokemon.addAll(Arrays.asList(pokemon));
   }

   public int adjustCritStage(PixelmonWrapper user) {
      return this.pokemon.contains(user.getSpecies()) ? 2 : 0;
   }
}
