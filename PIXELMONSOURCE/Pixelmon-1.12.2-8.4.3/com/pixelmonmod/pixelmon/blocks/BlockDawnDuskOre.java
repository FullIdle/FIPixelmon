package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.blocks.enums.EnumDawnDuskOre;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDawnDuskOre extends Block {
   public static final PropertyEnum CONTENTS = PropertyEnum.func_177709_a("contents", EnumDawnDuskOre.class);

   public BlockDawnDuskOre(float hardness) {
      super(Material.field_151576_e);
      this.func_149711_c(hardness);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149675_a(true);
      this.func_149663_c("dawnduskore");
      this.func_149647_a(PixelmonCreativeTabs.natural);
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{CONTENTS});
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(CONTENTS, EnumDawnDuskOre.values()[meta]);
   }

   public int func_176201_c(IBlockState state) {
      EnumDawnDuskOre type = (EnumDawnDuskOre)state.func_177229_b(CONTENTS);
      return type.ordinal();
   }

   public IBlockState func_180642_a(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      world.func_180497_b(pos, this, 20, 1);
      return super.func_180642_a(world, pos, facing, hitX, hitY, hitZ, meta, placer);
   }

   public void func_180650_b(World world, BlockPos pos, IBlockState state, Random rand) {
      if (!world.field_72995_K) {
         EnumDawnDuskOre contents = EnumDawnDuskOre.none;
         if (WorldTime.getCurrent(world).contains(WorldTime.DAWN)) {
            contents = EnumDawnDuskOre.dawn;
         } else if (WorldTime.getCurrent(world).contains(WorldTime.DUSK)) {
            contents = EnumDawnDuskOre.dusk;
         }

         if (state.func_177228_b().get(CONTENTS) != contents) {
            world.func_175656_a(pos, state.func_177226_a(CONTENTS, contents));
            world.func_180497_b(pos, this, 20, 1);
         }

         super.func_180650_b(world, pos, state, rand);
      }
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.MODEL;
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      EnumDawnDuskOre contents = (EnumDawnDuskOre)state.func_177229_b(CONTENTS);
      ArrayList drops = new ArrayList();
      switch (contents) {
         case dawn:
            drops.add(new ItemStack(PixelmonItems.dawnStoneShard, RandomHelper.getFortuneAmount(fortune)));
            break;
         case dusk:
            drops.add(new ItemStack(PixelmonItems.duskStoneShard, RandomHelper.getFortuneAmount(fortune)));
            break;
         case none:
            drops.add(new ItemStack(Blocks.field_150347_e));
      }

      return drops;
   }
}
