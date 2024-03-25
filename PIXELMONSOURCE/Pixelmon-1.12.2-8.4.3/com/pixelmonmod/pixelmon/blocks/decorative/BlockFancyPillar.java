package com.pixelmonmod.pixelmon.blocks.decorative;

import com.pixelmonmod.pixelmon.blocks.enums.EnumAxis;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFancyPillar extends BlockContainerPlus {
   public static final EnumFacing[] UP_VALS;
   public static final EnumFacing[] DOWN_VALS;
   public static final PropertyBool DAMAGED;

   public BlockFancyPillar(Material mat) {
      super(mat);
      this.func_149663_c("temple_pillar");
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(AXIS, EnumAxis.values()[meta & 7]).func_177226_a(DAMAGED, (meta & 8) > 0);
   }

   public int func_176201_c(IBlockState state) {
      byte meta = 0;
      int i = meta | ((EnumAxis)state.func_177229_b(AXIS)).ordinal();
      if ((Boolean)state.func_177229_b(DAMAGED)) {
         i |= 8;
      }

      return i;
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{AXIS, DAMAGED});
   }

   public boolean getIsDamaged(IBlockState state) {
      return (Boolean)state.func_177229_b(DAMAGED);
   }

   public void func_149666_a(CreativeTabs tabs, NonNullList list) {
      list.add(new ItemStack(this, 1, 0));
      list.add(new ItemStack(this, 1, 1));
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return super.func_180642_a(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).func_177226_a(DAMAGED, meta == 1);
   }

   public Connections getConnections(IBlockAccess world, BlockPos pos, IBlockState state) {
      EnumAxis axis = (EnumAxis)state.func_177229_b(AXIS);
      Connections result = new Connections();
      int index = axis.ordinal() - 1;
      if (index < 0) {
         index = 0;
      }

      result.top = this.isAnotherWithSameOrientationOnSide(world, pos, axis, DOWN_VALS[index]);
      result.bottom = this.isAnotherWithSameOrientationOnSide(world, pos, axis, UP_VALS[index]);
      return result;
   }

   public int func_180651_a(IBlockState state) {
      return (Boolean)state.func_177229_b(DAMAGED) ? 1 : 0;
   }

   public Boolean isEnd(World world, BlockPos pos, EnumFacing dir) {
      return world.func_180495_p(pos.func_177972_a(dir)).func_177230_c() == this ? null : world.func_180495_p(pos.func_177972_a(dir)).func_185904_a() == Material.field_151579_a;
   }

   static {
      UP_VALS = new EnumFacing[]{EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.UP};
      DOWN_VALS = new EnumFacing[]{EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.DOWN};
      DAMAGED = PropertyBool.func_177716_a("damaged");
   }

   public class Connections {
      public boolean top = false;
      public boolean bottom = false;
   }
}
