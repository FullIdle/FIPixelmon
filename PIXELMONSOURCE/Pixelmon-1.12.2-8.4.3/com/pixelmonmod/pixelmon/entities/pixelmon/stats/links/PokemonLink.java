package com.pixelmonmod.pixelmon.entities.pixelmon.stats.links;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class PokemonLink {
   public abstract Pokemon getPokemon();

   public BaseStats getBaseStats() {
      return this.getPokemon().getBaseStats();
   }

   public abstract Stats getStats();

   public abstract ItemHeld getHeldItem();

   public abstract void setHeldItem(ItemStack var1);

   public abstract int getHealth();

   public abstract int getMaxHealth();

   public abstract int getMaxHealth(boolean var1);

   public abstract void setHealth(int var1);

   public void setHealthDirect(int health) {
      this.setHealth(health);
   }

   public abstract int getLevel();

   public abstract void setLevel(int var1);

   public abstract int getExp();

   public abstract void setExp(int var1);

   public abstract int getFriendship();

   public abstract boolean doesLevel();

   public String getRealNickname() {
      return this.getNickname();
   }

   public abstract EntityPlayerMP getPlayerOwner();

   public abstract BattleControllerBase getBattleController();

   public abstract Moveset getMoveset();

   public abstract UUID getPokemonUUID();

   public abstract EntityPixelmon getEntity();

   public abstract void setScale(float var1);

   public abstract World getWorld();

   public abstract Gender getGender();

   public abstract BlockPos getPos();

   public abstract PartyStorage getStorage();

   public abstract void update(EnumUpdateType... var1);

   public abstract void updateStats();

   public abstract void updateLevelUp(PixelmonStatsData var1);

   public abstract void sendMessage(String var1, Object... var2);

   public abstract String getNickname();

   public abstract String getOriginalTrainer();

   public abstract boolean removeStatuses(StatusType... var1);

   public abstract EnumNature getNature();

   public abstract int getExpToNextLevel();

   public abstract StatusPersist getPrimaryStatus();

   public abstract AbilityBase getAbility();

   public abstract List getType();

   public abstract int getForm();

   public boolean isFainted() {
      return this.getHealth() <= 0;
   }

   public boolean isShiny() {
      return this.getPokemon().isShiny();
   }

   public abstract boolean isEgg();

   public abstract int getEggCycles();

   public EnumGrowth getGrowth() {
      return this.getPokemon().getGrowth();
   }

   public EnumPokeballs getCaughtBall() {
      return this.getPokemon().getCaughtBall();
   }

   public abstract int getPartyPosition();

   public ItemStack getHeldItemStack() {
      return this.getPokemon().getHeldItem();
   }

   public abstract boolean hasOwner();

   public abstract Optional getPokerus();

   public EnumSpecies getSpecies() {
      BaseStats stats = this.getBaseStats();
      return stats == null ? EnumSpecies.Bulbasaur : stats.getSpecies();
   }

   public PokemonForm getPokemonForm() {
      return new PokemonForm(this.getSpecies(), this.getForm());
   }

   public String toString() {
      return this.getNickname();
   }

   public abstract void adjustFriendship(int var1);
}
