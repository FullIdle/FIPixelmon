package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumHeroDuo;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemRustedShield extends ItemHeld {
   public ItemRustedShield() {
      super(EnumHeldItems.rustedShield, "rusted_shield");
   }

   public void applySwitchInEffect(PixelmonWrapper pw) {
      if (pw.getSpecies() == EnumSpecies.Zamazenta) {
         pw.setForm(EnumHeroDuo.CROWNED);
         Moveset moveset = pw.getMoveset();
         moveset.replaceMove("Iron Head", new Attack("Behemoth Bash"));
         pw.setTemporaryMoveset(moveset);
         pw.type = pw.getSpecies().getBaseStats(EnumHeroDuo.CROWNED).getTypeList();
      }

   }
}
