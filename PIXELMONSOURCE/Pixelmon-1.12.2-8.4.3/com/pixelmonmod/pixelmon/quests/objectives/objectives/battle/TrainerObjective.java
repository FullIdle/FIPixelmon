package com.pixelmonmod.pixelmon.quests.objectives.objectives.battle;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.comm.QuestMarker;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.objectives.IObjective;
import com.pixelmonmod.pixelmon.quests.objectives.Objective;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Argument;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Context;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.entity.Entity;

public class TrainerObjective implements IObjective {
   private final String identifier;

   public TrainerObjective(String identifier) {
      this.identifier = identifier;
   }

   public int quantity(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments) {
      return arguments.quantity(1, progress);
   }

   public String identifier() {
      return this.identifier;
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("entity_uuid", true, false, ArgumentType.ENTITY_UUID, new String[0]), new QuestElementArgument("count", false, false, ArgumentType.WHOLE_NUMBER, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), UUIDHelper::questUUID), Argument.from(args.get(1), Integer::parseInt));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      Entity entity = (Entity)context.get(0);
      UUID uuid = (UUID)arguments.value(0, progress);
      return uuid == null || uuid.equals(UUIDHelper.ZEROED_UUID) || entity.func_110124_au().equals(uuid);
   }

   public ArrayList mark(Stage stage, QuestProgress progress, Objective objective, int objectiveIndex, Arguments arguments, Context context) {
      ArrayList markers = new ArrayList();
      if (!progress.isObjectiveComplete(objectiveIndex)) {
         UUID uuid = (UUID)arguments.value(0, progress);
         if (uuid != null) {
            markers.add(new QuestMarker(uuid, 0, progress.getQuest().getColor(), progress.getMarkerType()));
         }
      }

      return markers;
   }
}
