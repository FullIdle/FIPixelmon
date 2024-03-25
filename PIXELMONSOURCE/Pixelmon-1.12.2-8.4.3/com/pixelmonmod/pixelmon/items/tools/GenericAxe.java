package com.pixelmonmod.pixelmon.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GenericAxe extends ItemAxe {
   public GenericAxe(Item.ToolMaterial material, String itemName) {
      super(material, material.func_78000_c(), -3.0F);
      this.func_77655_b(itemName);
      this.setRegistryName(itemName);
   }

   public String func_77658_a() {
      return super.func_77658_a().substring(5);
   }

   public EnumActionResult func_180614_a(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      return ToolEffects.processEffect(playerIn.func_184586_b(hand), playerIn, worldIn, pos.func_177984_a()) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
   }
}
