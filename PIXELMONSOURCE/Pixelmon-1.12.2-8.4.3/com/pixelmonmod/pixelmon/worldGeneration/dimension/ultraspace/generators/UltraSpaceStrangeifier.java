package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;

public class UltraSpaceStrangeifier {
   private Random rand;

   public UltraSpaceStrangeifier(long seed) {
      this.rand = new Random(seed);
   }

   public void hellify(ChunkPrimer primer) {
      if (this.rand.nextDouble() < 0.025) {
         for(int y = 60; y < 130; ++y) {
            if (primer.func_177856_a(8, y, 8).func_177230_c() == Blocks.field_150349_c || primer.func_177856_a(8, y, 8).func_177230_c() == Blocks.field_150354_m) {
               for(int x = 0; x < 16; ++x) {
                  for(int z = 0; z < 16; ++z) {
                     for(int dy = -25; dy < 25; ++dy) {
                        int xWeight = Math.abs(x - 8);
                        int zWeight = Math.abs(z - 8);
                        int weight = xWeight + zWeight;
                        if (this.rand.nextDouble() < 1.0 - (double)weight / 16.0 && primer.func_177856_a(x, y + dy, z).func_177230_c() != Blocks.field_150350_a) {
                           double pickBlock = this.rand.nextDouble();
                           if (pickBlock > 0.75) {
                              primer.func_177855_a(x, y + dy, z, Blocks.field_150353_l.func_176223_P());
                           } else if (pickBlock < 0.25) {
                              primer.func_177855_a(x, y + dy, z, Blocks.field_150425_aM.func_176223_P());
                           } else {
                              primer.func_177855_a(x, y + dy, z, Blocks.field_150424_aL.func_176223_P());
                           }
                        }
                     }
                  }
               }

               return;
            }
         }
      }

   }

   public void gaussify(ChunkPrimer primer) {
      if (this.rand.nextDouble() < 0.01) {
         for(int y = 60; y < 130; ++y) {
            if (primer.func_177856_a(8, y, 8).func_177230_c() == Blocks.field_150349_c) {
               for(int x = 0; x < 16; ++x) {
                  for(int z = 0; z < 16; ++z) {
                     for(int dy = 0; dy < 255; ++dy) {
                        int xWeight = Math.abs(x - 8);
                        int zWeight = Math.abs(z - 8);
                        int weight = xWeight + zWeight;
                        if (this.rand.nextDouble() < 1.0 - (double)weight / 16.0) {
                           if (primer.func_177856_a(x, dy, z).func_177230_c() != Blocks.field_150350_a) {
                              primer.func_177855_a(x, dy, z, Blocks.field_150350_a.func_176223_P());
                           } else if (this.rand.nextDouble() < (1.0 - (double)weight / 16.0) / 20.0) {
                              if (dy < 70) {
                                 primer.func_177855_a(x, dy, z, Blocks.field_150348_b.func_176223_P());
                              } else {
                                 primer.func_177855_a(x, dy, z, Blocks.field_150349_c.func_176223_P());
                              }
                           }
                        }
                     }
                  }
               }

               return;
            }
         }
      }

   }

   public void endify(ChunkPrimer primer) {
      if (this.rand.nextDouble() < 0.025) {
         for(int y = 60; y < 130; ++y) {
            if (primer.func_177856_a(8, y, 8).func_177230_c() == Blocks.field_150349_c || primer.func_177856_a(8, y, 8).func_177230_c() == Blocks.field_150354_m) {
               for(int x = 0; x < 16; ++x) {
                  for(int z = 0; z < 16; ++z) {
                     for(int dy = -25; dy < 25; ++dy) {
                        int xWeight = Math.abs(x - 8);
                        int zWeight = Math.abs(z - 8);
                        int weight = xWeight + zWeight;
                        if (this.rand.nextDouble() < 1.0 - (double)weight / 16.0 && primer.func_177856_a(x, y + dy, z).func_177230_c() != Blocks.field_150350_a) {
                           primer.func_177855_a(x, y + dy, z, Blocks.field_150377_bs.func_176223_P());
                        }
                     }
                  }
               }

               return;
            }
         }
      }

   }
}
