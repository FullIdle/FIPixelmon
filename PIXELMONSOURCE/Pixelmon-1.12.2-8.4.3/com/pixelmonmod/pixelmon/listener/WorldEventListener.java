package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityOccupiedPokeball;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;

public class WorldEventListener implements IWorldEventListener {
   public void func_72709_b(Entity entityIn) {
      if (!(entityIn instanceof EntityPixelmon) && entityIn instanceof EntityOccupiedPokeball) {
         Entity thrower = ((EntityOccupiedPokeball)entityIn).func_85052_h();
         if (thrower == null) {
            return;
         }

         if (thrower instanceof EntityPlayerMP) {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)thrower);
            party.transientData.thrownPokeball = null;
         }
      }

      if (PixelmonSpawning.coordinator != null) {
         Iterator var4 = PixelmonSpawning.coordinator.spawners.iterator();

         while(var4.hasNext()) {
            AbstractSpawner spawner = (AbstractSpawner)var4.next();
            if (spawner != null && spawner.spawnedTracker.removeEntity(entityIn)) {
               break;
            }
         }
      }

   }

   public void func_184376_a(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
   }

   public void func_174959_b(BlockPos pos) {
   }

   public void func_147585_a(int x1, int y1, int z1, int x2, int y2, int z2) {
   }

   public void func_184375_a(EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) {
   }

   public void func_184377_a(SoundEvent soundIn, BlockPos pos) {
   }

   public void func_180442_a(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
   }

   public void func_72703_a(Entity entityIn) {
   }

   public void func_180440_a(int soundID, BlockPos pos, int data) {
   }

   public void func_180439_a(EntityPlayer player, int type, BlockPos blockPosIn, int data) {
   }

   public void func_180441_b(int breakerId, BlockPos pos, int progress) {
   }

   public void func_190570_a(int id, boolean ignoreRange, boolean p_190570_3_, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
   }
}
