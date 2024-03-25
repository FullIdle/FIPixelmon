package com.pixelmonmod.pixelmon.entities.pokeballs;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.PokeballEffectEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityPokeBall extends EntityThrowable {
   public static final DataParameter dwPokeballType;
   public static final DataParameter dwIsWaiting;
   public static final DataParameter dwIsOnGround;
   public static final DataParameter dwInitialYaw;
   public static final DataParameter dwAnimation;
   public static final DataParameter dwId;
   public static final DataParameter dwInitialPitch;
   public static final DataParameter dwMode;
   protected static final DataParameter dwOwner;
   protected static final DataParameter dwPokeUUID;
   protected static final DataParameter dwSlot;
   public static final DataParameter dwCritical;
   protected final int ticksPerShake = 25;
   public int waitTimer;
   public static float scale;
   protected EntityLivingBase field_70192_c;
   public EntityPixelmon pixelmon;
   protected float endRotationYaw = 0.0F;
   public boolean dropItem;
   protected boolean canCatch = false;
   public float openAngle = 0.0F;
   String name = null;
   Vec3d initPos;
   Vec3d diff;
   float initialScale;
   public IncrementingVariable inc;
   public boolean firstContact = true;
   public boolean pausing = false;
   public AnimationType lastAnim = null;
   public int shakeCount = 0;
   private boolean startCapture = true;
   private static final AnimationType[] ANIMS;
   private boolean capture1 = false;
   private boolean capture2 = false;
   int numShakes = 0;
   public static final ArrayList majorStatuses;
   public static final ArrayList minorStatuses;
   ModelPokeballs model = null;

   public EntityPokeBall(World world) {
      super(world);
      this.field_70180_af.func_187214_a(dwPokeballType, EnumPokeballs.PokeBall.getIndex());
      this.field_70180_af.func_187214_a(dwIsWaiting, false);
      this.field_70180_af.func_187214_a(dwIsOnGround, false);
      this.field_70180_af.func_187214_a(dwInitialYaw, 0.0F);
      this.field_70180_af.func_187214_a(dwInitialPitch, 0.0F);
      this.field_70180_af.func_187214_a(dwAnimation, AnimationType.IDLE.ordinal());
      this.field_70180_af.func_187214_a(dwId, 0);
      this.field_70180_af.func_187214_a(dwMode, 0);
      this.field_70180_af.func_187214_a(dwOwner, Optional.absent());
      this.field_70180_af.func_187214_a(dwPokeUUID, Optional.absent());
      this.field_70180_af.func_187214_a(dwSlot, -1);
      this.field_70180_af.func_187214_a(dwCritical, false);
   }

   public EntityPokeBall(EnumPokeballs type, World world, EntityLivingBase thrower, EnumPokeBallMode mode) {
      super(world, thrower);
      this.field_70180_af.func_187214_a(dwPokeballType, type.getIndex());
      this.field_70180_af.func_187214_a(dwIsWaiting, false);
      this.field_70180_af.func_187214_a(dwIsOnGround, false);
      this.field_70180_af.func_187214_a(dwInitialYaw, 0.0F);
      this.field_70180_af.func_187214_a(dwInitialPitch, 0.0F);
      this.field_70180_af.func_187214_a(dwAnimation, AnimationType.IDLE.ordinal());
      this.field_70180_af.func_187214_a(dwId, 0);
      this.field_70180_af.func_187214_a(dwMode, mode.ordinal());
      this.field_70180_af.func_187214_a(dwOwner, Optional.absent());
      this.field_70180_af.func_187214_a(dwPokeUUID, Optional.absent());
      this.field_70180_af.func_187214_a(dwSlot, -1);
      this.field_70180_af.func_187214_a(dwCritical, false);
   }

   public String func_70005_c_() {
      if (this.name == null) {
         this.name = PixelmonItemsPokeballs.getItemFromEnum(this.getType()).getLocalizedName();
      }

      return this.name;
   }

   protected void func_70184_a(RayTraceResult movingobjectposition) {
   }

   public Item breakBall() {
      return PixelmonItemsPokeballs.getLidFromEnum(this.getType());
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.field_70170_p.field_72995_K && this.model != null) {
         if (this.inc == null) {
            this.inc = new IncrementingVariable(1.0F, 2.14748365E9F);
         }

         this.inc.tick();
         this.model.doAnimation(this);
      }

      if (this.getMode() == EnumPokeBallMode.full && this.getIsWaiting()) {
         this.field_70177_z = -1.0F * this.getInitialYaw();
         if (this.waitTimer > 0) {
            --this.waitTimer;
         }

         if (this.firstContact) {
            this.field_70181_x = 0.3;
            this.firstContact = false;
         } else if (this.field_70181_x <= 0.0 && !this.pausing) {
            this.waitTimer = 2;
            this.pausing = true;
         }

         if (this.waitTimer > 0) {
            this.field_70181_x = 0.0;
         } else if (this.pausing && this.waitTimer == 0) {
            if (this.getAnimation() != AnimationType.BOUNCECLOSED) {
               this.field_70181_x = 0.2;
               this.field_70159_w = 0.2 * (double)((float)Math.cos((double)this.getInitialYaw() * Math.PI / 180.0 - 1.5707963267948966));
               this.field_70179_y = 0.2 * (double)((float)Math.sin((double)this.getInitialYaw() * Math.PI / 180.0 - 1.5707963267948966));
               this.setAnimation(AnimationType.BOUNCECLOSED);
               if (this.field_70170_p.field_72995_K) {
                  this.field_70170_p.func_184134_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, PixelSounds.pokeballClose, SoundCategory.NEUTRAL, 0.1F, 1.0F, true);
                  this.field_70170_p.func_184134_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, PixelSounds.pokeballRelease, SoundCategory.NEUTRAL, 0.2F, 1.0F, true);
               }
            }

            if (!this.field_70170_p.field_72995_K) {
               PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)this.field_70192_c);
               Pokemon pokemon = storage.get((Byte)this.field_70180_af.func_187225_a(dwSlot));
               if (pokemon != null && pokemon.getPixelmonIfExists() == null && !pokemon.isEgg()) {
                  EntityPixelmon pixelmon = pokemon.getOrSpawnPixelmon(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, 0.0F);
                  pixelmon.onSendout();
               }
            }

            --this.waitTimer;
         }
      }

      if ((this.getMode() == EnumPokeBallMode.empty || this.getMode() == EnumPokeBallMode.battle && !this.getIsOnGround()) && this.getIsWaiting()) {
         if (this.waitTimer > 0) {
            --this.waitTimer;
         }

         if (this.firstContact) {
            this.field_70181_x = 0.3;
            this.firstContact = false;
         } else if (this.field_70181_x <= 0.0 && !this.pausing) {
            this.waitTimer = 25;
            this.pausing = true;
            if (!this.field_70170_p.field_72995_K) {
               this.initialScale = this.pixelmon.getPixelmonScale();
               this.initPos = new Vec3d(this.pixelmon.field_70165_t, this.pixelmon.field_70163_u, this.pixelmon.field_70161_v);
               Vec3d current = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
               current.func_178788_d(this.initPos);
               this.diff = current;
            }
         }

         if (this.waitTimer > 0) {
            this.field_70181_x = 0.0;
            if (this.waitTimer < 20 && !this.field_70170_p.field_72995_K) {
               if (this.startCapture) {
                  this.field_70170_p.func_184134_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, PixelSounds.pokeballCapture, SoundCategory.NEUTRAL, 0.2F, 1.0F, true);
                  this.startCapture = false;
               }

               this.pixelmon.setPixelmonScale(this.initialScale * (float)Math.pow(0.5, (double)((20 - this.waitTimer) / 5)));
               this.moveCloser((float)Math.pow(0.5, (double)((20 - this.waitTimer) / 5)));
            }

            if (this.waitTimer == 1 && !this.field_70170_p.field_72995_K) {
               this.pixelmon.unloadEntity();
               this.setAnimation(AnimationType.BOUNCECLOSED);
               this.field_70170_p.func_184134_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, PixelSounds.pokeballClose, SoundCategory.NEUTRAL, 0.1F, 1.0F, true);
            }
         }
      }

      if (!this.getIsWaiting() && !this.getIsOnGround()) {
         this.field_70177_z += 50.0F;
      } else if (this.getIsOnGround()) {
         this.field_70181_x = 0.0;
         if (this.getMode() == EnumPokeBallMode.empty || this.getMode() == EnumPokeBallMode.battle && this.field_70170_p.field_72995_K) {
            if (this.waitTimer > 0) {
               --this.waitTimer;
            }

            if (this.waitTimer <= 0 && this.shakeCount < Math.abs(this.getId())) {
               ++this.shakeCount;
               this.chooseAnimation();
               if (this.inc != null) {
                  this.inc.value = 0.0F;
               }

               this.getClass();
               this.waitTimer = 25;
            }
         }
      }

   }

   public boolean func_70039_c(NBTTagCompound compound) {
      return false;
   }

   private void chooseAnimation() {
      this.setAnimation(ANIMS[RandomHelper.getRandomNumberBetween(0, ANIMS.length - 1)]);
   }

   protected void moveCloser(float percent) {
      Vec3d newVec = this.initPos.func_72441_c(this.diff.field_72450_a * (double)(1.0F - percent), this.diff.field_72448_b * (double)(1.0F - percent), this.diff.field_72449_c * (double)(1.0F - percent));
      this.pixelmon.field_70165_t = newVec.field_72450_a;
      this.pixelmon.field_70142_S = newVec.field_72450_a;
      this.pixelmon.field_70163_u = newVec.field_72448_b;
      this.pixelmon.field_70137_T = newVec.field_72448_b;
      this.pixelmon.field_70161_v = newVec.field_72449_c;
      this.pixelmon.field_70136_U = newVec.field_72449_c;
   }

   public void func_70030_z() {
      if (!this.field_70170_p.field_72995_K && this.field_70163_u < 0.0) {
         this.func_174812_G();
      } else {
         if (this.getIsOnGround()) {
            this.field_70181_x = 0.0;
            this.field_70159_w = 0.0;
            this.field_70179_y = 0.0;
         }

      }
   }

   public void premierFlash() {
      if (this.getType() == EnumPokeballs.PremierBall) {
         this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t + 0.1, this.field_70163_u + 0.05, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
      }

   }

   public void onCaptureAttemptEffect() {
      if (!Pixelmon.EVENT_BUS.post(new PokeballEffectEvent.StartCaptureEffect(this))) {
         if (!this.capture2 && this.pausing) {
            if (this.getType() == EnumPokeballs.PremierBall) {
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t, this.field_70163_u + 0.4, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t, this.field_70163_u - 0.4, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t + 0.2, this.field_70163_u + 0.2, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t - 0.2, this.field_70163_u + 0.2, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t + 0.2, this.field_70163_u - 0.2, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t - 0.2, this.field_70163_u - 0.2, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
            }

            if (this.getType() == EnumPokeballs.MasterBall) {
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t, this.field_70163_u + 0.4, this.field_70161_v, 148.0, 0.0, 211.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t, this.field_70163_u - 0.4, this.field_70161_v, 148.0, 0.0, 211.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t + 0.2, this.field_70163_u + 0.2, this.field_70161_v, 148.0, 0.0, 211.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t - 0.2, this.field_70163_u + 0.2, this.field_70161_v, 148.0, 0.0, 211.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t + 0.2, this.field_70163_u - 0.2, this.field_70161_v, 148.0, 0.0, 211.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t - 0.2, this.field_70163_u - 0.2, this.field_70161_v, 148.0, 0.0, 211.0, new int[0]);
            }

            this.capture2 = true;
         }

      }
   }

   public void successfulCaptureEffect() {
      if (!Pixelmon.EVENT_BUS.post(new PokeballEffectEvent.SuccessfullCaptureEffect(this))) {
         if (!this.capture1) {
            this.field_70170_p.func_184134_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, PixelSounds.pokeballCaptureSuccess, SoundCategory.NEUTRAL, 0.07F, 1.0F, false);
            if (this.getType() == EnumPokeballs.PremierBall) {
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t, this.field_70163_u + 0.8, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t + 0.5, this.field_70163_u + 0.6, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t - 0.5, this.field_70163_u + 0.6, this.field_70161_v, 255.0, 250.0, 250.0, new int[0]);
            } else {
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t, this.field_70163_u + 0.8, this.field_70161_v, 255.0, 255.0, 0.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t + 0.5, this.field_70163_u + 0.6, this.field_70161_v, 255.0, 255.0, 0.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.REDSTONE, this.field_70165_t - 0.5, this.field_70163_u + 0.6, this.field_70161_v, 255.0, 255.0, 0.0, new int[0]);
            }

            this.capture1 = true;
         }

      }
   }

   public void releaseEffect() {
      if (!Pixelmon.EVENT_BUS.post(new PokeballEffectEvent.SentOutEffect(this))) {
         if (!this.capture1) {
            if (this.getType() == EnumPokeballs.PremierBall) {
               this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t, this.field_70163_u + 0.4, this.field_70161_v, 0.0, 0.0, 0.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t, this.field_70163_u - 0.4, this.field_70161_v, 0.0, 0.0, 0.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t + 0.2, this.field_70163_u + 0.2, this.field_70161_v, 0.0, 0.0, 0.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t - 0.2, this.field_70163_u + 0.2, this.field_70161_v, 0.0, 0.0, 0.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t + 0.2, this.field_70163_u - 0.2, this.field_70161_v, 0.0, 0.0, 0.0, new int[0]);
               this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t - 0.2, this.field_70163_u - 0.2, this.field_70161_v, 0.0, 0.0, 0.0, new int[0]);
            }

            this.capture1 = true;
         }

      }
   }

   protected void doCaptureCalc(EntityPixelmon p2) {
      int catchRate = PokeballTypeHelper.modifyCaptureRate(this.getType(), p2.getPokemonData(), p2.getBaseStats().getCatchRate());
      double ballBonus = PokeballTypeHelper.getBallBonus(this.getType(), this.field_70192_c, p2.getPokemonData(), this.getMode());
      CaptureEvent.StartCapture event = new CaptureEvent.StartCapture((EntityPlayerMP)this.func_85052_h(), p2, this, catchRate, ballBonus);
      if (!Pixelmon.EVENT_BUS.post(event) && p2.getBossMode() == EnumBossMode.NotBoss && p2.func_70902_q() == null && p2.getPokemonData().getLevel() <= PixelmonServerConfig.maxLevel && !p2.getPokemonData().getBonusStats().preventsCapture()) {
         if (this.getType() != EnumPokeballs.MasterBall && this.getType() != EnumPokeballs.ParkBall) {
            int passedShakes = 0;
            catchRate = event.getCatchRate();
            ballBonus = event.getBallBonus();
            if (catchRate > 0) {
               float hpMax = p2.func_110138_aP();
               float hpCurrent = p2.func_110143_aJ();
               PixelmonWrapper pw = p2.getPixelmonWrapper();
               if (pw != null) {
                  hpCurrent = (float)pw.getHealth();
               }

               double bonusStatus = 1.0;
               StatusType currentStatus = pw == null ? p2.getPokemonData().getStatus().type : pw.getPrimaryStatus().type;
               if (majorStatuses.contains(currentStatus)) {
                  bonusStatus = 2.5;
               } else if (minorStatuses.contains(currentStatus)) {
                  bonusStatus = 1.5;
               }

               double a = (double)((3.0F * hpMax - 2.0F * hpCurrent) * (float)catchRate) * ballBonus / (double)(3.0F * hpMax) * bonusStatus;
               double b = (double)Math.round(Math.pow(255.0 / a, 0.25) * 4096.0) / 4096.0;
               b = Math.floor(65536.0 / b);
               if (this.func_85052_h() instanceof EntityPlayerMP) {
                  EntityPlayerMP player = (EntityPlayerMP)this.func_85052_h();
                  PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
                  int caught = pps.pokedex.countCaught();
                  double am;
                  if (caught <= 30) {
                     am = 0.0;
                  } else if (caught <= 150) {
                     am = a * 0.5;
                  } else if (caught <= 300) {
                     am = a;
                  } else if (caught <= 450) {
                     am = a * 1.5;
                  } else if (caught <= 600) {
                     am = a * 2.0;
                  } else {
                     am = a * 2.5;
                  }

                  if (pps.getCatchingCharm().isActive()) {
                     am *= PixelmonConfig.catchingCharmMultiplier;
                  }

                  int c = (int)Math.floor(am / 6.0);
                  if (RandomHelper.rand.nextInt(256) < c) {
                     this.setCritical(true);
                  }
               }

               if (b != 0.0) {
                  if (!(a < 255.0)) {
                     this.canCatch = true;
                     this.numShakes = 4;
                     return;
                  }

                  for(int i = 0; i < 4; ++i) {
                     int roll = RandomHelper.rand.nextInt(65536);
                     if ((double)roll <= b) {
                        ++passedShakes;
                        if (this.getCritical()) {
                           this.canCatch = true;
                           this.numShakes = 1;
                           return;
                        }
                     }
                  }
               }
            }

            this.canCatch = passedShakes == 4;
            this.numShakes = passedShakes;
         } else {
            this.canCatch = true;
            this.numShakes = 4;
         }
      } else {
         this.canCatch = false;
         this.numShakes = 0;
      }
   }

   public EnumPokeballs getType() {
      return EnumPokeballs.getFromIndex((Integer)this.field_70180_af.func_187225_a(dwPokeballType));
   }

   protected void setIsWaiting(boolean value) {
      this.field_70180_af.func_187227_b(dwIsWaiting, value);
   }

   public boolean getIsWaiting() {
      return (Boolean)this.field_70180_af.func_187225_a(dwIsWaiting);
   }

   protected void setIsOnGround(boolean value) {
      this.field_70180_af.func_187227_b(dwIsOnGround, value);
   }

   protected boolean getIsOnGround() {
      return (Boolean)this.field_70180_af.func_187225_a(dwIsOnGround);
   }

   public void setAnimation(AnimationType animation) {
      this.field_70180_af.func_187227_b(dwAnimation, animation.ordinal());
   }

   public AnimationType getAnimation() {
      return AnimationType.values()[(Integer)this.field_70180_af.func_187225_a(dwAnimation)];
   }

   public int getId() {
      return (Integer)this.field_70180_af.func_187225_a(dwId);
   }

   public void setId(int id) {
      this.field_70180_af.func_187227_b(dwId, id);
   }

   public float getInitialYaw() {
      return (Float)this.field_70180_af.func_187225_a(dwInitialYaw);
   }

   public void setInitialYaw(float yaw) {
      this.field_70180_af.func_187227_b(dwInitialYaw, yaw);
   }

   public float getInitialPitch() {
      return (Float)this.field_70180_af.func_187225_a(dwInitialPitch);
   }

   public void setInitialPitch(float pitch) {
      this.field_70180_af.func_187227_b(dwInitialPitch, pitch);
   }

   protected void setCritical(boolean value) {
      this.field_70180_af.func_187227_b(dwCritical, value);
   }

   public boolean getCritical() {
      return (Boolean)this.field_70180_af.func_187225_a(dwCritical);
   }

   public EnumPokeBallMode getMode() {
      return EnumPokeBallMode.getFromOrdinal((Integer)this.field_70180_af.func_187225_a(dwMode));
   }

   public ModelPokeballs getModel() {
      if (this.model == null) {
         this.model = this.getType().getModel();
      }

      return this.model;
   }

   public UUID getOwnerId() {
      return (UUID)((Optional)this.field_70180_af.func_187225_a(dwOwner)).orNull();
   }

   public void setOwnerId(UUID ownerUuid) {
      this.field_70180_af.func_187227_b(dwOwner, Optional.of(ownerUuid));
   }

   public UUID getPokeUUID() {
      return (UUID)((Optional)this.field_70180_af.func_187225_a(dwPokeUUID)).orNull();
   }

   public void setPokeUUID(UUID pokeUUID) {
      this.field_70180_af.func_187227_b(dwPokeUUID, Optional.fromNullable(pokeUUID));
   }

   static {
      dwPokeballType = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187192_b);
      dwIsWaiting = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187198_h);
      dwIsOnGround = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187198_h);
      dwInitialYaw = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187193_c);
      dwAnimation = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187192_b);
      dwId = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187192_b);
      dwInitialPitch = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187193_c);
      dwMode = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187192_b);
      dwOwner = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187203_m);
      dwPokeUUID = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187203_m);
      dwSlot = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187191_a);
      dwCritical = EntityDataManager.func_187226_a(EntityPokeBall.class, DataSerializers.field_187198_h);
      scale = 0.0033333334F;
      ANIMS = new AnimationType[]{AnimationType.SHAKELEFT, AnimationType.SHAKERIGHT};
      majorStatuses = Lists.newArrayList(new StatusType[]{StatusType.Freeze, StatusType.Sleep});
      minorStatuses = Lists.newArrayList(new StatusType[]{StatusType.Paralysis, StatusType.Poison, StatusType.PoisonBadly, StatusType.Burn});
   }
}
