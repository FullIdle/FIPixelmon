package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.RidePokemonEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.AIHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.custom.PixelmonInteraction;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class Entity4Interactions extends Entity3HasStats {
   static final DataParameter dwNumInteractions;
   public int ridingPlayerVertical;
   public boolean isFlying = false;
   protected int initAir = 0;
   protected boolean ridingInitialised = false;
   protected AIHelper aiHelper;
   public int aggressionTimer = 0;
   private PixelmonInteraction interaction;
   private int numInteractions = 0;
   public float mountBoost = 1.0F;
   public int field_70773_bE = 0;
   public String skillId;
   public int targetX;
   public int targetY;
   public int targetZ;
   public EnumFacing targetSide;

   public Entity4Interactions(World par1World) {
      super(par1World);
      this.field_70180_af.func_187214_a(dwNumInteractions, (byte)0);
   }

   public AIHelper getAIHelper() {
      if (this.aiHelper == null) {
         this.resetAI();
      }

      return this.aiHelper;
   }

   public void resetAI() {
      this.aiHelper = new AIHelper();
      this.field_70699_by = this.aiHelper.createNavigator(this);
      this.aiHelper.populateTasks(this, this.field_70714_bg);
   }

   public boolean func_184645_a(EntityPlayer player, EnumHand hand) {
      if (player.field_70170_p.field_72995_K) {
         return super.func_184645_a(player, hand);
      } else if (hand == EnumHand.OFF_HAND) {
         return false;
      } else if (this.interaction != null && this.getNumInteractions() > 0 && this.interaction.processInteract(this, player, hand, player.func_184586_b(hand))) {
         return false;
      } else if (hand == EnumHand.MAIN_HAND) {
         if (!player.func_184218_aH() && PixelmonConfig.allowRiding && this.getBaseStats().IsRideable() && this.belongsTo(player)) {
            if (!PixelmonConfig.landMount && !this.canFly() && !this.canSurf()) {
               return super.func_184645_a(player, hand);
            } else {
               boolean allowMount = !Pixelmon.EVENT_BUS.post(new RidePokemonEvent((EntityPlayerMP)player, (EntityPixelmon)this));
               if (!allowMount) {
                  return super.func_184645_a(player, hand);
               } else {
                  player.func_184220_m(this);
                  return true;
               }
            }
         } else {
            return super.func_184645_a(player, hand);
         }
      } else {
         return super.func_184645_a(player, hand);
      }
   }

   public boolean canFly() {
      if (!PixelmonConfig.requireHM) {
         return this.getBaseStats().canFly();
      } else {
         Moveset moves = this.getPokemonData().getMoveset();
         return this.getBaseStats().canFly() && moves.hasAttack("Fly");
      }
   }

   public boolean canSurf() {
      if (!PixelmonConfig.requireHM) {
         return this.getBaseStats().canSurf();
      } else {
         Moveset moves = this.getPokemonData().getMoveset();
         return this.getBaseStats().canSurf() && moves.hasAttack("Surf");
      }
   }

   public void onSendout() {
      if (!this.field_70170_p.field_72995_K && this.interaction == null) {
         this.interaction = PixelmonInteraction.getInteraction(this.getSpecies());
         if (this.interaction != null) {
            this.setNumInteractions(this.pokemon.getPersistentData().func_74771_c("NumInteractions"));
            this.interaction.counter = this.pokemon.getPersistentData().func_74765_d("InteractionCount");
            if (this.interaction.counter == -1 && this.interaction.maxInteractions != this.getNumInteractions()) {
               this.interaction.resetCounter(this);
            }
         }
      }

   }

   public double func_70042_X() {
      return (double)this.field_70131_O * 0.9;
   }

   private boolean isFlying() {
      return this instanceof Entity6Moves && ((Entity6Moves)this).getIsFlying();
   }

   public void func_70636_d() {
      if (!this.isFlying()) {
         if (this.field_70716_bi > 0 && !this.func_184186_bw()) {
            double d0 = this.field_70165_t + (this.field_184623_bh - this.field_70165_t) / (double)this.field_70716_bi;
            double d1 = this.field_70163_u + (this.field_184624_bi - this.field_70163_u) / (double)this.field_70716_bi;
            double d2 = this.field_70161_v + (this.field_184625_bj - this.field_70161_v) / (double)this.field_70716_bi;
            double d3 = MathHelper.func_76138_g(this.field_184626_bk - (double)this.field_70177_z);
            this.field_70177_z = (float)((double)this.field_70177_z + d3 / (double)this.field_70716_bi);
            this.field_70125_A = (float)((double)this.field_70125_A + (this.field_70709_bj - (double)this.field_70125_A) / (double)this.field_70716_bi);
            --this.field_70716_bi;
            this.func_70107_b(d0, d1, d2);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
         } else if (!this.func_70613_aW()) {
            this.field_70159_w *= 0.98;
            this.field_70181_x *= 0.98;
            this.field_70179_y *= 0.98;
         }

         if (Math.abs(this.field_70159_w) < 0.003) {
            this.field_70159_w = 0.0;
         }

         if (Math.abs(this.field_70181_x) < 0.003) {
            this.field_70181_x = 0.0;
         }

         if (Math.abs(this.field_70179_y) < 0.003) {
            this.field_70179_y = 0.0;
         }

         this.field_70170_p.field_72984_F.func_76320_a("ai");
      }

      if (this.func_70610_aX()) {
         this.field_70703_bu = false;
         this.field_70702_br = 0.0F;
         this.field_191988_bg = 0.0F;
         this.field_70704_bt = 0.0F;
      } else if (this.func_70613_aW()) {
         this.field_70170_p.field_72984_F.func_76320_a("newAi");
         this.updateEntityActionStateAlt();
         this.field_70170_p.field_72984_F.func_76319_b();
      }

      this.field_70170_p.field_72984_F.func_76319_b();
      this.field_70170_p.field_72984_F.func_76320_a("jump");
      if (!this.isFlying()) {
         if (this.field_70703_bu) {
            if (this.func_70090_H()) {
               this.func_70629_bd();
            } else if (this.func_180799_ab()) {
               this.func_180466_bG();
            } else if (this.field_70122_E && this.field_70773_bE == 0) {
               this.func_70664_aZ();
               this.field_70773_bE = 10;
            }
         }

         this.field_70170_p.field_72984_F.func_76319_b();
         this.field_70170_p.field_72984_F.func_76320_a("travel");
         this.field_70702_br *= 0.98F;
         this.field_191988_bg *= 0.98F;
         this.field_70704_bt *= 0.9F;
         this.func_191986_a(this.field_70702_br, this.field_70701_bs, this.field_191988_bg);
      }

      this.field_70170_p.field_72984_F.func_76319_b();
      this.field_70170_p.field_72984_F.func_76320_a("push");
      this.func_85033_bc();
      this.field_70170_p.field_72984_F.func_76319_b();
   }

   protected void updateEntityActionStateAlt() {
      ++this.field_70708_bq;
      this.func_70623_bb();
      if (!Double.isNaN(this.field_70165_t) && !Double.isNaN(this.field_70163_u) && !Double.isNaN(this.field_70161_v)) {
         this.func_70635_at().func_75523_a();
         this.field_70715_bh.func_75774_a();
         this.field_70714_bg.func_75774_a();
         this.field_70699_by.func_75501_e();
         this.func_70619_bc();
         if (this.func_184218_aH() && this.func_184187_bx() instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)this.func_184187_bx();
            entityliving.func_70661_as().func_75484_a(this.func_70661_as().func_75505_d(), 1.5);
            entityliving.func_70605_aq().func_188487_a(this.func_70605_aq());
         }

         this.field_70765_h.func_75641_c();
         if (!this.isFlying()) {
            this.func_70671_ap().func_75649_a();
            this.field_70767_i.func_75661_b();
         }

      }
   }

   public void func_70106_y() {
      if (this.interaction != null && !this.field_70170_p.field_72995_K) {
         this.pokemon.getPersistentData().func_74774_a("NumInteractions", (byte)this.numInteractions);
         this.pokemon.getPersistentData().func_74777_a("InteractionCount", (short)this.interaction.counter);
      }

      super.func_70106_y();
   }

   public void func_70624_b(EntityLivingBase entity) {
      this.skillId = null;
      super.func_70624_b(entity);
   }

   public void setAttackTarget(EntityLivingBase entity, String skillId) {
      this.func_70624_b(entity);
      this.skillId = skillId;
   }

   public void setBlockTarget(int x, int y, int z, EnumFacing side, String skillId) {
      this.targetX = x;
      this.targetY = y;
      this.targetZ = z;
      this.targetSide = side;
      this.skillId = skillId;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.interaction != null) {
         if (this.interaction.counter > 0) {
            --this.interaction.counter;
         }

         if (this.interaction.counter == 0) {
            this.setNumInteractions(Math.min(this.interaction.maxInteractions, this.getNumInteractions() + 1));
            if (this.interaction.maxInteractions > this.getNumInteractions()) {
               this.interaction.resetCounter(this);
            } else {
               --this.interaction.counter;
            }
         }
      }

      if (this.func_184179_bs() != null && !this.ridingInitialised) {
         this.field_70714_bg.field_75782_a.clear();
         this.initAir = this.func_184179_bs().func_70086_ai();
         this.ridingInitialised = true;
      } else if (this.func_184179_bs() == null && this.ridingInitialised) {
         this.ridingInitialised = false;
         this.resetAI();
      }

      if (!this.field_70170_p.field_72995_K && this.aiHelper == null && this.getBaseStats() != null) {
         this.resetAI();
      }

      if (this.aggressionTimer > 0) {
         --this.aggressionTimer;
      }

   }

   public int getNumInteractions() {
      return this.numInteractions;
   }

   public void setNumInteractions(int newValue) {
      this.field_70180_af.func_187227_b(EntityPixelmon.dwNumInteractions, (byte)Math.max(0, newValue));
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      if (this.interaction != null && !this.field_70170_p.field_72995_K) {
         this.pokemon.getPersistentData().func_74774_a("NumInteractions", (byte)this.numInteractions);
         this.pokemon.getPersistentData().func_74777_a("InteractionCount", (short)this.interaction.counter);
      }

   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      if (!this.field_70170_p.field_72995_K && this.interaction != null) {
         this.setNumInteractions(this.pokemon.getPersistentData().func_74771_c("NumInteractions"));
         this.interaction.counter = this.pokemon.getPersistentData().func_74765_d("InteractionCount");
      }

   }

   public void func_184206_a(DataParameter key) {
      super.func_184206_a(key);
      if (key.func_187155_a() == dwNumInteractions.func_187155_a()) {
         this.numInteractions = (Byte)this.field_70180_af.func_187225_a(dwNumInteractions);
      }

   }

   protected boolean getUsingRidingSpecialConditions() {
      if (!(Math.abs(this.field_70159_w) > 0.01) && !(Math.abs(this.field_70179_y) > 0.01)) {
         return false;
      } else if (this.getBaseStats().getSpecies() == EnumSpecies.Gyarados) {
         return this.func_70090_H();
      } else {
         return this.getBaseStats().getSpecies() == EnumSpecies.Onix;
      }
   }

   public boolean shouldDismountInWater(Entity rider) {
      return this.canFly() && !this.canSurf();
   }

   public void unloadEntity() {
      if (this.func_184179_bs() != null) {
         this.func_184179_bs().func_184210_p();
         this.resetAI();
      }

   }

   public Entity func_184179_bs() {
      return this.func_184188_bt().isEmpty() ? null : (Entity)this.func_184188_bt().get(0);
   }

   static {
      dwNumInteractions = EntityDataManager.func_187226_a(Entity4Interactions.class, DataSerializers.field_187191_a);
   }
}
