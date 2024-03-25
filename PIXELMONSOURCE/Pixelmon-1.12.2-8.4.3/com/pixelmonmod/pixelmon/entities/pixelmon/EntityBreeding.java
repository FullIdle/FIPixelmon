package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.google.common.base.Optional;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.WorldHelper;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.custom.PixelmonInteraction;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.ShearableStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.tickHandlers.CastformTickHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.tickHandlers.ShearableTickHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.tickHandlers.TickHandlerBase;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumBreedingParticles;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityBreeding extends Entity2Client {
   public static final DataParameter dwNumBreedingLevels;
   static final DataParameter dwNumInteractions;
   static final Map dwMap;
   private PixelmonInteraction interaction;
   private int numInteractions = 0;
   private TickHandlerBase tickHandler;

   public EntityBreeding(World world) {
      super(world);
      this.field_70180_af.func_187214_a(dwNumBreedingLevels, -1);
      this.field_70180_af.func_187214_a(dwNumInteractions, (byte)0);
   }

   public void resetDataWatchers() {
      this.field_70180_af.func_187227_b(field_184756_bw, Optional.fromNullable(this.pokemon.getOwnerPlayerUUID()));
      this.field_70180_af.func_187227_b(dwSpecies, this.pokemon.getSpecies().getNationalPokedexInteger());
      this.field_70180_af.func_187227_b(dwForm, (byte)this.pokemon.getForm());
      this.field_70180_af.func_187227_b(dwGender, this.pokemon.getGender().getForm());
      this.field_70180_af.func_187227_b(dwGrowth, (byte)this.pokemon.getGrowth().index);
      this.field_70180_af.func_187227_b(dwLevel, this.pokemon.getLevel());
      this.field_70180_af.func_187227_b(dwMaxHP, this.pokemon.getMaxHealth());
      this.field_70180_af.func_187227_b(dwExp, this.pokemon.getExperience());
      this.field_70180_af.func_187227_b(dwScale, 1.0F);
      this.field_70180_af.func_187227_b(dwDynamaxScale, 0.0F);
      this.field_70180_af.func_187227_b(dwShiny, this.pokemon.isShiny());
      this.field_70180_af.func_187227_b(dwCustomTexture, this.pokemon.getCustomTexture());
      this.field_70180_af.func_187227_b(dwNickname, this.pokemon.getNickname() == null ? "" : this.pokemon.getNickname());
      this.field_70180_af.func_187227_b(dwSpawnLocation, -1);
      this.field_70180_af.func_187227_b(dwUUID, Optional.of(this.pokemon.getUUID()));
      this.field_70180_af.func_187227_b(dwRibbon, this.pokemon.getDisplayedRibbon().ordinal());
   }

   public Map getDataWatcherMap() {
      return dwMap;
   }

   public int getNumBreedingLevels() {
      return MathHelper.func_76125_a((Byte)this.field_70180_af.func_187225_a(dwNumBreedingLevels), 0, PixelmonConfig.numBreedingLevels);
   }

   public void setRanchBlockOwner(TileEntityRanchBlock tileEntityRanchBlock) {
      this.blockOwner = tileEntityRanchBlock;
      this.pokemon.updateDimensionAndEntityID(this.field_71093_bK, this.func_145782_y());
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

   public int getNumInteractions() {
      return this.numInteractions;
   }

   public void setNumInteractions(int newValue) {
      this.field_70180_af.func_187227_b(dwNumInteractions, (byte)Math.max(0, newValue));
   }

   public void func_70106_y() {
      super.func_70106_y();
      this.pokemon.updateDimensionAndEntityID(-1, -1);
      if (this.interaction != null && !this.field_70170_p.field_72995_K) {
         this.pokemon.getPersistentData().func_74774_a("NumInteractions", (byte)this.numInteractions);
         this.pokemon.getPersistentData().func_74777_a("InteractionCount", (short)this.interaction.counter);
      }

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

      if (this.field_70170_p.field_72995_K && this.getNumBreedingLevels() > 0) {
         int breedingLevel = this.getNumBreedingLevels();
         if (this.field_70173_aa % 32 <= 0 && RandomHelper.getRandomChance()) {
            ClientProxy.spawnParticle(EnumBreedingParticles.getFromIndex(breedingLevel), this.field_70170_p, this.field_70165_t, this.field_70163_u + (double)(this.field_70131_O * this.getPixelmonScale() * this.getScaleFactor()), this.field_70161_v, this.pokemon.isShiny());
         }
      }

      if (this.getSpecies() == EnumSpecies.Castform && !(this.tickHandler instanceof CastformTickHandler)) {
         this.tickHandler = new CastformTickHandler(this);
      } else if (ExtraStats.getExtraStats(this.getSpecies()) instanceof ShearableStats && !(this.tickHandler instanceof ShearableTickHandler)) {
         this.tickHandler = new ShearableTickHandler(this);
      } else {
         this.tickHandler = null;
      }

      if (this.tickHandler != null) {
         this.tickHandler.tick(this.field_70170_p);
      }

   }

   public boolean func_184645_a(EntityPlayer player, EnumHand hand) {
      if (player.field_70170_p.field_72995_K) {
         return true;
      } else if (this.interaction != null && this.getNumInteractions() > 0 && this.interaction.processInteract(this, player, hand, player.func_184586_b(hand))) {
         return true;
      } else {
         if (player instanceof EntityPlayerMP && this.func_70902_q() == player) {
            if (hand == EnumHand.OFF_HAND) {
               return super.func_184645_a(player, hand);
            }

            if (this.blockOwner == null) {
               return super.func_184645_a(player, hand);
            }

            if (this.blockOwner instanceof TileEntityRanchBlock) {
               TileEntityRanchBlock ranch = (TileEntityRanchBlock)this.blockOwner;
               return ranch.onActivate(player, this, hand) || super.func_184645_a(player, hand);
            }
         }

         return super.func_184645_a(player, hand);
      }
   }

   public boolean func_70601_bi() {
      AxisAlignedBB aabb = this.func_174813_aQ();
      BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

      int wDepth;
      for(wDepth = (int)Math.floor(aabb.field_72340_a); (double)wDepth < Math.ceil(aabb.field_72336_d); ++wDepth) {
         for(int y = (int)Math.floor(aabb.field_72338_b); (double)y < Math.ceil(aabb.field_72337_e); ++y) {
            for(int z = (int)Math.floor(aabb.field_72339_c); (double)z < Math.ceil(aabb.field_72334_f); ++z) {
               if (this.field_70170_p.func_180495_p(pos.func_181079_c(wDepth, y, z)).func_185904_a().func_76220_a()) {
                  return false;
               }
            }
         }
      }

      if (this.getSpawnLocation() == SpawnLocationType.Water) {
         if (this.getBaseStats().swimmingParameters == null) {
            this.getBaseStats().swimmingParameters = EnumSpecies.Magikarp.getBaseStats().swimmingParameters;
         }

         wDepth = WorldHelper.getWaterDepth(this.func_180425_c(), this.field_70170_p);
         if (wDepth > this.getBaseStats().swimmingParameters.depthRangeStart && wDepth < this.getBaseStats().swimmingParameters.depthRangeEnd) {
            return true;
         } else {
            double prevPosY = this.field_70163_u;
            this.field_70163_u -= (double)(this.getBaseStats().swimmingParameters.depthRangeStart + this.field_70146_Z.nextInt(this.getBaseStats().swimmingParameters.depthRangeEnd - this.getBaseStats().swimmingParameters.depthRangeStart));
            wDepth = WorldHelper.getWaterDepth(this.func_180425_c(), this.field_70170_p);
            if (wDepth > this.getBaseStats().swimmingParameters.depthRangeStart && wDepth < this.getBaseStats().swimmingParameters.depthRangeEnd) {
               this.field_70163_u = prevPosY;
               return false;
            } else {
               return true;
            }
         }
      } else {
         return true;
      }
   }

   public EnumBossMode getBossMode() {
      return EnumBossMode.NotBoss;
   }

   public void setBoss(EnumBossMode mode) {
   }

   public void setSpawnLocation(SpawnLocationType spawnLocation) {
      super.setSpawnLocation(spawnLocation);
      this.field_70180_af.func_187227_b(dwSpawnLocation, spawnLocation.ordinal());
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      return false;
   }

   public boolean func_70692_ba() {
      return this.pokemon != null && this.pokemon.getOwnerPlayer() == null || super.func_70692_ba();
   }

   public boolean func_70039_c(NBTTagCompound compound) {
      return false;
   }

   public void func_70014_b(NBTTagCompound nbt) {
      if (Pixelmon.devEnvironment) {
         nbt.func_74774_a("DebugBreedingLevels", (Byte)this.func_184212_Q().func_187225_a(dwNumBreedingLevels));
      }

      super.func_70014_b(nbt);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
   }

   public void func_184206_a(DataParameter key) {
      super.func_184206_a(key);
      if (key.func_187155_a() == dwNumInteractions.func_187155_a()) {
         this.numInteractions = (Byte)this.field_70180_af.func_187225_a(dwNumInteractions);
      }

   }

   static {
      dwNumBreedingLevels = EntityDataManager.func_187226_a(EntityBreeding.class, DataSerializers.field_187191_a);
      dwNumInteractions = EntityDataManager.func_187226_a(EntityBreeding.class, DataSerializers.field_187191_a);
      dwMap = new HashMap();
      dwMap.put("pokemon", dwSpecies);
      dwMap.put("form", dwForm);
      dwMap.put("gender", dwGender);
      dwMap.put("growth", dwGrowth);
      dwMap.put("level", dwLevel);
      dwMap.put("exp", dwExp);
      dwMap.put("shiny", dwShiny);
      dwMap.put("customTexture", dwCustomTexture);
      dwMap.put("nickname", dwNickname);
      dwMap.put("owner", EntityTameable.field_184756_bw);
      dwMap.put("health", ReflectionHelper.getPrivateValue(EntityLivingBase.class, (Object)null, new String[]{"HEALTH", "field_184632_c"}));
      dwMap.put("uuid", dwUUID);
      dwMap.put("ribbon", dwRibbon);
   }
}
