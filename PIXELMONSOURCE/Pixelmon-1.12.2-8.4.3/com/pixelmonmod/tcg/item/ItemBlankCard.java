package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.Energy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBlankCard extends Item {
   private final CardRarity cardRarity;
   private final Energy energy;

   public ItemBlankCard(CardRarity cardRarity, Energy energy) {
      this.cardRarity = cardRarity;
      this.energy = energy;
      this.func_77637_a(TCG.tabTCG);
      this.func_77655_b(this.func_77658_a());
      this.func_77625_d(16);
      this.setRegistryName("tcg", this.getName());
   }

   public void func_77663_a(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
      if (!world.field_72995_K && entity != null && entity instanceof EntityPlayerMP) {
         EntityPlayerMP p = (EntityPlayerMP)entity;
         if (itemStack.func_77978_p() == null) {
            itemStack.func_77982_d(new NBTTagCompound());
         }

         NBTTagCompound var7 = itemStack.func_77978_p();
      }

   }

   public String func_77653_i(ItemStack stack) {
      return super.func_77653_i(stack);
   }

   public String func_77658_a() {
      return "card.blank." + this.energy.getUnlocalizedName() + "." + this.cardRarity.getUnlocalizedName();
   }

   public CardRarity getRarity() {
      return this.cardRarity;
   }

   public Energy getEnergy() {
      return this.energy;
   }

   public String getName() {
      return "blank_card." + this.energy.getUnlocalizedName() + "." + this.cardRarity.getUnlocalizedName();
   }
}
