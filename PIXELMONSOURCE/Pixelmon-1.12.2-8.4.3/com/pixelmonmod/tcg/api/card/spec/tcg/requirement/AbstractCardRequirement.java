package com.pixelmonmod.tcg.api.card.spec.tcg.requirement;

import com.pixelmonmod.tcg.api.card.CardProxy;
import com.pixelmonmod.tcg.api.card.spec.requirement.AbstractRequirement;
import java.util.List;
import java.util.Set;
import net.minecraft.item.ItemStack;

public abstract class AbstractCardRequirement extends AbstractRequirement {
   public AbstractCardRequirement(Set keys) {
      super(keys);
   }

   public abstract List createSimple(String var1, String var2);

   public void applyMinecraft(ItemStack itemStack) {
      this.applyData(CardProxy.readCard(itemStack));
   }

   public boolean isMinecraftMatch(ItemStack itemStack) {
      return this.isDataMatch(CardProxy.readCard(itemStack));
   }
}
