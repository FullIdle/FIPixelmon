package com.pixelmonmod.pixelmon.items.heldItems;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTMs;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBlankTechnicalMachine extends ItemHeld {
   private final String prefix;
   public static final Map maxImprintCount = Maps.newHashMap();

   public ItemBlankTechnicalMachine(String prefix) {
      super(EnumHeldItems.other, prefix + "_blank");
      this.prefix = prefix;
      this.func_77637_a(PixelmonCreativeTabs.tms);
      this.setNoRepair();
   }

   public void func_77624_a(ItemStack stack, World world, List tooltip, ITooltipFlag advanced) {
      NBTTagCompound compound = stack.func_190925_c("tm");
      if (compound.func_74762_e("id") == 0 && compound.func_74765_d("count") == 0) {
         super.func_77624_a(stack, world, tooltip, advanced);
      } else {
         int tmId = compound.func_74762_e("id");
         short count = compound.func_74765_d("count");
         ITechnicalMove move = ITechnicalMove.getMoveFor(this.prefix, tmId);
         int maxCount = compound.func_74764_b("max_count") ? compound.func_74765_d("max_count") : (compound.func_74764_b("max_count_default") ? compound.func_74765_d("max_count_default") : (Integer)maxImprintCount.getOrDefault(move, 100));
         tooltip.add(I18n.func_135052_a("tm.blankdata.tooltip", new Object[]{move.getAttack().getLocalizedName(), count, maxCount}));
      }

   }

   public void onAttackUsed(PixelmonWrapper user, Attack attack) {
      ItemStack stack = user.pokemon.getHeldItem();
      if (stack.func_77973_b() == this) {
         ITechnicalMove move = ITechnicalMove.getMoveFor(this.prefix, attack.getActualMove().getAttackName());
         if (move != null) {
            NBTTagCompound compound;
            if (stack.func_179543_a("tm8") != null || stack.func_179543_a("tr8") != null) {
               compound = stack.func_179543_a("tm8");
               if (compound != null) {
                  stack.func_190919_e("tm8");
                  stack.func_77978_p().func_74782_a("tm", compound);
               }

               NBTTagCompound tr8 = stack.func_179543_a("tr8");
               if (tr8 != null) {
                  stack.func_190919_e("tr8");
                  stack.func_77978_p().func_74782_a("tm", tr8);
               }
            }

            compound = stack.func_190925_c("tm");
            if (move.getId() == 0 && !compound.func_82582_d()) {
               compound.func_74777_a("count", (short)((byte)(compound.func_74765_d("count") + 1)));
            } else if (compound.func_74762_e("id") == 0) {
               if ((Integer)maxImprintCount.getOrDefault(move, 0) == -1) {
                  return;
               }

               compound.func_74768_a("id", move.getId());
               compound.func_74777_a("count", (short)1);
               return;
            }

            if (compound.func_74762_e("id") == move.getId() && move.getId() != 0) {
               compound.func_74777_a("count", (short)((byte)(compound.func_74765_d("count") + 1)));
            }

            int maxCount = compound.func_74764_b("max_count") && compound.func_74765_d("max_count") != 0 ? compound.func_74765_d("max_count") : (Integer)maxImprintCount.getOrDefault(move, 100);
            compound.func_74777_a("max_count_default", (short)(Integer)maxImprintCount.getOrDefault(move, 100));
            if (maxCount <= compound.func_74765_d("count")) {
               user.pokemon.setHeldItem(PixelmonItemsTMs.createStackFor(move, 1));
            }
         }

      }
   }

   public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("tm")) {
         stack.func_190919_e("tm");
      }

   }
}
