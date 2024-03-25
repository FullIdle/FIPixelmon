package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.gui.GuiGuideBeginner;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGuideBeginner extends Item {
   private static final String name = "beginner_guide";

   public ItemGuideBeginner() {
      this.func_77637_a(TCG.tabTCG);
      this.func_77655_b(getName());
      this.func_77625_d(1);
      this.setRegistryName("tcg", getName());
   }

   public void func_77624_a(ItemStack stack, @Nullable World worldIn, List tooltip, ITooltipFlag flagIn) {
      tooltip.clear();
      tooltip.add(stack.func_82833_r());
      tooltip.add("Activate to view this guide");
   }

   @SideOnly(Side.CLIENT)
   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (world.field_72995_K) {
         Minecraft.func_71410_x().func_147108_a(new GuiGuideBeginner());
      }

      return new ActionResult(EnumActionResult.PASS, player.func_184586_b(hand));
   }

   public static String getName() {
      return "beginner_guide";
   }
}
