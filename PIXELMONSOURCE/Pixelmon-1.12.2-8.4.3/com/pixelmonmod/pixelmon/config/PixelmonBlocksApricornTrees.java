package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.blocks.apricornTrees.BlockApricornTree;
import com.pixelmonmod.pixelmon.enums.EnumApricornTrees;
import net.minecraft.block.Block;

public class PixelmonBlocksApricornTrees {
   public static Block apricornTreeBlack;
   public static Block apricornTreeWhite;
   public static Block apricornTreePink;
   public static Block apricornTreeGreen;
   public static Block apricornTreeBlue;
   public static Block apricornTreeYellow;
   public static Block apricornTreeRed;

   public static void load() {
      apricornTreeBlack = new BlockApricornTree(EnumApricornTrees.Black);
      apricornTreeWhite = new BlockApricornTree(EnumApricornTrees.White);
      apricornTreePink = new BlockApricornTree(EnumApricornTrees.Pink);
      apricornTreeGreen = new BlockApricornTree(EnumApricornTrees.Green);
      apricornTreeBlue = new BlockApricornTree(EnumApricornTrees.Blue);
      apricornTreeYellow = new BlockApricornTree(EnumApricornTrees.Yellow);
      apricornTreeRed = new BlockApricornTree(EnumApricornTrees.Red);
   }
}
