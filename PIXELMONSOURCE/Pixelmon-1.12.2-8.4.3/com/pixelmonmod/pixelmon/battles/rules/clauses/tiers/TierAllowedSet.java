package com.pixelmonmod.pixelmon.battles.rules.clauses.tiers;

import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.client.resources.I18n;

public class TierAllowedSet extends TierSet {
   private boolean convertToBanlist;

   public TierAllowedSet(String id, Set pokemon, boolean convertToBanlist) {
      super(id, pokemon);
      this.convertToBanlist = convertToBanlist;
   }

   protected Predicate getCondition() {
      return (p) -> {
         return this.convertToBanlist ? !this.isInSet(p) : this.isInSet(p);
      };
   }

   public String getTierDescription() {
      StringBuilder builder = new StringBuilder();
      builder.append(I18n.func_135052_a("gui.battlerules." + (this.convertToBanlist ? "banned" : "allowed"), new Object[0]));
      builder.append(": ");
      builder.append(super.getTierDescription());
      return builder.toString();
   }
}
