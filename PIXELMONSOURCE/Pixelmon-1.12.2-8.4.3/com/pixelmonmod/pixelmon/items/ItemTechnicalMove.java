package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTechnicalMove extends PixelmonItem {
   private static final IItemPropertyGetter TYPE_GETTER = new IItemPropertyGetter() {
      @SideOnly(Side.CLIENT)
      public float func_185085_a(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
         ITechnicalMove move = ItemTechnicalMove.getMove(stack);
         return move == null ? 0.0F : (float)move.getAttack().getAttackType().getIndex();
      }
   };
   private final String prefix;

   public ItemTechnicalMove(String prefix) {
      super(prefix);
      this.prefix = prefix;
      this.func_77627_a(true);
      this.func_77637_a(PixelmonCreativeTabs.tms);
      this.func_185043_a(new ResourceLocation("pixelmon", "move_type"), TYPE_GETTER);
   }

   public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (!worldIn.field_72995_K) {
         if (getMove(stack) == null) {
            if (!stack.func_77942_o()) {
               stack.func_77982_d(new NBTTagCompound());
            }

            stack.func_77978_p().func_74777_a("tm", (short)stack.func_77952_i());
            if (getMove(stack) == null) {
               stack.func_77978_p().func_74777_a("tm", (short)((ITechnicalMove)RandomHelper.getRandomElementFromArray(ITechnicalMove.getAllFor(this.prefix))).getId());
            }
         }

         if (stack.func_77952_i() != 0) {
            stack.func_77964_b(0);
         }
      }

      super.func_77663_a(stack, worldIn, entityIn, itemSlot, isSelected);
   }

   public int getMetadata(ItemStack stack) {
      return stack.func_77942_o() ? stack.func_77978_p().func_74765_d("tm") : -1;
   }

   public String func_77667_c(ItemStack stack) {
      ITechnicalMove technicalMove = getMove(stack);
      return technicalMove == null ? super.func_77667_c(stack) : "item." + this.prefix + "." + technicalMove.getId();
   }

   public String getTooltipText(ItemStack stack) {
      String ret = "";
      ITechnicalMove technicalMove = getMove(stack);
      if (technicalMove == null) {
         return ret;
      } else {
         AttackBase ab = technicalMove.getAttack();
         if (ab != null && ab.isAttack(technicalMove.getAttackName())) {
            String formattedData = I18n.func_74837_a("tm.movedata.tooltip", new Object[]{"§" + String.format("#%06X", 16777215 & ab.getAttackType().getColor()) + ab.getAttackType().getLocalizedName() + TextFormatting.GRAY, ab.getBasePower() != 0 ? ab.getBasePower() : "-", ab.getAccuracy() != -1 ? ab.getAccuracy() : "-", ab.getPPBase()});
            ret = ret + formattedData;
            if (I18n.func_94522_b(this.func_77658_a() + ".tooltip")) {
               ret = ret + I18n.func_74838_a(this.func_77658_a() + ".tooltip");
            }

            return ret;
         } else {
            return ret;
         }
      }
   }

   public void func_150895_a(CreativeTabs tab, NonNullList items) {
      if (this.func_194125_a(tab)) {
         ITechnicalMove[] arr = ITechnicalMove.getAllFor(this.prefix);
         if (arr == null) {
            return;
         }

         ITechnicalMove[] var4 = arr;
         int var5 = arr.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ITechnicalMove move = var4[var6];
            ItemStack stack = new ItemStack(this, 1, move.getId());
            if (!stack.func_77942_o()) {
               stack.func_77982_d(new NBTTagCompound());
            }

            stack.func_77978_p().func_74777_a("tm", (short)move.getId());
            items.add(stack);
         }
      }

   }

   @Nullable
   public NBTTagCompound getNBTShareTag(ItemStack stack) {
      NBTTagCompound compound = super.getNBTShareTag(stack);
      if (compound != null && compound.func_74764_b("tm")) {
         compound.func_74768_a("tm", compound.func_74765_d("tm"));
      }

      return compound;
   }

   public String type() {
      return this.prefix;
   }

   @Nullable
   public static ITechnicalMove getMove(ItemStack stack) {
      if (stack.func_77973_b() instanceof ItemTechnicalMove && stack.func_77942_o()) {
         int id = stack.func_77978_p().func_74765_d("tm");
         return ITechnicalMove.getMoveFor(((ItemTechnicalMove)stack.func_77973_b()).prefix, id);
      } else {
         return null;
      }
   }

   @Nullable
   public static ITechnicalMove getInstanceOf(ItemStack stack) {
      return stack.func_77973_b() instanceof ItemTechnicalMove ? ITechnicalMove.getMoveFor(((ItemTechnicalMove)stack.func_77973_b()).prefix, 0) : null;
   }
}
