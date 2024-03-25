package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs.trainers;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.TrainerData;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import java.util.ArrayList;

public class SpawnInfoNPCTrainer extends SpawnInfo {
   public static final String TYPE_ID_TRAINER = "trainer";
   public String trainerType = "Youngster";
   public String name = null;
   public ArrayList possiblePokemon = new ArrayList();
   public ArrayList guaranteedPokemon = new ArrayList();
   private Integer minLevel = null;
   private Integer maxLevel = null;
   private Integer minPartySize = null;
   private Integer maxPartySize = null;
   public String greeting = null;
   public String winMessage = null;
   public String loseMessage = null;
   public int winMoney = -1;
   public EnumBossMode bossMode;

   public SpawnInfoNPCTrainer() {
      super("trainer");
      this.bossMode = EnumBossMode.NotBoss;
   }

   public int getMinLevel() {
      TrainerData td = ServerNPCRegistry.trainers.getRandomData(this.trainerType);
      return this.minLevel != null ? this.minLevel : (td != null ? td.getMinLevel() : 1);
   }

   public int getMaxLevel() {
      TrainerData td = ServerNPCRegistry.trainers.getRandomData(this.trainerType);
      return this.maxLevel != null ? this.maxLevel : (td != null ? td.getMaxLevel() : 100);
   }

   public int getMinPartySize() {
      TrainerData td = ServerNPCRegistry.trainers.getRandomData(this.trainerType);
      return this.minPartySize != null ? this.minPartySize : (td != null ? td.getMinPartyPokemon() : 1);
   }

   public int getMaxPartySize() {
      TrainerData td = ServerNPCRegistry.trainers.getRandomData(this.trainerType);
      return this.maxPartySize != null ? this.maxPartySize : (td != null ? td.getMaxPartyPokemon() : 6);
   }

   public SpawnAction construct(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      return new SpawnActionNPCTrainer(this, spawnLocation);
   }

   public String toString() {
      return this.name != null ? this.name : this.trainerType;
   }
}
