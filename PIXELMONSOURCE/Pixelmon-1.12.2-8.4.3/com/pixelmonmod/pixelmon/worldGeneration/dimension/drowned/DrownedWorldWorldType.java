package com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DrownedWorldWorldType extends WorldType {
   public DrownedWorldWorldType() {
      super("drowned_world");
   }

   public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
      return new DrownedWorldChunkGenerator(world, world.func_72905_C());
   }

   public int getMinimumSpawnHeight(World world) {
      return 128;
   }

   public double getHorizon(World world) {
      return 0.0;
   }

   public double voidFadeMagnitude() {
      return 0.5;
   }

   public boolean handleSlimeSpawnReduction(Random random, World world) {
      return false;
   }

   public void onGUICreateWorldPress() {
   }

   public int getSpawnFuzz(WorldServer world, MinecraftServer server) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {
   }

   public boolean isCustomizable() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_151357_h() {
      return false;
   }

   public float getCloudHeight() {
      return 128.0F;
   }

   public BiomeProvider getBiomeProvider(World world) {
      return new DrownedWorldBiomeProvider(DrownedWorldBiomeRegistry.infiniteOcean);
   }
}
