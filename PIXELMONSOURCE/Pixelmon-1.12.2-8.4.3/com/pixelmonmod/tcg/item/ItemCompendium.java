package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.gui.GuiCompendium;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCompendium extends Item {
   private static final String name = "compendium";

   public ItemCompendium() {
      this.func_77637_a(TCG.tabTCG);
      this.func_77655_b(getName());
      this.func_77625_d(1);
      this.setRegistryName("tcg", getName());
   }

   @SideOnly(Side.CLIENT)
   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (world.field_72995_K) {
         Minecraft.func_71410_x().func_147108_a(new GuiCompendium());
      }

      return new ActionResult(EnumActionResult.PASS, player.func_184586_b(hand));
   }

   public static String getName() {
      return "compendium";
   }
}
