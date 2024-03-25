package com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DrownedWorldWorldProvider extends WorldProvider {
   private static final DrownedWorldCloudRenderer CLOUD_RENDERER = new DrownedWorldCloudRenderer();
   public static Random rand;

   public void func_76572_b() {
      super.func_76572_b();
      this.field_76579_a.func_72912_H().func_76085_a(DrownedWorld.WORLD_TYPE);
      this.field_76579_a.func_175723_af().func_177725_a((PixelmonConfig.drownedWorldRadius * 2 + 100) * 16);
      this.field_76578_c = new DrownedWorldBiomeProvider(DrownedWorldBiomeRegistry.infiniteOcean);
      this.field_191067_f = true;
      this.field_76577_b = DrownedWorld.WORLD_TYPE;
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
      return 0.2F;
   }

   @SideOnly(Side.CLIENT)
   public Vec3d func_76562_b(float p_76562_1_, float p_76562_2_) {
      return new Vec3d(0.0, 0.0, 0.0);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_76561_g() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
      return new Vec3d(0.0, 0.0, 0.0);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_76568_b(int x, int z) {
      return false;
   }

   @Nullable
   @SideOnly(Side.CLIENT)
   public IRenderHandler getCloudRenderer() {
      return CLOUD_RENDERER;
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
      return DrownedWorld.DIM_TYPE;
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
      return this.field_76579_a.func_72912_H().func_76063_b();
   }

   public String getSaveFolder() {
      return "drowned";
   }

   public IChunkGenerator func_186060_c() {
      return new DrownedWorldChunkGenerator(this.field_76579_a, this.getSeed());
   }

   public boolean shouldMapSpin(String entity, double x, double z, double rotation) {
      return true;
   }
}
