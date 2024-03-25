package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Collections;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PixelmonItem extends Item {
   private boolean hasEffect;
   private int isFood;
   public int itemUseDuration;
   private int healAmount;
   private float saturationModifier;
   private boolean isWolfsFavoriteMeat;
   private boolean alwaysEdible;
   private PotionEffect potionId;
   private float potionEffectProbability;

   public PixelmonItem(String name) {
      this.hasEffect = false;
      this.isFood = 0;
      this.func_77637_a(CreativeTabs.field_78026_f);
      this.func_77655_b(name);
      this.setRegistryName(name);
   }

   public PixelmonItem(String name, int amount, float saturation, boolean isWolfFood) {
      this(name);
      this.setFood(amount, saturation, isWolfFood);
   }

   public PixelmonItem setFood(int amount, float saturation) {
      this.setFood(amount, saturation, false);
      return this;
   }

   public PixelmonItem setFood(int amount, float saturation, boolean isWolfFood) {
      this.isFood = 1;
      this.itemUseDuration = 32;
      this.healAmount = amount;
      this.isWolfsFavoriteMeat = isWolfFood;
      this.saturationModifier = saturation;
      return this;
   }

   public PixelmonItem setDrink(int amount, float saturation) {
      this.setDrink(amount, saturation, false);
      return this;
   }

   public PixelmonItem setDrink(int amount, float saturation, boolean isWolfFood) {
      this.isFood = 2;
      this.itemUseDuration = 32;
      this.healAmount = amount;
      this.isWolfsFavoriteMeat = isWolfFood;
      this.saturationModifier = saturation;
      return this;
   }

   public boolean isEdible() {
      return this.isFood > 0;
   }

   public boolean isFood() {
      return this.isFood == 1;
   }

   public boolean isDrink() {
      return this.isFood == 2;
   }

   public PixelmonItem setHasEffect(boolean hasEffect) {
      this.hasEffect = hasEffect;
      return this;
   }

   public boolean useFromBag(PixelmonWrapper pixelmonWrapper, PixelmonWrapper target) {
      EntityPlayer player = pixelmonWrapper.getPlayerOwner();
      if (player != null) {
         return !player.field_71075_bZ.field_75098_d;
      } else {
         return false;
      }
   }

   public boolean useFromBag(PixelmonWrapper pixelmonWrapper, PixelmonWrapper target, int additionalInfo) {
      return this.useFromBag(pixelmonWrapper, target);
   }

   public String getLocalizedName() {
      return I18n.func_74838_a(this.func_77658_a() + ".name");
   }

   public boolean func_77636_d(ItemStack itemstack) {
      if (itemstack.func_77942_o() && itemstack.func_77978_p().func_74764_b("HasEffect")) {
         return true;
      } else {
         return this.hasEffect || itemstack.func_77948_v();
      }
   }

   @SideOnly(Side.CLIENT)
   public void func_77624_a(ItemStack stack, World world, List tooltip, ITooltipFlag advanced) {
      String tt = this.getTooltipText(stack);
      if (!tt.isEmpty()) {
         if (GameSettings.func_100015_a(Minecraft.func_71410_x().field_71474_y.field_74311_E)) {
            Collections.addAll(tooltip, tt.split("\n"));
         } else {
            tooltip.add(TextFormatting.GRAY + I18n.func_74838_a("gui.tooltip.collapsed"));
         }
      }

      super.func_77624_a(stack, world, tooltip, advanced);
   }

   public String getTooltipText(ItemStack stack) {
      NBTTagCompound nbt = stack.func_77978_p();
      return nbt != null && nbt.func_74764_b("tooltip") ? nbt.func_74779_i("tooltip") : this.getTooltipText();
   }

   public String getTooltipText() {
      return I18n.func_94522_b(this.func_77658_a() + ".tooltip") ? I18n.func_74838_a(this.func_77658_a() + ".tooltip") : "";
   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }

   public ItemStack func_77654_b(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
      if (this.isEdible()) {
         if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.func_71024_bL().func_75122_a(this.getHealAmount(stack), this.getSaturationModifier(stack));
            if (this.isFood()) {
               worldIn.func_184148_a((EntityPlayer)null, entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v, SoundEvents.field_187739_dZ, SoundCategory.PLAYERS, 0.5F, worldIn.field_73012_v.nextFloat() * 0.1F + 0.9F);
            }

            this.onFoodEaten(stack, worldIn, entityplayer);
            if (entityplayer instanceof EntityPlayerMP) {
               CriteriaTriggers.field_193138_y.func_193148_a((EntityPlayerMP)entityplayer, stack);
            }
         }

         stack.func_190918_g(1);
         return stack;
      } else {
         return super.func_77654_b(stack, worldIn, entityLiving);
      }
   }

   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
      if (this.isEdible() && !worldIn.field_72995_K && this.potionId != null && worldIn.field_73012_v.nextFloat() < this.potionEffectProbability) {
         player.func_70690_d(new PotionEffect(this.potionId));
      }

   }

   public int func_77626_a(ItemStack stack) {
      return this.isEdible() ? 32 : super.func_77626_a(stack);
   }

   public EnumAction func_77661_b(ItemStack stack) {
      if (this.isFood()) {
         return EnumAction.EAT;
      } else {
         return this.isDrink() ? EnumAction.DRINK : super.func_77661_b(stack);
      }
   }

   public ActionResult func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
      if (this.isEdible()) {
         ItemStack itemstack = playerIn.func_184586_b(handIn);
         if (playerIn.func_71043_e(this.alwaysEdible)) {
            playerIn.func_184598_c(handIn);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
         } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
         }
      } else {
         return super.func_77659_a(worldIn, playerIn, handIn);
      }
   }

   public int getHealAmount(ItemStack stack) {
      return this.healAmount;
   }

   public float getSaturationModifier(ItemStack stack) {
      return this.saturationModifier;
   }

   public boolean isWolfsFavoriteMeat() {
      return this.isWolfsFavoriteMeat;
   }

   public PixelmonItem setPotionEffect(PotionEffect effect, float probability) {
      this.potionId = effect;
      this.potionEffectProbability = probability;
      return this;
   }

   public PixelmonItem setAlwaysEdible() {
      this.alwaysEdible = true;
      return this;
   }
}
