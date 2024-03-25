package com.pixelmonmod.pixelmon.entities.bikes;

import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import com.pixelmonmod.pixelmon.enums.EnumBike;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBike extends Entity {
   private static final DataParameter dwType;
   private static final DataParameter dwColor;
   private EnumBike type;
   public boolean jumping;
   private int acceleratingTicks;
   private boolean pushing;
   private float outOfControlTicks;
   public float moveStrafing;
   public float moveVertical;
   public float moveForward;
   public float randomYawVelocity;
   public IncrementingVariable inc;
   private static int neededAccelerationTicks;

   public EntityBike(World world) {
      super(world);
      this.type = EnumBike.Mach;
      this.jumping = false;
      this.acceleratingTicks = 0;
      this.pushing = false;
      this.func_70105_a(1.1F, 1.5F);
      this.field_70138_W = 1.0F;
      this.field_70156_m = true;
   }

   protected void func_70088_a() {
      this.field_70180_af.func_187214_a(dwType, (byte)EnumBike.Mach.ordinal());
      this.field_70180_af.func_187214_a(dwColor, (byte)EnumBike.Mach.defaultColor.ordinal());
   }

   public String func_70005_c_() {
      return this.func_145818_k_() ? this.func_95999_t() : this.getType().getLocalizedName();
   }

   public void func_184206_a(DataParameter key) {
      if (key.func_187155_a() == dwType.func_187155_a()) {
         this.type = EnumBike.values()[(Byte)this.field_70180_af.func_187225_a(dwType)];
      }

      super.func_184206_a(key);
   }

   public double func_70042_X() {
      return 0.7;
   }

   public void func_70071_h_() {
      this.field_70125_A = 0.0F;
      if (this.field_70170_p.field_72995_K && this.shouldAnimate()) {
         float animationIncrement = this.getType() == EnumBike.Acro ? 1.75F : 1.25F;
         if (this.inc == null) {
            this.inc = new IncrementingVariable(animationIncrement, 2.14748365E9F);
         } else if (this.func_184179_bs() != null && this.func_184179_bs() instanceof EntityPlayer && ((EntityPlayer)this.func_184179_bs()).field_191988_bg < 0.0F) {
            this.inc.increment = -animationIncrement;
         } else {
            this.inc.increment = animationIncrement;
         }

         this.inc.tick();
      }

      if (this.field_70171_ac) {
         ++this.outOfControlTicks;
      } else if (this.func_180799_ab()) {
         ++this.outOfControlTicks;
      } else {
         this.outOfControlTicks = 0.0F;
      }

      if (!this.field_70170_p.field_72995_K && this.outOfControlTicks >= 60.0F) {
         this.func_184226_ay();
      }

      this.field_70169_q = this.field_70165_t;
      this.field_70167_r = this.field_70163_u;
      this.field_70166_s = this.field_70161_v;
      super.func_70071_h_();
      if (Math.abs(this.field_70159_w) < 0.003) {
         this.field_70159_w = 0.0;
      }

      if (Math.abs(this.field_70181_x) < 0.003) {
         this.field_70181_x = 0.0;
      }

      if (Math.abs(this.field_70179_y) < 0.003) {
         this.field_70179_y = 0.0;
      }

      if (this.jumping && this.field_70122_E) {
         this.jump();
      }

      this.jumping = false;
      if (this.acceleratingTicks > 0 && this.func_184179_bs() instanceof EntityLivingBase && this.getAcceleration() != 1.0F) {
         EntityLivingBase rider = (EntityLivingBase)this.func_184179_bs();
         if (rider.field_191988_bg == 0.0F) {
            --this.acceleratingTicks;
            this.moveEntityRidden(rider.field_70702_br / 3.0F, 0.98F);
         }
      }

      this.field_70170_p.field_72984_F.func_76320_a("travel");
      this.moveStrafing *= 0.98F;
      this.moveForward *= 0.98F;
      this.randomYawVelocity *= 0.9F;
      this.travel(this.moveStrafing, this.moveVertical, this.moveForward);
      this.field_70170_p.field_72984_F.func_76319_b();
   }

   public void travel(float strafe, float up, float forward) {
      EntityLivingBase rider = (EntityLivingBase)this.func_184179_bs();
      if (rider != null) {
         strafe = rider.field_70702_br / 3.0F;
         forward = rider.field_191988_bg;
         this.pushing = rider.field_191988_bg > 0.0F;
         if (this.pushing && this.acceleratingTicks < 0) {
            this.acceleratingTicks = 1;
         } else if (this.pushing) {
            this.acceleratingTicks = Math.min(this.acceleratingTicks + 1, neededAccelerationTicks);
         }
      } else {
         this.pushing = false;
      }

      this.field_70177_z -= strafe * 10.0F;
      this.field_70177_z = (this.field_70177_z + 180.0F) % 360.0F - 180.0F;
      strafe = 0.0F;
      this.moveEntityRidden(strafe, forward);
      double var9 = this.field_70165_t - this.field_70169_q;
      double var12 = this.field_70161_v - this.field_70166_s;
      float var11 = MathHelper.func_76133_a(var9 * var9 + var12 * var12) * 4.0F;
      if (var11 > 1.0F) {
         var11 = 1.0F;
      }

   }

   private void moveEntityRidden(float strafe, float forward) {
      float f4 = Math.signum(forward) * MathHelper.func_76131_a(1.0F * (float)this.acceleratingTicks / (float)neededAccelerationTicks * this.getRideSpeed(), 0.0F, this.getRideSpeed());
      if (this.getAcceleration() == 1.0F) {
         f4 = this.getRideSpeed();
      } else if (forward < 0.0F) {
         if (this.acceleratingTicks > 2) {
            this.acceleratingTicks -= 2;
         } else {
            f4 = 0.25F * this.getRideSpeed();
            this.acceleratingTicks = 0;
            this.pushing = false;
         }
      }

      double d0;
      if (this.func_70090_H()) {
         d0 = this.field_70163_u;
         this.func_191958_b(strafe, 0.0F, forward, 0.05F);
         this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
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
         float f2 = 0.5F;
         float f3 = 0.16277136F / (f2 * f2 * f2);
         f4 *= f3 * 2.8F;
         this.func_191958_b(strafe, 0.0F, forward, f4);
         f2 = 0.3F;
         this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
         if (this.field_70170_p.field_72995_K && (!this.field_70170_p.func_175667_e(new BlockPos((int)this.field_70165_t, 0, (int)this.field_70161_v)) || this.field_70170_p instanceof WorldServer && !((WorldServer)this.field_70170_p).func_72863_F().func_73149_a(this.field_70176_ah, this.field_70164_aj))) {
            if (this.field_70163_u > 0.0) {
               this.field_70181_x = -0.1;
            } else {
               this.field_70181_x = 0.0;
            }
         } else {
            this.field_70181_x -= 0.08;
         }

         this.field_70181_x *= 0.9800000190734863;
         this.field_70159_w *= (double)f2;
         this.field_70179_y *= (double)f2;
      }

   }

   public void func_180430_e(float fallDistance, float damageMultiplier) {
      if (this.func_184179_bs() instanceof EntityPlayerMP) {
         if (this.getType() == EnumBike.Mach) {
            this.func_184179_bs().func_180430_e(fallDistance / 1.5F, 1.0F);
         } else {
            this.func_184179_bs().func_180430_e(fallDistance / 6.0F, damageMultiplier);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_184190_l(Entity entityToUpdate) {
      this.applyYawToEntity(entityToUpdate);
   }

   public void func_184232_k(Entity passenger) {
      if (this.func_184196_w(passenger)) {
         float offset = -0.1F;
         Vec3d vec3d = (new Vec3d((double)offset, 0.0, 0.0)).func_178785_b(-this.field_70177_z * 0.017453292F - 1.5707964F);
         passenger.func_70107_b(this.field_70165_t + vec3d.field_72450_a, this.field_70163_u + this.func_70042_X() + passenger.func_70033_W(), this.field_70161_v + vec3d.field_72449_c);
         this.applyYawToEntity(passenger);
      }

   }

   protected void applyYawToEntity(Entity entity) {
      entity.func_181013_g(this.field_70177_z);
      float f = MathHelper.func_76142_g(entity.field_70177_z - this.field_70177_z);
      float f1 = MathHelper.func_76131_a(f, -105.0F, 105.0F);
      entity.field_70126_B += f1 - f;
      entity.field_70177_z += f1 - f;
      entity.func_70034_d(entity.field_70177_z);
   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      if (!player.func_70093_af() && hand != EnumHand.OFF_HAND) {
         if (!this.field_70170_p.field_72995_K) {
            ItemStack held = player.func_184586_b(hand);
            if (held.func_77973_b() instanceof ItemDye) {
               this.setColor(EnumDyeColor.func_176766_a(held.func_77960_j()));
               return true;
            } else {
               return !this.func_184207_aI() && player.func_184220_m(this);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      if (!this.field_70170_p.field_72995_K) {
         if (this.func_180431_b(source)) {
            return false;
         }

         if (!this.field_70128_L && !source.func_180136_u()) {
            ItemStack stack = new ItemStack(this.getType().getItem(), 1);
            stack.func_77983_a("color", new NBTTagByte((Byte)this.field_70180_af.func_187225_a(dwColor)));
            EntityItem drop = new EntityItem(this.field_70170_p);
            drop.func_92058_a(stack);
            drop.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            this.field_70170_p.func_72838_d(drop);
         }

         this.func_70106_y();
      }

      return true;
   }

   public AxisAlignedBB func_70046_E() {
      return this.func_70104_M() ? this.func_174813_aQ() : null;
   }

   public boolean func_70067_L() {
      return !this.field_70128_L;
   }

   public boolean func_70104_M() {
      return true;
   }

   public Entity func_184179_bs() {
      List list = this.func_184188_bt();
      return list.isEmpty() ? null : (Entity)list.get(0);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      this.setType(EnumBike.values()[nbt.func_74771_c("type")]);
      if (nbt.func_74764_b("color")) {
         this.setColor(EnumDyeColor.values()[nbt.func_74771_c("color")]);
      } else {
         this.setColor(this.getType().defaultColor);
      }

   }

   public void func_70014_b(NBTTagCompound nbt) {
      nbt.func_74774_a("type", (Byte)this.field_70180_af.func_187225_a(dwType));
      nbt.func_74774_a("color", (Byte)this.field_70180_af.func_187225_a(dwColor));
   }

   public EnumBike getType() {
      return this.type;
   }

   public void setType(EnumBike type) {
      this.field_70180_af.func_187227_b(dwType, (byte)type.ordinal());
      this.field_70180_af.func_187227_b(dwColor, (byte)type.defaultColor.ordinal());
   }

   public EnumDyeColor getColor() {
      return EnumDyeColor.values()[(Byte)this.field_70180_af.func_187225_a(dwColor)];
   }

   public void setColor(EnumDyeColor type) {
      this.field_70180_af.func_187227_b(dwColor, (byte)type.ordinal());
   }

   protected void jump() {
      this.field_70181_x = (double)(0.52F * (this.getType() == EnumBike.Acro ? 1.2F : 1.0F) * (this.func_70090_H() ? 0.3F : 1.0F));
      if (this.func_70051_ag()) {
         float f = this.field_70177_z * 0.017453292F;
         this.field_70159_w -= (double)(MathHelper.func_76126_a(f) * 0.2F);
         this.field_70179_y += (double)(MathHelper.func_76134_b(f) * 0.2F);
      }

      this.field_70160_al = true;
   }

   private float getAcceleration() {
      return this.getType() == EnumBike.Mach ? 0.1F : 0.9F;
   }

   private float getRideSpeed() {
      return 0.11F * (this.getType() == EnumBike.Mach ? 1.3F : 1.0F);
   }

   private boolean shouldAnimate() {
      return this.func_184179_bs() != null && Math.abs(this.field_70169_q - this.field_70165_t) + Math.abs(this.field_70166_s - this.field_70161_v) > 0.001;
   }

   static {
      dwType = EntityDataManager.func_187226_a(EntityBike.class, DataSerializers.field_187191_a);
      dwColor = EntityDataManager.func_187226_a(EntityBike.class, DataSerializers.field_187191_a);
      neededAccelerationTicks = 15;
   }
}
