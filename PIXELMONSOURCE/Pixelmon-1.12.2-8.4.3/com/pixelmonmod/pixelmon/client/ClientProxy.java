package com.pixelmonmod.pixelmon.client;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.pixelmonmod.pixelmon.CommonProxy;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.trading.TradePair;
import com.pixelmonmod.pixelmon.battles.animations.particles.ParticleBreeding;
import com.pixelmonmod.pixelmon.battles.animations.particles.ParticleGastly;
import com.pixelmonmod.pixelmon.blocks.GenericModelBlock;
import com.pixelmonmod.pixelmon.blocks.multiBlocks.BlockGenericModelMultiblock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCookingPot;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityInfuser;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityMechanicalAnvil;
import com.pixelmonmod.pixelmon.client.camera.EntityCamera;
import com.pixelmonmod.pixelmon.client.gui.GuiCameraOverlay;
import com.pixelmonmod.pixelmon.client.gui.GuiCharm;
import com.pixelmonmod.pixelmon.client.gui.GuiChatOverlay;
import com.pixelmonmod.pixelmon.client.gui.GuiDoctor;
import com.pixelmonmod.pixelmon.client.gui.GuiHealer;
import com.pixelmonmod.pixelmon.client.gui.GuiItemDrops;
import com.pixelmonmod.pixelmon.client.gui.GuiMegaItem;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.GuiSelectStat;
import com.pixelmonmod.pixelmon.client.gui.GuiTrading;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiBattleRulesFixed;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiBattleRulesPlayer;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiTeamSelect;
import com.pixelmonmod.pixelmon.client.gui.chooseMoveset.GuiChooseMoveset;
import com.pixelmonmod.pixelmon.client.gui.curryDex.GuiCurryDex;
import com.pixelmonmod.pixelmon.client.gui.custom.GuiInputScreen;
import com.pixelmonmod.pixelmon.client.gui.custom.dialogue.GuiDialogue;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.CustomNoticeOverlay;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.CustomOverlay;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.CustomScoreboardOverlay;
import com.pixelmonmod.pixelmon.client.gui.fishingLog.GuiFishingLog;
import com.pixelmonmod.pixelmon.client.gui.fishingLog.GuiFishingLogInformation;
import com.pixelmonmod.pixelmon.client.gui.fishingLog.GuiFishingLogMenu;
import com.pixelmonmod.pixelmon.client.gui.inventory.InventoryDetectionTickHandler;
import com.pixelmonmod.pixelmon.client.gui.machines.cookingpot.GuiCookingPot;
import com.pixelmonmod.pixelmon.client.gui.machines.infuser.GuiInfuser;
import com.pixelmonmod.pixelmon.client.gui.machines.mechanicalanvil.GuiMechanicalAnvil;
import com.pixelmonmod.pixelmon.client.gui.mail.GuiMail;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiChattingNPC;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiNPCTrader;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiQuestGiverNPC;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiRelearner;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiSelectPartyPokemon;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiShopkeeper;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiTutor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiChattingNPCEditor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiConfirmDeleteNPC;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiCreateNPC;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiQuestGiverNPCEditor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiShopkeeperEditor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiTradeEditor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiTrainerEditor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiTutorEditor;
import com.pixelmonmod.pixelmon.client.gui.pc.GuiPC;
import com.pixelmonmod.pixelmon.client.gui.pokedex.GuiPokedex;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiEditedPlayer;
import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.GuiPokemonEditorParty;
import com.pixelmonmod.pixelmon.client.gui.ranchblock.GuiExtendRanch;
import com.pixelmonmod.pixelmon.client.gui.ranchblock.GuiRanchBlock;
import com.pixelmonmod.pixelmon.client.gui.selectMove.SelectMoveScreen;
import com.pixelmonmod.pixelmon.client.gui.spawner.GuiPixelmonSpawner;
import com.pixelmonmod.pixelmon.client.gui.starter.GuiChooseStarter;
import com.pixelmonmod.pixelmon.client.gui.statueEditor.GuiStatueEditor;
import com.pixelmonmod.pixelmon.client.gui.vendingmachine.GuiVendingMachine;
import com.pixelmonmod.pixelmon.client.gui.zygarde.GuiZygardeCube;
import com.pixelmonmod.pixelmon.client.gui.zygarde.GuiZygardeReassemblyUnit;
import com.pixelmonmod.pixelmon.client.keybindings.ActionKey;
import com.pixelmonmod.pixelmon.client.keybindings.ChooseMoveSkillKey;
import com.pixelmonmod.pixelmon.client.keybindings.Descend;
import com.pixelmonmod.pixelmon.client.keybindings.HoverKey;
import com.pixelmonmod.pixelmon.client.keybindings.MinimizeMaximizeOverlayKey;
import com.pixelmonmod.pixelmon.client.keybindings.NextPokemonKey;
import com.pixelmonmod.pixelmon.client.keybindings.OldMovementHandler;
import com.pixelmonmod.pixelmon.client.keybindings.OptionsKey;
import com.pixelmonmod.pixelmon.client.keybindings.PixelmonMouseHelper;
import com.pixelmonmod.pixelmon.client.keybindings.PokedexKey;
import com.pixelmonmod.pixelmon.client.keybindings.PreviousPokemonKey;
import com.pixelmonmod.pixelmon.client.keybindings.QuestCycleKey;
import com.pixelmonmod.pixelmon.client.keybindings.QuestJournalKey;
import com.pixelmonmod.pixelmon.client.keybindings.SendPokemonKey;
import com.pixelmonmod.pixelmon.client.keybindings.SpectateKey;
import com.pixelmonmod.pixelmon.client.keybindings.TrainerCardKey;
import com.pixelmonmod.pixelmon.client.keybindings.UseMoveSkillKey;
import com.pixelmonmod.pixelmon.client.keybindings.WikiKey;
import com.pixelmonmod.pixelmon.client.listener.MouseOverPlayer;
import com.pixelmonmod.pixelmon.client.listener.UnderWaterFog;
import com.pixelmonmod.pixelmon.client.listener.WallpapersListener;
import com.pixelmonmod.pixelmon.client.models.ResourceLoader;
import com.pixelmonmod.pixelmon.client.models.items.ItemPixelmonSpriteModel;
import com.pixelmonmod.pixelmon.client.models.items.ItemUIElementModel;
import com.pixelmonmod.pixelmon.client.music.MusicHelper;
import com.pixelmonmod.pixelmon.client.particle.ParticleEvents;
import com.pixelmonmod.pixelmon.client.render.EmissiveTextures;
import com.pixelmonmod.pixelmon.client.render.ParticleBlocks;
import com.pixelmonmod.pixelmon.client.render.RenderBike;
import com.pixelmonmod.pixelmon.client.render.RenderBreeding;
import com.pixelmonmod.pixelmon.client.render.RenderDen;
import com.pixelmonmod.pixelmon.client.render.RenderHook;
import com.pixelmonmod.pixelmon.client.render.RenderNPC;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.client.render.RenderPokeball;
import com.pixelmonmod.pixelmon.client.render.RenderPokestop;
import com.pixelmonmod.pixelmon.client.render.RenderStatue;
import com.pixelmonmod.pixelmon.client.render.blockReveal.BlockRevealRenderer;
import com.pixelmonmod.pixelmon.client.render.custom.FontRendererPixelmon;
import com.pixelmonmod.pixelmon.client.render.custom.RenderPixelmonPainting;
import com.pixelmonmod.pixelmon.client.render.item.ItemRendererShrineOrb;
import com.pixelmonmod.pixelmon.client.render.item.PixelmonTEISR;
import com.pixelmonmod.pixelmon.client.render.layers.ArmorStandLayerEquippables;
import com.pixelmonmod.pixelmon.client.render.layers.LayerCharms;
import com.pixelmonmod.pixelmon.client.render.layers.LayerEquippables;
import com.pixelmonmod.pixelmon.client.render.layers.LayerMegaItems;
import com.pixelmonmod.pixelmon.client.render.layers.LayerSash;
import com.pixelmonmod.pixelmon.client.render.layers.PixelmonLayerHead;
import com.pixelmonmod.pixelmon.client.render.layers.PixelmonLayerRobe;
import com.pixelmonmod.pixelmon.client.render.player.PixelRenderPlayer;
import com.pixelmonmod.pixelmon.client.render.player.RenderEvents;
import com.pixelmonmod.pixelmon.client.render.tileEntities.RanchBlockHighlightRender;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.commands.client.DevScaleCommand;
import com.pixelmonmod.pixelmon.commands.client.RedeemCommand;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonBlocksApricornTrees;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsLures;
import com.pixelmonmod.pixelmon.config.TileEntityRegistry;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.EntityPokestop;
import com.pixelmonmod.pixelmon.entities.EntityWormhole;
import com.pixelmonmod.pixelmon.entities.bikes.EntityBike;
import com.pixelmonmod.pixelmon.entities.custom.EntityPixelmonPainting;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.npcs.NPCFisherman;
import com.pixelmonmod.pixelmon.entities.npcs.NPCNurseJoy;
import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
import com.pixelmonmod.pixelmon.entities.npcs.NPCRelearner;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrader;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityBreeding;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import com.pixelmonmod.pixelmon.entities.projectiles.EntityHook;
import com.pixelmonmod.pixelmon.enums.EnumBreedingParticles;
import com.pixelmonmod.pixelmon.enums.EnumGuiContainer;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumPixelmonParticles;
import com.pixelmonmod.pixelmon.enums.items.EnumCharms;
import com.pixelmonmod.pixelmon.listener.ClientNetworkListener;
import com.pixelmonmod.pixelmon.quests.client.editor.GuiQuestEditor;
import com.pixelmonmod.pixelmon.quests.client.rendering.QuestMarkerRenderer;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import com.pixelmonmod.pixelmon.util.helpers.RCFileHelper;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.wormhole.RenderWormhole;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.lang3.Validate;

public class ClientProxy extends CommonProxy {
   public static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor((r) -> {
      return new Thread(r, "Pixelmon client thread");
   });
   public static ClientBattleManager battleManager = new ClientBattleManager();
   public static ArrayList moveSkills = new ArrayList();
   public static TradePair currentTradePair = null;
   public static boolean playerHasTradeRequestPokemon = false;
   public static PixelmonMouseHelper mouseHelper = new PixelmonMouseHelper();
   public static EntityCamera camera;
   public static ActionKey actionKeyBind;
   public static UseMoveSkillKey useMoveSkillKeyBind;
   public static ChooseMoveSkillKey chooseMoveSkillKey;
   public static PokedexKey pokedexKeyBind;
   public static TrainerCardKey trainerCardKeyBind;
   public static WikiKey wikiKeyBind;
   public static SpectateKey spectateKeyBind;
   public static QuestJournalKey questJournalKeyBind;
   public static QuestCycleKey questCycleKeyBind;
   public static KeyBinding pcSearchKeyBind;
   public static KeyBinding pcRenameKeyBind;
   public static KeyBinding pcWallpaperKeyBind;
   static ConcurrentHashMap cachedSkins = new ConcurrentHashMap();
   static List invaildSkins = Lists.newArrayList();

   public void preInit() {
      super.preInit();
      this.addPokemonRenderers();
      DefaultServerList.tryFetchDefaultServers();
      MinecraftForge.EVENT_BUS.register(new ParticleEvents());
   }

   public void init() {
      FontRendererPixelmon.init();
      super.init();
      Minecraft mc = Minecraft.func_71410_x();
      MinecraftForge.EVENT_BUS.register(GuiCameraOverlay.class);
      MinecraftForge.EVENT_BUS.register(ClientNetworkListener.class);
      PlayerExtraDataStore.loadClientData();
      mc.field_71417_B = mouseHelper;
      ((IReloadableResourceManager)mc.func_110442_L()).func_110542_a(new WallpapersListener());
      ((IReloadableResourceManager)mc.func_110442_L()).func_110542_a(new EmissiveTextures());
      ((IReloadableResourceManager)mc.func_110442_L()).func_110542_a(new ResourceLoader());
   }

   public void postInit() {
      super.postInit();
      Minecraft mc = Minecraft.func_71410_x();
      mc.field_110448_aq = new PixelmonResourcePackRepository(mc.field_130070_K, new File(mc.field_71412_D, "server-resource-packs"), mc.field_110450_ap, mc.field_110452_an, mc.field_71474_y);
      MusicHelper.init(Minecraft.func_71410_x());
      PixelmonItemsLures.registerItemLayers();
      PixelmonItems.scrollOfDarkness.setTileEntityItemStackRenderer(PixelmonTEISR.instance);
      PixelmonItems.scrollOfWaters.setTileEntityItemStackRenderer(PixelmonTEISR.instance);
   }

   public void registerBlockModels() {
      PixelmonBlocks.registerModels();
   }

   public void registerRenderers() {
      TileEntityRegistry.registerRenderers();
      MinecraftForge.EVENT_BUS.register(new CustomOverlay());
      MinecraftForge.EVENT_BUS.register(new GuiPixelmonOverlay());
      MinecraftForge.EVENT_BUS.register(new GuiChatOverlay(Minecraft.func_71410_x()));
      MinecraftForge.EVENT_BUS.register(new UnderWaterFog());
      MinecraftForge.EVENT_BUS.register(new RanchBlockHighlightRender());
      MinecraftForge.EVENT_BUS.register(new MouseOverPlayer());
      MinecraftForge.EVENT_BUS.register(new RenderEvents());
      MinecraftForge.EVENT_BUS.register(BlockRevealRenderer.class);
      MinecraftForge.EVENT_BUS.register(new QuestMarkerRenderer());
      RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
      renderManager.field_178636_l.remove("default");
      renderManager.field_178636_l.remove("slim");
      renderManager.field_178636_l.put("default", new PixelRenderPlayer(renderManager));
      renderManager.field_178636_l.put("slim", new PixelRenderPlayer(renderManager, true));
      RenderPlayer rp = (RenderPlayer)renderManager.field_178636_l.get("default");
      rp.func_177094_a(new PixelmonLayerHead(rp));
      rp.func_177094_a(new LayerSash(rp));
      rp.func_177094_a(new PixelmonLayerRobe(rp));
      rp.func_177094_a(new LayerMegaItems(rp));
      rp.func_177094_a(new LayerCharms(rp));
      rp.func_177094_a(new LayerEquippables(rp));
      rp = (RenderPlayer)renderManager.field_178636_l.get("slim");
      rp.func_177094_a(new PixelmonLayerHead(rp));
      rp.func_177094_a(new LayerSash(rp));
      rp.func_177094_a(new PixelmonLayerRobe(rp));
      rp.func_177094_a(new LayerMegaItems(rp));
      rp.func_177094_a(new LayerCharms(rp));
      rp.func_177094_a(new LayerEquippables(rp));
      Render renderArmorStand = renderManager.func_78715_a(EntityArmorStand.class);
      RenderArmorStand armorStandRenderer = (RenderArmorStand)renderArmorStand;
      armorStandRenderer.func_177094_a(new ArmorStandLayerEquippables(armorStandRenderer));
   }

   @SubscribeEvent
   public void onModelBakeEvent(ModelBakeEvent event) {
      this.bakeOrb(event, ItemRendererShrineOrb.uno);
      this.bakeOrb(event, ItemRendererShrineOrb.dos);
      this.bakeOrb(event, ItemRendererShrineOrb.tres);
      this.bakeModels(event);
   }

   private void bakeModels(ModelBakeEvent event) {
      IBakedModel existingModel = (IBakedModel)event.getModelRegistry().func_82594_a(ItemPixelmonSpriteModel.modelResourceLocation);
      if (existingModel != null) {
         ItemPixelmonSpriteModel customModel = new ItemPixelmonSpriteModel(existingModel);
         event.getModelRegistry().func_82595_a(ItemPixelmonSpriteModel.modelResourceLocation, customModel);
      }

      IBakedModel existingModel2 = (IBakedModel)event.getModelRegistry().func_82594_a(ItemUIElementModel.modelResourceLocation);
      if (existingModel2 != null) {
         ItemUIElementModel customModel = new ItemUIElementModel(existingModel2);
         event.getModelRegistry().func_82595_a(ItemUIElementModel.modelResourceLocation, customModel);
      }

   }

   private void bakeOrb(ModelBakeEvent event, ModelResourceLocation resourceLocation) {
      Object resource = event.getModelRegistry().func_82594_a(resourceLocation);
      if (resource != null) {
         IBakedModel existingModel = (IBakedModel)resource;
         ItemRendererShrineOrb customModel = new ItemRendererShrineOrb(existingModel);
         event.getModelRegistry().func_82595_a(resourceLocation, customModel);
      }

   }

   @SubscribeEvent
   public void stitcherEventPre(TextureStitchEvent.Pre event) {
      event.getMap().func_174942_a(new ResourceLocation("pixelmon:items/back"));
      event.getMap().func_174942_a(new ResourceLocation("pixelmon:items/front"));
      event.getMap().func_174942_a(new ResourceLocation("pixelmon:items/unoorb"));
      event.getMap().func_174942_a(new ResourceLocation("pixelmon:items/dosorb"));
      event.getMap().func_174942_a(new ResourceLocation("pixelmon:items/tresorb"));
      this.loadSpritesToAtlas(event);
   }

   public void loadSpritesToAtlas(TextureStitchEvent.Pre event) {
      Path path;
      List pngPaths;
      Iterator var4;
      Path pngPath;
      String fileName;
      String folderName;
      try {
         path = RCFileHelper.pathFromResourceLocation(new ResourceLocation("pixelmon", "textures/sprites"));
         pngPaths = RCFileHelper.listFilesRecursively(path, (entry) -> {
            return entry.getFileName().toString().endsWith(".png");
         }, true);

         for(var4 = pngPaths.iterator(); var4.hasNext(); event.getMap().func_174942_a(new ResourceLocation("pixelmon", "sprites/" + folderName + fileName.substring(0, fileName.length() - 4)))) {
            pngPath = (Path)var4.next();
            fileName = pngPath.getFileName().toString();
            folderName = pngPath.getParent().getFileName().toString();
            if (!folderName.isEmpty()) {
               folderName = folderName + "/";
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      try {
         path = RCFileHelper.pathFromResourceLocation(new ResourceLocation("pixelmon", "textures/gui/uielements"));
         pngPaths = RCFileHelper.listFilesRecursively(path, (entry) -> {
            return entry.getFileName().toString().endsWith(".png");
         }, true);

         for(var4 = pngPaths.iterator(); var4.hasNext(); event.getMap().func_174942_a(new ResourceLocation("pixelmon", "gui/" + folderName + fileName.substring(0, fileName.length() - 4)))) {
            pngPath = (Path)var4.next();
            fileName = pngPath.getFileName().toString();
            folderName = pngPath.getParent().getFileName().toString();
            if (!folderName.isEmpty()) {
               folderName = folderName + "/";
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void fixModelDefs() {
      PixelmonStateMapper pixelmonStateMapper = new PixelmonStateMapper();
      ModelLoader.setCustomStateMapper(PixelmonBlocks.hiddenIronDoor, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.hiddenWoodenDoor, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.masterChest, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.pokeChest, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.ultraChest, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.beastChest, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.mechanicalAnvil, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.cookingPot, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.infuser, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.hiddenPressurePlate, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.standingScroll, pixelmonStateMapper);
      ModelLoader.setCustomStateMapper(PixelmonBlocks.hangingScroll, pixelmonStateMapper);
      Field[] var2 = PixelmonBlocks.class.getFields();
      int var3 = var2.length;

      int var4;
      Field field;
      Block block;
      for(var4 = 0; var4 < var3; ++var4) {
         field = var2[var4];

         try {
            if (field.get((Object)null) instanceof Block) {
               block = (Block)field.get((Object)null);
               if (block.func_149645_b(block.func_176223_P()) == EnumBlockRenderType.INVISIBLE || block instanceof GenericModelBlock || block instanceof BlockGenericModelMultiblock) {
                  ModelLoader.setCustomStateMapper(block, pixelmonStateMapper);
               }
            }
         } catch (IllegalAccessException var8) {
            var8.printStackTrace();
         }
      }

      var2 = PixelmonBlocksApricornTrees.class.getFields();
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         field = var2[var4];

         try {
            if (field.get((Object)null) instanceof Block) {
               block = (Block)field.get((Object)null);
               if (block.func_149645_b(block.func_176223_P()) == EnumBlockRenderType.MODEL) {
                  ModelLoader.setCustomStateMapper(block, pixelmonStateMapper);
               }
            }
         } catch (IllegalAccessException var7) {
            var7.printStackTrace();
         }
      }

   }

   public void registerKeyBindings() {
      MinecraftForge.EVENT_BUS.register(this);
      SendPokemonKey k1 = new SendPokemonKey();
      ClientRegistry.registerKeyBinding(k1);
      MinecraftForge.EVENT_BUS.register(k1);
      NextPokemonKey k2 = new NextPokemonKey();
      ClientRegistry.registerKeyBinding(k2);
      MinecraftForge.EVENT_BUS.register(k2);
      PreviousPokemonKey k3 = new PreviousPokemonKey();
      ClientRegistry.registerKeyBinding(k3);
      MinecraftForge.EVENT_BUS.register(k3);
      MinimizeMaximizeOverlayKey k4 = new MinimizeMaximizeOverlayKey();
      ClientRegistry.registerKeyBinding(k4);
      MinecraftForge.EVENT_BUS.register(k4);
      Descend k5 = new Descend();
      ClientRegistry.registerKeyBinding(k5);
      MinecraftForge.EVENT_BUS.register(k5);
      HoverKey k6 = new HoverKey();
      ClientRegistry.registerKeyBinding(k6);
      MinecraftForge.EVENT_BUS.register(k6);
      spectateKeyBind = new SpectateKey();
      ClientRegistry.registerKeyBinding(spectateKeyBind);
      MinecraftForge.EVENT_BUS.register(spectateKeyBind);
      questJournalKeyBind = new QuestJournalKey();
      ClientRegistry.registerKeyBinding(questJournalKeyBind);
      MinecraftForge.EVENT_BUS.register(questJournalKeyBind);
      questCycleKeyBind = new QuestCycleKey();
      ClientRegistry.registerKeyBinding(questCycleKeyBind);
      MinecraftForge.EVENT_BUS.register(questCycleKeyBind);
      actionKeyBind = new ActionKey();
      ClientRegistry.registerKeyBinding(actionKeyBind);
      MinecraftForge.EVENT_BUS.register(actionKeyBind);
      useMoveSkillKeyBind = new UseMoveSkillKey();
      ClientRegistry.registerKeyBinding(useMoveSkillKeyBind);
      MinecraftForge.EVENT_BUS.register(useMoveSkillKeyBind);
      chooseMoveSkillKey = new ChooseMoveSkillKey();
      ClientRegistry.registerKeyBinding(chooseMoveSkillKey);
      MinecraftForge.EVENT_BUS.register(chooseMoveSkillKey);
      pokedexKeyBind = new PokedexKey();
      ClientRegistry.registerKeyBinding(pokedexKeyBind);
      MinecraftForge.EVENT_BUS.register(pokedexKeyBind);
      trainerCardKeyBind = new TrainerCardKey();
      ClientRegistry.registerKeyBinding(trainerCardKeyBind);
      MinecraftForge.EVENT_BUS.register(trainerCardKeyBind);
      wikiKeyBind = new WikiKey();
      ClientRegistry.registerKeyBinding(wikiKeyBind);
      MinecraftForge.EVENT_BUS.register(wikiKeyBind);
      ClientRegistry.registerKeyBinding(pcSearchKeyBind = new KeyBinding("key.pcsearch", 31, "key.categories.pixelmon"));
      ClientRegistry.registerKeyBinding(pcRenameKeyBind = new KeyBinding("key.pcrename", 19, "key.categories.pixelmon"));
      ClientRegistry.registerKeyBinding(pcWallpaperKeyBind = new KeyBinding("key.pcwallpaper", 17, "key.categories.pixelmon"));
      MinecraftForge.EVENT_BUS.register(OldMovementHandler.class);
      OptionsKey k7 = new OptionsKey();
      ClientRegistry.registerKeyBinding(k7);
      MinecraftForge.EVENT_BUS.register(k7);
   }

   public void registerCommands() {
      ClientCommandHandler.instance.func_71560_a(new RedeemCommand());
      if (Pixelmon.devEnvironment) {
         ClientCommandHandler.instance.func_71560_a(new DevScaleCommand());
      }

   }

   private void addPokemonRenderers() {
      RenderingRegistry.registerEntityRenderingHandler(EntityPokeBall.class, RenderPokeball::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityHook.class, RenderHook::new);
      RenderingRegistry.registerEntityRenderingHandler(NPCTrainer.class, RenderNPC::new);
      RenderingRegistry.registerEntityRenderingHandler(NPCChatting.class, RenderNPC::new);
      RenderingRegistry.registerEntityRenderingHandler(NPCQuestGiver.class, RenderNPC::new);
      RenderingRegistry.registerEntityRenderingHandler(NPCTrader.class, RenderNPC::new);
      RenderingRegistry.registerEntityRenderingHandler(NPCRelearner.class, RenderNPC::new);
      RenderingRegistry.registerEntityRenderingHandler(NPCTutor.class, RenderNPC::new);
      RenderingRegistry.registerEntityRenderingHandler(NPCNurseJoy.class, RenderNPC::new);
      RenderingRegistry.registerEntityRenderingHandler(NPCFisherman.class, RenderNPC::new);
      RenderingRegistry.registerEntityRenderingHandler(NPCShopkeeper.class, RenderNPC::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityStatue.class, RenderStatue::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityPixelmon.class, RenderPixelmon::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityBreeding.class, RenderBreeding::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityCamera.class, RenderInvisibleCamera::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityWormhole.class, RenderWormhole::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityBike.class, RenderBike::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityPixelmonPainting.class, RenderPixelmonPainting::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityPokestop.class, RenderPokestop::new);
      RenderingRegistry.registerEntityRenderingHandler(EntityDen.class, RenderDen::new);
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      EnumGuiContainer container = EnumGuiContainer.getFromOrdinal(ID);
      if (container == null) {
         return null;
      } else {
         if (!Minecraft.func_71410_x().func_152345_ab()) {
            System.out.println("Dodgy gui call from non-main thread for " + container);
         }

         switch (container) {
            case MechanicalAnvil:
               return new GuiMechanicalAnvil(player.field_71071_by, (TileEntityMechanicalAnvil)world.func_175625_s(new BlockPos(x, y, z)));
            case Infuser:
               return new GuiInfuser(player.field_71071_by, (TileEntityInfuser)world.func_175625_s(new BlockPos(x, y, z)));
            case CookingPot:
               return new GuiCookingPot(player.field_71071_by, (TileEntityCookingPot)world.func_175625_s(new BlockPos(x, y, z)));
            default:
               return null;
         }
      }
   }

   public static GuiScreen createScreen(EntityPlayer player, EnumGuiScreen gui, int[] data) {
      switch (gui) {
         case ChooseStarter:
            return new GuiChooseStarter();
         case Dialogue:
            return new GuiDialogue();
         case Healer:
            return new GuiHealer();
         case PC:
            return new GuiPC();
         case PickPokemon:
            return new GuiSelectPartyPokemon(data[0], data[1]);
         case Pokedex:
            return new GuiPokedex(data[0]);
         case Battle:
            return new GuiBattle();
         case Trading:
            return new GuiTrading(data[0], data[1], data[2]);
         case Doctor:
            return new GuiDoctor();
         case ItemDrops:
            return new GuiItemDrops();
         case PixelmonSpawner:
            return new GuiPixelmonSpawner(data[0], data[1], data[2]);
         case TrainerEditor:
            return new GuiTrainerEditor(data[0]);
         case ChooseMoveset:
            return new GuiChooseMoveset(ClientStorageManager.party.get(data[0]));
         case RanchBlock:
            return new GuiRanchBlock(player.field_70170_p, data[0], data[1], data[2]);
         case NPCTrade:
            return new GuiTradeEditor(data[0]);
         case NPCTraderGui:
            return new GuiNPCTrader(data[0]);
         case StatueEditor:
            return new GuiStatueEditor(data[0]);
         case ExtendRanch:
            return new GuiExtendRanch(player.field_70170_p, data[0], data[1], data[2]);
         case InputScreen:
            return new GuiInputScreen();
         case CreateNpc:
            return new GuiCreateNPC(new BlockPos(data[0], data[1], data[2]));
         case DeleteNpc:
            return new GuiConfirmDeleteNPC(data[0]);
         case NPCChatEditor:
            return new GuiChattingNPCEditor(data[0]);
         case NPCChat:
            return new GuiChattingNPC(data[0]);
         case NPCQuestGiverEditor:
            return new GuiQuestGiverNPCEditor(data[0]);
         case NPCQuestGiver:
            return new GuiQuestGiverNPC(data[0]);
         case Relearner:
            return new GuiRelearner(ClientStorageManager.party.get(data[0]), data[1]);
         case Tutor:
            return new GuiTutor(ClientStorageManager.party.get(data[0]), data[1]);
         case TutorEditor:
            return new GuiTutorEditor(data[0]);
         case HealerNurseJoy:
            return new GuiHealer(data[0]);
         case Shopkeeper:
            return new GuiShopkeeper(data[0]);
         case ShopkeeperEditor:
            return new GuiShopkeeperEditor(data[0]);
         case VendingMachine:
            return new GuiVendingMachine(new BlockPos(data[0], data[1], data[2]));
         case PokemonEditor:
            return new GuiPokemonEditorParty();
         case EditedPlayer:
            return new GuiEditedPlayer();
         case MegaItem:
            return new GuiMegaItem(data.length != 0);
         case BattleRulesPlayer:
            return new GuiBattleRulesPlayer(data[0], data[1] == 1);
         case BattleRulesFixed:
            return new GuiBattleRulesFixed();
         case TeamSelect:
            return new GuiTeamSelect();
         case Mail:
            return new GuiMail(player.func_184586_b(EnumHand.values()[data[0]]));
         case SelectMove:
            return new SelectMoveScreen(data[0], data[1]);
         case ShinyCharm:
            return new GuiCharm(EnumCharms.Shiny);
         case OvalCharm:
            return new GuiCharm(EnumCharms.Oval);
         case ExpCharm:
            return new GuiCharm(EnumCharms.Exp);
         case CatchingCharm:
            return new GuiCharm(EnumCharms.Catching);
         case MarkCharm:
            return new GuiCharm(EnumCharms.Mark);
         case BottleCap:
            return new GuiSelectStat(data);
         case CurryDex:
            return new GuiCurryDex(data);
         case FishingLog:
            return new GuiFishingLog(data[0], Arrays.copyOfRange(data, 1, data.length));
         case FishingLogMenu:
            return new GuiFishingLogMenu(data);
         case FishingLogInformation:
            return new GuiFishingLogInformation(data[0], data[1], data[2], data);
         case ZygardeAssembly:
            return new GuiZygardeReassemblyUnit(data[0], data[1], data[2]);
         case ZygardeCube:
            return new GuiZygardeCube(EnumHand.values()[data[0]], data[1]);
         case QuestEditor:
            return new GuiQuestEditor();
         default:
            return null;
      }
   }

   public static File getMinecraftDir() {
      return Minecraft.func_71410_x().field_71412_D;
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load event) {
      CustomNoticeOverlay.resetNotice();
      CustomScoreboardOverlay.resetBoard();
   }

   @SubscribeEvent
   public void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
      battleManager.battleEnded = true;
   }

   @SubscribeEvent
   public void onWorldUnload(WorldEvent.Unload event) {
      Pixelmon.freeze = false;
      CustomNoticeOverlay.resetNotice();
      CustomScoreboardOverlay.resetBoard();
   }

   public static void spawnParticle(World w, double d1, double d2, double d3, Block stone) {
      ParticleBlocks fx = new ParticleBlocks(w, d1, d2, d3, 0.0, 0.0, 0.0, w.func_180495_p(new BlockPos(d1, d2, d3)));
      FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fx);
   }

   public static void spawnParticle(EnumPixelmonParticles particle, World worldObj, double posX, double posY, double posZ, boolean isShiny) {
      try {
         Object fx;
         if (particle.particleClass == ParticleGastly.class) {
            fx = new ParticleGastly(worldObj, posX, posY, posZ, 0.0, 0.0, 0.0, isShiny);
         } else {
            fx = (Particle)particle.particleClass.getConstructor(World.class, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE).newInstance(worldObj, posX, posY, posZ, 0.0, 0.0, 0.0);
         }

         Minecraft.func_71410_x().field_71452_i.func_78873_a((Particle)fx);
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public static void spawnParticle(EnumBreedingParticles particle, World worldObj, double posX, double posY, double posZ, boolean isShiny) {
      try {
         Particle fx = new ParticleBreeding(worldObj, posX, posY, posZ, 0.0, 0.0, 0.0, particle);
         Minecraft.func_71410_x().field_71452_i.func_78873_a(fx);
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public void registerTickHandlers() {
      MinecraftForge.EVENT_BUS.register(new InventoryDetectionTickHandler());
      MinecraftForge.EVENT_BUS.register(battleManager);
   }

   public static ResourceLocation bindPlayerTexture(String username) {
      if (!username.isEmpty() && !invaildSkins.contains(username)) {
         if (cachedSkins.containsKey(username)) {
            return (ResourceLocation)cachedSkins.get(username);
         } else {
            GameProfile profile = new GameProfile((UUID)null, username);
            profile = TileEntitySkull.func_174884_b(profile);
            if (profile.isComplete() && profile.getId().version() == 4 && profile.getProperties().containsKey("textures")) {
               Minecraft minecraft = Minecraft.func_71410_x();
               Map map = minecraft.func_152342_ad().func_152788_a(profile);
               ResourceLocation resourcelocation;
               if (map.containsKey(Type.SKIN)) {
                  resourcelocation = minecraft.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
                  cachedSkins.put(username, resourcelocation);
               } else {
                  resourcelocation = DefaultPlayerSkin.func_177335_a();
               }

               return resourcelocation;
            } else {
               invaildSkins.add(username);
               return DefaultPlayerSkin.func_177335_a();
            }
         }
      } else {
         return DefaultPlayerSkin.func_177335_a();
      }
   }

   public boolean resourceLocationExists(ResourceLocation resourceLocation) {
      try {
         return Minecraft.func_71410_x().func_110442_L().func_110536_a(resourceLocation).func_110527_b() != null;
      } catch (IOException var3) {
         return false;
      }
   }

   public BufferedInputStream getStreamForResourceLocation(ResourceLocation resourceLocation) {
      try {
         return new BufferedInputStream(Minecraft.func_71410_x().func_110442_L().func_110536_a(resourceLocation).func_110527_b());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void resetMouseOver() {
      Minecraft.func_71410_x().field_71476_x = null;
   }

   public static ListenableFuture scheduleNextTick(Callable callable) {
      Validate.notNull(callable);
      ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callable);
      Queue scheduledTasks = (Queue)ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.func_71410_x(), "scheduledTasks", "field_152351_aB");
      synchronized(scheduledTasks) {
         scheduledTasks.add(listenablefuturetask);
         return listenablefuturetask;
      }
   }

   public static void spawnBoostedTreeParticles(World worldObj, int x, int y, int z, int stage) {
      for(int i = 0; i < 20; ++i) {
         float yplus = worldObj.field_73012_v.nextFloat();
         if (stage <= 3) {
            yplus *= 2.0F;
         }

         spawnParticle(EnumPixelmonParticles.shiny, worldObj, (double)((float)x + worldObj.field_73012_v.nextFloat()), (double)((float)y + yplus), (double)((float)z + worldObj.field_73012_v.nextFloat()), true);
      }

   }

   public List loadDefaultMoveSkills() {
      moveSkills = new ArrayList();
      List s = super.loadDefaultMoveSkills();
      moveSkills.addAll(s);
      return s;
   }

   public static ArrayList getMoveSkills(Pokemon pokemon) {
      ArrayList moveSkills = Lists.newArrayList(ClientProxy.moveSkills);
      moveSkills.removeIf((moveSkill) -> {
         return !moveSkill.hasMoveSkill(pokemon);
      });
      return moveSkills;
   }
}
