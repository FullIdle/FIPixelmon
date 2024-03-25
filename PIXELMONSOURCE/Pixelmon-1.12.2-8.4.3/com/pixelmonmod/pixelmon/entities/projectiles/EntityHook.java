package com.pixelmonmod.pixelmon.entities.projectiles;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.FishingEvent;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.items.SpawnActionItem;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.api.spawning.conditions.TriggerLocation;
import com.pixelmonmod.pixelmon.api.spawning.util.SpatialData;
import com.pixelmonmod.pixelmon.api.world.MutableLocation;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.enums.items.EnumRodType;
import com.pixelmonmod.pixelmon.items.ItemFishingRod;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHook extends EntityFishHook implements IEntityAdditionalSpawnData {
   public static final DataParameter DATA_HOOK_STATE;
   public EnumRodType rodType;
   public SpawnAction caught = null;
   public int ticksTillCatch = 0;
   public int ticksTillEscape = 0;
   public float chanceOfNothing = 0.0F;
   public HookState state;
   public int dotsShowing;
   public int momentsShowingDots;
   public int rodQuality;

   public EntityHook(World world) {
      super(world, (EntityPlayer)null);
      this.state = EntityHook.HookState.FLYING;
      this.dotsShowing = 0;
      this.momentsShowingDots = 0;
      this.rodQuality = 0;
   }

   @SideOnly(Side.CLIENT)
   public EntityHook(World world, EntityPlayer player, double x, double y, double z) {
      super(world, player, x, y, z);
      this.state = EntityHook.HookState.FLYING;
      this.dotsShowing = 0;
      this.momentsShowingDots = 0;
      this.rodQuality = 0;
   }

   public EntityHook(World world, EntityPlayer player, EnumRodType rodType, int rodQuality) {
      super(world, player);
      this.state = EntityHook.HookState.FLYING;
      this.dotsShowing = 0;
      this.momentsShowingDots = 0;
      this.rodQuality = 0;
      this.rodType = rodType;
      this.rodQuality = rodQuality;
   }

   protected void func_70088_a() {
      this.field_70178_ae = true;
      this.func_184212_Q().func_187214_a(DATA_HOOK_STATE, -1);
      this.func_184212_Q().func_187214_a(field_184528_c, 0);
   }

   public void func_190626_a(EntityPlayer player) {
      this.func_70105_a(0.25F, 0.25F);
      this.field_70158_ak = true;
      if (player != null) {
         this.field_146042_b = player;
         this.field_146042_b.field_71104_cf = this;
      }

   }

   public void func_190620_n() {
      if (this.field_146042_b != null) {
         super.func_190620_n();
      }

   }

   public void writeSpawnData(ByteBuf buffer) {
      UUID uuid = this.field_146042_b != null ? this.field_146042_b.func_110124_au() : new UUID(0L, 0L);
      buffer.writeLong(uuid.getMostSignificantBits());
      buffer.writeLong(uuid.getLeastSignificantBits());
   }

   public void readSpawnData(ByteBuf additionalData) {
      long most = additionalData.readLong();
      long least = additionalData.readLong();
      UUID uuid = new UUID(most, least);
      this.setAngler(this.field_70170_p.func_152378_a(uuid));
   }

   public void setAngler(Entity entity) {
      if (entity instanceof EntityPlayer) {
         this.field_146042_b = (EntityPlayer)entity;
         this.field_146042_b.field_71104_cf = this;
         this.func_190620_n();
      }

   }

   public void func_70071_h_() {
      if (this.field_146042_b != null && this.field_146042_b.field_71104_cf == this) {
         if (this.field_70170_p.field_72995_K) {
            ++this.momentsShowingDots;
            if (this.momentsShowingDots > 40) {
               this.momentsShowingDots = 0;
               this.dotsShowing = (this.dotsShowing + 1) % 3;
            }
         }

         --this.ticksTillCatch;
         --this.ticksTillEscape;
         if (this.ticksTillEscape == 0 && this.caught != null) {
            this.caught = null;
            if (this.field_146042_b != null) {
               ChatHandler.sendFormattedChat(this.field_146042_b, TextFormatting.GRAY, "pixelmon.projectiles.gotaway");
               if (!this.field_70170_p.field_72995_K) {
                  this.func_70106_y();
               }
            }
         }

         this.func_70030_z();
         if (this.field_70170_p.field_72995_K || !this.func_190625_o()) {
            if (this.field_146051_au) {
               ++this.field_146049_av;
               if (this.field_146049_av >= 1200) {
                  this.func_70106_y();
                  return;
               }
            }

            float f = 0.0F;
            BlockPos blockpos = new BlockPos(this);
            IBlockState blockState = this.field_70170_p.func_180495_p(blockpos);
            if (BetterSpawnerConfig.getWaterBlocks().contains(blockState.func_177230_c()) || BetterSpawnerConfig.getLavaBlocks().contains(blockState.func_177230_c())) {
               f = BlockLiquid.func_190973_f(blockState, this.field_70170_p, blockpos);
            }

            if (this.state == EntityHook.HookState.FLYING) {
               if (this.field_146043_c != null) {
                  this.field_70159_w = 0.0;
                  this.field_70181_x = 0.0;
                  this.field_70179_y = 0.0;
                  this.state = EntityHook.HookState.HOOKED_IN_ENTITY;
                  return;
               }

               if (f > 0.0F) {
                  this.field_70159_w *= 0.3;
                  this.field_70181_x *= 0.2;
                  this.field_70179_y *= 0.3;
                  this.state = EntityHook.HookState.BOBBING;
                  if (!this.field_70170_p.field_72995_K) {
                     this.setNewTimeToCatch();
                  }

                  return;
               }

               if (!this.field_70170_p.field_72995_K) {
                  this.checkCollision();
               }

               if (!this.field_146051_au && !this.field_70122_E && !this.field_70123_F) {
                  ++this.field_146047_aw;
               } else {
                  this.field_146047_aw = 0;
                  this.field_70159_w = 0.0;
                  this.field_70181_x = 0.0;
                  this.field_70179_y = 0.0;
               }
            } else {
               double d0;
               if (this.state == EntityHook.HookState.HOOKED_IN_ENTITY) {
                  if (this.field_146043_c != null) {
                     if (this.field_146043_c.field_70128_L) {
                        this.field_146043_c = null;
                        this.state = EntityHook.HookState.FLYING;
                     } else {
                        this.field_70165_t = this.field_146043_c.field_70165_t;
                        d0 = (double)this.field_146043_c.field_70131_O;
                        this.field_70163_u = this.field_146043_c.func_174813_aQ().field_72338_b + d0 * 0.8;
                        this.field_70161_v = this.field_146043_c.field_70161_v;
                        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
                     }
                  }

                  return;
               }

               if (this.state == EntityHook.HookState.BOBBING) {
                  this.field_70159_w *= 0.9;
                  this.field_70179_y *= 0.9;
                  d0 = this.field_70163_u + this.field_70181_x - (double)blockpos.func_177956_o() - (double)f;
                  if (Math.abs(d0) < 0.01) {
                     d0 += Math.signum(d0) * 0.1;
                  }

                  if (!this.field_70170_p.field_72995_K && this.ticksTillCatch == 0 && this.field_146042_b != null && this.field_146042_b.func_184102_h() != null) {
                     if (f > 0.0F && this.field_146043_c == null) {
                        this.field_70181_x -= (double)Math.abs(this.field_70146_Z.nextFloat()) * 0.2;
                        if (PixelmonSpawning.fishingSpawner == null) {
                           return;
                        }

                        SpawnLocation spawnLocation = this.createSpawnLocation();
                        SpawnAction caught = PixelmonSpawning.fishingSpawner.getAction(spawnLocation, 1.0F - this.chanceOfNothing);
                        if (spawnLocation.diameter == 0) {
                           caught = null;
                        }

                        int displayedMarks = true;
                        if (caught != null) {
                           this.caught = caught;
                           float nominalRarity = caught.spawnInfo.calculateNominalRarity();
                           byte displayedMarks;
                           if (nominalRarity < 5.0F) {
                              displayedMarks = 3;
                           } else if (nominalRarity < 50.0F) {
                              displayedMarks = 2;
                           } else {
                              displayedMarks = 1;
                           }

                           int ticksTillEscape = 30;
                           FishingEvent.Catch catchEvent = new FishingEvent.Catch((EntityPlayerMP)this.field_146042_b, this, caught, ticksTillEscape, displayedMarks);
                           if (Pixelmon.EVENT_BUS.post(catchEvent)) {
                              caught = null;
                           } else {
                              this.func_184212_Q().func_187227_b(DATA_HOOK_STATE, catchEvent.getDisplayedMarks());
                              this.ticksTillEscape = catchEvent.getTicksTillEscape();
                           }
                        }

                        if (caught == null) {
                           ChatHandler.sendFormattedChat(this.field_146042_b, TextFormatting.GRAY, "pixelmon.projectiles.nibble");
                           this.func_70106_y();
                        }
                     }
                  } else {
                     this.field_70181_x -= d0 * (double)this.field_70146_Z.nextFloat() * 0.2;
                  }
               }
            }

            if (blockState.func_185904_a() != Material.field_151586_h && blockState.func_185904_a() != Material.field_151587_i) {
               this.field_70181_x -= 0.03;
            }

            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.updateRotation();
            this.field_70159_w *= 0.92;
            this.field_70181_x *= 0.92;
            this.field_70179_y *= 0.92;
            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
         }

      } else {
         this.func_70106_y();
      }
   }

   public SpawnLocation createSpawnLocation() {
      BlockPos pos = new BlockPos(this);
      IBlockState state = this.field_70170_p.func_180495_p(pos);
      TriggerLocation loc = BetterSpawnerConfig.getWaterBlocks().contains(state.func_177230_c()) ? this.rodType.locationInWater : (BetterSpawnerConfig.getLavaBlocks().contains(state.func_177230_c()) ? this.rodType.locationInLava : null);
      TriggerLocation qualityLoc = null;
      switch (this.rodQuality) {
         case 0:
            qualityLoc = LocationType.OK_ROD_QUALITY;
            break;
         case 1:
            qualityLoc = LocationType.SO_SO_ROD_QUALITY;
            break;
         case 2:
            qualityLoc = LocationType.GOOD_ROD_QUALITY;
            break;
         case 3:
            qualityLoc = LocationType.GREAT_ROD_QUALITY;
            break;
         case 4:
            qualityLoc = LocationType.RARE_ROD_QUALITY;
            break;
         case 5:
            qualityLoc = LocationType.PRO_ROD_QUALITY;
            break;
         case 6:
            qualityLoc = LocationType.SUPREME_ROD_QUALITY;
      }

      if (this.rodType == EnumRodType.OasRod) {
         qualityLoc = LocationType.SUPREME_ROD_QUALITY;
      }

      SpatialData data = PixelmonSpawning.fishingSpawner.calculateSpatialData(this.field_70170_p, this.func_180425_c(), 10, true, (block) -> {
         if (BetterSpawnerConfig.getWaterBlocks().contains(block)) {
            return true;
         } else if (BetterSpawnerConfig.getLavaBlocks().contains(block)) {
            return true;
         } else {
            return BetterSpawnerConfig.getAirBlocks().contains(block);
         }
      });
      boolean canSeeSky = true;
      BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(0, 0, 0);

      for(int y = pos.func_177956_o() + 1; y < 256; ++y) {
         if (!BetterSpawnerConfig.doesBlockSeeSky(this.field_70170_p.func_180495_p(mutablePos.func_181079_c(pos.func_177958_n(), y, pos.func_177952_p())))) {
            canSeeSky = false;
            break;
         }
      }

      HashSet sets = Sets.newHashSet(new LocationType[]{loc});
      if (qualityLoc != null) {
         sets.add(qualityLoc);
      }

      return new SpawnLocation(this.field_146042_b, new MutableLocation(this.field_70170_p, pos), sets, data.baseBlock, data.uniqueSurroundingBlocks, this.field_70170_p.func_180494_b(pos), canSeeSky, data.radius, this.field_70170_p.func_175699_k(pos.func_177984_a()));
   }

   public void setNewTimeToCatch() {
      this.caught = null;
      this.ticksTillCatch = 20 * RandomHelper.getRandomNumberBetween(5, 15);
      FishingEvent.Cast castEvent = new FishingEvent.Cast((EntityPlayerMP)this.field_146042_b, this, this.ticksTillCatch, this.chanceOfNothing);
      Pixelmon.EVENT_BUS.post(castEvent);
      this.ticksTillCatch = castEvent.getTicksUntilCatch();
      this.chanceOfNothing = castEvent.getChanceOfNothing();
      this.func_184212_Q().func_187227_b(DATA_HOOK_STATE, 0);
   }

   public boolean func_190625_o() {
      ItemStack itemstack = this.field_146042_b.func_184614_ca();
      ItemStack itemstack1 = this.field_146042_b.func_184592_cb();
      boolean flag = itemstack.func_77973_b() instanceof ItemFishingRod && ((ItemFishingRod)itemstack.func_77973_b()).getRodType() == this.rodType;
      boolean flag1 = itemstack1.func_77973_b() instanceof ItemFishingRod && ((ItemFishingRod)itemstack1.func_77973_b()).getRodType() == this.rodType;
      if (!this.field_146042_b.field_70128_L && this.field_146042_b.func_70089_S() && (flag || flag1) && this.func_70068_e(this.field_146042_b) <= 1024.0) {
         return false;
      } else {
         this.func_70106_y();
         return true;
      }
   }

   private void updateRotation() {
      float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      this.field_70177_z = (float)(MathHelper.func_181159_b(this.field_70159_w, this.field_70179_y) * 57.29577951308232);

      for(this.field_70125_A = (float)(MathHelper.func_181159_b(this.field_70181_x, (double)f) * 57.29577951308232); this.field_70125_A - this.field_70127_C < -180.0F; this.field_70127_C -= 360.0F) {
      }

      while(this.field_70125_A - this.field_70127_C >= 180.0F) {
         this.field_70127_C += 360.0F;
      }

      while(this.field_70177_z - this.field_70126_B < -180.0F) {
         this.field_70126_B -= 360.0F;
      }

      while(this.field_70177_z - this.field_70126_B >= 180.0F) {
         this.field_70126_B += 360.0F;
      }

      this.field_70125_A = this.field_70127_C + (this.field_70125_A - this.field_70127_C) * 0.2F;
      this.field_70177_z = this.field_70126_B + (this.field_70177_z - this.field_70126_B) * 0.2F;
   }

   private void checkCollision() {
      Vec3d vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
      Vec3d vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
      RayTraceResult raytraceresult = this.field_70170_p.func_147447_a(vec3d, vec3d1, false, true, false);
      vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
      vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
      if (raytraceresult != null) {
         vec3d1 = new Vec3d(raytraceresult.field_72307_f.field_72450_a, raytraceresult.field_72307_f.field_72448_b, raytraceresult.field_72307_f.field_72449_c);
      }

      Entity entity = null;
      List list = this.field_70170_p.func_72839_b(this, this.func_174813_aQ().func_72321_a(this.field_70159_w, this.field_70181_x, this.field_70179_y).func_186662_g(1.0));
      double d0 = 0.0;
      Iterator var8 = list.iterator();

      while(true) {
         Entity entity1;
         double d1;
         do {
            RayTraceResult raytraceresult1;
            do {
               do {
                  do {
                     if (!var8.hasNext()) {
                        if (entity != null) {
                           raytraceresult = new RayTraceResult(entity);
                        }

                        if (raytraceresult != null && raytraceresult.field_72313_a != Type.MISS) {
                           if (raytraceresult.field_72313_a == Type.ENTITY) {
                              this.field_146043_c = raytraceresult.field_72308_g;
                              this.func_184212_Q().func_187227_b(field_184528_c, this.field_146043_c.func_145782_y() + 1);
                           } else {
                              this.field_146051_au = true;
                              this.field_146045_ax = 0;
                              this.ticksTillEscape = 0;
                              this.caught = null;
                              this.state = EntityHook.HookState.FLYING;
                              this.func_184212_Q().func_187227_b(DATA_HOOK_STATE, -1);
                           }
                        }

                        return;
                     }

                     entity1 = (Entity)var8.next();
                  } while(!this.func_189739_a(entity1));
               } while(entity1 == this.field_146042_b && this.field_146047_aw < 5);

               AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_186662_g(0.30000001192092896);
               raytraceresult1 = axisalignedbb.func_72327_a(vec3d, vec3d1);
            } while(raytraceresult1 == null);

            d1 = vec3d.func_72436_e(raytraceresult1.field_72307_f);
         } while(!(d1 < d0) && d0 != 0.0);

         entity = entity1;
         d0 = d1;
      }
   }

   public int func_146034_e() {
      if (this.field_70170_p.field_72995_K) {
         return 0;
      } else {
         byte b0 = 0;
         if (this.field_146043_c != null) {
            this.func_184527_k();
            this.field_70170_p.func_72960_a(this, (byte)31);
            b0 = 3;
            this.func_70106_y();
            return b0;
         } else {
            Entity entity = this.caught == null ? null : this.caught.doSpawn(PixelmonSpawning.fishingSpawner);
            FishingEvent.Reel reelEvent = new FishingEvent.Reel((EntityPlayerMP)this.field_146042_b, this, entity);
            Pixelmon.EVENT_BUS.post(reelEvent);
            if (this.caught != null) {
               if (entity == null) {
                  return b0;
               }

               if (this.caught instanceof SpawnActionPokemon) {
                  EntityPixelmon pokemon = (EntityPixelmon)((SpawnActionPokemon)this.caught).getOrCreateEntity();
                  pokemon.setSpawnLocation(SpawnLocationType.Water);
                  pokemon.resetAI();
                  PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)this.field_146042_b);
                  if (storage.countAblePokemon() > 0) {
                     WildPixelmonParticipant wpp = new WildPixelmonParticipant(new EntityPixelmon[]{pokemon});
                     PlayerParticipant pp = new PlayerParticipant((EntityPlayerMP)this.field_146042_b, new EntityPixelmon[]{storage.getAndSendOutFirstAblePokemon(this.field_146042_b)});
                     BattleRegistry.startBattle(pp, wpp);
                  }
               } else if (this.caught instanceof SpawnActionItem) {
                  EntityItem caughtItem = (EntityItem)entity;
                  ItemStack stack = caughtItem.func_92059_d();
                  caughtItem.func_70106_y();
                  ChatHandler.sendFormattedChat(this.field_146042_b, TextFormatting.GREEN, "pixelmon.projectiles.fisheditem", stack.func_190916_E(), new TextComponentTranslation(stack.func_77973_b().func_77667_c(stack) + ".name", new Object[0]));
                  DropItemHelper.giveItemStack((EntityPlayerMP)this.field_146042_b, stack, false);
               }
            }

            if (this.field_146051_au) {
               b0 = 2;
            }

            this.func_70106_y();
            this.field_146042_b.field_71104_cf = null;
            return b0;
         }
      }
   }

   public boolean func_70039_c(NBTTagCompound compound) {
      return false;
   }

   static {
      DATA_HOOK_STATE = EntityDataManager.func_187226_a(EntityHook.class, DataSerializers.field_187192_b);
   }

   public static enum HookState {
      BOBBING,
      FLYING,
      HOOKED_IN_ENTITY;
   }
}
