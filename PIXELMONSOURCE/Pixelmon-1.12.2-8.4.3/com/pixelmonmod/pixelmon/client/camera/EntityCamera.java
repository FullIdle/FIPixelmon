package com.pixelmonmod.pixelmon.client.camera;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.camera.movement.CameraMovement;
import com.pixelmonmod.pixelmon.client.camera.movement.PlayerControlledMovement;
import com.pixelmonmod.pixelmon.client.camera.movement.PositionedMovement;
import com.pixelmonmod.pixelmon.client.gui.GuiEvolve;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityCamera extends EntityLiving {
   private ICameraTarget target;
   public CameraMode mode;
   private CameraMovement movement;

   public EntityCamera(World par1World) {
      this(par1World, CameraMode.Battle);
   }

   public EntityCamera(World par1World, CameraMode mode) {
      super(par1World);
      this.field_70131_O = 0.0F;
      this.field_70130_N = 0.0F;
      this.mode = mode;
      if (PixelmonConfig.playerControlCamera) {
         this.movement = new PlayerControlledMovement(par1World, this);
      } else {
         this.movement = new PositionedMovement(par1World, this);
      }

   }

   protected void func_70088_a() {
      super.func_70088_a();
   }

   public void func_70030_z() {
      this.field_70763_ax = this.field_70764_aw;
      this.field_70760_ar = this.field_70761_aq;
      this.field_70758_at = this.field_70759_as;
      this.field_70126_B = this.field_70177_z;
      this.field_70127_C = this.field_70125_A;
   }

   private float updateRotation(float par1, float par2, float par3) {
      float f3 = MathHelper.func_76142_g(par2 - par1);
      if (f3 > par3) {
         f3 = par3;
      }

      if (f3 < -par3) {
         f3 = -par3;
      }

      return par1 + f3;
   }

   public void setTarget(ICameraTarget t) {
      this.target = t;
   }

   public void setTargetRandomPosition(ICameraTarget t) {
      this.setTarget(t);
      this.getMovement().setRandomPosition(t);
   }

   public ICameraTarget getTarget() {
      return this.target;
   }

   public void updatePositionAndRotation() {
      if (Minecraft.func_71410_x().func_175606_aa() == this && this.target != null && this.target.isValidTarget()) {
         double vecX = this.target.getX() - this.field_70165_t;
         double vecY = this.target.getY() + this.target.getYSeeOffset() - 0.5 - this.field_70163_u;
         double vecZ = this.target.getZ() - this.field_70161_v;
         double length = (double)MathHelper.func_76133_a(vecX * vecX + vecZ * vecZ);
         float f2 = (float)(Math.atan2(vecZ, vecX) * 180.0 / Math.PI) - 90.0F;
         float f3 = (float)(-(Math.atan2(vecY, length) * 180.0 / Math.PI));
         this.field_70125_A = this.updateRotation(this.field_70125_A, f3, 50.0F);
         this.field_70177_z = this.updateRotation(this.field_70177_z, f2, 50.0F);
         this.movement.updatePositionAndRotation();
      }

   }

   public void func_70636_d() {
      if (this.field_70128_L || (this.mode != CameraMode.Battle || !ClientProxy.battleManager.battleEnded) && (this.mode != CameraMode.Evolution || Minecraft.func_71410_x().field_71462_r instanceof GuiEvolve)) {
         this.updatePositionAndRotation();
         if (!this.field_70128_L) {
            this.getMovement().onLivingUpdate();
         }

      } else {
         this.func_70106_y();
      }
   }

   public boolean func_70055_a(Material materialIn) {
      return false;
   }

   public float func_70047_e() {
      Pixelmon.proxy.resetMouseOver();
      return super.func_70047_e();
   }

   public CameraMovement getMovement() {
      return this.movement;
   }
}
