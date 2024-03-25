package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

public class TileEntityGracidea extends TileEntity implements ITickable {
   public static final float NEEDED_LIGHT = 14.0F;
   public static final int GROWTH_RANGE = 2;
   public static final float GROWTH_CHANCE = 1.0E-6F;
   private int ticks = 0;

   public void func_73660_a() {
      ++this.ticks;
      if (this.ticks % 20 == 0 && RandomHelper.getRandomChance(1.0E-6F) && this.canGrow()) {
         this.doGrow(1);
      }

   }

   public boolean canGrow() {
      return !((float)this.field_145850_b.func_175642_b(EnumSkyBlock.SKY, this.field_174879_c) < 14.0F);
   }

   public void doGrow(int numToGrow) {
      this.ticks = 0;
      ArrayList dirs = Lists.newArrayList(new BlockPos[]{this.field_174879_c.func_177968_d(), this.field_174879_c.func_177978_c(), this.field_174879_c.func_177974_f(), this.field_174879_c.func_177976_e()});

      int yOffset;
      for(int xOffset = -2; xOffset <= 2; ++xOffset) {
         for(yOffset = -2; yOffset <= 2; ++yOffset) {
            if (xOffset != yOffset || xOffset != 0) {
               dirs.add(this.field_174879_c.func_177964_d(yOffset).func_177965_g(yOffset));
            }
         }
      }

      dirs.removeIf((posx) -> {
         if (this.field_145850_b.func_180495_p(posx).func_177230_c() != Blocks.field_150350_a) {
            return true;
         } else if (this.field_145850_b.func_180495_p(posx.func_177977_b()).func_177230_c() != Blocks.field_150349_c) {
            return true;
         } else {
            return (float)this.field_145850_b.func_175642_b(EnumSkyBlock.SKY, posx) < 14.0F;
         }
      });
      ArrayList chosen = new ArrayList();

      for(yOffset = 0; yOffset < numToGrow; ++yOffset) {
         if (dirs.size() > 1) {
            chosen.add(dirs.remove(RandomHelper.getRandomNumberBetween(0, dirs.size() - 1)));
         } else if (dirs.size() == 1) {
            chosen.add(dirs.remove(0));
         }

         if (dirs.isEmpty()) {
            break;
         }
      }

      Iterator var8 = chosen.iterator();

      while(var8.hasNext()) {
         BlockPos pos = (BlockPos)var8.next();
         IBlockState state = PixelmonBlocks.gracideaBlock.func_176223_P();
         this.field_145850_b.func_175656_a(pos, state);
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
   }
}
