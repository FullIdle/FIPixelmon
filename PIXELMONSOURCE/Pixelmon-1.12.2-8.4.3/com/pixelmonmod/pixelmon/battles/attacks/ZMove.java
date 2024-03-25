package com.pixelmonmod.pixelmon.battles.attacks;

import java.util.List;
import net.minecraft.util.text.translation.I18n;

public class ZMove {
   public String crystal;
   public String attackName;
   public int basePower;
   public List effects;
   public List allowedPokemon;

   public ZMove(String crystal, String attackName, int basePower, List effects, List allowedPokemon) {
      this.crystal = crystal;
      this.attackName = attackName;
      this.basePower = basePower;
      this.effects = effects;
      this.allowedPokemon = allowedPokemon;
   }

   public String getLocalizedName() {
      return this.attackName.contains("Z-") ? "Z-" + I18n.func_74838_a("attack." + this.attackName.replace("Z-", "").replace(" ", "_").toLowerCase() + ".name") : I18n.func_74838_a("attack." + this.attackName.toLowerCase().replace(" ", "_") + ".name");
   }
}
