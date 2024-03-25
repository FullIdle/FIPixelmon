package com.pixelmonmod.pixelmon.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public class BlockProperties {
   public static final PropertyDirection FACING;
   public static final PropertyDirection FACING_D;
   public static final PropertyDirection FACING_ALL;

   static {
      FACING = BlockHorizontal.field_185512_D;
      FACING_D = PropertyDirection.func_177712_a("facing", (ef) -> {
         return ef != EnumFacing.DOWN;
      });
      FACING_ALL = PropertyDirection.func_177714_a("facing");
   }
}
