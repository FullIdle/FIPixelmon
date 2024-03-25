package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class ItemClause extends BattleClause {
   ItemClause() {
      super("item");
   }

   public boolean validateTeam(List team) {
      Set itemSet = new HashSet();
      Iterator var3 = team.iterator();

      while(var3.hasNext()) {
         Pokemon pokemon = (Pokemon)var3.next();
         ItemStack itemStack = pokemon.getHeldItem();
         if (!itemStack.func_190926_b()) {
            Item item = itemStack.func_77973_b();
            if (itemSet.contains(item)) {
               return false;
            }

            itemSet.add(item);
         }
      }

      return true;
   }
}
