package com.pixelmonmod.pixelmon.client.camera.movement;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.camera.EntityCamera;
import com.pixelmonmod.pixelmon.client.camera.ICameraTarget;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

public class PositionedMovement extends CameraMovement {
   int ticksToChange = 0;
   int currentPos = 0;
   ArrayList cameraPositionList = new ArrayList();
   boolean initPokePos = false;
   boolean initDiagonals = false;

   public PositionedMovement(World worldObj, EntityCamera camera) {
      super(worldObj, camera);
   }

   public void onLivingUpdate() {
      if ((this.initDiagonals || this.initPokePos) && !this.camera.field_70128_L && ClientProxy.battleManager.getViewEntity() != this.camera) {
         ClientProxy.battleManager.setViewEntity(this.camera);
         ClientProxy.camera = this.camera;
      }

      if (!PixelmonConfig.playerControlCamera && this.cameraPositionList.size() > this.currentPos) {
         BlockPos cameraPosition = (BlockPos)this.cameraPositionList.get(this.currentPos);
         if (!this.camera.func_180425_c().equals(cameraPosition)) {
            this.camera.func_70107_b((double)cameraPosition.func_177958_n(), (double)cameraPosition.func_177956_o(), (double)cameraPosition.func_177952_p());
         }
      }

      if (!this.initPokePos && ClientProxy.battleManager.getUserPokemon(this.camera.mode) != null) {
         this.generateCameraPositionsForPokemon();
      }

      if (!this.initDiagonals && ClientProxy.battleManager.getUserPokemon(this.camera.mode) != null) {
         this.generateCameraDiagonalsForPlayer();
      }

      --this.ticksToChange;
      int positionListSize = this.cameraPositionList.size();
      if (this.ticksToChange <= 0 && positionListSize > 0) {
         Random random = this.world.field_73012_v;
         this.currentPos = random.nextInt(positionListSize);
         this.ticksToChange = random.nextInt(40) + 70;
      }

   }

   public void generatePositions() {
      this.cameraPositionList.clear();
      this.initPokePos = false;
      this.initDiagonals = false;
      this.currentPos = 0;
      this.ticksToChange = this.world.field_73012_v.nextInt(100) + 80;
      if (ClientProxy.battleManager.getUserPokemon(this.camera.mode) != null) {
         if (!this.initPokePos) {
            this.generateCameraPositionsForPokemon();
         }

         if (!this.initDiagonals) {
            this.generateCameraDiagonalsForPlayer();
         }
      }

   }

   private void generateCameraPositionsForPokemon() {
      this.initPokePos = true;
      EntityPixelmon pix = ClientProxy.battleManager.getUserPokemon(this.camera.mode);
      if (pix != null) {
         BlockPos pokemonPos = pix.func_180425_c();
         EntityPlayer player = ClientProxy.battleManager.getViewPlayer();
         BlockPos playerPos = player.func_180425_c();

         for(int i = 0; i < 8; ++i) {
            BlockPos basePos = this.getRandomCameraPositionForPokemon(pokemonPos);
            Material mat = this.world.func_180495_p(basePos).func_185904_a();
            if ((mat == Material.field_151579_a || mat == Material.field_151586_h) && this.canPosSee(basePos, playerPos)) {
               this.cameraPositionList.add(basePos);
            }
         }
      }

      ClientProxy.battleManager.setViewEntity(this.camera);
   }

   private BlockPos getRandomCameraPositionForPokemon(BlockPos pokemonPos) {
      Random random = this.world.field_73012_v;
      int x = pokemonPos.func_177958_n() + random.nextInt(10) - 5;
      int y = pokemonPos.func_177956_o() + random.nextInt(7) + 1;
      int z = pokemonPos.func_177952_p() + random.nextInt(10) - 5;
      return new BlockPos(x, y, z);
   }

   private void generateCameraDiagonalsForPlayer() {
      EntityPixelmon pix = ClientProxy.battleManager.getUserPokemon(this.camera.mode);
      if (pix != null) {
         BlockPos pokemonPos = pix.func_180425_c();
         Vector3f pokemonVec = new Vector3f((float)pokemonPos.func_177958_n(), (float)pokemonPos.func_177956_o(), (float)pokemonPos.func_177952_p());
         pokemonVec.y += pix.func_70047_e();
         if (!this.checkDiagonals(pokemonVec)) {
            return;
         }
      }

      this.initDiagonals = true;
   }

   private boolean checkDiagonals(Vector3f pokemonVec) {
      EntityPlayer player = ClientProxy.battleManager.getViewPlayer();
      BlockPos playerPos = player.func_180425_c();
      Vector3f playerVec = new Vector3f((float)playerPos.func_177958_n(), (float)playerPos.func_177956_o(), (float)playerPos.func_177952_p());
      playerVec.y += player.func_70047_e();
      Vector3f distanceVec = new Vector3f();
      Vector3f.sub(playerVec, pokemonVec, distanceVec);
      distanceVec.y = 0.0F;
      if (distanceVec.length() == 0.0F) {
         return false;
      } else {
         Vector3f firstDiag = new Vector3f(distanceVec);
         firstDiag.normalise();
         float angle = 0.3926991F;
         Vector3f perp = new Vector3f();
         Vector3f.cross(firstDiag, new Vector3f(0.0F, 1.0F, 0.0F), perp);
         Vector3f rotated = this.rotateRoundGround(firstDiag, perp, angle);
         perp.normalise();

         for(int i = 0; i < 6; ++i) {
            this.generateDiagonalPos(playerPos, playerVec, rotated, perp);
         }

         return true;
      }
   }

   private void generateDiagonalPos(BlockPos playerPos, Vector3f playerVec, Vector3f rotated, Vector3f perp) {
      Vector3f newPos = new Vector3f();
      Random random = this.world.field_73012_v;
      float scale = (float)(random.nextInt(2) + 3);
      Vector3f rotatedScaled = new Vector3f(rotated);
      rotatedScaled.x *= scale;
      rotatedScaled.y *= scale;
      rotatedScaled.z *= scale;
      scale = (float)(random.nextInt(9) - 4);
      Vector3f perpScaled = new Vector3f(perp);
      perpScaled.x *= scale;
      perpScaled.y *= scale;
      perpScaled.z *= scale;
      Vector3f.add(playerVec, rotatedScaled, newPos);
      Vector3f.add(newPos, perpScaled, newPos);
      BlockPos basePos = new BlockPos((double)newPos.x, (double)newPos.y, (double)newPos.z);
      Material mat = this.world.func_180495_p(basePos).func_185904_a();
      if ((mat == Material.field_151579_a || mat == Material.field_151586_h) && this.canPosSee(basePos, playerPos)) {
         this.cameraPositionList.add(basePos);
      }

   }

   private Vector3f rotateRoundGround(Vector3f vector, Vector3f rotateVector, float angle) {
      Matrix4f matrix = new Matrix4f();
      matrix.m03 = vector.x;
      matrix.m13 = vector.y;
      matrix.m23 = vector.z;
      org.lwjgl.util.vector.Matrix4f.rotate(angle, rotateVector, matrix, matrix);
      return matrix.m13 < 0.0F ? this.rotateRoundGround(vector, rotateVector, -1.0F * angle) : new Vector3f(matrix.m03, matrix.m13, matrix.m23);
   }

   public void setRandomPosition(ICameraTarget t) {
      BlockPos randomPos = this.getRandomCameraPositionForPokemon(new BlockPos(t.getX(), t.getY(), t.getZ()));
      this.camera.func_70107_b((double)randomPos.func_177958_n(), (double)randomPos.func_177956_o(), (double)randomPos.func_177952_p());
   }

   public void updatePositionAndRotation() {
      this.camera.field_70142_S = this.camera.field_70169_q = this.camera.field_70165_t;
      this.camera.field_70137_T = this.camera.field_70167_r = this.camera.field_70163_u + this.camera.func_70033_W();
      this.camera.field_70136_U = this.camera.field_70166_s = this.camera.field_70161_v;
   }
}
