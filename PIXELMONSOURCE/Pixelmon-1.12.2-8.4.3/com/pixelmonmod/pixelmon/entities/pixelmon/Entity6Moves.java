package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.UpdatePixelmonMovementPacket;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.movement.FlyMode;
import com.pixelmonmod.pixelmon.entities.pixelmon.movement.PixelmonMoveHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.movement.PixelmonMovementBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.movement.PixelmonMovementFlying;
import com.pixelmonmod.pixelmon.entities.pixelmon.movement.PixelmonMovementGround;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.RidingOffsets;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.List;
import javax.vecmath.Vector3f;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Entity6Moves extends Entity5Battle {
   static final String NBT_FLY_HEIGHT = "flyheight";
   static final String NBT_IS_FLYING = "isFly";
   static final String NBT_CAN_FLY = "canFly";
   protected static final DataParameter FLYING;
   protected static final DataParameter HOVERING;
   public int baseFlyHeight = 30;
   public int baseSwimDepth = 5;
   private int flyHeight = -1;
   private int swimDepth = -1;
   public float fallRate = 0.04F;
   public float strafeUpDown = 0.0F;
   public float hoverRotationPitch = 20.0F;
   public float moveMultiplier = 0.3F;
   public static float maxMoveMultiplier;
   public static float minMoveMultiplier;
   public float thermalPower = 0.0F;
   public FlyData[] flyData = null;
   public boolean canFly = true;
   public boolean canFlyOverride = true;
   public boolean canSwim = false;
   public float rotationRoll = 0.0F;
   public float prevRotationRoll = 0.0F;
   public PixelmonMovementFlying movementFlying = new PixelmonMovementFlying(this);
   public PixelmonMovementGround movementGround = new PixelmonMovementGround(this);
   public int takeOffTicks = 0;
   public boolean lastFlyingState = false;
   Vec3d targetPosition;
   public float lastSpeed = -1.0F;
   public float lastStrafe = -1.0F;
   public float lastStrafeUp = -1.0F;
   float rotP = 0.0F;
   float rotY = 0.0F;
   float rotR = 0.0F;

   public PixelmonMovementBase getMovement() {
      return (PixelmonMovementBase)(this.getIsFlying() ? this.movementFlying : this.movementGround);
   }

   public Entity6Moves(World worldIn) {
      super(worldIn);
      this.field_70765_h = new PixelmonMoveHelper(this);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.field_70180_af.func_187214_a(FLYING, false);
      this.field_70180_af.func_187214_a(HOVERING, false);
   }

   public void setIsFlying(boolean isFlying) {
      this.field_70180_af.func_187227_b(FLYING, isFlying);
   }

   public boolean getIsFlying() {
      return (Boolean)this.field_70180_af.func_187225_a(FLYING);
   }

   public boolean getIsHovering() {
      return (Boolean)this.field_70180_af.func_187225_a(HOVERING);
   }

   public boolean func_70648_aU() {
      return this.canSwim;
   }

   public void toggleHover() {
      if (!this.field_70170_p.field_72995_K) {
         boolean hovering = (Boolean)this.field_70180_af.func_187225_a(HOVERING);
         if (this.getIsFlying() || hovering) {
            this.field_70180_af.func_187227_b(HOVERING, !hovering);
            this.calcMoveSpeed();
            Pixelmon.network.sendToAllTracking(new UpdatePixelmonMovementPacket(this), this);
         }

      }
   }

   public void handleMovement(List movements) {
      this.getMovement().handleMovement(movements);
      if (!this.field_70170_p.field_72995_K) {
         Pixelmon.network.sendToAllTracking(new UpdatePixelmonMovementPacket(this), this);
      }

   }

   public void doJump() {
      this.func_70664_aZ();
   }

   protected float func_175134_bD() {
      return (float)Math.max(0.42, (double)(super.func_175134_bD() * this.field_70131_O / 2.0F));
   }

   public void takeOff() {
      if (this.getIsHovering()) {
         this.toggleHover();
      }

      this.field_70125_A = 20.0F;
      this.lastSpeed = -1.0F;
      this.lastStrafe = -1.0F;
      this.lastStrafeUp = -1.0F;
      this.strafeUpDown = 0.0F;
      this.takeOffTicks = 20;
      if (!this.field_70170_p.field_72995_K) {
         this.moveMultiplier = 0.2F;
         this.field_70163_u += 0.3;
         Pixelmon.network.sendToAllTracking(new UpdatePixelmonMovementPacket(this), this);
         this.setIsFlying(true);
      }

   }

   public boolean func_82171_bF() {
      return true;
   }

   public void func_180430_e(float distance, float damageMultiplier) {
      float[] ret = ForgeHooks.onLivingFall(this, distance, damageMultiplier);
      if (ret != null) {
         double moveDown = Math.abs(this.field_70181_x) - 0.8;
         double moveForward = (double)Math.abs(this.field_191988_bg) - 0.8;
         double speed = moveDown > moveForward ? moveDown : moveForward;
         if (speed > 0.0) {
            this.func_184185_a(this.func_184588_d((int)speed * 6), 1.0F, 1.0F);
            this.func_70097_a(DamageSource.field_76379_h, (float)speed * 6.0F);
            int j = MathHelper.func_76128_c(this.field_70165_t);
            int k = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224);
            int l = MathHelper.func_76128_c(this.field_70161_v);
            IBlockState iblockstate = this.field_70170_p.func_180495_p(new BlockPos(j, k, l));
            if (iblockstate.func_185904_a() != Material.field_151579_a) {
               SoundType soundtype = iblockstate.func_177230_c().getSoundType(iblockstate, this.field_70170_p, new BlockPos(j, k, l), this);
               this.func_184185_a(soundtype.func_185842_g(), soundtype.func_185843_a() * 0.5F, soundtype.func_185847_b() * 0.75F);
            }
         }

      }
   }

   public void calcMoveSpeed() {
      if (!this.getIsHovering()) {
         this.field_191988_bg = (0.2F + (this.getBaseStats().mountedFlying.upperAngleLimit - this.field_70125_A) / (this.getBaseStats().mountedFlying.upperAngleLimit - this.getBaseStats().mountedFlying.lowerAngleLimit) * this.getBaseStats().mountedFlying.maxFlySpeed + 0.1F) * this.moveMultiplier;
      } else if (this.field_191988_bg > PixelmonConfig.flyingSpeedLimit) {
         this.field_191988_bg = PixelmonConfig.flyingSpeedLimit;
      } else {
         this.field_191988_bg = 0.01F;
      }

   }

   public void setTarget(Vec3d targetPosition) {
      this.targetPosition = targetPosition;
   }

   public Vec3d getTargetPosition() {
      return this.targetPosition;
   }

   public void func_70636_d() {
      if (!this.getIsFlying()) {
         this.field_70125_A = 0.0F;
         this.rotationRoll = 0.0F;
      }

      this.getMovement().onLivingUpdate();
      if (this.field_70170_p.field_72995_K || !PixelmonConfig.disabledNonPlayerPixelmonMovement || this.pokemon.getStorageAndPosition() != null && this.pokemon.getStorage() != null && this.pokemon.getStorage() instanceof PlayerPartyStorage) {
         if (this.func_184179_bs() != null && this.getBaseStats() != null && !this.getIsFlying()) {
            if (this.canSurf() && this.ridingInitialised) {
               this.func_184179_bs().func_70050_g(this.initAir);
            }

            if (!this.func_184179_bs().func_70090_H() && this.canSurf()) {
               this.field_191988_bg = 0.0F;
            } else {
               this.field_191988_bg *= 0.4F;
            }

            if ((double)this.field_191988_bg > -0.1 && (double)this.field_191988_bg < 0.1) {
               this.field_191988_bg = 0.0F;
            }

            if (!this.func_70090_H() && this.ridingPlayerVertical > 0) {
               if (this.field_70122_E) {
                  this.func_70664_aZ();
                  this.field_70181_x *= (double)(1.0F + (float)this.pokemon.getStat(StatsType.Speed) / 500.0F);
               } else if (this.canFly()) {
                  double moty = (double)(0.04F + 0.06F * ((float)this.pokemon.getStat(StatsType.Speed) / 500.0F));
                  if (this.field_70181_x + moty <= 0.4) {
                     this.field_70181_x += moty;
                  } else {
                     this.field_70181_x += Math.max(0.0, 0.4 - this.field_70181_x);
                  }

                  this.isFlying = true;
               }
            } else if (this.func_70090_H()) {
               if (this.ridingPlayerVertical > 0) {
                  if (this.canSurf()) {
                     this.field_70181_x += 0.029999999329447746;
                  } else {
                     this.field_70181_x += 0.10000000149011612;
                  }
               } else if (this.ridingPlayerVertical < 0) {
                  this.field_70181_x -= 0.029999999329447746;
               }
            }

            this.ridingPlayerVertical = 0;
            if (this.field_70122_E && this.isFlying) {
               this.isFlying = false;
            }
         }

         super.func_70636_d();
      }
   }

   public void onLanding() {
   }

   public float getFlyingDirection() {
      return this.func_184179_bs().field_70177_z;
   }

   public void func_191986_a(float strafe, float vertical, float forward) {
      if (this.func_184207_aI() && this.func_184186_bw() && this.getCanFly()) {
         this.rotationRoll = 0.0F;
         EntityLivingBase passenger = (EntityLivingBase)this.func_184179_bs();
         if (passenger != null) {
            this.field_70125_A = passenger.field_70125_A * 0.5F;
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
            this.field_70761_aq = this.field_70177_z;
            this.field_70759_as = this.field_70761_aq;
            strafe = passenger.field_70702_br * 0.5F;
            forward = passenger.field_191988_bg;
            if (forward <= 0.0F) {
               forward *= 0.25F;
            }
         }

         this.field_70747_aH = this.func_70689_ay() * 0.1F;
         this.func_70659_e((float)this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e());
         super.func_191986_a(strafe, vertical, forward);
      } else {
         this.field_70747_aH = 0.02F;
         this.travelAlt(strafe, vertical, forward);
      }

   }

   public void travelAlt(float strafe, float up, float forward) {
      this.field_70138_W = 0.5F;
      SpawnLocationType location = this.getSpawnLocation();
      if (location == SpawnLocationType.AirPersistent) {
         this.field_70181_x = 0.0;
      }

      double d5;
      double d7;
      if (this.getBaseStats() != null && this.func_184188_bt().size() != 0) {
         if (this.func_184188_bt().size() > 0 && this.func_184179_bs() instanceof EntityLivingBase) {
            EntityLivingBase rider = (EntityLivingBase)this.func_184179_bs();
            if (PixelmonConfig.enablePointToSteer) {
               this.field_70126_B = this.field_70177_z = this.func_184179_bs().field_70177_z;
            }

            strafe = rider.field_70702_br * 0.5F * PixelmonServerConfig.ridingSpeedMultiplier;
            this.field_70177_z -= strafe * 10.0F;
            strafe = 0.0F;
            this.field_70125_A = this.func_184179_bs().field_70125_A * 0.5F;
            this.field_70759_as = this.field_70761_aq = this.field_70177_z;
            forward = rider.field_191988_bg * PixelmonServerConfig.ridingSpeedMultiplier;
            this.moveMounted(strafe, up, forward);
            if (PixelmonConfig.enablePointToSteer) {
               rider.field_70760_ar = rider.field_70761_aq = this.field_70761_aq;
               rider.field_70126_B = rider.field_70177_z = this.field_70177_z;
            }

            this.field_184618_aE = this.field_70721_aZ;
            double d5 = this.field_70165_t - this.field_70169_q;
            d5 = this.field_70161_v - this.field_70166_s;
            d7 = this instanceof EntityFlying ? this.field_70163_u - this.field_70167_r : 0.0;
            float f10 = MathHelper.func_76133_a(d5 * d5 + d7 * d7 + d5 * d5) * 4.0F;
            if (f10 > 1.0F) {
               f10 = 1.0F;
            }

            this.field_70721_aZ += (f10 - this.field_70721_aZ) * 0.4F;
            this.field_184619_aG += this.field_70721_aZ;
            if (this.field_70170_p.field_72995_K) {
               this.field_70122_E = !this.field_70170_p.func_175623_d(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76141_d((float)(MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1)), MathHelper.func_76128_c(this.field_70161_v)));
            }
         }

         if (location == SpawnLocationType.AirPersistent) {
            this.field_70181_x = 0.0;
         }

         if (location == SpawnLocationType.Air && this.isFlying) {
            this.field_70181_x -= 0.01;
         }

      } else {
         if (this.getBaseStats() != null && !this.getBaseStats().canFly() && this.getSpawnLocation() == SpawnLocationType.Water && this.func_70090_H()) {
            float f4 = 0.9F;
            float f5 = 0.02F;
            float f6 = (float)EnchantmentHelper.func_185294_d(this);
            if (f6 > 3.0F) {
               f6 = 3.0F;
            }

            if (!this.field_70122_E) {
               f6 *= 0.5F;
            }

            if (f6 > 0.0F) {
               f4 += (0.54600006F - f4) * f6 / 3.0F;
               f5 += (this.func_70689_ay() - f5) * f6 / 3.0F;
            }

            this.func_191958_b(strafe, 0.0F, forward, f5);
            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= (double)f4;
            this.field_70181_x *= 0.800000011920929;
            this.field_70179_y *= (double)f4;
            this.field_70181_x -= 0.02;
            if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6000000238418579, this.field_70179_y)) {
               this.field_70181_x = 0.30000001192092896;
            }

            this.field_184618_aE = this.field_70721_aZ;
            d5 = this.field_70165_t - this.field_70169_q;
            d7 = this.field_70161_v - this.field_70166_s;
            double d9 = this instanceof EntityFlying ? this.field_70163_u - this.field_70167_r : 0.0;
            float f10 = MathHelper.func_76133_a(d5 * d5 + d9 * d9 + d7 * d7) * 4.0F;
            if (f10 > 1.0F) {
               f10 = 1.0F;
            }

            this.field_70721_aZ += (f10 - this.field_70721_aZ) * 0.4F;
            this.field_184619_aG += this.field_70721_aZ;
         } else {
            super.func_191986_a(strafe, up, forward);
         }

      }
   }

   protected void moveMounted(float strafe, float up, float forward) {
      if (this.func_184179_bs() != null && this.canSurf() && this.field_70171_ac) {
         double var9 = this.field_70163_u;
         this.func_191958_b(strafe, 0.0F, forward, this.field_70747_aH);
         this.func_70091_d(MoverType.SELF, this.field_70159_w * (double)this.mountBoost, this.field_70181_x, this.field_70179_y * (double)this.mountBoost);
         if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6000000238418579 - this.field_70163_u + var9, this.field_70179_y)) {
            this.field_70181_x = 0.30000001192092896;
         }

         this.field_70181_x *= 0.9800000190734863;
         this.field_70159_w *= 0.9599999785423279;
         this.field_70179_y *= 0.9599999785423279;
      } else if (this.canFly() && !this.func_70090_H() && !this.func_180799_ab()) {
         float var3 = 0.91F;
         if (this.field_70122_E) {
            var3 = 0.54600006F;
            IBlockState state = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v)));
            if (state.func_185904_a() != Material.field_151579_a) {
               var3 = state.func_177230_c().field_149765_K * 0.91F;
            }
         }

         float var8 = 0.16277136F / (var3 * var3 * var3);
         float var5 = this.getRideSpeed();
         if (this.field_70122_E) {
            var5 *= 0.6F * var8;
         } else {
            var5 *= 0.8F;
         }

         this.func_191958_b(strafe, 0.0F, forward, var5);
         this.func_70091_d(MoverType.SELF, this.field_70159_w * (double)this.mountBoost, this.field_70181_x, this.field_70179_y * (double)this.mountBoost);
         var3 = 0.91F;
         if (this.field_70122_E) {
            var3 = 0.54600006F;
            IBlockState state = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v)));
            if (state.func_185904_a() != Material.field_151579_a) {
               var3 = state.func_177230_c().field_149765_K * 0.91F;
            }
         }

         if (!this.field_70170_p.field_72995_K && !this.field_70170_p.func_175667_e(new BlockPos((int)this.field_70165_t, 0, (int)this.field_70161_v))) {
            if (this.field_70163_u > 0.0) {
               this.field_70181_x -= 0.01;
            } else {
               this.field_70181_x = 0.0;
            }
         } else {
            this.field_70181_x -= 0.02;
         }

         this.field_70181_x *= 0.9800000190734863;
         this.field_70159_w *= (double)var3;
         this.field_70179_y *= (double)var3;
      } else {
         this.moveEntityRidden(strafe, forward * this.mountBoost);
         super.func_191986_a(strafe, up, forward * this.mountBoost);
      }
   }

   private void moveEntityRidden(float strafe, float forward) {
      double d0;
      if (this.func_70090_H()) {
         d0 = this.field_70163_u;
         this.func_191958_b(strafe, 0.0F, forward, 0.05F);
         this.func_70091_d(MoverType.SELF, this.field_70159_w * (double)this.mountBoost, this.field_70181_x, this.field_70179_y * (double)this.mountBoost);
         this.field_70159_w *= 0.800000011920929;
         this.field_70181_x *= 0.800000011920929;
         this.field_70179_y *= 0.800000011920929;
         this.field_70181_x -= 0.019;
         if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6000000238418579 - this.field_70163_u + d0, this.field_70179_y)) {
            this.field_70181_x = 0.30000001192092896;
         }
      } else if (this.func_180799_ab()) {
         d0 = this.field_70163_u;
         this.func_191958_b(strafe, 0.0F, forward, 0.02F);
         this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
         this.field_70159_w *= 0.6;
         this.field_70181_x *= 0.6;
         this.field_70179_y *= 0.6;
         this.field_70181_x -= 0.02;
         if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6000000238418579 - this.field_70163_u + d0, this.field_70179_y)) {
            this.field_70181_x = 0.30000001192092896;
         }
      } else {
         if (SpawnLocationType.containsOnly(this.getBaseStats().spawnLocations, SpawnLocationType.Water)) {
            return;
         }

         float f2 = 0.91F;
         if (this.field_70122_E) {
            f2 = 0.54600006F;
            IBlockState state = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v)));
            if (state.func_185904_a() != Material.field_151579_a) {
               f2 = state.func_177230_c().field_149765_K * 0.91F;
            }
         }

         float f3 = 0.16277136F / (f2 * f2 * f2);
         float f4 = this.getRideSpeed();
         if (this.field_70122_E) {
            f4 *= f3 * 2.8F;
         }

         this.func_191958_b(strafe, 0.0F, forward, f4);
         f2 = 0.91F;
         if (this.field_70122_E) {
            f2 = 0.54600006F;
            IBlockState state = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v)));
            if (state.func_185904_a() != Material.field_151579_a) {
               f2 = state.func_177230_c().field_149765_K * 0.91F;
            }
         }

         float sh = (float)Math.ceil((double)(this.getBaseStats().getHeight() * this.getScaleFactor() / 5.0F));
         this.field_70138_W = sh;
         this.func_70091_d(MoverType.PLAYER, this.field_70159_w * (double)this.mountBoost, this.field_70181_x, this.field_70179_y * (double)this.mountBoost);
         if (!this.field_70170_p.field_72995_K || this.field_70170_p.func_175667_e(new BlockPos((int)this.field_70165_t, 0, (int)this.field_70161_v)) && (!(this.field_70170_p instanceof WorldServer) || ((WorldServer)this.field_70170_p).func_72863_F().func_73149_a(this.field_70176_ah, this.field_70164_aj))) {
            this.field_70181_x -= 0.04;
         } else if (this.field_70163_u > 0.0) {
            this.field_70181_x = -0.1;
         } else {
            this.field_70181_x = 0.0;
         }

         this.field_70181_x *= 0.9800000190734863;
         this.field_70159_w *= (double)f2;
         this.field_70179_y *= (double)f2;
      }

   }

   public void func_70091_d(MoverType type, double x, double y, double z) {
      super.func_70091_d(type, x, y, z);
   }

   private float getRideSpeed() {
      return 0.07F + 0.05F * (float)this.pokemon.getStat(StatsType.Speed) / 500.0F;
   }

   protected void func_184225_p(Entity passenger) {
      super.func_184225_p(passenger);
      this.func_184651_r();
      this.lastSpeed = -1.0F;
      this.setIsFlying(false);
      this.field_70125_A = 0.0F;
      this.rotationRoll = 0.0F;
   }

   public int getFlyHeight() {
      if (this.flyHeight == -1) {
         int variance = (int)((float)this.baseFlyHeight / 10.0F);
         this.flyHeight = this.baseFlyHeight + this.field_70146_Z.nextInt(variance + 1) - variance / 2;
      }

      return this.flyHeight;
   }

   public int getSwimDepth() {
      if (this.swimDepth == -1) {
         int variance = (int)((float)this.baseSwimDepth / 10.0F);
         this.swimDepth = this.baseSwimDepth + this.field_70146_Z.nextInt(variance + 1) - variance / 2;
      }

      return this.swimDepth;
   }

   public void func_70014_b(NBTTagCompound compound) {
      compound.func_74768_a("flyheight", this.getFlyHeight());
      compound.func_74757_a("canFly", this.canFly);
      compound.func_74757_a("isFly", this.getIsFlying());
      super.func_70014_b(compound);
   }

   public void func_70037_a(NBTTagCompound compound) {
      this.flyHeight = compound.func_74762_e("flyheight");
      this.canFly = compound.func_74767_n("canFly");
      this.setIsFlying(compound.func_74767_n("isFly"));
      super.func_70037_a(compound);
   }

   public boolean func_145770_h(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
      double d3;
      double d4;
      double d5;
      if (this.getIsFlying()) {
         d3 = this.field_70165_t - p_145770_1_;
         d4 = this.field_70161_v - p_145770_5_;
         d5 = d3 * d3 + d4 * d4;
         return this.func_70112_a(d5);
      } else {
         d3 = this.field_70165_t - p_145770_1_;
         d4 = this.field_70163_u - p_145770_3_;
         d5 = this.field_70161_v - p_145770_5_;
         double d6 = d3 * d3 + d4 * d4 + d5 * d5;
         return this.func_70112_a(d6);
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean func_70112_a(double distance) {
      double d0 = this.func_174813_aQ().func_72320_b();
      if (Double.isNaN(d0)) {
         d0 = 1.0;
      }

      d0 = d0 * 1200.0 * 1.0;
      return distance < d0 * d0;
   }

   public void setCanFly(boolean canFly) {
      this.canFly = canFly;
   }

   public boolean getCanFly() {
      return this.canFly() && this.getBaseStats().mountedFlying != null;
   }

   public void func_184232_k(Entity passenger) {
      if (this.getBaseStats() != null) {
         try {
            if (this.getBaseStats().ridingOffsets == null) {
               this.getBaseStats().ridingOffsets = new RidingOffsets();
            }

            Vector3f offsets;
            if (this.getBaseStats().ridingOffsets.standing != null) {
               offsets = this.getBaseStats().ridingOffsets.standing;
            } else {
               offsets = new Vector3f();
            }

            if (this.getBaseStats().ridingOffsets.moving != null && (this.flyingDelayCounter >= flyingDelayLimit || this.getUsingRidingSpecialConditions())) {
               offsets = this.getBaseStats().ridingOffsets.moving;
            }

            if (this.func_184196_w(passenger) && this.getIsFlying()) {
               this.getMovement().updatePassenger(passenger, offsets);
            } else {
               Vec3d vec = new Vec3d((double)(offsets.x * this.getPixelmonScale() * this.getScaleFactor()), (double)((offsets.y + this.field_70131_O) * this.getPixelmonScale() * this.getScaleFactor()), (double)(offsets.z * this.getPixelmonScale() * this.getScaleFactor()));
               vec = vec.func_178785_b(-1.0F * this.field_70761_aq * 3.1415927F / 180.0F);
               passenger.func_70107_b(this.field_70165_t + vec.field_72450_a, this.field_70163_u + vec.field_72448_b, this.field_70161_v + vec.field_72449_c);
            }
         } catch (Exception var4) {
            passenger.func_184210_p();
         }
      }

   }

   public abstract SoundEvent getFlyingSound();

   public boolean getStaysHorizontalInRender() {
      return this.getBaseStats() != null && this.getBaseStats().mountedFlying != null ? this.getBaseStats().mountedFlying.staysHorizontalFlying : false;
   }

   static {
      FLYING = EntityDataManager.func_187226_a(Entity6Moves.class, DataSerializers.field_187198_h);
      HOVERING = EntityDataManager.func_187226_a(Entity6Moves.class, DataSerializers.field_187198_h);
      maxMoveMultiplier = 1.4F;
      minMoveMultiplier = 0.3F;
   }

   public class FlyData {
      public FlyMode mode;
      public int probability;

      public FlyData(FlyMode mode, int probability) {
         this.mode = mode;
         this.probability = probability;
      }
   }
}
