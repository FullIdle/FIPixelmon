package com.pixelmonmod.tcg.inventory;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SlotPrinterOutput extends Slot {
   private EntityPlayer thePlayer;
   private int field_75228_b;

   public SlotPrinterOutput(EntityPlayer player, IInventory inventory, int slotIndex, int xPosition, int yPosition) {
      super(inventory, slotIndex, xPosition, yPosition);
      this.thePlayer = player;
   }

   public boolean func_75214_a(ItemStack stack) {
      return false;
   }

   public ItemStack func_75209_a(int amount) {
      if (this.func_75216_d()) {
         this.field_75228_b += Math.min(amount, this.func_75211_c().func_190916_E());
      }

      return super.func_75209_a(amount);
   }

   public ItemStack func_190901_a(EntityPlayer playerIn, ItemStack stack) {
      this.func_75208_c(stack);
      return super.func_190901_a(playerIn, stack);
   }

   protected void func_75210_a(ItemStack stack, int amount) {
      this.field_75228_b += amount;
      this.func_75208_c(stack);
   }

   protected void func_75208_c(ItemStack stack) {
      stack.func_77980_a(this.thePlayer.field_70170_p, this.thePlayer, this.field_75228_b);
      if (!this.thePlayer.field_70170_p.field_72995_K) {
         int i = this.field_75228_b;
         float f = FurnaceRecipes.func_77602_a().func_151398_b(stack);
         int j;
         if (f == 0.0F) {
            i = 0;
         } else if (f < 1.0F) {
            j = MathHelper.func_76141_d((float)i * f);
            if (j < MathHelper.func_76123_f((float)i * f) && Math.random() < (double)((float)i * f - (float)j)) {
               ++j;
            }

            i = j;
         }

         while(i > 0) {
            j = EntityXPOrb.func_70527_a(i);
            i -= j;
            this.thePlayer.field_70170_p.func_72838_d(new EntityXPOrb(this.thePlayer.field_70170_p, this.thePlayer.field_70165_t, this.thePlayer.field_70163_u + 0.5, this.thePlayer.field_70161_v + 0.5, j));
         }
      }

      this.field_75228_b = 0;
      FMLCommonHandler.instance().firePlayerSmeltedEvent(this.thePlayer, stack);
   }
}
