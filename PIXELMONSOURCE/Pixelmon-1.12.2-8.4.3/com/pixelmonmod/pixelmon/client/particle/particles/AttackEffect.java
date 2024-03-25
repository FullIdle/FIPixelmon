package com.pixelmonmod.pixelmon.client.particle.particles;

import com.pixelmonmod.pixelmon.battles.attacks.animations.StandardParticleAnimationData;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.IAttackEffect;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import com.pixelmonmod.pixelmon.client.particle.ParticleMathHelper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AttackEffect extends ParticleEffect {
   public Random rand = new Random();
   public double startX;
   public double startY;
   public double startZ;
   public double endX;
   public double endY;
   public double endZ;
   public double innaccuracy;
   public double travelledDistance = 0.0;
   public double totalDistance;
   public int maxAge;
   public int age = 0;
   public boolean eol = false;
   public double speed;
   public double mX;
   public double mY;
   public double mZ;
   public EnumType type;
   public float r = -1.0F;
   public float g = -1.0F;
   public float b = -1.0F;
   public float scale = -1.0F;
   public ResourceLocation texture = null;
   public int hiddenUntil;
   public boolean hiddenIsDisable;
   public int ticker = 0;
   public double noiseParam = 20.0;
   public int startID;
   public int endID;
   public Minecraft mc;
   public IAttackEffect callback = null;

   public AttackEffect() {
   }

   public AttackEffect setLifetimeTicks(int ticks) {
      this.maxAge = ticks;
      return this;
   }

   public AttackEffect setHideTicks(int hideTicks, boolean hideWhileDisabled) {
      if (hideTicks == 0) {
         this.hiddenUntil = 0;
         this.hiddenIsDisable = false;
         return this;
      } else {
         this.hiddenUntil = hideTicks;
         this.hiddenIsDisable = hideWhileDisabled;
         return this;
      }
   }

   public AttackEffect setNoise(double noiseParam) {
      this.noiseParam = noiseParam;
      if (noiseParam == 0.0) {
         this.innaccuracy = 0.0;
      }

      return this;
   }

   public AttackEffect setStartPos(float startX, float startY, float startZ) {
      this.startX = (double)startX;
      this.startY = (double)startY;
      this.startZ = (double)startZ;
      this.setHeading();
      return this;
   }

   public AttackEffect setStartPos(double startX, double startY, double startZ) {
      return this.setStartPos((float)startX, (float)startY, (float)startZ);
   }

   public AttackEffect setEndPos(float endX, float endY, float endZ) {
      this.endX = (double)endX;
      this.endY = (double)endY;
      this.endZ = (double)endZ;
      this.setHeading();
      return this;
   }

   public AttackEffect setEndPos(double endX, double endY, double endZ) {
      return this.setEndPos((float)endX, (float)endY, (float)endZ);
   }

   public AttackEffect(IAttackEffect effect, int startEntityID, float[] startPos, int endEntityID, float[] endPos, StandardParticleAnimationData data) {
      this.callback = effect;
      this.startID = startEntityID;
      this.startX = (double)startPos[0];
      this.startY = (double)startPos[1];
      this.startZ = (double)startPos[2];
      this.endID = endEntityID;
      this.endX = (double)endPos[0];
      this.endY = (double)endPos[1];
      this.endZ = (double)endPos[2];
      if (data != null) {
         this.speed = (double)data.speed;
         this.r = (float)data.rgb[0] / 255.0F;
         this.g = (float)data.rgb[1] / 255.0F;
         this.b = (float)data.rgb[2] / 255.0F;
         this.texture = data.texture.texture;
         this.maxAge = data.lifetimeTicks;
         this.scale = data.scale;
      }

      this.innaccuracy = 0.05000000074505806;
      this.setHeading();
      this.callback = effect;
      this.callback.onConstruct(this);
   }

   public AttackEffect(IAttackEffect effect, double startX, double startY, double startZ, double endX, double endY, double endZ, int accuracy, int hiddenUntil, boolean hiddenIsDisable, double noiseParam, double speed, int startID, int endID) {
      this.innaccuracy = 1.0 - (double)accuracy / 100.0 + 0.05;
      this.speed = speed;
      this.hiddenUntil = hiddenUntil;
      this.hiddenIsDisable = hiddenIsDisable;
      this.noiseParam = noiseParam;
      this.startID = startID;
      this.endID = endID;
      this.mc = Minecraft.func_71410_x();
      this.callback = effect;
      this.callback.onConstruct(this);
   }

   public void setHeading() {
      double dX = this.endX - this.startX;
      double dY = this.endY - this.startY;
      double dZ = this.endZ - this.startZ;
      this.totalDistance = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
      double distanceFactor = this.speed / this.totalDistance;
      Vec3d offsetVector = this.offset();
      this.mX = dX * distanceFactor + offsetVector.field_72450_a;
      this.mY = dY * distanceFactor + offsetVector.field_72448_b;
      this.mZ = dZ * distanceFactor + offsetVector.field_72449_c;
   }

   private Vec3d offset() {
      return ParticleMathHelper.generatePointInSphere(0.5 * this.innaccuracy * 1.2, this.rand);
   }

   private double noise() {
      return this.noiseParam == 0.0 ? 0.0 : (this.rand.nextDouble() - 0.5) / this.noiseParam;
   }

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      if (this.callback == null && particle.getEffect() instanceof IAttackEffect) {
         this.callback = (IAttackEffect)particle.getEffect();
      }

      if (this.callback == null) {
         particle.func_187112_i();
      } else {
         particle.setRGBA(this.r, this.g, this.b, 1.0F);
         if (this.hiddenUntil < 1) {
            particle.setScale(this.scale);
         } else {
            particle.setScale(0.0F);
         }

         particle.func_187114_a(this.maxAge);
         particle.setPos(x, y, z);
         particle.setPrevPos(x, y, z);
         particle.setMotion(this.mX, this.mY, this.mZ);
         this.callback.onInit(particle, this);
      }
   }

   public void update(ParticleArcanery particle) {
      ++this.ticker;
      if (this.ticker > 500) {
         particle.func_187112_i();
      }

      if (this.hiddenUntil > 0 && this.ticker >= this.hiddenUntil) {
         particle.setScale(this.scale);
         if (this.hiddenIsDisable) {
            this.callback.onEnable(particle, this);
         }

         this.hiddenIsDisable = false;
      }

      particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
      if (!this.hiddenIsDisable) {
         particle.func_187110_a(particle.getMotionX(), particle.getMotionY(), particle.getMotionZ());
         particle.setMotion(particle.getMotionX() + this.noise(), particle.getMotionY() + this.noise(), particle.getMotionZ() + this.noise());
         this.travelledDistance += this.speed;
         this.callback.onUpdate(particle, this);
      }

      if (!this.hiddenIsDisable && this.travelledDistance >= this.totalDistance && !this.eol) {
         this.eol = true;
         this.callback.onTarget(particle, this);
      }

      if (this.eol) {
         particle.incrementAge();
         if (particle.getAge() >= particle.getMaxAge()) {
            particle.func_187112_i();
         }

         particle.setScale(Math.max(particle.getScale() * 0.95F, 0.0F));
         this.callback.onUpdateEol(particle, this);
      }

      this.callback.onUpdateLast(particle, this);
   }

   public boolean customRenderer() {
      return this.callback.hasCustomRenderer(this);
   }

   public void preRender(ParticleArcanery particle, float partialTicks) {
      this.callback.onPreRender(particle, partialTicks, this);
   }

   public void render(ParticleArcanery particle, Tessellator tessellator, float partialTicks) {
      this.callback.onRender(particle, tessellator, partialTicks, this);
   }

   public void postRender(ParticleArcanery particle, float partialTicks) {
      this.callback.onPostRender(particle, partialTicks, this);
   }

   public ResourceLocation texture() {
      return this.texture;
   }

   public static enum EnumParticleTexture {
      NORMAL(new ResourceLocation("pixelmon", "textures/particles/attack.png")),
      LEAF(new ResourceLocation("pixelmon", "textures/particles/leaf.png")),
      PETAL(new ResourceLocation("pixelmon", "textures/particles/petal.png")),
      HEART_PURPLE(new ResourceLocation("pixelmon", "textures/particles/heartpurple.png")),
      HEART_RED(new ResourceLocation("pixelmon", "textures/particles/heartred.png")),
      FLAME(new ResourceLocation("pixelmon", "textures/particles/flame.png")),
      BUBBLE(new ResourceLocation("pixelmon", "textures/particles/bubble.png")),
      WATER(new ResourceLocation("pixelmon", "textures/particles/water.png")),
      SPIRAL(new ResourceLocation("pixelmon", "textures/particles/spiral.png")),
      DUST(new ResourceLocation("pixelmon", "textures/particles/dust.png")),
      SOLID(new ResourceLocation("pixelmon", "textures/particles/solid.png")),
      UP_ARROW(new ResourceLocation("pixelmon", "textures/particles/uparrow.png")),
      DOWN_ARROW(new ResourceLocation("pixelmon", "textures/particles/downarrow.png")),
      GOLD_NUGGET(new ResourceLocation("pixelmon", "textures/particles/gold_nugget.png")),
      BLUE_MAGIC(new ResourceLocation("pixelmon", "textures/particles/blue_magic.png")),
      ORB(new ResourceLocation("pixelmon", "textures/particles/orb.png")),
      FEATHER(new ResourceLocation("pixelmon", "textures/particles/feather.png"));

      public final ResourceLocation texture;

      private EnumParticleTexture(ResourceLocation texture) {
         this.texture = texture;
      }
   }
}
