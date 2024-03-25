package com.pixelmonmod.pixelmon.entities.pixelmon.stats.links;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.NoStatus;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class DelegateLink extends PokemonLink {
   public Pokemon pokemon;

   public DelegateLink(Pokemon pokemon) {
      this.pokemon = pokemon;
   }

   public Pokemon getPokemon() {
      return this.pokemon;
   }

   public Stats getStats() {
      return this.pokemon.getStats();
   }

   public ItemHeld getHeldItem() {
      return this.pokemon.getHeldItemAsItemHeld();
   }

   public void setHeldItem(ItemStack item) {
      this.pokemon.setHeldItem(item);
   }

   public int getHealth() {
      return this.pokemon.getHealth();
   }

   public int getMaxHealth() {
      return this.getMaxHealth(false);
   }

   public int getMaxHealth(boolean ignoreDynamax) {
      return this.pokemon.getMaxHealth();
   }

   public void setHealth(int health) {
      this.pokemon.setHealth(health);
   }

   public int getLevel() {
      return this.pokemon.getLevel();
   }

   public void setLevel(int level) {
      this.pokemon.setLevelNum(level);
   }

   public int getExp() {
      return this.pokemon.getExperience();
   }

   public void setExp(int experience) {
      this.pokemon.setExperience(experience);
   }

   public int getFriendship() {
      return this.pokemon.getFriendship();
   }

   public boolean doesLevel() {
      return this.pokemon.doesLevel();
   }

   public EntityPlayerMP getPlayerOwner() {
      return this.pokemon.getOwnerPlayer();
   }

   public BattleControllerBase getBattleController() {
      PixelmonWrapper pw = this.pokemon.getPixelmonWrapperIfExists();
      return pw == null ? null : pw.bc;
   }

   public Moveset getMoveset() {
      return this.pokemon.getMoveset();
   }

   public UUID getPokemonUUID() {
      return this.pokemon.getUUID();
   }

   public EntityPixelmon getEntity() {
      return this.pokemon.getOrSpawnPixelmon(this.getPlayerOwner() == null ? null : this.getPlayerOwner());
   }

   public void setScale(float scale) {
      this.pokemon.ifEntityExists((pixelmon) -> {
         pixelmon.setPixelmonScale(scale);
      });
   }

   public World getWorld() {
      EntityPixelmon pixelmon = this.pokemon.getPixelmonIfExists();
      return pixelmon == null ? null : pixelmon.func_130014_f_();
   }

   public Gender getGender() {
      return this.pokemon.getGender();
   }

   public BlockPos getPos() {
      EntityPixelmon pixelmon = this.pokemon.getPixelmonIfExists();
      return pixelmon == null ? null : pixelmon.func_180425_c();
   }

   public PartyStorage getStorage() {
      return (PartyStorage)this.pokemon.getStorageAndPosition().func_76341_a();
   }

   public void update(EnumUpdateType... updateTypes) {
      this.pokemon.markDirty(updateTypes);
   }

   public void updateStats() {
      this.pokemon.getStats().setLevelStats(this.getNature(), this.getBaseStats(), this.getLevel());
      this.update(EnumUpdateType.Stats);
   }

   public void updateLevelUp(PixelmonStatsData stats) {
      EntityPlayerMP owner = this.pokemon.getOwnerPlayer();
      if (owner != null) {
         PixelmonStatsData stats2 = PixelmonStatsData.createPacket(this);
         Pixelmon.network.sendTo(new LevelUp(this.getPokemonUUID(), this.getLevel(), stats, stats2, new PixelmonInGui(this.pokemon)), owner);
         this.pokemon.markDirty(EnumUpdateType.Stats);
      }

   }

   public void sendMessage(String langKey, Object... data) {
      EntityPlayerMP player = this.pokemon.getOwnerPlayer();
      if (player != null) {
         player.func_145747_a(new TextComponentTranslation(langKey, data));
      }

   }

   public String getNickname() {
      return this.pokemon.getDisplayName();
   }

   public String getOriginalTrainer() {
      return this.pokemon.getOriginalTrainer();
   }

   public boolean removeStatuses(StatusType... statuses) {
      StatusType[] var2 = statuses;
      int var3 = statuses.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         StatusType statusType = var2[var4];
         if (statusType.isPrimaryStatus()) {
            this.pokemon.setStatus(NoStatus.noStatus);
            return true;
         }
      }

      return false;
   }

   public EnumNature getNature() {
      return this.pokemon.getNature();
   }

   public int getExpToNextLevel() {
      return this.pokemon.getExperienceToLevelUp();
   }

   public StatusPersist getPrimaryStatus() {
      return this.pokemon.getStatus();
   }

   public AbilityBase getAbility() {
      return this.pokemon.getAbility();
   }

   public List getType() {
      return this.getBaseStats().types;
   }

   public int getForm() {
      return this.pokemon.getForm();
   }

   public boolean isEgg() {
      return this.pokemon.isEgg();
   }

   public int getEggCycles() {
      return this.pokemon.getEggCycles();
   }

   public int getPartyPosition() {
      return ((StoragePosition)this.pokemon.getStorageAndPosition().func_76340_b()).order;
   }

   public boolean hasOwner() {
      return this.pokemon.getStorageAndPosition().func_76341_a() != null;
   }

   public Optional getPokerus() {
      return Optional.ofNullable(this.pokemon.getPokerus());
   }

   public void adjustFriendship(int change) {
      if (change > 0) {
         this.pokemon.increaseFriendship(change);
      } else {
         this.pokemon.decreaseFriendship(-change);
      }

   }
}
