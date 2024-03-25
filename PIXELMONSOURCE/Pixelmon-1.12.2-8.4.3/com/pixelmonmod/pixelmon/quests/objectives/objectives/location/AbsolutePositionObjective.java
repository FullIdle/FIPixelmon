package com.pixelmonmod.pixelmon.quests.objectives.objectives.location;

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
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;

public class AbsolutePositionObjective implements IObjective {
   public String identifier() {
      return "ABSOLUTE_POSITION";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("pos_A_X", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("pos_A_Y", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("pos_A_Z", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("pos_B_X", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("pos_B_Y", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("pos_B_Z", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("dimension", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("ignore_y", false, false, ArgumentType.BOOLEAN, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), Integer::parseInt, Integer.MIN_VALUE), Argument.from(args.get(1), Integer::parseInt, Integer.MIN_VALUE), Argument.from(args.get(2), Integer::parseInt, Integer.MIN_VALUE), Argument.from(args.get(3), Integer::parseInt, Integer.MAX_VALUE), Argument.from(args.get(4), Integer::parseInt, Integer.MAX_VALUE), Argument.from(args.get(5), Integer::parseInt, Integer.MAX_VALUE), Argument.from(args.get(6), Integer::parseInt, 0), Argument.from(args.get(7), Boolean::parseBoolean, false));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      BlockPos pos = (BlockPos)context.get(0);
      int dim = data.getPlayer().field_71093_bK;
      int d = (Integer)arguments.value(6, progress);
      if (dim == d) {
         int x1 = (Integer)arguments.value(0, progress);
         int y1 = (Integer)arguments.value(1, progress);
         int z1 = (Integer)arguments.value(2, progress);
         int x2 = (Integer)arguments.value(3, progress);
         int y2 = (Integer)arguments.value(4, progress);
         int z2 = (Integer)arguments.value(5, progress);
         boolean ignoreY = (Boolean)arguments.value(7, progress);
         return this.compare(pos, x1, y1, z1, x2, y2, z2, ignoreY);
      } else {
         return false;
      }
   }

   public ArrayList mark(Stage stage, QuestProgress progress, Objective objective, int objectiveIndex, Arguments arguments, Context context) {
      ArrayList markers = new ArrayList();
      int dim = progress.getParent().getPlayer().field_71093_bK;
      int d = (Integer)arguments.value(6, progress);
      if (dim == d) {
         boolean ignoreY = (Boolean)arguments.value(7, progress);
         if (!ignoreY) {
            int x1 = (Integer)arguments.value(0, progress);
            int y1 = (Integer)arguments.value(1, progress);
            int z1 = (Integer)arguments.value(2, progress);
            int x2 = (Integer)arguments.value(3, progress);
            int y2 = (Integer)arguments.value(4, progress);
            int z2 = (Integer)arguments.value(5, progress);
            markers.add(new QuestMarker(this.getMarkerPos(x1, y1, z1, x2, y2, z2), dim, progress.getQuest().getColor(), progress.getMarkerType()));
         }
      }

      return markers;
   }

   private boolean compare(BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2, boolean ignoreY) {
      if ((x1 <= x2 || pos.func_177958_n() < x2 || pos.func_177958_n() > x1) && (x1 >= x2 || pos.func_177958_n() < x1 || pos.func_177958_n() > x2) && x1 != pos.func_177958_n() && x2 != pos.func_177958_n() || (z1 <= z2 || pos.func_177952_p() < z2 || pos.func_177952_p() > z1) && (z1 >= z2 || pos.func_177952_p() < z1 || pos.func_177952_p() > z2) && z1 != pos.func_177952_p() && z2 != pos.func_177952_p()) {
         return false;
      } else {
         return ignoreY || y1 > y2 && pos.func_177956_o() >= y2 && pos.func_177956_o() <= y1 || y1 < y2 && pos.func_177956_o() >= y1 && pos.func_177956_o() <= y2 || y1 == pos.func_177956_o() || y2 == pos.func_177956_o();
      }
   }

   private BlockPos getMarkerPos(int x1, int y1, int z1, int x2, int y2, int z2) {
      return new BlockPos(((double)x1 + (double)x2) / 2.0, ((double)y1 + (double)y2) / 2.0, ((double)z1 + (double)z2) / 2.0);
   }
}
