package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UltraSpaceWorldProvider extends WorldProvider {
   public static Random rand;
   private double r = 1.0;
   private double g = 0.0;
   private double b = 0.0;
   private Mode mode;
   private double speed;

   public UltraSpaceWorldProvider() {
      this.mode = UltraSpaceWorldProvider.Mode.gIncr;
      this.speed = 1.0E-4;
   }

   public void func_76572_b() {
      super.func_76572_b();
      this.field_76579_a.func_72912_H().func_76085_a(UltraSpace.WORLD_TYPE);
      this.field_76579_a.func_175723_af().func_177725_a(29999984);
      this.field_76578_c = new UltraSpaceBiomeProvider(this.field_76579_a.func_72912_H());
      this.field_191067_f = true;
      this.field_76577_b = UltraSpace.WORLD_TYPE;
      rand = new Random(this.getSeed());
   }

   public float func_76563_a(long par1, float par3) {
      return 0.225F;
   }

   @SideOnly(Side.CLIENT)
   public float[] func_76560_a(float celestialAngle, float partialTicks) {
      return null;
   }

   public float getSunBrightnessFactor(float par1) {
      return 0.3F;
   }

   @SideOnly(Side.CLIENT)
   public Vec3d func_76562_b(float p_76562_1_, float p_76562_2_) {
      if (PixelmonConfig.ultraSpaceColourblindMode) {
         return super.func_76562_b(p_76562_1_, p_76562_2_);
      } else {
         switch (this.mode) {
            case rIncr:
               this.r += this.speed;
               if (this.r >= 1.0) {
                  this.mode = UltraSpaceWorldProvider.Mode.bDecr;
               }
               break;
            case rDecr:
               this.r -= this.speed;
               if (this.r <= 0.0) {
                  this.mode = UltraSpaceWorldProvider.Mode.bIncr;
               }
               break;
            case gIncr:
               this.g += this.speed;
               if (this.g >= 1.0) {
                  this.mode = UltraSpaceWorldProvider.Mode.rDecr;
               }
               break;
            case gDecr:
               this.g -= this.speed;
               if (this.g <= 0.0) {
                  this.mode = UltraSpaceWorldProvider.Mode.rIncr;
               }
               break;
            case bIncr:
               this.b += this.speed;
               if (this.b >= 1.0) {
                  this.mode = UltraSpaceWorldProvider.Mode.gDecr;
               }
               break;
            case bDecr:
               this.b -= this.speed;
               if (this.b <= 0.0) {
                  this.mode = UltraSpaceWorldProvider.Mode.gIncr;
               }
         }

         return new Vec3d(this.r - 0.5, this.g - 0.5, this.b - 0.5);
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean func_76561_g() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
      return this.field_76579_a.getSkyColorBody(cameraEntity, partialTicks);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_76568_b(int x, int z) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public float func_76571_f() {
      return 90.0F;
   }

   public boolean func_76567_e() {
      return false;
   }

   public boolean func_76569_d() {
      return false;
   }

   public boolean func_76566_a(int x, int z) {
      return false;
   }

   public int func_76557_i() {
      return 64;
   }

   public DimensionType func_186058_p() {
      return UltraSpace.DIM_TYPE;
   }

   public BiomeProvider func_177499_m() {
      return this.field_76578_c;
   }

   public int getHeight() {
      return 256;
   }

   public int getActualHeight() {
      return 256;
   }

   public long getSeed() {
      return -this.field_76579_a.func_72912_H().func_76063_b();
   }

   public String getSaveFolder() {
      return "ultra";
   }

   public IChunkGenerator func_186060_c() {
      return new UltraSpaceChunkGenerator(this.field_76579_a, this.getSeed(), true, "[]");
   }

   public boolean shouldMapSpin(String entity, double x, double z, double rotation) {
      return true;
   }

   @Nullable
   @SideOnly(Side.CLIENT)
   public MusicTicker.MusicType getMusicType() {
      return PixelSounds.ULTRA_SPACE_MUSIC_TYPE;
   }

   static enum Mode {
      rIncr,
      rDecr,
      gIncr,
      gDecr,
      bIncr,
      bDecr;
   }
}
