package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.GenLayerBiomeUltraSpace;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UltraSpaceWorldType extends WorldType {
   public UltraSpaceWorldType() {
      super("ultra_space");
   }

   public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
      return new UltraSpaceChunkGenerator(world, 1L, true, "[]");
   }

   public int getMinimumSpawnHeight(World world) {
      return world.func_181545_F() + 1;
   }

   public double getHorizon(World world) {
      return 63.0;
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
      return Math.max(0, server.func_184108_a(world));
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
      return new UltraSpaceBiomeProvider(world.func_72912_H());
   }

   public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer, ChunkGeneratorSettings chunkSettings) {
      GenLayer ret = new GenLayerBiomeUltraSpace(200L, parentLayer, this, chunkSettings);
      GenLayer ret = GenLayerZoom.func_75915_a(1000L, ret, 2);
      GenLayer ret = new GenLayerBiomeEdge(1000L, ret);
      return ret;
   }
}
