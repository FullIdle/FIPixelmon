package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.spawning.LegendarySpawnEvent;
import com.pixelmonmod.pixelmon.api.item.JsonItemStack;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SuperLuck;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BonusStats;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class SpawnActionPokemon extends SpawnAction {
   public PokemonSpec baseSpec;
   public PokemonSpec usingSpec;

   public SpawnActionPokemon(SpawnInfoPokemon spawnInfo, SpawnLocation spawnLocation, PokemonSpec baseSpec, PokemonSpec usingSpec) {
      super(spawnInfo, spawnLocation);
      this.baseSpec = baseSpec;
      this.usingSpec = usingSpec;
   }

   protected EntityPixelmon createEntity() {
      SpawnInfoPokemon spawnInfo = (SpawnInfoPokemon)this.spawnInfo;

      try {
         double haMultiplier = 1.0;
         if (this.spawnLocation.cause instanceof EntityPlayerMP) {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(this.spawnLocation.cause.func_110124_au());
            List team = party.getTeam();
            if (team.size() > 0 && ((Pokemon)team.get(0)).getHeldItemAsItemHeld().getHeldItemType() == EnumHeldItems.cleanseTag && RandomHelper.getRandomChance(0.33333334F)) {
               return null;
            }

            if (party.getLure() != null && party.getLure().type == ItemLure.LureType.HA) {
               haMultiplier *= Math.sqrt((double)party.getLure().strength.multiplier);
            }
         }

         float haRate = (float)Math.ceil((double)PixelmonConfig.hiddenAbilityRate / haMultiplier);
         float oldRate = PixelmonConfig.hiddenAbilityRate;
         PixelmonConfig.hiddenAbilityRate = haRate;
         EntityPixelmon pixelmon = this.usingSpec.create(this.spawnLocation.location.world);
         PixelmonConfig.hiddenAbilityRate = oldRate;
         if (spawnInfo.locationTypes.contains(LocationType.AIR)) {
            pixelmon.setSpawnLocation(SpawnLocationType.AirPersistent);
         } else if (CollectionHelper.anyMatch(this.spawnLocation.types, Arrays.asList(LocationType.WATER, LocationType.UNDERGROUND_WATER, LocationType.SURFACE_WATER, LocationType.SEAFLOOR, LocationType.LAVA, LocationType.SURFACE_LAVA, LocationType.UNDERGROUND_LAVA, LocationType.OLD_ROD_LAVA, LocationType.GOOD_ROD_LAVA, LocationType.SUPER_ROD_LAVA, LocationType.OLD_ROD, LocationType.GOOD_ROD, LocationType.SUPER_ROD, LocationType.OAS_ROD))) {
            pixelmon.setSpawnLocation(SpawnLocationType.Water);
         } else {
            pixelmon.setSpawnLocation(SpawnLocationType.Land);
         }

         pixelmon.resetAI();
         if (spawnInfo.heldItems != null && !spawnInfo.heldItems.isEmpty()) {
            ItemStack heldItem = JsonItemStack.choose(spawnInfo.heldItems);
            if (heldItem == null && this.spawnLocation.cause instanceof EntityPlayerMP) {
               PlayerPartyStorage party = Pixelmon.storageManager.getParty(this.spawnLocation.cause.func_110124_au());
               if (party.getTeam().size() > 0 && ((Pokemon)party.getTeam().get(0)).getAbility() instanceof SuperLuck) {
                  heldItem = JsonItemStack.choose(spawnInfo.heldItems);
               }
            }

            if (heldItem != null) {
               pixelmon.getPokemonData().setHeldItem(heldItem);
            }
         }

         return pixelmon;
      } catch (Exception var9) {
         Pixelmon.LOGGER.error("There was a problem spawning a " + this.usingSpec.name);
         var9.printStackTrace();
         return null;
      }
   }

   public EntityPixelmon doSpawn(AbstractSpawner spawner) {
      EntityPixelmon pokemon = (EntityPixelmon)this.getOrCreateEntity();
      if (pokemon == null) {
         return null;
      } else if (pokemon.isLegendary() && !pokemon.isBossPokemon() && Pixelmon.EVENT_BUS.post(new LegendarySpawnEvent.DoSpawn(spawner, this))) {
         return null;
      } else if (super.doSpawn(spawner) == null) {
         return null;
      } else {
         if (PixelmonConfig.doLegendaryEvent && pokemon.isLegendary() && !pokemon.isBossPokemon()) {
            ITextComponent translatePoke = new TextComponentTranslation("pixelmon." + pokemon.getSpecies().name.toLowerCase() + ".name", new Object[0]);
            translatePoke.func_150256_b().func_150238_a(TextFormatting.GREEN);
            ITextComponent translateMessage = new TextComponentTranslation("spawn.legendarymessage", new Object[]{translatePoke, this.spawnLocation.biome.field_76791_y});
            translateMessage.func_150256_b().func_150238_a(TextFormatting.GREEN);
            this.spawnLocation.location.world.func_73046_m().func_184103_al().func_148539_a(new TextComponentTranslation("chat.type.announcement", new Object[]{TextFormatting.LIGHT_PURPLE + "Pixelmon" + TextFormatting.RESET + TextFormatting.GREEN, translateMessage}));
            BlockPos pos = this.spawnLocation.location.pos;
            this.spawnLocation.location.world.func_73046_m().func_145747_a(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Spawned " + pokemon.getPokemonName() + " at: " + this.spawnLocation.location.world.func_72912_H().func_76065_j() + " x:" + pos.func_177958_n() + ", y:" + pos.func_177956_o() + ", z:" + pos.func_177952_p()));
            ((EntityPixelmon)this.entity).legendaryTicks = PixelmonConfig.legendaryDespawnTicks;
         }

         ((EntityPixelmon)this.entity).resetDataWatchers();
         ((EntityPixelmon)this.entity).func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)pokemon.getPokemonData().getMaxHealth());
         ((EntityPixelmon)this.entity).func_70606_j((float)pokemon.getPokemonData().getHealth());
         ((EntityPixelmon)this.entity).getPokemonData().setBonusStats(new BonusStats(((EntityPixelmon)this.entity).getSpecies()));
         return (EntityPixelmon)this.entity;
      }
   }

   public static int getLevelBasedOnDistance(SpawnLocation spawnLocation) {
      BlockPos spawnPos = spawnLocation.location.world.func_175694_M();
      double distanceFromSpawn = spawnPos.func_185332_f(spawnLocation.location.pos.func_177958_n(), spawnLocation.location.pos.func_177956_o(), spawnLocation.location.pos.func_177952_p());
      int levelBase = (int)(distanceFromSpawn / (double)PixelmonConfig.distancePerLevel) + 1;
      if (levelBase > PixelmonConfig.maxLevelByDistance) {
         levelBase = PixelmonConfig.maxLevelByDistance;
      }

      return levelBase;
   }
}
