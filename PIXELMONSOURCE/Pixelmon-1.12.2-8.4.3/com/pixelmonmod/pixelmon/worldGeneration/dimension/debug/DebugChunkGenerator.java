package com.pixelmonmod.pixelmon.worldGeneration.dimension.debug;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumStatueTextureType;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

public class DebugChunkGenerator implements IChunkGenerator {
   private World world;
   int GRID_WIDTH;
   int GRID_HEIGHT;
   private static final IBlockState BARRIER;
   private static List uniqueStatues;

   public DebugChunkGenerator(World world) {
      this.GRID_WIDTH = MathHelper.func_76123_f(MathHelper.func_76129_c((float)uniqueStatues.size()));
      this.GRID_HEIGHT = MathHelper.func_76123_f((float)uniqueStatues.size() / (float)this.GRID_WIDTH);
      this.world = world;
   }

   public Chunk func_185932_a(int x, int z) {
      ChunkPrimer chunkprimer = new ChunkPrimer();

      int b;
      for(int i = 0; i < 16; ++i) {
         for(int j = 0; j < 16; ++j) {
            b = x * 16 + i;
            int l = z * 16 + j;
            chunkprimer.func_177855_a(i, 60, j, BARRIER);
         }
      }

      Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
      chunk.func_76603_b();
      byte[] biomeArray = chunk.func_76605_m();

      for(b = 0; b < biomeArray.length; ++b) {
         biomeArray[b] = (byte)Biome.func_185362_a(Biomes.field_76772_c);
      }

      chunk.func_76603_b();
      return chunk;
   }

   public void func_185931_b(int x, int z) {
      for(int i = 0; i < 16; ++i) {
         for(int j = 0; j < 16; ++j) {
            int k = x * 16 + i;
            int l = z * 16 + j;
            EntityStatue statue = this.getStatueAt(k, l);
            if (statue != null) {
               statue.func_70107_b((double)k, 62.0, (double)l);
               this.world.func_72838_d(statue);
            }
         }
      }

   }

   public EntityStatue getStatueAt(int x, int z) {
      if (x > 0 && z > 0 && x % 6 == 1 && z % 6 == 1) {
         x /= 6;
         z /= 6;
         if (x <= this.GRID_WIDTH && z <= this.GRID_HEIGHT) {
            int i = MathHelper.func_76130_a(x * this.GRID_WIDTH + z);
            if (i < uniqueStatues.size()) {
               return ((UniqueStatue)uniqueStatues.get(i)).create(this.world);
            }
         }
      }

      return null;
   }

   public boolean func_185933_a(Chunk chunkIn, int x, int z) {
      return false;
   }

   public List func_177458_a(EnumCreatureType creatureType, BlockPos pos) {
      return null;
   }

   @Nullable
   public BlockPos func_180513_a(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
      return null;
   }

   public void func_180514_a(Chunk chunkIn, int x, int z) {
   }

   public boolean func_193414_a(World worldIn, String structureName, BlockPos pos) {
      return false;
   }

   static {
      BARRIER = Blocks.field_180401_cv.func_176223_P();
      uniqueStatues = new ArrayList();
      EnumSpecies[] var0 = EnumSpecies.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumSpecies species = var0[var2];
         Iterator var4 = species.getPossibleForms(false).iterator();

         while(var4.hasNext()) {
            IEnumForm form = (IEnumForm)var4.next();
            Gender[] genders;
            if (species.getBaseStats().getMalePercent() < 0.0) {
               genders = new Gender[]{Gender.None};
            } else if (species.getBaseStats().getMalePercent() == 0.0) {
               genders = new Gender[]{Gender.Female};
            } else if (EnumSpecies.mfTextured.contains(species)) {
               genders = new Gender[]{Gender.Male, Gender.Female};
            } else {
               genders = new Gender[]{Gender.Male};
            }

            Gender[] var7 = genders;
            int var8 = genders.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               Gender gender = var7[var9];
               Boolean[] var11 = new Boolean[]{false, true};
               int var12 = var11.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  Boolean bool = var11[var13];
                  uniqueStatues.add(new UniqueStatue(new PokemonBase(species, form.getForm(), gender), bool));
               }
            }
         }
      }

   }

   public static class UniqueStatue {
      PokemonBase base;
      boolean shiny;

      public UniqueStatue(PokemonBase base, boolean shiny) {
         this.base = base;
         this.shiny = shiny;
      }

      public EntityStatue create(World world) {
         EntityStatue statue = new EntityStatue(world);
         statue.setPokemon(this.base);
         if (this.shiny) {
            statue.setTextureType(EnumStatueTextureType.Shiny);
         }

         return statue;
      }
   }
}
