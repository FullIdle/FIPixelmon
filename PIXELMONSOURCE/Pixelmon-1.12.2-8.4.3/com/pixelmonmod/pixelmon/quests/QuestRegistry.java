package com.pixelmonmod.pixelmon.quests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.quests.actions.IAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.AbandonableAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.CommandAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.DialogueInjectAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.EndDialogueAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.FinishQuestAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.FlagAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.GiveItemAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.GivePokemonAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.GiveXPAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.KeyItemAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.PotionAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.SendMessageAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.ServerCosmeticAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.SetStageAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.SoundAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.SpawnPokemonAction;
import com.pixelmonmod.pixelmon.quests.actions.actions.TeleportAction;
import com.pixelmonmod.pixelmon.quests.listeners.BlockListeners;
import com.pixelmonmod.pixelmon.quests.listeners.EntityListeners;
import com.pixelmonmod.pixelmon.quests.listeners.ItemListeners;
import com.pixelmonmod.pixelmon.quests.listeners.PlayerListeners;
import com.pixelmonmod.pixelmon.quests.listeners.TickListeners;
import com.pixelmonmod.pixelmon.quests.objectives.IObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.battle.AttackObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.battle.TrainerObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.entity.DialogueInjectObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.entity.EntityInteractObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.entity.EntityVicinityObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.entity.NPCObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.entity.PokemonObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.entity.TileEntityVicinityObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertBooleanObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertConcatenationObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertDecimalObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertIntegerObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertNPCObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertNameObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertOperationObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertSpecObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertStringObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters.InsertTypingObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.location.AbsolutePositionObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.meta.BlockerObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.meta.DateObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.meta.FlagObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.meta.FollowthroughObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.meta.QueryObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.meta.RandomObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.meta.ServerTimeObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.meta.TestDataObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.meta.TimerObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.player.ItemObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.world.DimensionObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.world.StructureObjective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.world.WorldTimeObjective;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.QuestTypeAdapter;
import com.pixelmonmod.pixelmon.util.helpers.RCFileHelper;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class QuestRegistry {
   public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(Quest.class, new QuestTypeAdapter()).disableHtmlEscaping().create();
   private static final QuestRegistry instance = new QuestRegistry();
   private final HashMap quests = new HashMap();
   private final HashMap actions = new HashMap();
   private final HashMap objectives = new HashMap();

   public static QuestRegistry getInstance() {
      return instance;
   }

   private HashMap getQuests() {
      return this.quests;
   }

   public void addQuest(Quest quest) {
      try {
         quest.parseAndMap();
         this.quests.put(quest.getFilename(), quest);
      } catch (Exception var3) {
         Pixelmon.LOGGER.error("Unable to parse quest " + quest.getFilename());
         var3.printStackTrace();
      }

   }

   public void createOrUpdateQuest(Quest quest) {
      this.addQuest(quest);
      this.writeQuest(quest);
   }

   public void removeQuest(Quest quest) {
      this.quests.remove(quest.getFilename());
   }

   public void deleteQuest(Quest quest) {
      this.removeQuest(quest);
      this.destroyQuest(quest);
   }

   private HashMap getActions() {
      return this.actions;
   }

   private HashMap getObjectives() {
      return this.objectives;
   }

   public Quest getQuest(String filepath) {
      return (Quest)this.quests.get(filepath);
   }

   public IAction getAction(String id) {
      return (IAction)this.actions.get(id);
   }

   public IObjective getObjective(String id) {
      return (IObjective)this.objectives.get(id);
   }

   public Collection getQuestCollection() {
      return this.quests.values();
   }

   public ArrayList getQuestElements() {
      ArrayList elements = new ArrayList();
      Iterator var2 = this.objectives.values().iterator();

      while(var2.hasNext()) {
         IObjective objective = (IObjective)var2.next();
         elements.add(objective.getStructure());
      }

      var2 = this.actions.values().iterator();

      while(var2.hasNext()) {
         IAction action = (IAction)var2.next();
         elements.add(action.getStructure());
      }

      return elements;
   }

   public Set getQuestFilepaths(boolean underscoreSpaces) {
      HashSet filepaths;
      if (underscoreSpaces) {
         filepaths = new HashSet();
         Iterator var3 = this.quests.keySet().iterator();

         while(var3.hasNext()) {
            String filepath = (String)var3.next();
            filepaths.add(filepath.replace(" ", "_").replace(".json", ""));
         }
      } else {
         filepaths = new HashSet(this.quests.keySet());
      }

      return filepaths;
   }

   public static void registerQuests(boolean classes) throws Exception {
      Pixelmon.LOGGER.info("Registering quests.");
      if (classes) {
         getInstance().registerClasses();
      }

      getInstance().getQuests().clear();
      File file = new File("pixelmon/quests");
      if (!PixelmonConfig.useExternalJSONFilesQuests || !file.exists()) {
         try {
            Path rootPath = RCFileHelper.pathFromResourceLocation(new ResourceLocation("pixelmon", "quests"));
            List ls = RCFileHelper.listFilesRecursively(rootPath, (p) -> {
               return true;
            }, true);
            Iterator var4 = ls.iterator();

            while(var4.hasNext()) {
               Path path = (Path)var4.next();
               InputStream is = Quest.class.getResourceAsStream(path.toUri().toString().substring(path.toUri().toString().indexOf("/assets")).replace("%20", " "));
               Scanner s = new Scanner(is);
               s.useDelimiter("\\A");
               String json = s.hasNext() ? s.next() : "";
               s.close();
               Quest quest = null;

               try {
                  quest = (Quest)GSON.fromJson(json, Quest.class);
                  quest.setFilename(path.getFileName().toString());
               } catch (JsonSyntaxException var12) {
                  var12.printStackTrace();
               }

               if (quest == null) {
                  Pixelmon.LOGGER.error("Unable to load quest JSON: " + path.toString());
               } else if (getInstance().getQuest(quest.getFilename()) != null) {
                  Pixelmon.LOGGER.error("Duplicate quest JSON: " + path.toString());
               } else if (PixelmonConfig.useExternalJSONFilesQuests) {
                  file.mkdirs();
                  PrintWriter pw = new PrintWriter(new File("pixelmon/quests/" + quest.getFilename()));
                  pw.write(json);
                  pw.close();
               } else {
                  getInstance().addQuest(quest);
               }
            }
         } catch (URISyntaxException | IOException var13) {
            var13.printStackTrace();
         }
      }

      if (PixelmonConfig.useExternalJSONFilesQuests) {
         file.mkdirs();
         ArrayList files = new ArrayList();
         RCFileHelper.recursiveJSONSearch("pixelmon/quests", files);
         Iterator var16 = files.iterator();

         while(var16.hasNext()) {
            File questFile = (File)var16.next();

            try {
               FileReader fr = new FileReader(questFile);
               Quest quest = (Quest)GSON.fromJson(fr, Quest.class);
               quest.setFilename(questFile.getName());
               if (getInstance().getQuest(quest.getFilename()) != null) {
                  Pixelmon.LOGGER.error("Duplicate quest JSON: " + questFile.toString());
               } else {
                  getInstance().addQuest(quest);
               }

               fr.close();
            } catch (JsonIOException | FileNotFoundException | JsonSyntaxException var11) {
               Pixelmon.LOGGER.error("Unable to load external quest JSON " + questFile.getName());
               var11.printStackTrace();
            }
         }
      }

      Iterator var15 = getInstance().getQuestCollection().iterator();

      while(var15.hasNext()) {
         Quest quest = (Quest)var15.next();
         quest.parseAndMap();
      }

      Pixelmon.LOGGER.info("Loaded " + getInstance().getQuestCollection().size() + " Quests.");
   }

   private void writeQuest(Quest quest) {
      File file = new File("pixelmon/quests/" + quest.getFilename());

      try {
         String json = GSON.toJson(quest);
         OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
         fw.write(json);
         fw.close();
      } catch (Exception var5) {
         Pixelmon.LOGGER.error("Unable to write external quest JSON " + file.getName());
         var5.printStackTrace();
      }

   }

   private void destroyQuest(Quest quest) {
      File file = new File("pixelmon/quests/" + quest.getFilename());

      try {
         file.delete();
      } catch (Exception var4) {
         Pixelmon.LOGGER.error("Unable to delete external quest JSON " + file.getName());
         var4.printStackTrace();
      }

   }

   private void registerClasses() throws Exception {
      registerObjectives(new ItemObjective("BLOCK_USE"), new ItemObjective("BLOCK_BREAK"), new ItemObjective("BLOCK_PLACE"), new ItemObjective("ITEM_USE"), new ItemObjective("APRICORN_HARVEST"), new ItemObjective("ITEM_PICKUP"), new ItemObjective("ITEM_CRAFT"), new ItemObjective("ITEM_SMELT"), new ItemObjective("ITEM_DROP"), new ItemObjective("ITEM_HAS", true, true), new ItemObjective("ITEM_HAS_NOT", true, false), new PokemonObjective("POKEMON_HAS"), new PokemonObjective("POKEMON_CAPTURE"), new PokemonObjective("POKEMON_DEFEAT"), new PokemonObjective("POKEMON_DEFEAT_NPC"), new PokemonObjective("POKEMON_DEFEAT_WILD"), new PokemonObjective("POKEMON_DEFEAT_PLAYER"), new PokemonObjective("POKEMON_HATCH"), new PokemonObjective("POKEMON_EVOLVE_PRE"), new PokemonObjective("POKEMON_EVOLVE_POST"), new PokemonObjective("POKEMON_TRADE_GET"), new PokemonObjective("POKEMON_TRADE_GIVE"), new PokemonObjective("POKEMON_DEFEATED_BY"), new NPCObjective("NPC_RESPOND", NPCObjective.Type.RESPOND), new NPCObjective("NPC_GIVE", NPCObjective.Type.GIVE), new NPCObjective("NPC_SHOW", NPCObjective.Type.SHOW), new NPCObjective("NPC_TALK", NPCObjective.Type.TALK), new AttackObjective("BATTLE_MOVE_TARGET"), new AttackObjective("BATTLE_MOVE_USER"), new TrainerObjective("TRAINER_DEFEAT"), new TrainerObjective("TRAINER_DEFEATED_BY"), new DialogueInjectObjective(), new WorldTimeObjective(), new ServerTimeObjective(), new TimerObjective(), new AbsolutePositionObjective(), new StructureObjective(), new DimensionObjective(), new EntityInteractObjective(), new EntityVicinityObjective(), new TileEntityVicinityObjective(), new BlockerObjective(), new RandomObjective(), new FollowthroughObjective(), new DateObjective(), new QueryObjective(), new FlagObjective(), new TestDataObjective(), new InsertNPCObjective(InsertNPCObjective.Mode.TIMED), new InsertNPCObjective(InsertNPCObjective.Mode.SPAWN), new InsertSpecObjective(InsertSpecObjective.Mode.DEX_VALUES), new InsertSpecObjective(InsertSpecObjective.Mode.TYPE_VALUES), new InsertNameObjective(), new InsertStringObjective(), new InsertIntegerObjective(), new InsertDecimalObjective(), new InsertTypingObjective(), new InsertBooleanObjective(), new InsertConcatenationObjective(), new InsertOperationObjective());
      registerActions(new SendMessageAction(false), new SendMessageAction(true), new SetStageAction(), new TeleportAction(), new FinishQuestAction(true), new FinishQuestAction(false), new GivePokemonAction(), new SpawnPokemonAction(), new GiveItemAction(), new CommandAction(), new KeyItemAction(true), new KeyItemAction(false), new DialogueInjectAction(), new EndDialogueAction(), new FlagAction(true), new FlagAction(false), new PotionAction(), new SoundAction(), new GiveXPAction(), new AbandonableAction(), new ServerCosmeticAction(true), new ServerCosmeticAction(false));
      MinecraftForge.EVENT_BUS.register(new BlockListeners());
      MinecraftForge.EVENT_BUS.register(new EntityListeners());
      MinecraftForge.EVENT_BUS.register(new ItemListeners());
      MinecraftForge.EVENT_BUS.register(new PlayerListeners());
      MinecraftForge.EVENT_BUS.register(new TickListeners());
      Pixelmon.EVENT_BUS.register(new BlockListeners());
      Pixelmon.EVENT_BUS.register(new EntityListeners());
      Pixelmon.EVENT_BUS.register(new ItemListeners());
      Pixelmon.EVENT_BUS.register(new PlayerListeners());
      Pixelmon.EVENT_BUS.register(new TickListeners());
   }

   public static void registerActions(IAction... actions) throws Exception {
      IAction[] var1 = actions;
      int var2 = actions.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         IAction action = var1[var3];
         registerAction(action);
      }

   }

   public static void registerObjectives(IObjective... objectives) throws Exception {
      IObjective[] var1 = objectives;
      int var2 = objectives.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         IObjective objective = var1[var3];
         registerObjective(objective);
      }

   }

   public static void registerAction(IAction action) throws Exception {
      if (getInstance().getActions().containsKey(action.identifier())) {
         throw new Exception("Action identifier " + action.identifier() + " collision!");
      } else {
         getInstance().getActions().put(action.identifier(), action);
      }
   }

   public static void registerObjective(IObjective objective) throws Exception {
      if (getInstance().getObjectives().containsKey(objective.identifier())) {
         throw new Exception("Objective identifier " + objective.identifier() + " collision!");
      } else {
         getInstance().getObjectives().put(objective.identifier(), objective);
      }
   }

   private static void extractQuestsDir(File questsDir) {
      File[] dir = questsDir.listFiles();
      if (dir != null) {
         File[] var2 = dir;
         int var3 = dir.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            File questFile = var2[var4];

            try {
               extractFile("/assets/pixelmon/quests/" + questFile.getName(), questsDir, questFile.getName());
            } catch (Exception var7) {
            }
         }
      }

   }

   private static void extractFile(String resourceName, File dir, String filename) {
      try {
         File file = new File(dir, filename);
         if (!file.exists()) {
            InputStream link = ServerNPCRegistry.class.getResourceAsStream(resourceName);
            InputStream in = new BufferedInputStream(link);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[2048];

            while(true) {
               int nBytes = in.read(buffer);
               if (nBytes <= 0) {
                  out.flush();
                  out.close();
                  in.close();
                  break;
               }

               out.write(buffer, 0, nBytes);
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }
}
