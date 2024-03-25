package com.pixelmonmod.pixelmon.client.camera.movement;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.camera.CameraMode;
import com.pixelmonmod.pixelmon.client.camera.CameraTargetEntity;
import com.pixelmonmod.pixelmon.client.camera.CameraTargetLocation;
import com.pixelmonmod.pixelmon.client.camera.EntityCamera;
import com.pixelmonmod.pixelmon.client.camera.ICameraTarget;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix3f;

public class PlayerControlledMovement extends CameraMovement {
   private final int KEY_W = 17;
   private final int KEY_S = 31;
   private final int KEY_A = 30;
   private final int KEY_D = 32;
   private final int KEY_Q = 16;
   private final int KEY_E = 18;
   private boolean initSphere = false;
   private Vec3d centerPos;
   private Vec3d baseVec;
   private float radius = 0.0F;
   private float pitch = 0.0F;
   private float yaw = 0.0F;
   private boolean spherePosChanged = false;
   private ICameraTarget target;

   public PlayerControlledMovement(World world, EntityCamera entityCamera) {
      super(world, entityCamera);
   }

   public void setRandomPosition(ICameraTarget t) {
      this.target = t;
   }

   public void onLivingUpdate() {
      if (this.camera.mode == CameraMode.Evolution) {
         if (!this.initSphere && this.target != null) {
            this.generateSphere();
         }

         this.updatePosition();
      } else {
         EntityPixelmon pix = ClientProxy.battleManager.getUserPokemon(this.camera.mode);
         if (pix == null) {
            return;
         }

         EntityPlayer player = ClientProxy.battleManager.getViewPlayer();
         if (!this.initSphere && pix.field_70165_t != player.field_70165_t && pix.field_70161_v != player.field_70161_v) {
            this.generateSphere();
         }

         EntityPixelmon opp = this.getOpponentPokemon();
         this.centerPos = new Vec3d(pix.field_70165_t, pix.field_70163_u, pix.field_70161_v);
         if (opp != null) {
            this.centerPos = new Vec3d((this.centerPos.field_72450_a + opp.field_70165_t) / 2.0, (this.centerPos.field_72448_b + opp.field_70163_u) / 2.0, (this.centerPos.field_72449_c + opp.field_70161_v) / 2.0);
         }

         Vec3d playerPos = new Vec3d(player.field_70165_t, pix.field_70163_u + 1.0, player.field_70161_v);
         Vec3d vec = playerPos.func_178788_d(this.centerPos);
         vec = new Vec3d(vec.field_72450_a, 0.0, vec.field_72449_c);
         if (vec.field_72450_a == 0.0 && vec.field_72449_c == 0.0) {
            vec.func_178787_e(new Vec3d(1.0, 0.0, 0.0));
         }

         this.camera.setTarget(new CameraTargetLocation((int)this.centerPos.field_72450_a, (int)this.centerPos.field_72448_b, (int)this.centerPos.field_72449_c));
         this.baseVec = vec.func_72432_b();
         this.updatePosition();
      }

   }

   private void updatePosition() {
      if ((this.target instanceof CameraTargetEntity || !ClientProxy.battleManager.battleEnded) && this.spherePosChanged) {
         Matrix3f mat = this.constructYawMatrix();
         if (mat == null || this.baseVec == null) {
            return;
         }

         Vec3d newPos = multiply(mat, this.baseVec);
         mat = this.constructRotMatrix(newPos.func_72431_c(new Vec3d(0.0, 1.0, 0.0)), this.pitch);
         Vec3d rotPos = multiply(mat, newPos);
         Vec3d testPos = new Vec3d(this.centerPos.field_72450_a + rotPos.field_72450_a * (double)this.radius, this.centerPos.field_72448_b + rotPos.field_72448_b * (double)this.radius, this.centerPos.field_72449_c + rotPos.field_72449_c * (double)this.radius);
         this.camera.func_70107_b(testPos.field_72450_a, testPos.field_72448_b, testPos.field_72449_c);
         this.spherePosChanged = false;
         this.camera.updatePositionAndRotation();
      }

   }

   private EntityPixelmon getOpponentPokemon() {
      if (ClientProxy.battleManager != null && ClientProxy.battleManager.displayedEnemyPokemon != null && ClientProxy.battleManager.displayedEnemyPokemon.length != 0) {
         UUID uuid = ClientProxy.battleManager.displayedEnemyPokemon[0].pokemonUUID;
         if (uuid != null) {
            Minecraft mc = Minecraft.func_71410_x();
            if (mc.field_71441_e != null) {
               List loadedEntityList = mc.field_71441_e.field_72996_f;

               for(int i = 0; i < loadedEntityList.size(); ++i) {
                  Entity e = (Entity)loadedEntityList.get(i);
                  if (e.func_110124_au().equals(uuid)) {
                     return (EntityPixelmon)e;
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private Matrix3f constructYawMatrix() {
      return this.constructRotMatrix(new Vec3d(0.0, 1.0, 0.0), this.yaw);
   }

   private Matrix3f constructRotMatrix(Vec3d axis, float rot) {
      Matrix3f mat = new Matrix3f();
      mat.m00 = (float)(Math.cos((double)rot) + axis.field_72450_a * axis.field_72450_a * (1.0 - Math.cos((double)rot)));
      mat.m01 = (float)(axis.field_72450_a * axis.field_72448_b * (1.0 - Math.cos((double)rot)) - axis.field_72449_c * Math.sin((double)rot));
      mat.m02 = (float)(axis.field_72450_a * axis.field_72449_c * (1.0 - Math.cos((double)rot)) + axis.field_72448_b * Math.sin((double)rot));
      mat.m10 = (float)(axis.field_72450_a * axis.field_72448_b * (1.0 - Math.cos((double)rot)) + axis.field_72449_c * Math.sin((double)rot));
      mat.m11 = (float)(Math.cos((double)rot) + axis.field_72448_b * axis.field_72448_b * (1.0 - Math.cos((double)rot)));
      mat.m12 = (float)(axis.field_72448_b * axis.field_72449_c * (1.0 - Math.cos((double)rot)) - axis.field_72450_a * Math.sin((double)rot));
      mat.m20 = (float)(axis.field_72450_a * axis.field_72449_c * (1.0 - Math.cos((double)rot)) - axis.field_72448_b * Math.sin((double)rot));
      mat.m21 = (float)(axis.field_72448_b * axis.field_72449_c * (1.0 - Math.cos((double)rot)) + axis.field_72450_a * Math.sin((double)rot));
      mat.m22 = (float)(Math.cos((double)rot) + axis.field_72449_c * axis.field_72449_c * (1.0 - Math.cos((double)rot)));
      return mat;
   }

   public static Vec3d multiply(Matrix3f matrix, Vec3d vector) {
      double x = (double)matrix.m00 * vector.field_72450_a + (double)matrix.m01 * vector.field_72448_b + (double)matrix.m02 * vector.field_72449_c;
      double y = (double)matrix.m10 * vector.field_72450_a + (double)matrix.m11 * vector.field_72448_b + (double)matrix.m12 * vector.field_72449_c;
      double z = (double)matrix.m20 * vector.field_72450_a + (double)matrix.m21 * vector.field_72448_b + (double)matrix.m22 * vector.field_72449_c;
      return new Vec3d(x, y, z);
   }

   public void generateSphere() {
      EntityPixelmon poke;
      Vec3d lookVec;
      if (this.camera.mode == CameraMode.Evolution) {
         this.initSphere = true;
         if (this.target.getTargetData() instanceof EntityPixelmon) {
            poke = (EntityPixelmon)this.target.getTargetData();
            BaseStats stats = poke.getBaseStats();
            float height = 0.0F;
            if (stats != null) {
               height = stats.getHeight();
            }

            this.centerPos = new Vec3d(this.target.getX(), this.target.getY() + (double)height, this.target.getZ());
            lookVec = poke.func_70040_Z();
            lookVec = new Vec3d(lookVec.field_72450_a, 0.0, lookVec.field_72449_c);
            lookVec.func_72432_b();
            this.baseVec = lookVec;
            this.pitch = 0.19634955F;
            this.yaw = 0.0F;
            this.radius = (float)(poke.getBaseStats().getBoundsData().getWidth() * 4.0);
            this.radius = this.radius < 4.0F ? 4.0F : this.radius;
            this.spherePosChanged = true;
         }
      } else {
         this.initSphere = true;
         poke = ClientProxy.battleManager.getUserPokemon(this.camera.mode);
         EntityPlayer player = ClientProxy.battleManager.getViewPlayer();
         EntityPixelmon opp = this.getOpponentPokemon();
         this.centerPos = new Vec3d(poke.field_70165_t, poke.field_70163_u, poke.field_70161_v);
         if (opp != null) {
            this.centerPos = new Vec3d((this.centerPos.field_72450_a + opp.field_70165_t) / 2.0, (this.centerPos.field_72448_b + opp.field_70163_u) / 2.0, (this.centerPos.field_72449_c + opp.field_70161_v) / 2.0);
         }

         lookVec = new Vec3d(player.field_70165_t, poke.field_70163_u + 1.0, player.field_70161_v);
         Vec3d vec = lookVec.func_178788_d(this.centerPos);
         vec = new Vec3d(vec.field_72450_a, 0.0, vec.field_72449_c);
         if (vec.field_72450_a == 0.0 && vec.field_72449_c == 0.0) {
            vec.func_178787_e(new Vec3d(1.0, 0.0, 0.0));
         }

         this.baseVec = vec.func_72432_b();
         this.pitch = 0.7853982F;
         this.yaw = 0.0F;
         this.radius = (float)lookVec.func_72438_d(this.centerPos) + 2.0F;
         this.radius = this.radius < 10.0F ? 10.0F : this.radius;
         this.spherePosChanged = true;
      }

      ClientProxy.battleManager.setViewEntity(this.camera);
   }

   public void generatePositions() {
   }

   public void handleKeyboardInput() {
      if (Keyboard.isKeyDown(17)) {
         if ((double)this.pitch <= 1.5707963267948966) {
            this.pitch = (float)((double)this.pitch + 0.01);
         }

         this.spherePosChanged = true;
      }

      if (Keyboard.isKeyDown(31)) {
         if ((double)this.pitch >= -1.5707963267948966) {
            this.pitch = (float)((double)this.pitch - 0.01);
         }

         this.spherePosChanged = true;
      }

      if (Keyboard.isKeyDown(30)) {
         this.yaw = (float)((double)this.yaw - 0.01);
         this.spherePosChanged = true;
      }

      if (Keyboard.isKeyDown(32)) {
         this.yaw = (float)((double)this.yaw + 0.01);
         this.spherePosChanged = true;
      }

      if (Keyboard.isKeyDown(16)) {
         this.radius = (float)((double)this.radius - 0.1);
         this.spherePosChanged = true;
      }

      if (Keyboard.isKeyDown(18)) {
         this.radius = (float)((double)this.radius + 0.1);
         this.spherePosChanged = true;
      }

      this.updatePosition();
   }

   public void handleMouseMovement(int dx, int dy, int dwheel) {
      if (dx != 0 || dy != 0 || dwheel != 0) {
         this.yaw -= (float)dx * 0.005F;
         this.pitch -= (float)dy * 0.008F;
         this.radius -= (float)dwheel * 0.01F;
         if (this.radius > 40.0F) {
            this.radius = 40.0F;
         } else if (this.camera.mode == CameraMode.Battle) {
            if (this.radius < 4.0F) {
               this.radius = 4.0F;
            }
         } else if (this.radius < 2.0F) {
            this.radius = 2.0F;
         }

         this.spherePosChanged = true;
         this.updatePosition();
      }

   }
}
