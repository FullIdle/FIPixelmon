package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.util.Bounds;

public interface IPokemonOwner {
   Bounds getBounds();

   void updateStatus();

   int getEntityCount();
}
