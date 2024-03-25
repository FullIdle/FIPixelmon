package com.pixelmonmod.pixelmon.quests.quest;

import com.pixelmonmod.pixelmon.quests.actions.Action;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.quests.objectives.Objective;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class Stage {
   private short stage;
   private short nextStage;
   private StageIcon icon = null;
   private ArrayList objectives = new ArrayList();
   private ArrayList actions = new ArrayList();
   private transient ArrayList parsedObjectives = new ArrayList();
   private transient ArrayList parsedActions = new ArrayList();

   public Stage() {
      this.stage = 0;
      this.nextStage = 10;
   }

   public Stage(int stage, int nextStage) {
      this.stage = (short)stage;
      this.nextStage = (short)nextStage;
   }

   public Stage(int stage, int nextStage, String objective) {
      this.stage = (short)stage;
      this.nextStage = (short)nextStage;
      this.objectives.add(objective);
   }

   public Stage(Stage toCopy) {
      this.stage = toCopy.stage;
      this.nextStage = toCopy.nextStage;
      this.icon = toCopy.icon;
      this.objectives.addAll(toCopy.objectives);
      this.actions.addAll(toCopy.actions);
   }

   public Stage(ByteBuf buf) {
      this.readFromByteBuf(buf);
   }

   public short getStage() {
      return this.stage;
   }

   public short getNextStage() {
      return this.nextStage;
   }

   public StageIcon getIcon() {
      if (this.icon == null) {
         this.icon = this.getNextStage() == -1 ? StageIcon.QUESTION_MARK : StageIcon.EXCLAMATION_MARK;
      }

      return this.icon;
   }

   public void changeIcon() {
      StageIcon icon = this.getIcon();
      int ordinal = icon.ordinal() + 1;
      this.icon = StageIcon.values()[ordinal == StageIcon.values().length ? 0 : ordinal];
   }

   public ArrayList getRawObjectives() {
      return this.objectives;
   }

   public ArrayList getRawActions() {
      return this.actions;
   }

   public ArrayList getParsedObjectives() {
      return this.parsedObjectives;
   }

   public ArrayList getParsedActions() {
      return this.parsedActions;
   }

   public void setStage(short stage) {
      this.stage = stage;
   }

   public void setNextStage(short nextStage) {
      this.nextStage = nextStage;
   }

   protected void parse(Quest quest) throws InvalidQuestArgsException {
      this.parsedObjectives.clear();
      this.parsedActions.clear();
      int i = 0;
      Iterator var3 = this.objectives.iterator();

      while(var3.hasNext()) {
         String objectiveString = (String)var3.next();
         String[] args = objectiveString.split(" ");
         this.parsedObjectives.add(new Objective(args, quest, this, i++));
      }

      int j = 0;
      Iterator var8 = this.actions.iterator();

      while(var8.hasNext()) {
         String actionString = (String)var8.next();
         String[] args = actionString.split(" ");
         this.parsedActions.add(new Action(args, quest, this, j++));
      }

   }

   public static Builder builder(Quest.Builder questBuilder) {
      return new Builder(questBuilder);
   }

   public void readFromByteBuf(ByteBuf buf) {
      this.stage = buf.readShort();
      this.nextStage = buf.readShort();
      this.icon = StageIcon.getIcon(buf.readByte());
      this.objectives = new ArrayList();
      int objectiveCount = buf.readShort();

      for(int i = 0; i < objectiveCount; ++i) {
         this.objectives.add(ByteBufUtils.readUTF8String(buf));
      }

      this.actions = new ArrayList();
      int actionCount = buf.readShort();

      for(int i = 0; i < actionCount; ++i) {
         this.actions.add(ByteBufUtils.readUTF8String(buf));
      }

   }

   public void writeToByteBuf(ByteBuf buf) {
      buf.writeShort(this.stage);
      buf.writeShort(this.nextStage);
      buf.writeByte(this.getIcon().ordinal());
      buf.writeShort(this.objectives.size());
      Iterator var2 = this.objectives.iterator();

      String action;
      while(var2.hasNext()) {
         action = (String)var2.next();
         ByteBufUtils.writeUTF8String(buf, action);
      }

      buf.writeShort(this.actions.size());
      var2 = this.actions.iterator();

      while(var2.hasNext()) {
         action = (String)var2.next();
         ByteBufUtils.writeUTF8String(buf, action);
      }

   }

   public static class Builder {
      private final Quest.Builder questBuilder;
      private final Stage stage;

      public Builder(Quest.Builder questBuilder) {
         this.questBuilder = questBuilder;
         this.stage = new Stage();
      }

      public Builder setStage(short stage) {
         this.stage.stage = stage;
         return this;
      }

      public Builder setNextStage(short nextStage) {
         this.stage.nextStage = nextStage;
         return this;
      }

      public Builder setStage(int stage) {
         this.stage.stage = (short)stage;
         return this;
      }

      public Builder setNextStage(int nextStage) {
         this.stage.nextStage = (short)nextStage;
         return this;
      }

      public Builder addObjective(String objective) {
         this.stage.objectives.add(objective);
         return this;
      }

      public Builder addAction(String action) {
         this.stage.actions.add(action);
         return this;
      }

      public Quest.Builder build() {
         this.questBuilder.addStage(this.stage);
         return this.questBuilder;
      }
   }
}
