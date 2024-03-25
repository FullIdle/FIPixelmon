package com.pixelmonmod.pixelmon.blocks;

import net.minecraft.util.math.AxisAlignedBB;

public class BoundingBoxSet {
   public AxisAlignedBB AABBBaseEast;
   public AxisAlignedBB AABBBaseWest;
   public AxisAlignedBB AABBBaseSouth;
   public AxisAlignedBB AABBBaseNorth;

   public BoundingBoxSet(int width, double height, int length) {
      this.AABBBaseSouth = new AxisAlignedBB((double)(-width + 1), 0.0, 0.0, 1.0, height, (double)length);
      this.AABBBaseNorth = new AxisAlignedBB(0.0, 0.0, 0.0, (double)width, height, (double)length);
      this.AABBBaseEast = new AxisAlignedBB(0.0, 0.0, 0.0, (double)length, height, (double)width);
      this.AABBBaseWest = new AxisAlignedBB(0.0, 0.0, (double)((float)(-1 * width) + 1.0F), (double)length, height, 1.0);
   }
}
