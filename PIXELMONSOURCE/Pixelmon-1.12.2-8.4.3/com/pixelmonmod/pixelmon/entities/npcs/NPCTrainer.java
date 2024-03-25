package com.pixelmonmod.pixelmon.entities.npcs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.AI.AIExecuteAction;
import com.pixelmonmod.pixelmon.AI.AIMoveTowardsTarget;
import com.pixelmonmod.pixelmon.AI.AITargetNearest;
import com.pixelmonmod.pixelmon.AI.AITrainerInBattle;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.HiddenPower;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ClearTrainerPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.UpdateClientRules;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCEditData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.StoreTrainerPokemon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GymNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ITrainerData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.NPCRegistryTrainers;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.TrainerChat;
import com.pixelmonmod.pixelmon.entities.npcs.registry.TrainerData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemQueryList;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DroppedItem;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumOldGenMode;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumTrainerAI;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleAIMode;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import com.pixelmonmod.pixelmon.tools.Quadstate;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.GymInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.MovesetDefinition;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.PokemonDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.ArrayUtils;

public class NPCTrainer extends EntityNPC {
   private static final DataParameter dwEncounterMode;
   private static final DataParameter dwMegaItem;
   private static final DataParameter dwOldGen;
   private static final DataParameter dwBossMode;
   private static final DataParameter dwTrainerLevel;
   private static final DataParameter dwBattleAI;
   private static final DataParameter dwEngageDistance;
   private TrainerPartyStorage party = new TrainerPartyStorage(this);
   public boolean usingDefaultName = true;
   public boolean usingDefaultGreeting = true;
   public boolean usingDefaultWin = true;
   public boolean usingDefaultLose = true;
   public String greeting = "";
   public String winMessage = "";
   public String loseMessage = "";
   public int winMoney;
   public int level;
   private String trainerId = "";
   ItemStack[] winnings = new ItemStack[0];
   private boolean startRotationSet = false;
   private float startRotationYaw;
   public boolean isGymLeader = false;
   public transient boolean canEngage = true;
   public HashMap playerEncounters = new HashMap();
   public ArrayList winCommands = new ArrayList();
   public ArrayList loseCommands = new ArrayList();
   public ArrayList forfeitCommands = new ArrayList();
   public ArrayList preBattleCommands = new ArrayList();
   public BattleRules battleRules = new BattleRules();
   public BattleControllerBase battleController;

   public NPCTrainer(World par1World) {
      super(par1World);
      this.field_70180_af.func_187214_a(dwEncounterMode, 0);
      this.field_70180_af.func_187214_a(dwBossMode, 0);
      this.field_70180_af.func_187214_a(dwTrainerLevel, 0);
      this.field_70180_af.func_187214_a(dwBattleAI, 0);
      this.field_70180_af.func_187214_a(dwMegaItem, 0);
      this.field_70180_af.func_187214_a(dwOldGen, 0);
      this.field_70180_af.func_187214_a(dwEngageDistance, 4);
   }

   public void init(BaseTrainer trainer) {
      super.init(trainer.name);
      this.party = new TrainerPartyStorage(this);
      TrainerData info = ServerNPCRegistry.trainers.getRandomData(trainer);
      if (info == null) {
         BaseTrainer b = ServerNPCRegistry.trainers.getRandomBaseWithData();
         info = ServerNPCRegistry.trainers.getRandomData(b.name);
      }

      this.chatIndex = info.getRandomChat();
      if (this.usingDefaultName) {
         this.setName("" + info.getRandomName());
      }

      this.trainerId = info.id;
      this.winMoney = info.winnings;
      this.level = info.getRandomLevel();
      if (this.level == 0) {
         this.level = 1;
      }

      this.field_70180_af.func_187227_b(dwTrainerLevel, this.level);
      this.setBaseTrainer(info.trainerType);
      if (info.trainerType.textures.size() > 1) {
         this.setTextureIndex(this.field_70170_p.field_73012_v.nextInt(info.trainerType.textures.size()));
      }

      this.loadPokemon(info.getRandomParty());
   }

   public void init(GymNPCData trainer, GymInfo info, int tier) {
      super.init(trainer.id);
      this.party = new TrainerPartyStorage(this);
      this.chatIndex = trainer.getRandomTrainerChatIndex();
      this.setName("" + trainer.getRandomNameIndex());
      this.trainerId = trainer.id;
      this.winMoney = trainer.winnings;
      this.level = info.level;
      if (this.level > 0) {
         this.level = Math.min(PixelmonServerConfig.maxLevel, Math.max(1, this.level - tier));
      } else {
         this.setBossMode(EnumBossMode.Equal);
         this.level = 100;
      }

      this.field_70180_af.func_187227_b(dwTrainerLevel, this.level);
      this.setBaseTrainer(NPCRegistryTrainers.Steve);
      if (trainer.textures.size() > 0) {
         this.setCustomSteveTexture(trainer.getRandomTexture().replaceAll(".png", ""));
         this.setTextureIndex(4);
      }

      int numPokemon = 6;
      if (tier > 0) {
         numPokemon = 1 + this.field_70170_p.field_73012_v.nextInt(numPokemon - tier);
         this.setBattleAIMode(PixelmonConfig.battleAITrainer);
      } else {
         this.setBattleAIMode(EnumBattleAIMode.Advanced);
         this.isGymLeader = true;
      }

      int tries = 0;

      while(this.party.countPokemon() < numPokemon && tries++ < 100) {
         PokemonDefinition p = (PokemonDefinition)RandomHelper.getRandomElementFromList(info.pokemon);
         this.initializePokemon(p, this.level, false);
      }

      if (this.party.countPokemon() == 0) {
         this.func_70106_y();
      } else {
         this.updateLvl();
      }
   }

   public String getName(String langCode) {
      if (this.usingDefaultName) {
         try {
            int index = Integer.parseInt(this.func_70005_c_());
            ITrainerData translatedData = this.getTranslatedData(langCode);
            return translatedData == null ? this.func_70005_c_() : translatedData.getName(index);
         } catch (NumberFormatException var4) {
            return this.func_70005_c_();
         }
      } else {
         return this.func_70005_c_();
      }
   }

   public void clearGreetings() {
      this.usingDefaultGreeting = false;
      this.usingDefaultWin = false;
      this.usingDefaultLose = false;
      this.greeting = null;
      this.winMessage = null;
      this.loseMessage = null;
   }

   public String getGreeting(String langCode) {
      return this.usingDefaultGreeting ? this.getChat(langCode).opening : this.greeting;
   }

   public String getWinMessage(String langCode) {
      return this.usingDefaultWin ? this.getChat(langCode).win : this.winMessage;
   }

   public String getLoseMessage(String langCode) {
      return this.usingDefaultLose ? this.getChat(langCode).lose : this.loseMessage;
   }

   private ITrainerData getTranslatedData(String langCode) {
      return ServerNPCRegistry.getTranslatedData(langCode, this.getBaseTrainer(), this.trainerId);
   }

   public TrainerChat getChat(String langCode) {
      ITrainerData data = this.getTranslatedData(langCode);
      return data == null ? new TrainerChat("", "", "") : data.getChat(this.chatIndex);
   }

   public int getWinMoney() {
      return this.winMoney;
   }

   public void setEncounterMode(EnumEncounterMode mode) {
      this.field_70180_af.func_187227_b(dwEncounterMode, mode.ordinal());
      this.playerEncounters.clear();
   }

   public void setMegaItem(EnumMegaItemsUnlocked megaItem) {
      this.field_70180_af.func_187227_b(dwMegaItem, megaItem.ordinal());
   }

   public void setOldGenMode(EnumOldGenMode mode) {
      this.field_70180_af.func_187227_b(dwOldGen, mode.ordinal());
   }

   public void setEngageDistance(int engageDistance) {
      this.field_70180_af.func_187227_b(dwEngageDistance, engageDistance);
   }

   public EnumEncounterMode getEncounterMode() {
      return EnumEncounterMode.getFromIndex((Integer)this.field_70180_af.func_187225_a(dwEncounterMode));
   }

   public EnumMegaItemsUnlocked getMegaItem() {
      return EnumMegaItemsUnlocked.values()[(Integer)this.field_70180_af.func_187225_a(dwMegaItem)];
   }

   public EnumOldGenMode getOldGen() {
      return EnumOldGenMode.getFromIndex((Integer)this.field_70180_af.func_187225_a(dwOldGen));
   }

   public int getEngageDistance() {
      return (Integer)this.field_70180_af.func_187225_a(dwEngageDistance);
   }

   public void setTrainerType(BaseTrainer model, EntityPlayer player) {
      this.init(model);
      this.setTextureIndex(0);
      this.setBaseTrainer(model);
      EntityPlayerMP playerMP = (EntityPlayerMP)player;
      Pixelmon.network.sendTo(new ClearTrainerPokemon(), playerMP);
      Iterator var4 = this.party.getTeam().iterator();

      while(var4.hasNext()) {
         Pokemon pokemon = (Pokemon)var4.next();
         Pixelmon.network.sendTo(new StoreTrainerPokemon(pokemon), playerMP);
      }

      String loc = playerMP.field_71148_cg;
      this.setName(ServerNPCRegistry.trainers.getTranslatedRandomName(loc, model, this.trainerId));
      SetTrainerData p = new SetTrainerData(this, loc);
      Pixelmon.network.sendTo(new SetNPCEditData(p), playerMP);
   }

   public void func_110159_bB() {
      if (this.func_110167_bD()) {
         this.func_110160_i(true, true);
      }

   }

   public void func_70024_g(double par1, double par3, double par5) {
      if (this.func_70104_M()) {
         super.func_70024_g(par1, par3, par5);
      }

   }

   public boolean func_70067_L() {
      return true;
   }

   public boolean func_70104_M() {
      EnumTrainerAI ai = this.getAIMode();
      return ai != EnumTrainerAI.StandStill && ai != EnumTrainerAI.StillAndEngage;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.startRotationSet && this.getAIMode() == EnumTrainerAI.StillAndEngage) {
         this.field_70177_z = this.startRotationYaw;
         this.field_70759_as = this.startRotationYaw;
      }

   }

   protected void checkForRarityDespawn() {
      if (this.battleController == null) {
         super.checkForRarityDespawn();
      }
   }

   public EntityPixelmon releasePokemon(UUID newPokemonUUID) {
      return this.party.find(newPokemonUUID).getOrSpawnPixelmon(this);
   }

   public void loadPokemon(ArrayList pokemonList) {
      for(int i = 0; i < 6; ++i) {
         this.party.set(i, (Pokemon)null);
      }

      if (pokemonList != null && !pokemonList.isEmpty()) {
         Iterator var4 = pokemonList.iterator();

         while(var4.hasNext()) {
            Pokemon pokemon = (Pokemon)var4.next();
            pokemon.setLevel(Math.max(1, RandomHelper.getRandomNumberBetween(this.level - 1, this.level + 1)));
            this.party.add(pokemon);
         }
      } else {
         this.party.add((new PokemonSpec(new String[]{EnumSpecies.Rattata.name, "lvl:5"})).create());
      }

   }

   private void initializePokemon(PokemonDefinition definition, int level, boolean isDouble) {
      MovesetDefinition moves = (MovesetDefinition)RandomHelper.getRandomElementFromList(definition.movesets);
      if (level >= moves.minLevel && level >= definition.minLevel && level <= definition.maxLevel) {
         if (!moves.doubleOnly || isDouble) {
            Pokemon pokemon = Pixelmon.pokemonFactory.create(new PokemonSpec(new String[]{definition.pokemon.name, "form:" + moves.form, "lvl:" + Math.max(1, Math.min(PixelmonServerConfig.maxLevel, RandomHelper.getRandomNumberBetween(level - 1, level + 1)))}));
            if (this.isGymLeader) {
               pokemon.getEVs().attack = moves.evAtk;
               pokemon.getEVs().attack = moves.evAtk;
               pokemon.getEVs().defence = moves.evDef;
               pokemon.getEVs().hp = moves.evHP;
               pokemon.getEVs().specialAttack = moves.evSpAtk;
               pokemon.getEVs().specialDefence = moves.evSpDef;
               pokemon.getEVs().speed = moves.evSpeed;
               pokemon.getIVs().attack = moves.ivAtk;
               pokemon.getIVs().defence = moves.ivDef;
               pokemon.getIVs().hp = moves.ivHP;
               pokemon.getIVs().specialAttack = moves.ivSpAtk;
               pokemon.getIVs().specialDefence = moves.ivSpDef;
               pokemon.getIVs().speed = moves.ivSpeed;
               if (moves.nature != null && moves.nature.length > 0) {
                  pokemon.setNature((EnumNature)RandomHelper.getRandomElementFromArray(moves.nature));
               }
            }

            pokemon.setHealth(pokemon.getMaxHealth());
            if (moves.ability != null && moves.ability.length > 0) {
               pokemon.setAbility((AbilityBase)RandomHelper.getRandomElementFromArray(moves.ability));
            } else {
               List randomAbilities = new ArrayList();
               String[] var7 = pokemon.getBaseStats().getAbilitiesArray();
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  String ability = var7[var9];
                  if (ability != null) {
                     randomAbilities.add(ability);
                  }
               }

               if (!randomAbilities.isEmpty()) {
                  pokemon.setAbility((AbilityBase)AbilityBase.getAbility((String)RandomHelper.getRandomElementFromList(randomAbilities)).get());
               }
            }

            if (moves.heldItem != null && moves.heldItem.length > 0) {
               BaseShopItem item = ServerNPCRegistry.shopkeepers.getItem((String)RandomHelper.getRandomElementFromArray(moves.heldItem));
               if (item != null) {
                  item.getItem().func_190920_e(1);
                  pokemon.setHeldItem(item.getItem().func_77946_l());
               }
            }

            pokemon.setFriendship(255);
            Moveset moveset = pokemon.getMoveset();
            moveset.clear();
            this.addGymTrainerMove(moves.move1, pokemon, moves.ivsDefined);
            this.addGymTrainerMove(moves.move2, pokemon, moves.ivsDefined);
            this.addGymTrainerMove(moves.move3, pokemon, moves.ivsDefined);
            this.addGymTrainerMove(moves.move4, pokemon, moves.ivsDefined);
            this.party.add(pokemon);
            int pos = ((StoragePosition)this.party.find(pokemon.getUUID()).getStorageAndPosition().func_76340_b()).order;
            if (moves.lead && pos != 0) {
               this.party.swap(pos, 0);
            }

            this.calculateGen();
         }
      }
   }

   private void addGymTrainerMove(String[] possibleMoves, Pokemon pokemon, boolean ivsDefined) {
      if (possibleMoves != null && possibleMoves.length > 0) {
         Moveset moveset = pokemon.getMoveset();
         int randomIndex = this.field_70170_p.field_73012_v.nextInt(possibleMoves.length);
         String selectedMove = possibleMoves[randomIndex];
         EnumType hiddenPowerType = null;
         if (selectedMove.contains("Hidden Power")) {
            if (!ivsDefined) {
               hiddenPowerType = EnumType.parseType(selectedMove.replace("Hidden Power ", ""));
            }

            selectedMove = "Hidden Power";
         }

         Attack attack = new Attack(selectedMove);
         if (attack != null && !moveset.contains(attack)) {
            moveset.add(attack);
            if (hiddenPowerType != null) {
               pokemon.getIVs().CopyIVs(HiddenPower.getOptimalIVs(hiddenPowerType));
            }
         } else {
            String[] reducedPossible = (String[])ArrayUtils.remove(possibleMoves, randomIndex);
            this.addGymTrainerMove(reducedPossible, pokemon, ivsDefined);
         }
      }

   }

   public void startBattle(BattleParticipant battleParticipant) {
      this.calculateGen();
      if (battleParticipant instanceof PlayerParticipant) {
         PlayerParticipant player = (PlayerParticipant)battleParticipant;
         EnumEncounterMode encounterMode = this.getEncounterMode();
         if (encounterMode == EnumEncounterMode.OncePerMCDay) {
            this.playerEncounters.put(player.player.func_110124_au(), this.field_70170_p.func_82737_E());
         } else if (encounterMode == EnumEncounterMode.OncePerDay) {
            this.playerEncounters.put(player.player.func_110124_au(), System.currentTimeMillis());
         }
      }

      this.healAllPokemon();
   }

   public Quadstate calculateGen() {
      return this.party.isOldGen(this.field_71093_bK);
   }

   public void loseBattle(ArrayList opponents) {
      if (opponents.get(0) instanceof PlayerParticipant) {
         String langCode = ((EntityPlayerMP)((BattleParticipant)opponents.get(0)).getEntity()).field_71148_cg;
         if (this.getLoseMessage(langCode) != null) {
            ChatHandler.sendBattleMessage(opponents, this.getLoseMessage(langCode));
         }
      }

      if (opponents.size() == 1 && opponents.get(0) instanceof PlayerParticipant) {
         EntityPlayerMP player = (EntityPlayerMP)((BattleParticipant)opponents.get(0)).getEntity();
         Pixelmon.EVENT_BUS.post(new BeatTrainerEvent(player, this));
         if (this.getEncounterMode() == EnumEncounterMode.Once) {
            this.func_70623_bb();
            this.func_70106_y();
         } else {
            this.healAllPokemon();
            if (this.getEncounterMode() == EnumEncounterMode.OncePerPlayer) {
               this.playerEncounters.put(player.func_110124_au(), this.field_70170_p.func_82737_E());
            }
         }

         if (player.field_70170_p.func_152378_a(player.func_110124_au()) == null) {
            return;
         }

         int calculatedWinMoney;
         if (this.winMoney > 0) {
            calculatedWinMoney = this.winMoney * this.party.getAverageLevel();
            if (!this.func_70692_ba()) {
               calculatedWinMoney = this.winMoney;
            }

            PlayerParticipant playerParticipant = (PlayerParticipant)((PlayerParticipant)opponents.get(0));
            calculatedWinMoney *= playerParticipant.getPrizeMoneyMultiplier();
            IPixelmonBankAccount account = (IPixelmonBankAccount)Pixelmon.moneyManager.getBankAccount(playerParticipant.player).orElse((Object)null);
            if (account != null) {
               account.changeMoney(calculatedWinMoney);
               if (playerParticipant.bc == null) {
                  ChatHandler.sendFormattedChat(player, TextFormatting.GREEN, "pixelmon.entitytrainer.winnings", "" + calculatedWinMoney, this.getName(player.field_71148_cg));
               } else {
                  ChatHandler.sendBattleMessage((Entity)playerParticipant.getEntity(), "pixelmon.entitytrainer.winnings", "" + calculatedWinMoney, this.getName(player.field_71148_cg));
               }
            }
         }

         int number2;
         if (this.winnings.length > 0) {
            ArrayList drops = new ArrayList();
            number2 = 0;
            ItemStack[] var12 = this.winnings;
            int var6 = var12.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               ItemStack item = var12[var7];
               if (item.func_190916_E() == 0) {
                  item.func_190920_e(1);
               }

               drops.add(new DroppedItem(item.func_77946_l(), number2++));
            }

            DropItemQueryList.register(this, drops, player);
         }

         if (this.getBaseTrainer() != null && this.getBaseTrainer().name.equals("Fisherman")) {
            calculatedWinMoney = RandomHelper.getRandomNumberBetween(1, 100);
            number2 = RandomHelper.getRandomNumberBetween(1, 1000);
            if (calculatedWinMoney == 43) {
               DropItemHelper.giveItemStack(player, new ItemStack(PixelmonItems.goodRod), false);
               ChatHandler.sendFormattedChat(player, TextFormatting.GREEN, "pixelmon.entitytrainer.goodrod");
            } else if (number2 == 564) {
               DropItemHelper.giveItemStack(player, new ItemStack(PixelmonItems.superRod), false);
               ChatHandler.sendFormattedChat(player, TextFormatting.GREEN, "pixelmon.entitytrainer.superrod");
            }
         }
      }

   }

   public void winBattle(ArrayList opponents) {
      if (opponents.get(0) instanceof PlayerParticipant) {
         String langCode = ((EntityPlayerMP)((BattleParticipant)opponents.get(0)).getEntity()).field_71148_cg;
         if (this.getWinMessage(langCode) != null) {
            ChatHandler.sendBattleMessage(opponents, this.getWinMessage(langCode));
         }
      }

      if (opponents.size() == 1 && opponents.get(0) instanceof PlayerParticipant) {
         EntityPlayerMP player = (EntityPlayerMP)((BattleParticipant)opponents.get(0)).getEntity();
         Pixelmon.EVENT_BUS.post(new LostToTrainerEvent(player, this));
      }

   }

   public void healAllPokemon() {
      this.party.getTeam().forEach((pokemon) -> {
         pokemon.heal();
      });
   }

   public void restoreAllFriendship() {
      this.party.getTeam().forEach((pokemon) -> {
         pokemon.setFriendship(255);
      });
   }

   public UUID getNextPokemonUUID() {
      Pokemon pokemon = this.party.findOne((p) -> {
         return !p.isEgg() && p.getPixelmonIfExists() == null;
      });
      return pokemon == null ? null : pokemon.getUUID();
   }

   public int getLvl() {
      return (Integer)this.field_70180_af.func_187225_a(dwTrainerLevel);
   }

   public boolean interactWithNPC(EntityPlayer player, EnumHand hand) {
      Pixelmon.EVENT_BUS.post(new NPCEvent.Interact(this, EnumNPCType.Trainer, player));
      return false;
   }

   protected boolean func_184645_a(EntityPlayer player, EnumHand hand) {
      ItemStack stack = player.func_184586_b(hand);
      if (player instanceof EntityPlayerMP) {
         if (!stack.func_190926_b() && stack.func_77973_b() instanceof ItemNPCEditor) {
            EntityPlayerMP playerMP = (EntityPlayerMP)player;
            if (!ItemNPCEditor.checkPermission(playerMP)) {
               return false;
            }

            this.func_110163_bv();
            this.field_70714_bg.field_75782_a.clear();
            this.party.sendCacheToPlayer(playerMP);
            String loc = playerMP.field_71148_cg;
            SetTrainerData p = new SetTrainerData(this, loc);
            Pixelmon.network.sendTo(new SetNPCEditData(p), playerMP);
            if (BattleClauseRegistry.getClauseVersion() > 0) {
               Pixelmon.network.sendTo(new UpdateClientRules(), playerMP);
            }

            OpenScreen.open(player, EnumGuiScreen.TrainerEditor, this.getId());
         }
      } else {
         this.field_70714_bg.field_75782_a.clear();
      }

      return super.func_184645_a(player, hand);
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      NBTTagCompound pokemonNbt = new NBTTagCompound();
      this.party.writeToNBT(pokemonNbt);
      nbt.func_74782_a("pokeStore", pokemonNbt);
      nbt.func_74777_a("BossMode", (short)this.getBossMode().index);
      nbt.func_74778_a("Greeting", this.greeting);
      nbt.func_74778_a("WinMessage", this.winMessage);
      nbt.func_74778_a("LoseMessage", this.loseMessage);
      NBTTagCompound tmpWinnings = new NBTTagCompound();

      NBTTagCompound commandsNbt;
      for(int i = 0; i < this.winnings.length; ++i) {
         commandsNbt = new NBTTagCompound();
         if (this.winnings[i] != null) {
            this.winnings[i].func_77955_b(commandsNbt);
            tmpWinnings.func_74782_a("item" + i, commandsNbt);
         }
      }

      nbt.func_74768_a("NPCLevel", this.level);
      nbt.func_74768_a("WinMoney", this.winMoney);
      nbt.func_74782_a("WinningsTag", tmpWinnings);
      EnumEncounterMode mode = this.getEncounterMode();
      nbt.func_74777_a("EncMode", (short)mode.ordinal());
      nbt.func_74768_a("EngageDistance", this.getEngageDistance());
      if (mode != EnumEncounterMode.Once && mode != EnumEncounterMode.Unlimited) {
         NBTTagList list = new NBTTagList();

         NBTTagCompound compound;
         for(Iterator var6 = this.playerEncounters.entrySet().iterator(); var6.hasNext(); list.func_74742_a(compound)) {
            Map.Entry pair = (Map.Entry)var6.next();
            compound = new NBTTagCompound();
            compound.func_186854_a("UUID", (UUID)pair.getKey());
            if (mode != EnumEncounterMode.OncePerPlayer) {
               compound.func_74772_a("time", (Long)pair.getValue());
            }
         }

         nbt.func_74782_a("Encounters", list);
      }

      nbt.func_74777_a("BattleAIMode", (short)this.getBattleAIMode().ordinal());
      nbt.func_74757_a("DefaultName", this.usingDefaultName);
      nbt.func_74757_a("DefaultWin", this.usingDefaultWin);
      nbt.func_74757_a("DefaultLose", this.usingDefaultLose);
      nbt.func_74757_a("DefaultGreet", this.usingDefaultGreeting);
      nbt.func_74778_a("TrainerIndex", this.trainerId);
      nbt.func_74768_a("ChatIndex", this.chatIndex);
      nbt.func_74768_a("hasMegaItem", this.getMegaItem().ordinal());
      if (this.getOldGen() != null) {
         nbt.func_74768_a("oldGen", this.getOldGen().ordinal());
      }

      if (this.getAIMode() == EnumTrainerAI.StillAndEngage) {
         nbt.func_74776_a("TrainerRotation", this.startRotationYaw);
      }

      nbt.func_74757_a("GymLeader", this.isGymLeader);
      this.battleRules.writeToNBT(nbt);
      commandsNbt = nbt.func_74775_l("Commands");
      NBTTagList winList = new NBTTagList();
      Iterator var15 = this.winCommands.iterator();

      while(var15.hasNext()) {
         String winCommand = (String)var15.next();
         winList.func_74742_a(new NBTTagString(winCommand));
      }

      commandsNbt.func_74782_a("winCommands", winList);
      NBTTagList loseList = new NBTTagList();
      Iterator var18 = this.loseCommands.iterator();

      while(var18.hasNext()) {
         String loseCommand = (String)var18.next();
         loseList.func_74742_a(new NBTTagString(loseCommand));
      }

      commandsNbt.func_74782_a("loseCommands", loseList);
      NBTTagList forfeitList = new NBTTagList();
      Iterator var20 = this.forfeitCommands.iterator();

      while(var20.hasNext()) {
         String forfeitCommand = (String)var20.next();
         forfeitList.func_74742_a(new NBTTagString(forfeitCommand));
      }

      commandsNbt.func_74782_a("forfeitCommands", forfeitList);
      NBTTagList preBattleList = new NBTTagList();
      Iterator var22 = this.preBattleCommands.iterator();

      while(var22.hasNext()) {
         String preBattleCommand = (String)var22.next();
         preBattleList.func_74742_a(new NBTTagString(preBattleCommand));
      }

      commandsNbt.func_74782_a("preBattleCommands", preBattleList);
      nbt.func_74782_a("Commands", commandsNbt);
   }

   public boolean func_70039_c(NBTTagCompound nbt) {
      return this.canEngage && super.func_70039_c(nbt);
   }

   public boolean canStartBattle(EntityPlayer opponent, boolean printMessages) {
      if (opponent == null) {
         return true;
      } else if (!this.canEngage) {
         return false;
      } else if (this.battleController != null) {
         if (printMessages) {
            ChatHandler.sendChat(opponent, "pixelmon.entitytrainer.inbattle");
         }

         return false;
      } else {
         Long lastEncounter = (Long)this.playerEncounters.get(opponent.func_110124_au());
         if (lastEncounter != null) {
            EnumEncounterMode mode = this.getEncounterMode();
            long curTime;
            long oldTime;
            long curDay;
            long oldDay;
            if (mode == EnumEncounterMode.OncePerDay) {
               curTime = System.currentTimeMillis();
               oldTime = lastEncounter;
               curDay = curTime / 86400000L;
               oldDay = oldTime / 86400000L;
               if (curDay <= oldDay) {
                  if (printMessages) {
                     ChatHandler.sendChat(opponent, "pixelmon.entitytrainer.onceday");
                  }

                  return false;
               }
            } else if (mode == EnumEncounterMode.OncePerMCDay) {
               curTime = this.field_70170_p.func_82737_E();
               oldTime = lastEncounter;
               curDay = curTime / 24000L;
               oldDay = oldTime / 24000L;
               if (curDay <= oldDay) {
                  if (printMessages) {
                     ChatHandler.sendChat(opponent, "pixelmon.entitytrainer.oncemcday");
                  }

                  return false;
               }
            } else if (mode == EnumEncounterMode.OncePerPlayer) {
               if (printMessages) {
                  ChatHandler.sendChat(opponent, "pixelmon.entitytrainer.onceplayer");
               }

               return false;
            }
         }

         return true;
      }
   }

   public void func_70037_a(NBTTagCompound nbt) {
      if (nbt.func_74764_b("DefaultName")) {
         this.usingDefaultName = nbt.func_74767_n("DefaultName");
         this.usingDefaultWin = nbt.func_74767_n("DefaultWin");
         this.usingDefaultLose = nbt.func_74767_n("DefaultLose");
         this.usingDefaultGreeting = nbt.func_74767_n("DefaultGreet");
         this.trainerId = nbt.func_74779_i("TrainerIndex");
         this.chatIndex = nbt.func_74762_e("ChatIndex");
      }

      super.func_70037_a(nbt);
      if (nbt.func_74764_b("BossMode")) {
         this.setBossMode(EnumBossMode.getMode(nbt.func_74765_d("BossMode")));
      }

      this.party.readFromNBT(nbt.func_74775_l("pokeStore"));
      if (nbt.func_74764_b("Greeting")) {
         this.greeting = nbt.func_74779_i("Greeting");
         this.winMessage = nbt.func_74779_i("WinMessage");
         this.loseMessage = nbt.func_74779_i("LoseMessage");
      }

      if (nbt.func_74764_b("hasMegaItem")) {
         this.setMegaItem(EnumMegaItemsUnlocked.values()[nbt.func_74762_e("hasMegaItem")]);
      }

      if (nbt.func_74764_b("oldGen")) {
         this.setOldGenMode(EnumOldGenMode.values()[nbt.func_74762_e("oldGen")]);
      }

      NBTTagCompound cmdNbt;
      int numEncounters;
      String uuid;
      int i;
      if (nbt.func_74764_b("WinningsTag")) {
         cmdNbt = nbt.func_74775_l("WinningsTag");
         this.winnings = new ItemStack[cmdNbt.func_150296_c().size()];
         numEncounters = 0;

         for(Iterator var4 = cmdNbt.func_150296_c().iterator(); var4.hasNext(); this.winnings[numEncounters++] = new ItemStack(cmdNbt.func_74775_l(uuid))) {
            uuid = (String)var4.next();
         }
      } else if (nbt.func_74764_b("Winnings")) {
         int[] testArray = nbt.func_74759_k("Winnings");
         ArrayList array = new ArrayList();
         int[] var16 = testArray;
         int var19 = testArray.length;

         for(int var6 = 0; var6 < var19; ++var6) {
            int aTestArray = var16[var6];
            array.add(Item.func_150899_d(aTestArray));
         }

         this.winnings = new ItemStack[array.size()];

         for(i = 0; i < array.size(); ++i) {
            this.winnings[i] = new ItemStack((Item)array.get(i));
         }
      }

      if (nbt.func_74764_b("TrainerRotation")) {
         this.setStartRotationYaw(nbt.func_74760_g("TrainerRotation"));
      }

      if (nbt.func_74764_b("EngageDistance")) {
         this.setEngageDistance(nbt.func_74762_e("EngageDistance"));
      }

      NBTTagList preBattle;
      if (nbt.func_74764_b("EncMode")) {
         EnumEncounterMode mode = EnumEncounterMode.getFromIndex(nbt.func_74765_d("EncMode"));
         this.setEncounterMode(mode);
         this.playerEncounters.clear();
         if (mode != EnumEncounterMode.Once && mode != EnumEncounterMode.Unlimited && nbt.func_74764_b("numEncounters")) {
            numEncounters = nbt.func_74762_e("numEncounters");

            for(i = 0; i < numEncounters; ++i) {
               uuid = nbt.func_74779_i("encPl" + i);
               long time = 0L;
               if (mode != EnumEncounterMode.OncePerPlayer) {
                  time = nbt.func_74763_f("encTi" + i);
                  long curDay;
                  long oldDay;
                  if (mode == EnumEncounterMode.OncePerDay) {
                     curDay = System.currentTimeMillis() / 86400000L;
                     oldDay = time / 86400000L;
                     if (curDay <= oldDay) {
                        this.playerEncounters.put(UUID.fromString(uuid), time);
                     }
                  } else if (mode == EnumEncounterMode.OncePerMCDay) {
                     curDay = this.field_70170_p.func_82737_E() / 24000L;
                     oldDay = time / 24000L;
                     if (curDay <= oldDay) {
                        this.playerEncounters.put(UUID.fromString(uuid), time);
                     }
                  }
               } else {
                  this.playerEncounters.put(UUID.fromString(uuid), 0L);
               }
            }
         }

         if (mode != EnumEncounterMode.Once && mode != EnumEncounterMode.Unlimited && nbt.func_74764_b("Encounters")) {
            preBattle = nbt.func_150295_c("Encounters", 10);

            for(i = 0; i < preBattle.func_74745_c(); ++i) {
               NBTTagCompound compound = preBattle.func_150305_b(i);
               UUID uuid = compound.func_186857_a("UUID");
               long time = mode != EnumEncounterMode.OncePerPlayer ? compound.func_74763_f("time") : 0L;
               long curDay;
               long oldDay;
               if (mode == EnumEncounterMode.OncePerDay) {
                  curDay = System.currentTimeMillis() / 86400000L;
                  oldDay = time / 86400000L;
                  if (curDay <= oldDay) {
                     this.playerEncounters.put(uuid, time);
                  }
               } else if (mode == EnumEncounterMode.OncePerMCDay) {
                  curDay = this.field_70170_p.func_82737_E() / 24000L;
                  oldDay = time / 24000L;
                  if (curDay <= oldDay) {
                     this.playerEncounters.put(uuid, time);
                  }
               } else {
                  this.playerEncounters.put(uuid, 0L);
               }
            }
         }
      } else {
         this.setEncounterMode(EnumEncounterMode.Once);
      }

      if (nbt.func_74764_b("BattleAIMode")) {
         this.setBattleAIMode(EnumBattleAIMode.getFromIndex(nbt.func_74765_d("BattleAIMode")));
      }

      if (nbt.func_74764_b("NPCLevel")) {
         this.level = nbt.func_74762_e("NPCLevel");
         this.field_70180_af.func_187227_b(dwTrainerLevel, this.level);
         this.updateLvl();
      }

      if (nbt.func_74764_b("Commands")) {
         cmdNbt = nbt.func_74775_l("Commands");
         if (cmdNbt.func_74764_b("winCommands")) {
            preBattle = cmdNbt.func_150295_c("winCommands", 8);

            for(i = 0; i < preBattle.func_74745_c(); ++i) {
               this.winCommands.add(preBattle.func_150307_f(i));
            }
         }

         if (cmdNbt.func_74764_b("loseCommands")) {
            preBattle = cmdNbt.func_150295_c("loseCommands", 8);

            for(i = 0; i < preBattle.func_74745_c(); ++i) {
               this.loseCommands.add(preBattle.func_150307_f(i));
            }
         }

         if (cmdNbt.func_74764_b("forfeitCommands")) {
            preBattle = cmdNbt.func_150295_c("forfeitCommands", 8);

            for(i = 0; i < preBattle.func_74745_c(); ++i) {
               this.forfeitCommands.add(preBattle.func_150307_f(i));
            }
         }

         if (cmdNbt.func_74764_b("preBattleCommands")) {
            preBattle = cmdNbt.func_150295_c("preBattleCommands", 8);

            for(i = 0; i < preBattle.func_74745_c(); ++i) {
               this.preBattleCommands.add(preBattle.func_150307_f(i));
            }
         }
      }

      if (nbt.func_74764_b("WinMoney")) {
         this.winMoney = nbt.func_74762_e("WinMoney");
      }

      if (nbt.func_74764_b("GymLeader")) {
         this.isGymLeader = nbt.func_74767_n("GymLeader");
      }

      this.battleRules.readFromNBT(nbt);
      if (nbt.func_74764_b("BattleType")) {
         this.battleRules.battleType = EnumBattleType.values()[nbt.func_74765_d("BattleType")];
         nbt.func_82580_o("BattleType");
      }

      this.updateLvl();
   }

   public void randomisePokemon(EntityPlayer player) {
      BaseTrainer base = this.getBaseTrainer();
      ArrayList randomParty;
      if (base.name.equals("Steve")) {
         int partySize = RandomHelper.getRandomNumberBetween(1, 6);
         randomParty = new ArrayList(partySize);

         for(int i = 0; i < partySize; ++i) {
            randomParty.add((new PokemonSpec(EnumSpecies.randomPoke().name)).create());
         }

         this.level = RandomHelper.getRandomNumberBetween(2, 99);
      } else {
         TrainerData data = ServerNPCRegistry.trainers.getRandomData(base);
         if (data == null) {
            FMLCommonHandler.instance().getFMLLogger().error(base.name + " has no trainer data set.");
            return;
         }

         randomParty = data.getRandomParty();
      }

      this.loadPokemon(randomParty);
      this.updateLvl();
      Pixelmon.network.sendTo(new ClearTrainerPokemon(), (EntityPlayerMP)player);
      Iterator var7 = this.party.getTeam().iterator();

      while(var7.hasNext()) {
         Pokemon pokemon = (Pokemon)var7.next();
         Pixelmon.network.sendTo(new StoreTrainerPokemon(pokemon), (EntityPlayerMP)player);
      }

   }

   public EnumBossMode getBossMode() {
      return EnumBossMode.getMode((Integer)this.field_70180_af.func_187225_a(dwBossMode));
   }

   public void setBossMode(EnumBossMode mode) {
      this.field_70180_af.func_187227_b(dwBossMode, mode.index);
   }

   public EnumBattleAIMode getBattleAIMode() {
      return EnumBattleAIMode.getFromIndex((Integer)this.field_70180_af.func_187225_a(dwBattleAI));
   }

   public void setBattleAIMode(EnumBattleAIMode mode) {
      if (mode != null) {
         this.field_70180_af.func_187227_b(dwBattleAI, mode.ordinal());
      }

   }

   public EnumBattleType getBattleType() {
      return this.battleRules.battleType;
   }

   /** @deprecated */
   @Deprecated
   public void setBattleType(EnumBattleType type) {
      this.battleRules.battleType = type;
   }

   public void updateLvl() {
      int lvlTotal = 0;
      int count = 0;

      for(Iterator var3 = this.party.getTeam().iterator(); var3.hasNext(); ++count) {
         Pokemon pokemon = (Pokemon)var3.next();
         lvlTotal += pokemon.getLevel();
      }

      this.field_70180_af.func_187227_b(dwTrainerLevel, lvlTotal / count);
   }

   public void initAI() {
      this.field_70714_bg.field_75782_a.clear();
      switch (this.getAIMode()) {
         case StandStill:
            this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
            this.field_70714_bg.func_75776_a(1, new AITrainerInBattle(this));
            this.field_70714_bg.func_75776_a(2, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
            this.field_70714_bg.func_75776_a(3, new EntityAIWatchClosest(this, EntityPixelmon.class, 6.0F));
            break;
         case Wander:
            this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
            this.field_70714_bg.func_75776_a(1, new AITrainerInBattle(this));
            this.field_70714_bg.func_75776_a(2, new EntityAIWander(this, SharedMonsterAttributes.field_111263_d.func_111110_b()));
            break;
         case StillAndEngage:
            this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
            this.field_70714_bg.func_75776_a(1, new AITrainerInBattle(this));
            this.field_70714_bg.func_75776_a(2, new AIExecuteAction(this));
            this.field_70714_bg.func_75776_a(3, new AITargetNearest(this, (float)this.getEngageDistance(), true));
            this.field_70714_bg.func_75776_a(4, new AIMoveTowardsTarget(this, (float)this.getEngageDistance()));
            break;
         case WanderAndEngage:
            this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
            this.field_70714_bg.func_75776_a(1, new AITrainerInBattle(this));
            this.field_70714_bg.func_75776_a(2, new EntityAIWander(this, this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e()));
            this.field_70714_bg.func_75776_a(3, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
            this.field_70714_bg.func_75776_a(4, new EntityAIWatchClosest(this, EntityPixelmon.class, 6.0F));
            this.field_70714_bg.func_75776_a(5, new AIExecuteAction(this));
            this.field_70714_bg.func_75776_a(6, new AIMoveTowardsTarget(this, (float)this.getEngageDistance()));
            this.field_70714_bg.func_75776_a(7, new AITargetNearest(this, (float)this.getEngageDistance(), true));
      }

   }

   public void update(SetTrainerData p) {
      if (!p.greeting.equals("")) {
         this.greeting = p.greeting;
         this.usingDefaultGreeting = false;
      }

      if (!p.lose.equals("")) {
         this.loseMessage = p.lose;
         this.usingDefaultLose = false;
      }

      if (!p.win.equals("")) {
         this.winMessage = p.win;
         this.usingDefaultWin = false;
      }

      this.winMoney = p.winMoney;
      if (p.rules != null) {
         this.battleRules = p.rules;
      }

      if (p.name != null && !p.name.isEmpty()) {
         this.setName(p.name);
      }

   }

   public void setStartRotationYaw(float f) {
      this.startRotationSet = true;
      this.startRotationYaw = f;
      this.field_70177_z = f;
      this.field_70759_as = f;
   }

   public String getDisplayText() {
      String s = "boss";
      if (this.getBossMode() == EnumBossMode.NotBoss) {
         s = "" + this.getLvl();
      }

      return s;
   }

   public String getSubTitleText() {
      return I18n.func_74838_a("gui.screenpokechecker.lvl");
   }

   public void setBattleController(BattleControllerBase battleController) {
      this.battleController = battleController;
   }

   public BattleControllerBase getBattleController() {
      return this.battleController;
   }

   public TrainerPartyStorage getPokemonStorage() {
      return this.party;
   }

   public EntityLiving getEntity() {
      return this;
   }

   public void setAttackTargetPix(EntityLivingBase entity) {
      this.func_70624_b(entity);
   }

   public void updateDrops(ItemStack[] drops) {
      this.winnings = drops;
   }

   public ItemStack[] getWinnings() {
      return this.winnings;
   }

   public void setLevel(int level) {
      this.level = level;
      this.field_70180_af.func_187227_b(dwTrainerLevel, level);
      BaseTrainer base = this.getBaseTrainer();
      TrainerData data = ServerNPCRegistry.trainers.getRandomData(base);
      this.loadPokemon(data.getRandomParty());
   }

   static {
      dwEncounterMode = EntityDataManager.func_187226_a(NPCTrainer.class, DataSerializers.field_187192_b);
      dwMegaItem = EntityDataManager.func_187226_a(NPCTrainer.class, DataSerializers.field_187192_b);
      dwOldGen = EntityDataManager.func_187226_a(NPCTrainer.class, DataSerializers.field_187192_b);
      dwBossMode = EntityDataManager.func_187226_a(NPCTrainer.class, DataSerializers.field_187192_b);
      dwTrainerLevel = EntityDataManager.func_187226_a(NPCTrainer.class, DataSerializers.field_187192_b);
      dwBattleAI = EntityDataManager.func_187226_a(NPCTrainer.class, DataSerializers.field_187192_b);
      dwEngageDistance = EntityDataManager.func_187226_a(NPCTrainer.class, DataSerializers.field_187192_b);
   }
}
