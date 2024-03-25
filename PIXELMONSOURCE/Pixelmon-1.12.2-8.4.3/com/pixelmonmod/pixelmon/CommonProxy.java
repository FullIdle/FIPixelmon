package com.pixelmonmod.pixelmon;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.trading.PossibleTradeList;
import com.pixelmonmod.pixelmon.battles.BattleTickHandler;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.RulesRegistry;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCookingPot;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityInfuser;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityMechanicalAnvil;
import com.pixelmonmod.pixelmon.client.gui.machines.cookingpot.ContainerCookingPot;
import com.pixelmonmod.pixelmon.client.gui.machines.infuser.ContainerInfuser;
import com.pixelmonmod.pixelmon.client.gui.machines.mechanicalanvil.ContainerMechanicalAnvil;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.RaidSpawningRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionAbilityCapsule;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionAbilityPatch;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionBerryJuice;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionBottleCap;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionChangeForm;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionColorfulShake;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionCurry;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionDeveloper;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionDynamaxCandy;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionElixir;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionEther;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionEvolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionGaeBolg;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionHeldItem;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionMaxSoup;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionMint;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionPPUp;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionPerilousSoup;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionPotion;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionRainbowWing;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionRareCandy;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionTM;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionTechnicalMove;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionVitamins;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionZombie;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionZygardeCube;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.AnalysePokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Cut;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Dig;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Explode;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Flash;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Fly;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Forage;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.GroundBirds;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Headbutt;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.HealOther;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Ignite;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Lightning;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.MegaEvolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.MountBoost;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.OreSense;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.RockSmash;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Scare;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Smelt;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.SweetScent;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Teleport;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.WeatherChanger;
import com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills.Wormhole;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStatsLoader;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import com.pixelmonmod.pixelmon.enums.EnumGuiContainer;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import com.pixelmonmod.pixelmon.listener.BlockBreak;
import com.pixelmonmod.pixelmon.listener.ElevatorEvents;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.listener.PixelmonLootTables;
import com.pixelmonmod.pixelmon.listener.PixelmonPlayerTracker;
import com.pixelmonmod.pixelmon.listener.PlayerFallListener;
import com.pixelmonmod.pixelmon.listener.PokerusSpreader;
import com.pixelmonmod.pixelmon.listener.PorygonListener;
import com.pixelmonmod.pixelmon.listener.RotomListener;
import com.pixelmonmod.pixelmon.listener.SleepHandler;
import com.pixelmonmod.pixelmon.listener.WorldLoaded;
import com.pixelmonmod.pixelmon.listener.spawn.BlockMCMobSpawn;
import com.pixelmonmod.pixelmon.listener.spawn.ReplaceMCVillagers;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import com.pixelmonmod.pixelmon.spawning.ZygardeCellsSpawner;
import com.pixelmonmod.pixelmon.worldGeneration.GenericOreGenerator;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenBauxiteOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenDawnDuskOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenDen;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenEvolutionRock;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenFireStoneOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenFossils;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenGracideaFlowers;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenHiddenGrotto;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenIlexShrine;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenLeafStoneOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenMaxMushrooms;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenPixelmonTrees;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenPokeChest;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenThunderStoneOre;
import com.pixelmonmod.pixelmon.worldGeneration.WorldGenWaterStoneOre;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.DrownedWorld;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.DrownedWorldEventHandler;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpace;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpaceEventHandler;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpaceWorldGenFixListener;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureRegistry;
import com.pixelmonmod.pixelmon.worldGeneration.structure.worldGen.WorldGenGym;
import com.pixelmonmod.pixelmon.worldGeneration.structure.worldGen.WorldGenScatteredFeature;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.World;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class CommonProxy implements IGuiHandler {
   public void preInit() {
      ExtraStats.init();
   }

   public void init() {
      UltraSpace.setupBiomes();
      DrownedWorld.setupBiomes();
      GameRegistry.registerWorldGenerator(new WorldGenLeafStoneOre(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenWaterStoneOre(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenThunderStoneOre(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenFireStoneOre(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenDawnDuskOre(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenGracideaFlowers(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenMaxMushrooms(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenIlexShrine(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenBauxiteOre(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenFossils(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenEvolutionRock(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenPixelmonTrees(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenPokeChest(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenHiddenGrotto(), 1);
      GameRegistry.registerWorldGenerator(new WorldGenDen(), 1);
      GameRegistry.registerWorldGenerator(new GenericOreGenerator(PixelmonBlocks.amethystOre), 1);
      GameRegistry.registerWorldGenerator(new GenericOreGenerator(PixelmonBlocks.siliconOre), 1);
      GameRegistry.registerWorldGenerator(new GenericOreGenerator(PixelmonBlocks.sapphireOre), 1);
      GameRegistry.registerWorldGenerator(new GenericOreGenerator(PixelmonBlocks.rubyOre), 1);
      GameRegistry.registerWorldGenerator(new GenericOreGenerator(PixelmonBlocks.crystalOre), 1);
      BetterSpawnerConfig.load();

      try {
         ServerNPCRegistry.shopkeepers.registerShopItems();
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      try {
         ServerNPCRegistry.registerNPCS(ServerNPCRegistry.en_us);
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      try {
         PossibleTradeList.registerTrades();
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      try {
         StructureRegistry.registerStructures();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      try {
         DropItemRegistry.registerDropItems();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      try {
         RulesRegistry.registerRules();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      MinecraftForge.TERRAIN_GEN_BUS.register(BlockMCMobSpawn.class);
      MinecraftForge.EVENT_BUS.register(EntityPlayerExtension.class);
      MinecraftForge.EVENT_BUS.register(BlockMCMobSpawn.class);
      MinecraftForge.EVENT_BUS.register(SleepHandler.class);
      MinecraftForge.EVENT_BUS.register(PlayerFallListener.class);
      MinecraftForge.EVENT_BUS.register(PixelmonPlayerTracker.class);
      MinecraftForge.EVENT_BUS.register(BattleTickHandler.class);
      MinecraftForge.EVENT_BUS.register(WorldLoaded.class);
      MinecraftForge.EVENT_BUS.register(PixelmonLootTables.class);
      MinecraftForge.EVENT_BUS.register(PorygonListener.class);
      MinecraftForge.EVENT_BUS.register(BlockBreak.class);
      MinecraftForge.EVENT_BUS.register(ElevatorEvents.class);
      MinecraftForge.EVENT_BUS.register(UltraSpaceEventHandler.class);
      MinecraftForge.EVENT_BUS.register(DrownedWorldEventHandler.class);
      MinecraftForge.EVENT_BUS.register(new TickHandler());
      Pixelmon.EVENT_BUS.register(RotomListener.class);
      Pixelmon.EVENT_BUS.register(PokerusSpreader.class);
      if (PixelmonConfig.replaceMCVillagers) {
         MinecraftForge.EVENT_BUS.register(ReplaceMCVillagers.class);
      }

      MinecraftForge.TERRAIN_GEN_BUS.register(UltraSpaceWorldGenFixListener.class);
      Pixelmon.LOGGER.info("Registering Pokémon stats.");
      BaseStatsLoader.loadAllBaseStats();
      Pokedex.init();

      try {
         QuestRegistry.registerQuests(true);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if (PixelmonConfig.spawnStructures) {
         GameRegistry.registerWorldGenerator(new WorldGenScatteredFeature(), 0);
      }

      if (PixelmonConfig.spawnGyms) {
         GameRegistry.registerWorldGenerator(new WorldGenGym(), 0);
      }

      try {
         LanguageMap fallback = (LanguageMap)ReflectionHelper.getPrivateValue(I18n.class, (Object)null, 1);
         InputStream inputstream = LanguageMap.class.getResourceAsStream("/assets/pixelmon/lang/en_US.lang");
         ReflectionHelper.findMethod(LanguageMap.class, "inject", "inject", new Class[]{LanguageMap.class, InputStream.class}).invoke((Object)null, fallback, inputstream);
      } catch (Exception var3) {
      }

      this.loadDefaultMoveSkills();
   }

   public void postInit() {
      try {
         ITechnicalMove.mapToTypes();
         PixelmonSpawning.loadAndInitialize();
         ZygardeCellsSpawner.setup();
         RaidSpawningRegistry.registerRaidSpawning();
         if (Pixelmon.devEnvironment) {
            int next = 1;
            List collect = (List)AttackBase.ATTACKS.stream().sorted(Comparator.comparing(AttackBase::getAttackId)).collect(Collectors.toList());
            Iterator var3 = collect.iterator();

            while(var3.hasNext()) {
               AttackBase attackBase = (AttackBase)var3.next();
               if (attackBase.getAttackId() == next) {
                  ++next;
               }
            }

            System.out.println("Next open attack ID is " + next);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void registerRenderers() {
   }

   public void registerBlockModels() {
   }

   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      EnumGuiContainer container = EnumGuiContainer.getFromOrdinal(ID);
      if (container == null) {
         Pixelmon.LOGGER.warn("A plugin made a bad call to player.openGui() They need to replace it with OpenScreen.open() Offending screen:");
         (new Throwable()).printStackTrace();
         return null;
      } else {
         switch (container) {
            case MechanicalAnvil:
               return new ContainerMechanicalAnvil(player.field_71071_by, (TileEntityMechanicalAnvil)world.func_175625_s(new BlockPos(x, y, z)));
            case Infuser:
               return new ContainerInfuser(player.field_71071_by, (TileEntityInfuser)world.func_175625_s(new BlockPos(x, y, z)));
            case CookingPot:
               return new ContainerCookingPot(player.field_71071_by, (TileEntityCookingPot)world.func_175625_s(new BlockPos(x, y, z)));
            default:
               return null;
         }
      }
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return null;
   }

   public void registerKeyBindings() {
   }

   public void registerCommands() {
   }

   public void registerTickHandlers() {
   }

   public void registerInteractions() {
      EntityPixelmon.interactionList.add(new InteractionTM());
      EntityPixelmon.interactionList.add(new InteractionTechnicalMove());
      EntityPixelmon.interactionList.add(new InteractionEther());
      EntityPixelmon.interactionList.add(new InteractionElixir());
      EntityPixelmon.interactionList.add(new InteractionRareCandy());
      EntityPixelmon.interactionList.add(new InteractionDynamaxCandy());
      EntityPixelmon.interactionList.add(new InteractionMaxSoup());
      EntityPixelmon.interactionList.add(new InteractionVitamins());
      EntityPixelmon.interactionList.add(new InteractionZombie());
      EntityPixelmon.interactionList.add(new InteractionAbilityCapsule());
      EntityPixelmon.interactionList.add(new InteractionAbilityPatch());
      EntityPixelmon.interactionList.add(new InteractionMint());
      EntityPixelmon.interactionList.add(new InteractionPPUp());
      EntityPixelmon.interactionList.add(new InteractionBottleCap());
      EntityPixelmon.interactionList.add(new InteractionRainbowWing());
      EntityPixelmon.interactionList.add(new InteractionZygardeCube());
      EntityPixelmon.interactionList.add(new InteractionCurry());
      EntityPixelmon.interactionList.add(new InteractionEvolution());
      EntityPixelmon.interactionList.add(new InteractionChangeForm());
      EntityPixelmon.interactionList.add(new InteractionHeldItem());
      EntityPixelmon.interactionList.add(new InteractionPotion());
      EntityPixelmon.interactionList.add(new InteractionGaeBolg());
      EntityPixelmon.interactionList.add(new InteractionColorfulShake());
      EntityPixelmon.interactionList.add(new InteractionPerilousSoup());
      EntityPixelmon.interactionList.add(new InteractionDeveloper());
      EntityPixelmon.interactionList.add(new InteractionBerryJuice());
   }

   public void removeDungeonMobs() {
      if (!PixelmonConfig.allowNonPixelmonMobs) {
         ReflectionHelper.setPrivateValue(DungeonHooks.class, (Object)null, new ArrayList(), new String[]{"dungeonMobs"});
         DungeonHooks.addDungeonMob(new ResourceLocation("pig"), 100);
      }

   }

   public boolean resourceLocationExists(ResourceLocation resourceLocation) {
      return Pixelmon.class.getResourceAsStream("/assets/" + resourceLocation.func_110624_b() + "/" + resourceLocation.func_110623_a()) != null;
   }

   public BufferedInputStream getStreamForResourceLocation(ResourceLocation resourceLocation) {
      return new BufferedInputStream(Pixelmon.class.getResourceAsStream("/assets/" + resourceLocation.func_110624_b() + "/" + resourceLocation.func_110623_a()));
   }

   public void fixModelDefs() {
   }

   public void resetMouseOver() {
   }

   public void spawnEntitySafely(Entity entity, World worldObj) {
      worldObj.func_73046_m().func_152344_a(() -> {
         worldObj.func_72838_d(entity);
      });
   }

   public void sendPokeball(EntityPokeBall pokeball, EntityPlayerMP player) {
      player.field_70170_p.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187737_v, SoundCategory.NEUTRAL, 0.5F, 0.4F / (player.field_70170_p.field_73012_v.nextFloat() * 0.4F + 0.8F));
      player.field_70170_p.func_73046_m().func_152344_a(() -> {
         player.field_70170_p.func_72838_d(pokeball);
      });
   }

   public List loadDefaultMoveSkills() {
      MoveSkill.moveSkills.clear();
      MoveSkill.moveSkills.add(Forage.createMoveSkill());
      MoveSkill.moveSkills.add(Cut.createMoveSkill());
      MoveSkill.moveSkills.add(Dig.createMoveSkill());
      MoveSkill.moveSkills.add(Explode.createMoveSkill());
      MoveSkill.moveSkills.add(Fly.createMoveSkill());
      MoveSkill.moveSkills.add(Headbutt.createMoveSkill());
      MoveSkill.moveSkills.add(HealOther.createMoveSkill());
      MoveSkill.moveSkills.add(Ignite.createMoveSkill());
      MoveSkill.moveSkills.add(Lightning.createMoveSkill());
      MoveSkill.moveSkills.add(MegaEvolution.createMegaMoveSkill());
      MoveSkill.moveSkills.add(MegaEvolution.createPrimalMoveSkill());
      MoveSkill.moveSkills.add(MegaEvolution.createUltraMoveSkill());
      MoveSkill.moveSkills.add(MegaEvolution.createCrownedMoveSkill());
      MoveSkill.moveSkills.add(MegaEvolution.createChangeForm());
      MoveSkill.moveSkills.add(RockSmash.createMoveSkill());
      MoveSkill.moveSkills.add(SweetScent.createMoveSkill());
      MoveSkill.moveSkills.add(Teleport.createMoveSkill());
      MoveSkill.moveSkills.add(Wormhole.createMoveSkill());
      MoveSkill.moveSkills.add(Flash.createMoveSkill());
      MoveSkill.moveSkills.add(MountBoost.createMoveSkill());
      MoveSkill.moveSkills.add(GroundBirds.createMoveSkill());
      MoveSkill.moveSkills.add(WeatherChanger.createRainMoveSkill());
      MoveSkill.moveSkills.add(WeatherChanger.createSunMoveSkill());
      MoveSkill.moveSkills.add(Scare.createMoveSkill());
      MoveSkill.moveSkills.add(AnalysePokemon.createMoveSkill());
      MoveSkill.moveSkills.add(Smelt.createMoveSkill());
      MoveSkill.moveSkills.add(OreSense.createMoveSkill());
      return MoveSkill.moveSkills;
   }
}
