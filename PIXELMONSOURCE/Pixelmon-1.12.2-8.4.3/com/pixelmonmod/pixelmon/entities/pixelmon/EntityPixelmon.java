package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.google.common.base.Optional;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.WorldHelper;
import com.pixelmonmod.pixelmon.api.events.PixelmonUpdateEvent;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.BattleDamageSource;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.blocks.spawning.TileEntityPixelmonSpawner;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.LensInfoPacket;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.InteractEvolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.LevelingEvolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.TickingEvolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.TradeEvolution;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumEggGroup;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPixelmon extends Entity8HoldsItems {
   static final Map dwMap = new HashMap();
   public int legendaryTicks = -1;
   public int despawnCounter = -1;
   public static int TICKSPERSECOND;
   public static int intMinTicksToDespawn;
   public static int intMaxTicksToDespawn;
   private LensInfoPacket info = null;
   public boolean grounded = false;
   private IEnumForm priorForm;
   public static ArrayList interactionList;
   int despawnTick = 0;
   public boolean canMove = true;
   public boolean stopRender = false;
   public TileEntityPixelmonSpawner spawner = null;
   public ArrayList cameraCapturedPlayers = new ArrayList();

   public EntityPixelmon(World world) {
      super(world);
      if (world != null && world.field_72995_K) {
         func_184227_b(PixelmonConfig.renderDistanceWeight);
      }

      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      ((PathNavigateGround)this.func_70661_as()).func_179693_d(false);
   }

   public void resetDataWatchers() {
      this.field_70180_af.func_187227_b(field_184756_bw, Optional.fromNullable(this.pokemon.getOwnerPlayerUUID()));
      this.field_70180_af.func_187227_b(dwSpecies, this.pokemon.getSpecies().getNationalPokedexInteger());
      this.field_70180_af.func_187227_b(dwForm, (byte)this.pokemon.getForm());
      this.field_70180_af.func_187227_b(dwGender, this.pokemon.getGender().getForm());
      this.field_70180_af.func_187227_b(dwGrowth, (byte)this.pokemon.getGrowth().index);
      this.field_70180_af.func_187227_b(dwBossMode, (byte)this.getBossMode().index);
      this.field_70180_af.func_187227_b(dwLevel, this.pokemon.getLevel());
      this.field_70180_af.func_187227_b(dwMaxHP, this.pokemon.getMaxHealth());
      this.field_70180_af.func_187227_b(dwExp, this.pokemon.getExperience());
      this.field_70180_af.func_187227_b(dwScale, 1.0F);
      this.field_70180_af.func_187227_b(dwDynamaxScale, 0.0F);
      this.field_70180_af.func_187227_b(dwShiny, this.pokemon.isShiny());
      this.field_70180_af.func_187227_b(dwCustomTexture, this.pokemon.getCustomTexture());
      this.field_70180_af.func_187227_b(dwNickname, this.pokemon.getNickname() == null ? "" : this.pokemon.getNickname());
      this.field_70180_af.func_187227_b(dwSpawnLocation, this.getSpawnLocation().ordinal());
      this.field_70180_af.func_187227_b(dwUUID, Optional.of(this.pokemon.getUUID()));
      this.field_70180_af.func_187227_b(dwRibbon, this.pokemon.getDisplayedRibbon().ordinal());
   }

   public Map getDataWatcherMap() {
      return dwMap;
   }

   public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
      if (type == EnumCreatureType.WATER_CREATURE && this.getSpawnLocation() == SpawnLocationType.Water) {
         return true;
      } else {
         return type == EnumCreatureType.CREATURE;
      }
   }

   public void func_70645_a(DamageSource cause) {
      if (!this.field_70170_p.field_72995_K) {
         super.func_70645_a(cause);
         if (this.func_70902_q() != null) {
            this.func_70606_j(0.0F);
            this.retrieve();
         } else {
            if (cause.func_76346_g() instanceof EntityPlayerMP && PixelmonConfig.canPokemonBeHit) {
               ArrayList items = DropItemRegistry.getDropsForPokemon(this);
               Iterator var3 = items.iterator();

               while(var3.hasNext()) {
                  ItemStack stack = (ItemStack)var3.next();
                  DropItemHelper.dropItemOnGround(this.func_174791_d(), (EntityPlayerMP)cause.func_76346_g(), stack, false, false);
               }
            }

            this.func_70106_y();
         }
      }

   }

   public boolean func_184645_a(EntityPlayer player, EnumHand hand) {
      if (player instanceof EntityPlayerMP && hand == EnumHand.MAIN_HAND) {
         ItemStack itemstack = player.func_184586_b(hand);
         Iterator var4 = interactionList.iterator();

         IInteraction i;
         do {
            do {
               if (!var4.hasNext()) {
                  return super.func_184645_a(player, hand);
               }

               i = (IInteraction)var4.next();
               if (!itemstack.func_190926_b() && player.func_184811_cZ().func_185141_a(itemstack.func_77973_b())) {
                  return true;
               }
            } while(!i.processOnEmptyHand(this, player, hand, itemstack) && itemstack.func_190926_b());
         } while(!i.processInteract(this, player, hand, itemstack));

         if (!itemstack.func_190926_b()) {
            player.func_184811_cZ().func_185145_a(itemstack.func_77973_b(), 20);
         }

         return true;
      } else {
         return super.func_184645_a(player, hand);
      }
   }

   public Pokemon getStoragePokemonData() {
      return this.field_70170_p.field_72995_K ? ClientStorageManager.party.find(this.func_110124_au()) : this.pokemon;
   }

   public void retrieve() {
      if (this.battleController == null) {
         if (this.priorForm != null) {
            this.setForm(this.priorForm);
            this.priorForm = null;
         } else if (this.getFormEnum().isTemporary()) {
            this.setForm(this.getFormEnum().getDefaultFromTemporary(this.getPokemonData()));
         }
      }

      this.unloadEntity();
   }

   public void releaseFromPokeball() {
      if (this.hasOwner()) {
         this.aggression = EnumAggression.passive;
      }

      this.field_70128_L = false;

      try {
         this.field_70170_p.func_72838_d(this);
         if (this.pokemon != null) {
            this.pokemon.updateDimensionAndEntityID(this.field_71093_bK, this.func_145782_y());
         }
      } catch (IllegalStateException var2) {
      }

   }

   public void clearAttackTarget() {
      this.func_70604_c((EntityLivingBase)null);
      this.func_70624_b((EntityLivingBase)null);
   }

   public void playPixelmonSound() {
      if (this.getBaseStats().hasSoundForGender(this.getPokemonData().getGender())) {
         this.func_70642_aH();
      }

   }

   public void setPriorForm(IEnumForm form) {
      this.priorForm = form;
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
         if (this.getSwimmingParameters() == null) {
            this.getBaseStats().swimmingParameters = EnumSpecies.Magikarp.getBaseStats().swimmingParameters;
         }

         wDepth = WorldHelper.getWaterDepth(this.func_180425_c(), this.field_70170_p);
         if (wDepth > this.getSwimmingParameters().depthRangeStart && wDepth < this.getSwimmingParameters().depthRangeEnd) {
            return true;
         } else {
            double prevPosY = this.field_70163_u;
            this.field_70163_u -= (double)(this.getSwimmingParameters().depthRangeStart + this.field_70146_Z.nextInt(this.getSwimmingParameters().depthRangeEnd - this.getSwimmingParameters().depthRangeStart));
            wDepth = WorldHelper.getWaterDepth(this.func_180425_c(), this.field_70170_p);
            if (wDepth > this.getSwimmingParameters().depthRangeStart && wDepth < this.getSwimmingParameters().depthRangeEnd) {
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

   public void func_110159_bB() {
   }

   public boolean func_70692_ba() {
      return this.legendaryTicks <= 0 && super.func_70692_ba();
   }

   public EntityLivingBase func_70902_q() {
      if (!this.hasOwner()) {
         return null;
      } else {
         UUID trainerUUID = this.pokemon.getOwnerTrainerUUID();
         return (EntityLivingBase)(trainerUUID != null ? (NPCTrainer)CollectionHelper.find(this.field_70170_p.field_72996_f, (e) -> {
            return e.func_110124_au().equals(trainerUUID);
         }) : super.func_70902_q());
      }
   }

   public void setSpawnLocation(SpawnLocationType spawnLocation) {
      super.setSpawnLocation(spawnLocation);
      this.field_70180_af.func_187227_b(dwSpawnLocation, spawnLocation.ordinal());
   }

   public void func_70071_h_() {
      try {
         this.field_70170_p.field_72984_F.func_76320_a("pixelmon");
         if (!this.field_70170_p.field_72995_K && this.pokemon == null) {
            this.func_70106_y();
            return;
         }

         if (Pixelmon.freeze) {
            return;
         }

         if (Pixelmon.EVENT_BUS.post(new PixelmonUpdateEvent(this, Phase.START))) {
            return;
         }

         if (this.field_70163_u < 0.0 && !this.field_70170_p.field_72995_K) {
            if (this.battleController == null) {
               this.func_70106_y();
            } else {
               this.field_70163_u = 0.0;
               this.field_70159_w = this.field_70181_x = this.field_70179_y = 0.0;
            }
         }

         if (this.pokemon.getStorage() instanceof TrainerPartyStorage && (this.battleController == null || this.battleController.battleEnded)) {
            this.func_70106_y();
         }

         if ((this.pokemon.getStorage() != null || this.pokemon.getOriginalTrainerUUID() != null) && this.pokemon.getEntityID() == -1) {
            this.func_70106_y();
         }

         if (this.canDespawn && !this.field_70170_p.field_72995_K) {
            this.updateDespawn();
         }

         if (this.pokemon.getOwnerPlayerUUID() != null && (this.pokemon.getOwnerPlayer() == null || this.pokemon.getOwnerPlayer().field_71093_bK != this.field_71093_bK) || this.pokemon.getOwnerTrainerUUID() != null && this.pokemon.getOwnerTrainer() == null) {
            this.retrieve();
         }

         super.func_70071_h_();
         if (!this.field_70170_p.field_72995_K && this.field_70173_aa % 100 == 1 && !this.isEvolving()) {
            this.testTickingEvolution();
         }

         Pixelmon.EVENT_BUS.post(new PixelmonUpdateEvent(this, Phase.END));
      } catch (Exception var5) {
         Pixelmon.LOGGER.error("Error in ticking Pixelmon entity.");
         var5.printStackTrace();
      } finally {
         this.field_70170_p.field_72984_F.func_76319_b();
      }

   }

   public void updateDespawn() {
      this.despawnTick = (this.despawnTick + 1) % 60;
      if (this.despawnTick == 0) {
         if (this.legendaryTicks > 0 || this.battleController != null && this.battleController.containsParticipantType(PlayerParticipant.class) || this.func_70902_q() != null || this.getBaseStats() == null || this.blockOwner != null) {
            return;
         }

         if (this.playersNearby() && this.despawnCounter != 0) {
            this.despawnCounter = (int)(Math.random() * (double)(intMaxTicksToDespawn - intMinTicksToDespawn) + (double)intMinTicksToDespawn);
         } else {
            if (this.battleController != null) {
               if (this.battleController.playerNumber != 0) {
                  return;
               }

               this.battleController.endBattle(EnumBattleEndCause.FORCE);
            }

            this.func_70106_y();
         }
      }

      this.checkForRarityDespawn();
      if (this.legendaryTicks >= 0 && this.battleController == null) {
         --this.legendaryTicks;
         if (this.legendaryTicks == 0 && this.canDespawn) {
            this.func_70106_y();
         }
      }

   }

   public void func_70106_y() {
      if (this.battleController == null) {
         if (this.priorForm != null) {
            this.setForm(this.priorForm);
            this.priorForm = null;
         } else if (this.getFormEnum().isTemporary()) {
            this.setForm(this.getFormEnum().getDefaultFromTemporary(this.getPokemonData()));
         }
      }

      super.func_70106_y();
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      if (!(source instanceof BattleDamageSource) && source != DamageSource.field_76380_i) {
         if (source.func_76346_g() instanceof EntityPlayerMP && (source.func_76364_f() == null || source.func_76364_f() == source.func_76346_g())) {
            this.onAttackedByPlayer((EntityPlayerMP)source.func_76346_g());
         }

         BaseStats baseStats = this.getPokemonData().getBaseStats();
         boolean waterSpawn = false;
         SpawnLocationType[] var5 = baseStats.spawnLocations;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            SpawnLocationType slt = var5[var7];
            if (slt == SpawnLocationType.Water) {
               waterSpawn = true;
               break;
            }
         }

         if (this.getPokemonData().isPokemon(new EnumSpecies[]{EnumSpecies.Uxie, EnumSpecies.Mesprit, EnumSpecies.Azelf})) {
            waterSpawn = true;
         }

         if (this.getFormEnum() == EnumSpecial.Drowned) {
            waterSpawn = true;
         }

         List typeList = baseStats.getTypeList();
         if ((typeList.contains(EnumType.Fire) || typeList.contains(EnumType.Water) || baseStats.hasEggGroup(EnumEggGroup.Mineral)) && (source == DamageSource.field_76372_a || source == DamageSource.field_76370_b || source == DamageSource.field_190095_e)) {
            return false;
         }

         if (typeList.contains(EnumType.Fire) && source == DamageSource.field_76371_c) {
            return false;
         }

         if (typeList.contains(EnumType.Ghost) && source != DamageSource.field_76376_m) {
            return false;
         }

         if (typeList.contains(EnumType.Electric) && source == DamageSource.field_180137_b) {
            this.func_70691_i(amount);
            return false;
         }

         if ((typeList.contains(EnumType.Water) || baseStats.hasEggGroup(EnumEggGroup.Water1) || baseStats.hasEggGroup(EnumEggGroup.Water2) || baseStats.hasEggGroup(EnumEggGroup.Water3) || waterSpawn) && source == DamageSource.field_76369_e) {
            return false;
         }

         if ((typeList.contains(EnumType.Steel) || baseStats.hasEggGroup(EnumEggGroup.Mineral)) && source == DamageSource.field_76376_m) {
            return false;
         }
      }

      return super.func_70097_a(source, amount);
   }

   private void onAttackedByPlayer(EntityPlayerMP player) {
      if (this.battleController == null && !this.hasOwner() && player.func_184614_ca().func_77973_b() == Items.field_151055_y) {
         int knockback = EnchantmentHelper.func_77506_a(Enchantments.field_180313_o, player.func_184614_ca());
         if (knockback > 0) {
            if (!player.field_71075_bZ.field_75098_d && RandomHelper.getRandomChance(40)) {
               player.func_71121_q().func_184133_a((EntityPlayer)null, player.func_180425_c(), SoundEvents.field_187635_cQ, SoundCategory.PLAYERS, 0.8F, 0.8F + this.field_70170_p.field_73012_v.nextFloat() * 0.4F);
               player.func_184614_ca().func_190918_g(1);
            } else {
               this.field_70159_w = player.func_70040_Z().field_72450_a * (double)knockback;
               this.field_70181_x = 0.3 * (double)knockback;
               this.field_70179_y = player.func_70040_Z().field_72449_c * (double)knockback;
               this.field_70133_I = true;
               player.func_71121_q().func_184133_a((EntityPlayer)null, player.func_180425_c(), SoundEvents.field_187721_dT, SoundCategory.PLAYERS, 1.0F, 1.0F);
               this.func_70604_c(player);
            }
         }
      }

   }

   private void checkForRarityDespawn() {
      if (this.legendaryTicks <= 0 && (this.battleController == null || !this.battleController.containsParticipantType(PlayerParticipant.class)) && this.func_70902_q() == null && this.getBaseStats() != null && this.blockOwner == null) {
         if (this.despawnCounter > 0) {
            --this.despawnCounter;
         } else if (this.despawnCounter == 0) {
            if (!this.playersNearby()) {
               if (this.battleController != null) {
                  this.battleController.endBattle(EnumBattleEndCause.FORCE);
               }

               this.func_70106_y();
            }
         } else {
            this.despawnCounter = (int)(Math.random() * (double)(intMaxTicksToDespawn - intMinTicksToDespawn) + (double)intMinTicksToDespawn);
         }

      }
   }

   private boolean playersNearby() {
      for(int i = 0; i < this.field_70170_p.field_73010_i.size(); ++i) {
         EntityPlayer player = (EntityPlayer)this.field_70170_p.field_73010_i.get(i);
         double distancex = player.field_70165_t - this.field_70165_t;
         double distancey = player.field_70163_u - this.field_70163_u;
         double distancez = player.field_70161_v - this.field_70161_v;
         double distancesquared = distancex * distancex + distancey * distancey + distancez * distancez;
         if (distancesquared < (double)(PixelmonConfig.despawnRadius * PixelmonConfig.despawnRadius)) {
            return true;
         }
      }

      return false;
   }

   public boolean func_70039_c(NBTTagCompound par1nbtTagCompound) {
      return !this.field_70128_L && this.pokemon != null && !this.isRaidPokemon() && this.pokemon.getStorage() == null && this.blockOwner == null && (!this.func_70692_ba() || PixelmonConfig.writeEntitiesToWorld) && super.func_70039_c(par1nbtTagCompound);
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      if (this.getSpawnLocation() == null) {
         this.setSpawnLocation(SpawnLocationType.Land);
      }

      nbt.func_74774_a("pixelmonType", (byte)this.getSpawnLocation().ordinal());
      if (this.legendaryTicks > 0) {
         nbt.func_74768_a("legendaryTicks", this.legendaryTicks);
         nbt.func_74772_a("legendaryTime", this.field_70170_p.func_82737_E());
      }

      nbt.func_74774_a("BossMode", (byte)this.getBossMode().ordinal());
   }

   public void func_70037_a(NBTTagCompound nbt) {
      try {
         super.func_70037_a(nbt);
         float h = this.func_110143_aJ();
         this.func_70606_j(h);
         if (nbt.func_74764_b("pixelmonType")) {
            this.setSpawnLocation(SpawnLocationType.getFromIndex(nbt.func_74771_c("pixelmonType")));
         } else if (this.getBaseStats().spawnLocations[0] == SpawnLocationType.Land) {
            this.setSpawnLocation(SpawnLocationType.Land);
         } else {
            this.setSpawnLocation(SpawnLocationType.Water);
         }

         this.resetAI();
         if (nbt.func_74764_b("legendaryTicks")) {
            this.legendaryTicks = nbt.func_74762_e("legendaryTicks");
            long lastTime = nbt.func_74763_f("legendaryTime");
            this.legendaryTicks -= (int)(this.field_70170_p.func_82737_E() - lastTime);
            if (this.legendaryTicks <= 0) {
               this.func_70106_y();
            }
         }

         if (nbt.func_74764_b("BossMode")) {
            this.setBoss(EnumBossMode.getMode(nbt.func_74765_d("BossMode")));
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void unloadEntity() {
      super.unloadEntity();
      this.field_70170_p.func_72900_e(this);
      this.clearAttackTarget();
      if (this.pokemon != null) {
         this.pokemon.updateDimensionAndEntityID(-1, -1);
      }

   }

   public void startEvolution(Evolution evolution, int form) {
      if (!(this.func_110143_aJ() < 1.0F)) {
         Pixelmon.storageManager.getParty((EntityPlayerMP)this.func_70902_q()).guiOpened = true;
         new EvolutionQuery(this, evolution, form);
      }
   }

   public boolean isLoaded() {
      return this.isLoaded(false);
   }

   public boolean isLoaded(boolean checkChunk) {
      boolean isLoaded = true;
      if (checkChunk) {
         isLoaded = this.field_70170_p.func_175697_a(this.func_180425_c(), 1);
      }

      if (isLoaded) {
         isLoaded = this.field_70170_p.func_73045_a(this.func_145782_y()) != null;
      }

      return isLoaded;
   }

   public boolean isRaidPokemon() {
      return this.getPixelmonWrapper() != null && this.getPixelmonWrapper().isRaidPokemon();
   }

   public boolean testTradeEvolution(EnumSpecies with) {
      if (this.getPokemonData().getHeldItemAsItemHeld().getHeldItemType() == EnumHeldItems.everStone) {
         return false;
      } else {
         ArrayList tradeEvolutions = this.getPokemonData().getEvolutions(TradeEvolution.class);
         Iterator var3 = tradeEvolutions.iterator();

         TradeEvolution evo;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            evo = (TradeEvolution)var3.next();
         } while(!evo.canEvolve(this, with));

         return evo.doEvolution(this);
      }
   }

   public boolean testLevelEvolution(int level) {
      if (this.getPokemonData().getHeldItemAsItemHeld().getHeldItemType() == EnumHeldItems.everStone) {
         return false;
      } else {
         ArrayList levelingEvolutions = this.getPokemonData().getEvolutions(LevelingEvolution.class);
         Iterator var3 = levelingEvolutions.iterator();

         LevelingEvolution evo;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            evo = (LevelingEvolution)var3.next();
         } while(!evo.canEvolve(this, level));

         return evo.doEvolution(this);
      }
   }

   public boolean testInteractEvolution(ItemStack stack) {
      if (this.getPokemonData().getHeldItemAsItemHeld().getHeldItemType() == EnumHeldItems.everStone) {
         return false;
      } else {
         ArrayList interactingEvolutions = this.getPokemonData().getEvolutions(InteractEvolution.class);
         Iterator var3 = interactingEvolutions.iterator();

         InteractEvolution evo;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            evo = (InteractEvolution)var3.next();
         } while(!evo.canEvolve(this, stack));

         return evo.doEvolution(this) && !evo.emptyHand;
      }
   }

   public boolean testTickingEvolution() {
      if (this.getPokemonData().getHeldItemAsItemHeld().getHeldItemType() == EnumHeldItems.everStone) {
         return false;
      } else {
         ArrayList tickingEvolutions = this.getPokemonData().getEvolutions(TickingEvolution.class);
         Iterator var2 = tickingEvolutions.iterator();

         TickingEvolution evo;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            evo = (TickingEvolution)var2.next();
         } while(!evo.canEvolve(this));

         return evo.doEvolution(this);
      }
   }

   public void setSpawnerParent(TileEntityPixelmonSpawner spawner) {
      this.spawner = spawner;
   }

   public boolean func_145770_h(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
      double d3;
      double d4;
      double d5;
      if (!this.field_70122_E && this.field_70163_u > 64.0) {
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

   public double getYCentre() {
      return this.field_70163_u + (this.func_174813_aQ().field_72337_e - this.func_174813_aQ().field_72338_b) / 2.0;
   }

   @SideOnly(Side.CLIENT)
   public void setClientOnlyInfo(LensInfoPacket info) {
      this.info = info;
   }

   @SideOnly(Side.CLIENT)
   public LensInfoPacket getClientOnlyInfo() {
      return this.info;
   }

   public void exposeInfo(EntityPlayerMP player) {
      Pixelmon.network.sendTo(new LensInfoPacket(this), player);
   }

   public void hideInfo(EntityPlayerMP player) {
      Pixelmon.network.sendTo(new LensInfoPacket(this, true), player);
   }

   public static boolean displacePokemonIfShouldered(EntityPlayerMP player, UUID uuid) {
      NBTTagCompound nbt;
      if (player.func_192023_dk() != null) {
         nbt = player.func_192023_dk();
         if (!nbt.func_82582_d() && nbt.func_186857_a("UUID").equals(uuid)) {
            player.func_192029_h(new NBTTagCompound());
            return true;
         }
      }

      if (player.func_192025_dl() != null) {
         nbt = player.func_192025_dl();
         if (!nbt.func_82582_d() && nbt.func_186857_a("UUID").equals(uuid)) {
            player.func_192031_i(new NBTTagCompound());
            return true;
         }
      }

      return false;
   }

   public SoundEvent getFlyingSound() {
      return null;
   }

   static {
      dwMap.put("pokemon", dwSpecies);
      dwMap.put("form", dwForm);
      dwMap.put("gender", dwGender);
      dwMap.put("growth", dwGrowth);
      dwMap.put("bossMode", dwBossMode);
      dwMap.put("level", dwLevel);
      dwMap.put("exp", dwExp);
      dwMap.put("shiny", dwShiny);
      dwMap.put("customTexture", dwCustomTexture);
      dwMap.put("nickname", dwNickname);
      dwMap.put("owner", EntityTameable.field_184756_bw);
      dwMap.put("health", ReflectionHelper.getPrivateValue(EntityLivingBase.class, (Object)null, new String[]{"HEALTH", "field_184632_c"}));
      dwMap.put("uuid", dwUUID);
      dwMap.put("ribbon", dwRibbon);
      TICKSPERSECOND = 20;
      intMinTicksToDespawn = 15 * TICKSPERSECOND;
      intMaxTicksToDespawn = 180 * TICKSPERSECOND;
      interactionList = new ArrayList();
   }
}
