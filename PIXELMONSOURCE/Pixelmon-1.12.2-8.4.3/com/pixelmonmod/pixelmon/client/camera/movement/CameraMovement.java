package com.pixelmonmod.pixelmon.client.camera.movement;

import com.pixelmonmod.pixelmon.client.camera.EntityCamera;
import com.pixelmonmod.pixelmon.client.camera.ICameraTarget;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class CameraMovement {
   protected World world;
   protected EntityCamera camera;

   public CameraMovement(World worldObj, EntityCamera camera) {
      this.world = worldObj;
      this.camera = camera;
   }

   protected boolean canPosSee(BlockPos pos1, BlockPos pos2) {
      return this.world.func_72933_a(new Vec3d((double)pos1.func_177958_n(), (double)pos1.func_177956_o(), (double)pos1.func_177952_p()), new Vec3d((double)pos2.func_177958_n(), (double)pos2.func_177956_o(), (double)pos2.func_177952_p())) == null;
   }

   public abstract void setRandomPosition(ICameraTarget var1);

   public abstract void onLivingUpdate();

   public abstract void generatePositions();

   public void handleKeyboardInput() {
   }

   public void updatePositionAndRotation() {
      this.camera.field_70169_q = this.camera.field_70165_t;
      this.camera.field_70167_r = this.camera.field_70163_u + this.camera.func_70033_W();
      this.camera.field_70166_s = this.camera.field_70161_v;
   }

   public void handleMouseMovement(int dx, int dy, int dwheel) {
   }
}
