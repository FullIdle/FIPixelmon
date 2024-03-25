package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.projectiles.EntityHook;
import com.pixelmonmod.pixelmon.enums.items.EnumRodType;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFishingRod extends PixelmonItem {
   private EnumRodType rodType;

   public ItemFishingRod(EnumRodType rodType, String name) {
      super(name);
      this.rodType = rodType;
      this.canRepair = false;
      this.func_77656_e(rodType.maxDamage);
      this.func_77637_a(CreativeTabs.field_78040_i);
      this.func_77625_d(1);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_77662_d() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_77629_n_() {
      return true;
   }

   public ActionResult func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand hand) {
      if (worldIn.field_72995_K) {
         return new ActionResult(EnumActionResult.SUCCESS, playerIn.func_184586_b(hand));
      } else {
         if (playerIn.field_71104_cf != null) {
            boolean canPull = true;
            if (playerIn.field_71104_cf.field_146043_c != null) {
               canPull = playerIn.field_71104_cf.field_146043_c.func_70104_M();
            }

            if (canPull) {
               int i = playerIn.field_71104_cf.func_146034_e();
               playerIn.func_184586_b(hand).func_77972_a(i, playerIn);
            }
         } else if (Pixelmon.storageManager.getParty(playerIn.func_110124_au()).getTeam().stream().anyMatch(Pokemon::canBattle)) {
            worldIn.func_184148_a((EntityPlayer)null, playerIn.field_70165_t, playerIn.field_70163_u + 0.5, playerIn.field_70161_v, SoundEvents.field_187737_v, SoundCategory.PLAYERS, 0.5F, 1.0F);
            int rodLevel = -1;
            if (playerIn.func_184586_b(hand).func_77942_o()) {
               rodLevel = playerIn.func_184586_b(hand).func_77978_p().func_74762_e("rodQuality");
            }

            worldIn.func_72838_d(new EntityHook(worldIn, playerIn, this.rodType, rodLevel));
         } else {
            ChatHandler.sendChat(playerIn, "pixelmon.projectiles.teamfainted");
         }

         playerIn.func_184609_a(hand);
         return new ActionResult(EnumActionResult.SUCCESS, playerIn.func_184586_b(hand));
      }
   }

   public EnumRodType getRodType() {
      return this.rodType;
   }

   public void func_77624_a(ItemStack stack, World world, List tooltip, ITooltipFlag advanced) {
      int rodLevel = -1;
      if (stack.func_77942_o()) {
         rodLevel = stack.func_77978_p().func_74762_e("rodQuality");
      }

      if (rodLevel > -1) {
         tooltip.add(I18n.func_74838_a("item." + this.rodType.name().toLowerCase() + ".quality_" + rodLevel));
      }

      super.func_77624_a(stack, world, tooltip, advanced);
   }
}
