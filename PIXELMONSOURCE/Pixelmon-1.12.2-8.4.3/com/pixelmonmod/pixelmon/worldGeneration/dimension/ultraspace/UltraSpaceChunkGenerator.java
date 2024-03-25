package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.worldGeneration.GenericOreGenerator;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenBauxiteOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenDawnDuskOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenEvolutionRock;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenFireStoneOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenFossils;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenGracideaFlowers;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenHiddenGrotto;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenLeafStoneOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenPixelmonTrees;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenPokeChest;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenThunderStoneOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenWaterStoneOre;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.MapGenCavesUltra;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.MapGenEndCityUltraSpace;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.UltraSpaceStrangeifier;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.WorldGenGlowstone;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.WorldGenMiniIsland;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.WorldGenPyramidInverse;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.WorldGenSpheroid;
import com.pixelmonmod.pixelmon.worldGeneration.structure.worldGen.WorldGenScatteredFeature;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.minecraft.world.gen.ChunkGeneratorSettings.Factory;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class UltraSpaceChunkGenerator implements IChunkGenerator {
   protected static final IBlockState STONE;
   protected static final IBlockState AIR;
   private final Random rand;
   private NoiseGeneratorOctaves minLimitPerlinNoise;
   private NoiseGeneratorOctaves maxLimitPerlinNoise;
   private NoiseGeneratorOctaves mainPerlinNoise;
   private NoiseGeneratorPerlin surfaceNoise;
   public NoiseGeneratorOctaves scaleNoise;
   public NoiseGeneratorOctaves depthNoise;
   public NoiseGeneratorOctaves forestNoise;
   private final World world;
   private final boolean mapFeaturesEnabled;
   private final WorldType terrainType;
   private final double[] heightMap;
   private final float[] biomeWeights;
   private IBlockState oceanBlock;
   private NoiseGeneratorSimplex islandNoise;
   private double[] depthBuffer;
   private MapGenBase caveGenerator;
   private MapGenVillage villageGenerator;
   private MapGenScatteredFeature scatteredFeatureGenerator;
   private MapGenBase ravineGenerator;
   private MapGenNetherBridge genNetherBridge;
   private MapGenEndCityUltraSpace genEndCity;
   private MapGenCavesUltra caveGenerator2;
   private final WorldGenMiniIsland endIslands;
   private final WorldGenSpheroid spheroid;
   private final WorldGenGlowstone glowStone;
   private final WorldGenPyramidInverse inversePyramid;
   private final WorldGenBauxiteOre bauxite;
   private final WorldGenPixelmonTrees pixelmontrees;
   private final WorldGenDawnDuskOre dawnDusk;
   private final WorldGenEvolutionRock evoRock;
   private final WorldGenFireStoneOre fireStone;
   private final WorldGenFossils fossils;
   private final WorldGenGracideaFlowers gracidea;
   private final WorldGenHiddenGrotto grottos;
   private final WorldGenLeafStoneOre leafStone;
   private final WorldGenPokeChest pokeChest;
   private final WorldGenThunderStoneOre thunderStone;
   private final WorldGenWaterStoneOre waterStoneOre;
   private final GenericOreGenerator amethyst;
   private final GenericOreGenerator silicon;
   private final GenericOreGenerator sapphire;
   private final GenericOreGenerator ruby;
   private final GenericOreGenerator crystal;
   private final WorldGenScatteredFeature scatteredFeature;
   private ChunkGeneratorSettings settings;
   private Biome[] biomesForGeneration;
   double[] mainNoiseRegion;
   double[] minLimitRegion;
   double[] maxLimitRegion;
   double[] depthRegion;
   private int chunkX;
   private int chunkZ;
   private UltraSpaceStrangeifier strangeifier;

   public UltraSpaceChunkGenerator(World worldIn, long seed, boolean mapFeaturesEnabledIn, String generatorOptions) {
      this.oceanBlock = Blocks.field_150350_a.func_176223_P();
      this.caveGenerator = new MapGenCaves();
      this.villageGenerator = new MapGenVillage();
      this.scatteredFeatureGenerator = new MapGenScatteredFeature();
      this.ravineGenerator = new MapGenRavine();
      this.genNetherBridge = new MapGenNetherBridge();
      this.genEndCity = new MapGenEndCityUltraSpace(this);
      this.caveGenerator2 = new MapGenCavesUltra();
      this.endIslands = new WorldGenMiniIsland();
      this.spheroid = new WorldGenSpheroid();
      this.glowStone = new WorldGenGlowstone();
      this.inversePyramid = new WorldGenPyramidInverse(Blocks.field_150322_A.func_176223_P());
      this.bauxite = new WorldGenBauxiteOre();
      this.pixelmontrees = new WorldGenPixelmonTrees();
      this.dawnDusk = new WorldGenDawnDuskOre();
      this.evoRock = new WorldGenEvolutionRock();
      this.fireStone = new WorldGenFireStoneOre();
      this.fossils = new WorldGenFossils();
      this.gracidea = new WorldGenGracideaFlowers();
      this.grottos = new WorldGenHiddenGrotto();
      this.leafStone = new WorldGenLeafStoneOre();
      this.pokeChest = new WorldGenPokeChest();
      this.thunderStone = new WorldGenThunderStoneOre();
      this.waterStoneOre = new WorldGenWaterStoneOre();
      this.amethyst = new GenericOreGenerator(PixelmonBlocks.amethystOre);
      this.silicon = new GenericOreGenerator(PixelmonBlocks.siliconOre);
      this.sapphire = new GenericOreGenerator(PixelmonBlocks.sapphireOre);
      this.ruby = new GenericOreGenerator(PixelmonBlocks.rubyOre);
      this.crystal = new GenericOreGenerator(PixelmonBlocks.crystalOre);
      this.scatteredFeature = new WorldGenScatteredFeature();
      this.chunkX = 0;
      this.chunkZ = 0;
      this.caveGenerator = TerrainGen.getModdedMapGen(this.caveGenerator, EventType.CAVE);
      this.villageGenerator = (MapGenVillage)TerrainGen.getModdedMapGen(this.villageGenerator, EventType.VILLAGE);
      this.scatteredFeatureGenerator = (MapGenScatteredFeature)TerrainGen.getModdedMapGen(this.scatteredFeatureGenerator, EventType.SCATTERED_FEATURE);
      this.ravineGenerator = TerrainGen.getModdedMapGen(this.ravineGenerator, EventType.RAVINE);
      this.genNetherBridge = (MapGenNetherBridge)TerrainGen.getModdedMapGen(this.genNetherBridge, EventType.NETHER_BRIDGE);
      this.caveGenerator2 = new MapGenCavesUltra();
      this.world = worldIn;
      this.mapFeaturesEnabled = mapFeaturesEnabledIn;
      this.terrainType = UltraSpace.WORLD_TYPE;
      this.rand = new Random(seed);
      this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
      this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
      this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
      this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
      this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
      this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
      this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
      this.islandNoise = new NoiseGeneratorSimplex(this.rand);
      this.heightMap = new double[825];
      this.biomeWeights = new float[25];

      for(int i = -2; i <= 2; ++i) {
         for(int j = -2; j <= 2; ++j) {
            float f = 10.0F / MathHelper.func_76129_c((float)(i * i + j * j) + 0.2F);
            this.biomeWeights[i + 2 + (j + 2) * 5] = f;
         }
      }

      if (generatorOptions != null) {
         this.settings = Factory.func_177865_a(generatorOptions).func_177864_b();
         this.oceanBlock = this.settings.field_177778_E ? Blocks.field_150353_l.func_176223_P() : Blocks.field_150355_j.func_176223_P();
         worldIn.func_181544_b(this.settings.field_177841_q);
      }

      InitNoiseGensEvent.ContextOverworld ctxOver = new InitNoiseGensEvent.ContextOverworld(this.minLimitPerlinNoise, this.maxLimitPerlinNoise, this.mainPerlinNoise, this.surfaceNoise, this.scaleNoise, this.depthNoise, this.forestNoise);
      ctxOver = (InitNoiseGensEvent.ContextOverworld)TerrainGen.getModdedNoiseGenerators(worldIn, this.rand, ctxOver);
      InitNoiseGensEvent.ContextEnd ctxEnd = new InitNoiseGensEvent.ContextEnd(this.minLimitPerlinNoise, this.maxLimitPerlinNoise, this.mainPerlinNoise, this.scaleNoise, this.depthNoise, this.islandNoise);
      ctxEnd = (InitNoiseGensEvent.ContextEnd)TerrainGen.getModdedNoiseGenerators(worldIn, this.rand, ctxEnd);
      this.minLimitPerlinNoise = ctxOver.getLPerlin1();
      this.maxLimitPerlinNoise = ctxOver.getLPerlin2();
      this.mainPerlinNoise = ctxOver.getPerlin();
      this.surfaceNoise = ctxOver.getHeight();
      this.scaleNoise = ctxOver.getScale();
      this.depthNoise = ctxOver.getDepth();
      this.forestNoise = ctxOver.getForest();
      this.islandNoise = ctxEnd.getIsland();
      this.strangeifier = new UltraSpaceStrangeifier(worldIn.func_72905_C());
   }

   public void setChunkToBedrock(int x, int z, ChunkPrimer primer) {
      UltraSpaceBiomeProvider bp = (UltraSpaceBiomeProvider)this.world.func_72959_q();
      this.biomesForGeneration = bp.func_76937_a(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
      this.generateHeightmap(x * 4, 0, z * 4);

      for(int a = 0; a < 16; ++a) {
         for(int b = 0; b < 64; ++b) {
            for(int c = 0; c < 16; ++c) {
               primer.func_177855_a(a, b, c, Blocks.field_150357_h.func_176223_P());
            }
         }
      }

   }

   public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
      UltraSpaceBiomeProvider bp = (UltraSpaceBiomeProvider)this.world.func_72959_q();
      this.biomesForGeneration = bp.func_76937_a(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
      this.generateHeightmap(x * 4, 0, z * 4);
      int i = true;
      int j = true;
      int k = true;
      int l = true;
      this.depthBuffer = this.getHeights(this.depthBuffer, x * 2, 0, z * 2, 3, 33, 3);

      for(int i1 = 0; i1 < 2; ++i1) {
         for(int j1 = 0; j1 < 2; ++j1) {
            for(int k1 = 0; k1 < 32; ++k1) {
               double d0 = 0.25;
               double d1 = this.depthBuffer[((i1 + 0) * 3 + j1 + 0) * 33 + k1 + 0];
               double d2 = this.depthBuffer[((i1 + 0) * 3 + j1 + 1) * 33 + k1 + 0];
               double d3 = this.depthBuffer[((i1 + 1) * 3 + j1 + 0) * 33 + k1 + 0];
               double d4 = this.depthBuffer[((i1 + 1) * 3 + j1 + 1) * 33 + k1 + 0];
               double d5 = (this.depthBuffer[((i1 + 0) * 3 + j1 + 0) * 33 + k1 + 1] - d1) * 0.25;
               double d6 = (this.depthBuffer[((i1 + 0) * 3 + j1 + 1) * 33 + k1 + 1] - d2) * 0.25;
               double d7 = (this.depthBuffer[((i1 + 1) * 3 + j1 + 0) * 33 + k1 + 1] - d3) * 0.25;
               double d8 = (this.depthBuffer[((i1 + 1) * 3 + j1 + 1) * 33 + k1 + 1] - d4) * 0.25;

               for(int l1 = 0; l1 < 4; ++l1) {
                  double d9 = 0.125;
                  double d10 = d1;
                  double d11 = d2;
                  double d12 = (d3 - d1) * 0.125;
                  double d13 = (d4 - d2) * 0.125;

                  for(int i2 = 0; i2 < 8; ++i2) {
                     double d14 = 0.125;
                     double d15 = d10;
                     double d16 = (d11 - d10) * 0.125;

                     for(int j2 = 0; j2 < 8; ++j2) {
                        IBlockState iblockstate = AIR;
                        if (d15 > 0.0) {
                           iblockstate = STONE;
                        }

                        int k2 = i2 + i1 * 8;
                        int l2 = l1 + k1 * 4;
                        int i3 = j2 + j1 * 8;
                        primer.func_177855_a(k2, l2 + 30, i3, iblockstate);
                        d15 += d16;
                     }

                     d10 += d12;
                     d11 += d13;
                  }

                  d1 += d5;
                  d2 += d6;
                  d3 += d7;
                  d4 += d8;
               }
            }
         }
      }

   }

   private double[] getHeights(double[] p_185963_1_, int p_185963_2_, int p_185963_3_, int p_185963_4_, int p_185963_5_, int p_185963_6_, int p_185963_7_) {
      ChunkGeneratorEvent.InitNoiseField event = new ChunkGeneratorEvent.InitNoiseField(this, p_185963_1_, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_);
      MinecraftForge.EVENT_BUS.post(event);
      if (event.getResult() == Result.DENY) {
         return event.getNoisefield();
      } else {
         if (p_185963_1_ == null) {
            p_185963_1_ = new double[p_185963_5_ * p_185963_6_ * p_185963_7_];
         }

         double d0 = 684.412;
         double d1 = 684.412;
         d0 *= 2.0;
         this.mainNoiseRegion = this.mainPerlinNoise.func_76304_a(this.mainNoiseRegion, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0 / 80.0, 4.277575000000001, d0 / 80.0);
         this.minLimitRegion = this.minLimitPerlinNoise.func_76304_a(this.minLimitRegion, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0, 684.412, d0);
         this.maxLimitRegion = this.maxLimitPerlinNoise.func_76304_a(this.maxLimitRegion, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0, 684.412, d0);
         int i = p_185963_2_ / 2;
         int j = p_185963_4_ / 2;
         int k = 0;

         for(int l = 0; l < p_185963_5_; ++l) {
            for(int i1 = 0; i1 < p_185963_7_; ++i1) {
               float f = this.getIslandHeightValue(i, j, l, i1);

               for(int j1 = 0; j1 < p_185963_6_; ++j1) {
                  double d2 = this.minLimitRegion[k] / 512.0;
                  double d3 = this.maxLimitRegion[k] / 512.0;
                  double d5 = (this.mainNoiseRegion[k] / 10.0 + 1.0) / 2.0;
                  double d4;
                  if (d5 < 0.0) {
                     d4 = d2;
                  } else if (d5 > 1.0) {
                     d4 = d3;
                  } else {
                     d4 = d2 + (d3 - d2) * d5;
                  }

                  d4 -= 8.0;
                  d4 += (double)f;
                  int k1 = 2;
                  double d7;
                  if (j1 > p_185963_6_ / 2 - k1) {
                     d7 = (double)((float)(j1 - (p_185963_6_ / 2 - k1)) / 64.0F);
                     d7 = MathHelper.func_151237_a(d7, 0.0, 1.0);
                     d4 = d4 * (1.0 - d7) + -3000.0 * d7;
                  }

                  k1 = 8;
                  if (j1 < k1) {
                     d7 = (double)((float)(k1 - j1) / ((float)k1 - 1.0F));
                     d4 = d4 * (1.0 - d7) + -30.0 * d7;
                  }

                  p_185963_1_[k] = d4;
                  ++k;
               }
            }
         }

         return p_185963_1_;
      }
   }

   private float getIslandHeightValue(int p_185960_1_, int p_185960_2_, int p_185960_3_, int p_185960_4_) {
      float f = (float)(p_185960_1_ * 2 + p_185960_3_);
      float f1 = (float)(p_185960_2_ * 2 + p_185960_4_);
      float f2 = 100.0F - MathHelper.func_76129_c(f * f + f1 * f1) * 8.0F;
      if (f2 > 80.0F) {
         f2 = 80.0F;
      }

      if (f2 < -100.0F) {
         f2 = -100.0F;
      }

      for(int i = -12; i <= 12; ++i) {
         for(int j = -12; j <= 12; ++j) {
            long k = (long)(p_185960_1_ + i);
            long l = (long)(p_185960_2_ + j);
            if (this.islandNoise.func_151605_a((double)k, (double)l) < -0.8899999761581421) {
               float f3 = (MathHelper.func_76135_e((float)k) * 3439.0F + MathHelper.func_76135_e((float)l) * 147.0F) % 13.0F + 9.0F;
               f = (float)(p_185960_3_ - i * 2);
               f1 = (float)(p_185960_4_ - j * 2);
               float f4 = 100.0F - MathHelper.func_76129_c(f * f + f1 * f1) * f3;
               if (f4 > 80.0F) {
                  f4 = 80.0F;
               }

               if (f4 < -100.0F) {
                  f4 = -100.0F;
               }

               if (f4 > f2) {
                  f2 = f4;
               }
            }
         }
      }

      return f2;
   }

   public boolean isIslandChunk(int p_185961_1_, int p_185961_2_) {
      return true;
   }

   public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn) {
      if (ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, this.world)) {
         double d0 = 0.03125;
         this.depthBuffer = this.surfaceNoise.func_151599_a(this.depthBuffer, (double)(x * 16), (double)(z * 16), 16, 16, 0.0625, 0.0625, 1.0);

         for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
               Biome biome = biomesIn[j + i * 16];
               biome.func_180622_a(this.world, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
            }
         }

      }
   }

   public void removeBedrockLayer(ChunkPrimer primer) {
      for(int a = 0; a < 16; ++a) {
         for(int b = 0; b < 6; ++b) {
            for(int c = 0; c < 16; ++c) {
               if (primer.func_177856_a(a, b, c).func_177230_c() == Blocks.field_150357_h) {
                  primer.func_177855_a(a, b, c, Blocks.field_150350_a.func_176223_P());
               }
            }
         }
      }

   }

   public Chunk func_185932_a(int x, int z) {
      ChunkPrimer chunkprimer = new ChunkPrimer();
      UltraSpaceBiomeProvider bp = (UltraSpaceBiomeProvider)this.world.func_72959_q();
      this.setBlocksInChunk(x, z, chunkprimer);
      this.biomesForGeneration = bp.func_76933_b(this.biomesForGeneration, x * 16, z * 16, 16, 16);
      this.replaceBiomeBlocks(x, z, chunkprimer, this.biomesForGeneration);
      this.removeBedrockLayer(chunkprimer);
      this.strangeifier.hellify(chunkprimer);
      this.strangeifier.endify(chunkprimer);
      this.strangeifier.gaussify(chunkprimer);
      this.caveGenerator.func_186125_a(this.world, x, z, chunkprimer);
      this.ravineGenerator.func_186125_a(this.world, x, z, chunkprimer);
      this.caveGenerator2.func_186125_a(this.world, x, z, chunkprimer);
      this.villageGenerator.func_186125_a(this.world, x, z, chunkprimer);
      this.scatteredFeatureGenerator.func_186125_a(this.world, x, z, chunkprimer);
      if (PixelmonConfig.spawnUltraSpaceNetherForts) {
         this.genNetherBridge.func_186125_a(this.world, x, z, chunkprimer);
      }

      if (PixelmonConfig.spawnUltraSpaceEndCities) {
         this.genEndCity.func_186125_a(this.world, x, z, chunkprimer);
      }

      Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
      byte[] abyte = chunk.func_76605_m();

      for(int i = 0; i < abyte.length; ++i) {
         byte biomeId = (byte)Biome.func_185362_a(this.biomesForGeneration[i]);
         abyte[i] = biomeId;
      }

      chunk.func_76603_b();
      return chunk;
   }

   private void generateHeightmap(int p_185978_1_, int p_185978_2_, int p_185978_3_) {
      this.depthRegion = this.depthNoise.func_76305_a(this.depthRegion, p_185978_1_, p_185978_3_, 5, 5, (double)this.settings.field_177808_e, (double)this.settings.field_177803_f, (double)this.settings.field_177804_g);
      float f = this.settings.field_177811_a;
      float f1 = this.settings.field_177809_b;
      this.mainNoiseRegion = this.mainPerlinNoise.func_76304_a(this.mainNoiseRegion, p_185978_1_, p_185978_2_, p_185978_3_, 5, 33, 5, (double)(f / this.settings.field_177825_h), (double)(f1 / this.settings.field_177827_i), (double)(f / this.settings.field_177821_j));
      this.minLimitRegion = this.minLimitPerlinNoise.func_76304_a(this.minLimitRegion, p_185978_1_, p_185978_2_, p_185978_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
      this.maxLimitRegion = this.maxLimitPerlinNoise.func_76304_a(this.maxLimitRegion, p_185978_1_, p_185978_2_, p_185978_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
      int i = 0;
      int j = 0;

      for(int k = 0; k < 5; ++k) {
         for(int l = 0; l < 5; ++l) {
            float f2 = 0.0F;
            float f3 = 0.0F;
            float f4 = 0.0F;
            int i1 = true;
            Biome biome = this.biomesForGeneration[k + 2 + (l + 2) * 10];

            for(int j1 = -2; j1 <= 2; ++j1) {
               for(int k1 = -2; k1 <= 2; ++k1) {
                  Biome biome1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
                  float f5 = this.settings.field_177813_n + biome1.func_185355_j() * this.settings.field_177819_m;
                  float f6 = this.settings.field_177843_p + biome1.func_185360_m() * this.settings.field_177815_o;
                  if (f5 > 0.0F) {
                     f5 = 1.0F + f5 * 2.0F;
                     f6 = 1.0F + f6 * 4.0F;
                  }

                  float f7 = this.biomeWeights[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);
                  if (biome1.func_185355_j() > biome.func_185355_j()) {
                     f7 /= 2.0F;
                  }

                  f2 += f6 * f7;
                  f3 += f5 * f7;
                  f4 += f7;
               }
            }

            f2 /= f4;
            f3 /= f4;
            f2 = f2 * 0.9F + 0.1F;
            f3 = (f3 * 4.0F - 1.0F) / 8.0F;
            double d7 = this.depthRegion[j] / 8000.0;
            if (d7 < 0.0) {
               d7 = -d7 * 0.3;
            }

            d7 = d7 * 3.0 - 2.0;
            if (d7 < 0.0) {
               d7 /= 2.0;
               if (d7 < -1.0) {
                  d7 = -1.0;
               }

               d7 /= 1.4;
               d7 /= 2.0;
            } else {
               if (d7 > 1.0) {
                  d7 = 1.0;
               }

               d7 /= 8.0;
            }

            ++j;
            double d8 = (double)f3;
            double d9 = (double)f2;
            d8 += d7 * 0.2;
            d8 = d8 * (double)this.settings.field_177823_k / 8.0;
            double d0 = (double)this.settings.field_177823_k + d8 * 4.0;

            for(int l1 = 0; l1 < 33; ++l1) {
               double d1 = ((double)l1 - d0) * (double)this.settings.field_177817_l * 128.0 / 256.0 / d9;
               if (d1 < 0.0) {
                  d1 *= 4.0;
               }

               double d2 = this.minLimitRegion[i] / (double)this.settings.field_177806_d;
               double d3 = this.maxLimitRegion[i] / (double)this.settings.field_177810_c;
               double d4 = (this.mainNoiseRegion[i] / 10.0 + 1.0) / 2.0;
               double d5 = MathHelper.func_151238_b(d2, d3, d4) - d1;
               if (l1 > 29) {
                  double d6 = (double)((float)(l1 - 29) / 3.0F);
                  d5 = d5 * (1.0 - d6) + -10.0 * d6;
               }

               this.heightMap[i] = d5;
               ++i;
            }
         }
      }

   }

   public void func_185931_b(int x, int z) {
      BlockFalling.field_149832_M = false;
      int i = x * 16;
      int j = z * 16;
      BlockPos blockpos = new BlockPos(i, 0, j);
      Biome biome = this.world.func_180494_b(blockpos.func_177982_a(16, 0, 16));
      this.rand.setSeed(this.world.func_72905_C());
      long k = this.rand.nextLong() / 2L * 2L + 1L;
      long l = this.rand.nextLong() / 2L * 2L + 1L;
      this.rand.setSeed((long)x * k + (long)z * l ^ this.world.func_72905_C());
      boolean flag = false;
      ChunkPos chunkpos = new ChunkPos(x, z);
      ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);
      flag = this.villageGenerator.func_175794_a(this.world, this.rand, chunkpos);
      this.scatteredFeatureGenerator.func_175794_a(this.world, this.rand, chunkpos);
      if (PixelmonConfig.spawnUltraSpaceNetherForts) {
         this.genNetherBridge.func_175794_a(this.world, this.rand, chunkpos);
      }

      if (PixelmonConfig.spawnUltraSpaceEndCities) {
         this.genEndCity.func_175794_a(this.world, this.rand, chunkpos);
      }

      if (PixelmonConfig.spawnUltraSpaceExtraPlants) {
         this.pixelmontrees.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.gracidea.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
      }

      if (PixelmonConfig.spawnUltraSpaceExtraLoots) {
         this.pokeChest.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
      }

      if (PixelmonConfig.spawnUltraSpaceExtraOres) {
         this.leafStone.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.thunderStone.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.waterStoneOre.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.amethyst.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.silicon.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.sapphire.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.ruby.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.crystal.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.bauxite.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.dawnDusk.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.evoRock.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.fireStone.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
         this.fossils.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
      }

      float f = this.getIslandHeightValue(x, z, 1, 1);
      if (f < -20.0F && this.rand.nextInt(9) == 0) {
         this.endIslands.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
         if (this.rand.nextInt(6) == 0) {
            this.endIslands.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
         }

         if (this.rand.nextInt(3) == 0) {
            this.endIslands.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
         }
      }

      if (biome == UltraSpaceBiomeRegistry.ultraDeepSea) {
         if (this.rand.nextInt(9) == 0) {
            this.spheroid.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 70 + this.rand.nextInt(16) + this.rand.nextInt(16) + this.rand.nextInt(32), this.rand.nextInt(16) + 8));
            if (this.rand.nextInt(6) == 0) {
               this.spheroid.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 70 + this.rand.nextInt(16) + this.rand.nextInt(16) + this.rand.nextInt(32), this.rand.nextInt(16) + 8));
            }

            if (this.rand.nextInt(3) == 0) {
               this.spheroid.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 70 + this.rand.nextInt(16) + this.rand.nextInt(16) + this.rand.nextInt(32), this.rand.nextInt(16) + 8));
            }
         }
      } else if (biome == UltraSpaceBiomeRegistry.ultraDesert && this.rand.nextInt(10) == 0) {
         this.inversePyramid.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 90 + this.rand.nextInt(60), this.rand.nextInt(16) + 8));
      }

      if (biome == UltraSpaceBiomeRegistry.ultraDeepSea) {
         if (this.rand.nextInt(9) == 0) {
            this.spheroid.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 70 + this.rand.nextInt(16) + this.rand.nextInt(16) + this.rand.nextInt(32), this.rand.nextInt(16) + 8));
         }

         if (this.rand.nextInt(6) == 0) {
            this.spheroid.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 70 + this.rand.nextInt(16) + this.rand.nextInt(16) + this.rand.nextInt(32), this.rand.nextInt(16) + 8));
         }

         if (this.rand.nextInt(3) == 0) {
            this.spheroid.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 70 + this.rand.nextInt(16) + this.rand.nextInt(16) + this.rand.nextInt(32), this.rand.nextInt(16) + 8));
         }
      }

      int k2;
      int j3;
      int l3;
      if (!flag && this.rand.nextInt(this.settings.field_177782_B) == 0 && TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE)) {
         k2 = this.rand.nextInt(16) + 8;
         j3 = this.rand.nextInt(256);
         l3 = this.rand.nextInt(16) + 8;
         (new WorldGenLakes(Blocks.field_150355_j)).func_180709_b(this.world, this.rand, blockpos.func_177982_a(k2, j3, l3));
      }

      if (!flag && this.rand.nextInt(this.settings.field_177777_D / 10) == 0 && TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA)) {
         k2 = this.rand.nextInt(16) + 8;
         j3 = this.rand.nextInt(this.rand.nextInt(248) + 8);
         l3 = this.rand.nextInt(16) + 8;
         if (j3 < this.world.func_181545_F() || this.rand.nextInt(this.settings.field_177777_D / 8) == 0) {
            (new WorldGenLakes(Blocks.field_150353_l)).func_180709_b(this.world, this.rand, blockpos.func_177982_a(k2, j3, l3));
         }
      }

      for(k2 = 0; k2 < this.settings.field_177835_t; ++k2) {
         j3 = this.rand.nextInt(16) + 8;
         l3 = this.rand.nextInt(256);
         int l1 = this.rand.nextInt(16) + 8;
         (new WorldGenDungeons()).func_180709_b(this.world, this.rand, blockpos.func_177982_a(j3, l3, l1));
      }

      for(k2 = 0; k2 < this.rand.nextInt(this.rand.nextInt(10) + 1); ++k2) {
         this.glowStone.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, this.rand.nextInt(50) + 30, this.rand.nextInt(16) + 8));
      }

      biome.func_180624_a(this.world, this.rand, new BlockPos(i, 0, j));
      if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS)) {
         WorldEntitySpawner.func_77191_a(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
      }

      blockpos = blockpos.func_177982_a(8, 0, 8);
      if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE)) {
         for(k2 = 0; k2 < 16; ++k2) {
            for(j3 = 0; j3 < 16; ++j3) {
               BlockPos blockpos1 = this.world.func_175725_q(blockpos.func_177982_a(k2, 0, j3));
               BlockPos blockpos2 = blockpos1.func_177977_b();
               if (this.world.func_175675_v(blockpos2)) {
                  this.world.func_180501_a(blockpos2, Blocks.field_150432_aD.func_176223_P(), 2);
               }

               if (this.world.func_175708_f(blockpos1, true)) {
                  this.world.func_180501_a(blockpos1, Blocks.field_150431_aC.func_176223_P(), 2);
               }
            }
         }
      }

      if (PixelmonConfig.spawnUltraSpaceExtraShrines) {
         this.scatteredFeature.generate(this.rand, x, z, this.world, this, this.world.func_72863_F());
      }

      BlockFalling.field_149832_M = false;
   }

   public boolean func_185933_a(Chunk chunkIn, int x, int z) {
      return false;
   }

   public List func_177458_a(EnumCreatureType creatureType, BlockPos pos) {
      Biome biome = this.world.func_180494_b(pos);
      return this.mapFeaturesEnabled && creatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.func_175798_a(pos) ? this.scatteredFeatureGenerator.func_82667_a() : biome.func_76747_a(creatureType);
   }

   public boolean func_193414_a(World worldIn, String structureName, BlockPos pos) {
      return false;
   }

   @Nullable
   public BlockPos func_180513_a(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
      return null;
   }

   public void func_180514_a(Chunk chunkIn, int x, int z) {
      if (this.mapFeaturesEnabled) {
         this.villageGenerator.func_186125_a(this.world, x, z, (ChunkPrimer)null);
         this.scatteredFeatureGenerator.func_186125_a(this.world, x, z, (ChunkPrimer)null);
         if (PixelmonConfig.spawnUltraSpaceNetherForts) {
            this.genNetherBridge.func_186125_a(this.world, x, z, (ChunkPrimer)null);
         }

         if (PixelmonConfig.spawnUltraSpaceEndCities) {
            this.genEndCity.func_186125_a(this.world, x, z, (ChunkPrimer)null);
         }
      }

   }

   static {
      STONE = Blocks.field_150348_b.func_176223_P();
      AIR = Blocks.field_150350_a.func_176223_P();
   }
}
