package com.pixelmonmod.pixelmon.client.render.blockReveal;

import java.awt.Color;
import net.minecraft.util.math.BlockPos;

public class BlockReveal {
   private int ticksRemaining;
   private BlockPos pos;
   private Color color;
   private int pattern;
   public boolean drawDown = true;
   public boolean drawUp = true;
   public boolean drawNorth = true;
   public boolean drawSouth = true;
   public boolean drawEast = true;
   public boolean drawWest = true;

   public BlockReveal(int ticksRemaining, BlockPos pos, int color, int pattern) {
      this.ticksRemaining = ticksRemaining;
      this.pos = pos;
      this.color = new Color(color);
      this.pattern = pattern;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Color getColor() {
      return this.color;
   }

   public int getTicksRemaining() {
      return this.ticksRemaining;
   }

   public void setTicksRemaining(int ticksRemaining) {
      this.ticksRemaining = ticksRemaining;
   }

   public void decreaseTicksRemaining(int ticks) {
      this.setTicksRemaining(this.ticksRemaining - ticks);
   }

   public int getPattern() {
      return this.pattern;
   }
}
