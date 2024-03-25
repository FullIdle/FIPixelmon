package com.pixelmonmod.pixelmon.entities.pixelmon.stats.links;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.LevelUp;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WrapperLink extends PokemonLink {
   private PixelmonWrapper pixelmon;
   private Pokemon innerLink;

   public WrapperLink(PixelmonWrapper pixelmon) {
      this.pixelmon = pixelmon;
      this.innerLink = pixelmon.getInnerLink();
   }

   public Pokemon getPokemon() {
      return this.innerLink;
   }

   public Stats getStats() {
      return this.pixelmon.getStats();
   }

   public ItemHeld getHeldItem() {
      return this.pixelmon.getHeldItem();
   }

   public void setHeldItem(ItemStack item) {
      this.pixelmon.setHeldItem(item);
   }

   public int getHealth() {
      return this.pixelmon.getHealth();
   }

   public int getMaxHealth() {
      return this.getMaxHealth(false);
   }

   public int getMaxHealth(boolean ignoreDynamax) {
      return this.pixelmon.getMaxHealth(ignoreDynamax);
   }

   public void setHealth(int health) {
      int currentHealth = this.getHealth();
      if (health > currentHealth) {
         this.pixelmon.healEntityBy(health - currentHealth);
      } else if (health < currentHealth) {
         this.pixelmon.doBattleDamage(this.pixelmon, (float)(currentHealth - health), DamageTypeEnum.SELF);
      }

   }

   public void setHealthDirect(int health) {
      this.pixelmon.setHealth(health);
   }

   public int getLevel() {
      return this.pixelmon.getLevelNum();
   }

   public void setLevel(int level) {
      this.pixelmon.setLevelNum(level);
   }

   public int getExp() {
      return this.pixelmon.getExp();
   }

   public void setExp(int experience) {
      this.pixelmon.setExp(experience);
   }

   public int getFriendship() {
      return this.pixelmon.getFriendship();
   }

   public boolean doesLevel() {
      return this.pixelmon.doesLevel();
   }

   public EntityPlayerMP getPlayerOwner() {
      return this.pixelmon.getPlayerOwner();
   }

   public String getRealNickname() {
      return this.pixelmon.getNickname();
   }

   public BattleControllerBase getBattleController() {
      return this.pixelmon.bc;
   }

   public Moveset getMoveset() {
      return this.pixelmon.getMoveset();
   }

   public UUID getPokemonUUID() {
      return this.pixelmon.getPokemonUUID();
   }

   public EntityPixelmon getEntity() {
      if (this.pixelmon.entity != null) {
         return this.pixelmon.entity;
      } else {
         BattleParticipant participant = this.pixelmon.getParticipant();
         return participant.getStorage().find(this.getPokemonUUID()).getOrSpawnPixelmon(participant.getEntity());
      }
   }

   public void setScale(float scale) {
   }

   public World getWorld() {
      return this.pixelmon.getParticipant().getWorld();
   }

   public Gender getGender() {
      return this.pixelmon.getGender();
   }

   public BlockPos getPos() {
      EntityLivingBase entity = this.pixelmon.entity;
      if (this.pixelmon.entity == null) {
         entity = this.pixelmon.getParticipant().getEntity();
      }

      return ((EntityLivingBase)entity).func_180425_c();
   }

   public PartyStorage getStorage() {
      return this.pixelmon.getParticipant().getStorage();
   }

   public void update(EnumUpdateType... updateTypes) {
      this.pixelmon.update(updateTypes);
      EnumUpdateType[] var2 = updateTypes;
      int var3 = updateTypes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumUpdateType updateType = var2[var4];
         if (updateType == EnumUpdateType.Moveset) {
            this.getPokemonWrapper().setTemporaryMoveset(this.getPokemonWrapper().getMoveset());
         }
      }

   }

   public void updateStats() {
      this.pixelmon.getStats().setLevelStats(this.pixelmon.getNature(), this.pixelmon.getBaseStats(), this.pixelmon.getLevelNum());
      this.pixelmon.update(EnumUpdateType.HP, EnumUpdateType.Stats);
   }

   public void updateLevelUp(PixelmonStatsData stats) {
      EntityPlayerMP owner = this.pixelmon.getPlayerOwner();
      if (owner != null) {
         PixelmonStatsData stats2 = PixelmonStatsData.createPacket(this);
         Pixelmon.network.sendTo(new LevelUp(this.pixelmon.getPokemonUUID(), this.getLevel(), stats, stats2, new PixelmonInGui(this.getPokemon())), owner);
         this.pixelmon.updateHPIncrease();
      }

   }

   public void sendMessage(String langKey, Object... data) {
      this.pixelmon.bc.sendToAll(langKey, data);
   }

   public String getNickname() {
      return this.pixelmon.getNickname();
   }

   public String getOriginalTrainer() {
      return this.pixelmon.getOriginalTrainer();
   }

   public boolean removeStatuses(StatusType... statuses) {
      return this.pixelmon.removeStatuses(statuses);
   }

   public EnumNature getNature() {
      return this.pixelmon.getNature();
   }

   public int getExpToNextLevel() {
      return this.pixelmon.getLevel().expToNextLevel;
   }

   public StatusPersist getPrimaryStatus() {
      return this.pixelmon.getPrimaryStatus();
   }

   public AbilityBase getAbility() {
      return this.pixelmon.getAbility();
   }

   public List getType() {
      return this.pixelmon.type;
   }

   public int getForm() {
      return this.pixelmon.getForm();
   }

   public boolean isEgg() {
      return false;
   }

   public int getEggCycles() {
      return 0;
   }

   public int getPartyPosition() {
      return this.pixelmon.getPartyPosition();
   }

   public boolean hasOwner() {
      return this.pixelmon.getParticipant().getType() != ParticipantType.WildPokemon;
   }

   public Optional getPokerus() {
      return Optional.ofNullable(this.innerLink.getPokerus());
   }

   public void adjustFriendship(int change) {
      if (change > 0) {
         this.pixelmon.getInnerLink().increaseFriendship(change);
      } else {
         this.pixelmon.getInnerLink().decreaseFriendship(-change);
      }

   }

   public PixelmonWrapper getPokemonWrapper() {
      return this.pixelmon;
   }
}
