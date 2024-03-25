package com.pixelmonmod.pixelmon.worldGeneration.dimension.debug;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.init.Biomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DebugWorldType extends WorldType {
   public DebugWorldType() {
      super(DebugWorldRegistry.DIM_NAME);
   }

   public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
      return new DebugChunkGenerator(world);
   }

   public int getMinimumSpawnHeight(World world) {
      return world.func_181545_F() + 1;
   }

   public double getHorizon(World world) {
      return 61.0;
   }

   public double voidFadeMagnitude() {
      return 0.03125;
   }

   public boolean handleSlimeSpawnReduction(Random random, World world) {
      return false;
   }

   public void onGUICreateWorldPress() {
   }

   public int getSpawnFuzz(WorldServer world, MinecraftServer server) {
      return Math.max(0, 1);
   }

   @SideOnly(Side.CLIENT)
   public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {
   }

   public boolean isCustomizable() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_151357_h() {
      return true;
   }

   public float getCloudHeight() {
      return 128.0F;
   }

   public BiomeProvider getBiomeProvider(World world) {
      return new BiomeProviderSingle(Biomes.field_76772_c);
   }
}
