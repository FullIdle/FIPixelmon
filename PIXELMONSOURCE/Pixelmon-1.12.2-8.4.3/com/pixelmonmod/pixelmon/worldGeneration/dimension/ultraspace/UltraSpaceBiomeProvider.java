package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.EnumBiome;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.ChunkGeneratorSettings.Factory;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;

public class UltraSpaceBiomeProvider extends BiomeProvider {
   public ChunkGeneratorSettings field_190945_a;
   public GenLayer field_76944_d;
   public GenLayer field_76945_e;
   public final BiomeCache field_76942_f;
   public static List allowedBiomes = Lists.newArrayList(EnumBiome.getBiomes());
   public final List field_76943_g;

   public UltraSpaceBiomeProvider() {
      this.field_76942_f = new BiomeCache(this);
      this.field_76943_g = Lists.newArrayList(allowedBiomes);
   }

   public UltraSpaceBiomeProvider(long seed, WorldType worldTypeIn, String options) {
      this();
      if (worldTypeIn == WorldType.field_180271_f && !options.isEmpty()) {
         this.field_190945_a = Factory.func_177865_a(options).func_177864_b();
      }

      GenLayer[] agenlayer = UltraSpaceBiomeRegistry.initUltraSpaceBiomeGenerators(seed, UltraSpace.WORLD_TYPE, this.field_190945_a);
      this.field_76944_d = agenlayer[0];
      this.field_76945_e = agenlayer[1];
   }

   public UltraSpaceBiomeProvider(WorldInfo info) {
      this(info.func_76063_b(), UltraSpace.WORLD_TYPE, info.func_82571_y());
   }

   public Biome[] func_76933_b(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth) {
      return this.func_76931_a(oldBiomeList, x, z, width, depth, true);
   }

   public Biome[] func_76931_a(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
      IntCache.func_76446_a();
      if (listToReuse == null || listToReuse.length < width * length) {
         listToReuse = new Biome[width * length];
      }

      if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0) {
         Biome[] abiome = this.field_76942_f.func_76839_e(x, z);
         System.arraycopy(abiome, 0, listToReuse, 0, width * length);
         return listToReuse;
      } else {
         int[] aint = this.field_76945_e.func_75904_a(x, z, width, length);

         for(int i = 0; i < width * length; ++i) {
            listToReuse[i] = Biome.func_180276_a(aint[i], Biomes.field_180279_ad);
         }

         return listToReuse;
      }
   }

   public Biome[] func_76937_a(Biome[] biomes, int x, int z, int width, int height) {
      IntCache.func_76446_a();
      if (biomes == null || biomes.length < width * height) {
         biomes = new Biome[width * height];
      }

      int[] aint = this.field_76944_d.func_75904_a(x, z, width, height);

      try {
         for(int i = 0; i < width * height; ++i) {
            biomes[i] = Biome.func_180276_a(aint[i], Biomes.field_180279_ad);
         }

         return biomes;
      } catch (Throwable var10) {
         CrashReport crashreport = CrashReport.func_85055_a(var10, "Invalid Biome id");
         CrashReportCategory crashreportcategory = crashreport.func_85058_a("RawBiomeBlock");
         crashreportcategory.func_71507_a("biomes[] size", biomes.length);
         crashreportcategory.func_71507_a("x", x);
         crashreportcategory.func_71507_a("z", z);
         crashreportcategory.func_71507_a("w", width);
         crashreportcategory.func_71507_a("h", height);
         throw new ReportedException(crashreport);
      }
   }

   public List func_76932_a() {
      return allowedBiomes;
   }

   public boolean func_76940_a(int x, int z, int radius, List allowed) {
      IntCache.func_76446_a();
      int i = x - radius >> 2;
      int j = z - radius >> 2;
      int k = x + radius >> 2;
      int l = z + radius >> 2;
      int i1 = k - i + 1;
      int j1 = l - j + 1;
      int[] aint = this.field_76944_d.func_75904_a(i, j, i1, j1);

      try {
         for(int k1 = 0; k1 < i1 * j1; ++k1) {
            Biome biome = Biome.func_150568_d(aint[k1]);
            if (!allowed.contains(biome)) {
               return false;
            }
         }

         return true;
      } catch (Throwable var15) {
         CrashReport crashreport = CrashReport.func_85055_a(var15, "Invalid Biome id");
         CrashReportCategory crashreportcategory = crashreport.func_85058_a("Layer");
         crashreportcategory.func_71507_a("Layer", this.field_76944_d.toString());
         crashreportcategory.func_71507_a("x", x);
         crashreportcategory.func_71507_a("z", z);
         crashreportcategory.func_71507_a("diameter", radius);
         crashreportcategory.func_71507_a("allowed", allowed);
         throw new ReportedException(crashreport);
      }
   }

   @Nullable
   public BlockPos func_180630_a(int x, int z, int range, List biomes, Random random) {
      IntCache.func_76446_a();
      int i = x - range >> 2;
      int j = z - range >> 2;
      int k = x + range >> 2;
      int l = z + range >> 2;
      int i1 = k - i + 1;
      int j1 = l - j + 1;
      int[] aint = this.field_76944_d.func_75904_a(i, j, i1, j1);
      BlockPos blockpos = null;
      int k1 = 0;

      for(int l1 = 0; l1 < i1 * j1; ++l1) {
         int i2 = i + l1 % i1 << 2;
         int j2 = j + l1 / i1 << 2;
         Biome biome = Biome.func_150568_d(aint[l1]);
         if (biomes.contains(biome) && (blockpos == null || random.nextInt(k1 + 1) == 0)) {
            blockpos = new BlockPos(i2, 0, j2);
            ++k1;
         }
      }

      return blockpos;
   }

   public void func_76938_b() {
      this.field_76942_f.func_76838_a();
   }

   public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original) {
      WorldTypeEvent.InitBiomeGens event = new WorldTypeEvent.InitBiomeGens(worldType, seed, original);
      MinecraftForge.TERRAIN_GEN_BUS.post(event);
      return event.getNewBiomeGens();
   }

   public boolean func_190944_c() {
      return this.field_190945_a != null && this.field_190945_a.field_177779_F >= 0;
   }

   public Biome func_190943_d() {
      return this.field_190945_a != null && this.field_190945_a.field_177779_F >= 0 ? Biome.func_185357_a(this.field_190945_a.field_177779_F) : null;
   }
}
