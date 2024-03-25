package com.pixelmonmod.pixelmon.quests.client.editor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.quests.comm.editor.FetchQuestData;
import com.pixelmonmod.pixelmon.quests.comm.editor.PushQuestData;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class QuestEditorState {
   private static final QuestEditorState INSTANCE = new QuestEditorState();
   private final ArrayList quests = new ArrayList();
   private final ArrayList objectives = new ArrayList();
   private final ArrayList actions = new ArrayList();
   private Quest quest = null;
   private boolean isDirty = false;

   private QuestEditorState() {
   }

   public static QuestEditorState get() {
      return INSTANCE;
   }

   public void fetch(boolean openGui) {
      Pixelmon.network.sendToServer(new FetchQuestData(openGui));
   }

   public void push(String oldName, Quest quest, boolean openUI) {
      Pixelmon.network.sendToServer(new PushQuestData(oldName, quest, openUI));
   }

   public void delete(Quest quest) {
      Pixelmon.network.sendToServer(new PushQuestData(quest.getFilename(), quest, true, false));
   }

   public void setQuests(ArrayList quests, ArrayList elements) {
      this.quests.clear();
      this.objectives.clear();
      this.actions.clear();
      this.quests.addAll(quests);
      Iterator var3 = elements.iterator();

      while(var3.hasNext()) {
         QuestElement element = (QuestElement)var3.next();
         if (element.getType() == QuestElementType.OBJECTIVE) {
            this.objectives.add(element);
         } else if (element.getType() == QuestElementType.ACTION) {
            this.actions.add(element);
         }
      }

      this.markDirty();
   }

   public ArrayList getAllQuests() {
      return this.quests;
   }

   public boolean hasQuest(String name) {
      Iterator var2 = this.quests.iterator();

      Quest quest;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         quest = (Quest)var2.next();
      } while(!quest.getPrintableName().equalsIgnoreCase(name));

      return true;
   }

   public ArrayList getObjectives() {
      return this.objectives;
   }

   public QuestElement getObjective(String identifier) {
      Iterator var2 = this.objectives.iterator();

      QuestElement element;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         element = (QuestElement)var2.next();
      } while(!element.getIdentifier().equalsIgnoreCase(identifier));

      return element;
   }

   public ArrayList getActions() {
      return this.actions;
   }

   public QuestElement getAction(String identifier) {
      Iterator var2 = this.actions.iterator();

      QuestElement element;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         element = (QuestElement)var2.next();
      } while(!element.getIdentifier().equalsIgnoreCase(identifier));

      return element;
   }

   public ArrayList getObjectivesForStage(Stage stage) {
      ArrayList objectives = new ArrayList();
      Iterator var3 = stage.getRawObjectives().iterator();

      while(var3.hasNext()) {
         String objective = (String)var3.next();
         String identifier = objective.split(" ")[0];
         Iterator var6 = this.getObjectives().iterator();

         while(var6.hasNext()) {
            QuestElement element = (QuestElement)var6.next();
            if (element.getType() == QuestElementType.OBJECTIVE && element.getIdentifier().equalsIgnoreCase(identifier)) {
               objectives.add(element.create(this.quest, objective));
            }
         }
      }

      return objectives;
   }

   public ArrayList getActionsForStage(Stage stage) {
      ArrayList actions = new ArrayList();
      Iterator var3 = stage.getRawActions().iterator();

      while(var3.hasNext()) {
         String action = (String)var3.next();
         String identifier = action.split(" ")[1];
         Iterator var6 = this.getActions().iterator();

         while(var6.hasNext()) {
            QuestElement element = (QuestElement)var6.next();
            if (element.getType() == QuestElementType.ACTION && element.getIdentifier().equalsIgnoreCase(identifier)) {
               actions.add(element.create(this.quest, action));
            }
         }
      }

      return actions;
   }

   public void selectQuest(Quest quest) {
      this.quest = quest;
   }

   public boolean hasQuestSelected() {
      return this.quest != null;
   }

   public Quest getSelectedQuest() {
      return this.quest;
   }

   public void addNewStage() {
      Quest quest = this.getSelectedQuest();
      if (quest != null) {
         quest.getStages().sort(Comparator.comparing(Stage::getStage));
         Stage lastStage = (Stage)quest.getStages().get(quest.getStages().size() - 1);
         short lastStageValue = lastStage.getStage();
         short div10 = (short)(lastStageValue / 10 * 10);
         short newStage = (short)(div10 + 10);
         lastStage.setNextStage(newStage);
         Stage stage = new Stage(newStage, -1);
         quest.getStages().add(stage);
         quest.setDefaultStrings(newStage, 0);
      }

   }

   public void markDirty() {
      this.isDirty = true;
   }

   public boolean isDirty() {
      boolean dirty = this.isDirty;
      this.isDirty = false;
      return dirty;
   }
}
