package com.pixelmonmod.pixelmon.battles.raids;

import com.pixelmonmod.pixelmon.RandomHelper;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

public class WeightedItemStacks {
   private int total = 0;
   private final ArrayList stacks = new ArrayList();

   private WeightedItemStacks() {
   }

   public static WeightedItemStacks create() {
      return new WeightedItemStacks();
   }

   public WeightedItemStacks add(int weight, ItemStack stack) {
      this.stacks.add(new Tuple(weight, stack));
      this.total += weight;
      return this;
   }

   public WeightedItemStacks addAll(WeightedItemStacks stacks) {
      Tuple stack;
      for(Iterator var2 = stacks.stacks.iterator(); var2.hasNext(); this.total += (Integer)stack.func_76341_a()) {
         stack = (Tuple)var2.next();
         this.stacks.add(new Tuple(stack.func_76341_a(), ((ItemStack)stack.func_76340_b()).func_77946_l()));
      }

      return this;
   }

   public ItemStack get() {
      if (this.total == 0) {
         return ItemStack.field_190927_a;
      } else {
         int value = RandomHelper.rand.nextInt(this.total) + 1;
         Iterator var2 = this.stacks.iterator();

         Tuple tuple;
         do {
            if (!var2.hasNext()) {
               return ItemStack.field_190927_a;
            }

            tuple = (Tuple)var2.next();
            value -= (Integer)tuple.func_76341_a();
         } while(value > 0);

         return ((ItemStack)tuple.func_76340_b()).func_77946_l();
      }
   }

   public ItemStack pop() {
      if (this.total == 0) {
         return ItemStack.field_190927_a;
      } else {
         int value = RandomHelper.rand.nextInt(this.total) + 1;
         Iterator iterator = this.stacks.iterator();

         Tuple tuple;
         do {
            if (!iterator.hasNext()) {
               return ItemStack.field_190927_a;
            }

            tuple = (Tuple)iterator.next();
            value -= (Integer)tuple.func_76341_a();
         } while(value > 0);

         iterator.remove();
         this.total -= (Integer)tuple.func_76341_a();
         return ((ItemStack)tuple.func_76340_b()).func_77946_l();
      }
   }
}
