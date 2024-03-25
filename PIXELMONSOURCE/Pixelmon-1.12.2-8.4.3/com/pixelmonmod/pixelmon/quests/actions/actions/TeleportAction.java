package com.pixelmonmod.pixelmon.quests.actions.actions;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.actions.IAction;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Argument;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import com.pixelmonmod.pixelmon.util.helpers.DimensionHelper;
import net.minecraft.entity.player.EntityPlayerMP;

public class TeleportAction implements IAction {
   public String identifier() {
      return "TELEPORT";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("x", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("y", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("z", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("yawangle", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("pitchangle", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("dimension", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("relative_x", true, false, ArgumentType.BOOLEAN, new String[0]), new QuestElementArgument("relative_y", true, false, ArgumentType.BOOLEAN, new String[0]), new QuestElementArgument("relative_z", true, false, ArgumentType.BOOLEAN, new String[0]), new QuestElementArgument("relative_yaw", true, false, ArgumentType.BOOLEAN, new String[0]), new QuestElementArgument("relative_pitch", true, false, ArgumentType.BOOLEAN, new String[0]), new QuestElementArgument("relative_dimension", true, false, ArgumentType.BOOLEAN, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), Double::parseDouble, 0.0), Argument.from(args.get(1), Double::parseDouble, 0.0), Argument.from(args.get(2), Double::parseDouble, 0.0), Argument.from(args.get(3), Float::parseFloat, 0.0F), Argument.from(args.get(4), Float::parseFloat, 0.0F), Argument.from(args.get(5), Integer::parseInt, 0), Argument.from(args.get(6), Boolean::parseBoolean, true), Argument.from(args.get(7), Boolean::parseBoolean, true), Argument.from(args.get(8), Boolean::parseBoolean, true), Argument.from(args.get(9), Boolean::parseBoolean, true), Argument.from(args.get(10), Boolean::parseBoolean, true), Argument.from(args.get(11), Boolean::parseBoolean, true));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      double x = (Double)arguments.value(0, progress);
      double y = (Double)arguments.value(1, progress);
      double z = (Double)arguments.value(2, progress);
      float yaw = (Float)arguments.value(3, progress);
      float pitch = (Float)arguments.value(4, progress);
      int dim = (Integer)arguments.value(5, progress);
      boolean rX = (Boolean)arguments.value(6, progress);
      boolean rY = (Boolean)arguments.value(7, progress);
      boolean rZ = (Boolean)arguments.value(8, progress);
      boolean rYaw = (Boolean)arguments.value(9, progress);
      boolean rPitch = (Boolean)arguments.value(10, progress);
      boolean rDim = (Boolean)arguments.value(11, progress);
      EntityPlayerMP player = data.getPlayer();
      DimensionHelper.forceTeleport(player, rDim ? player.field_71093_bK + dim : dim, rX ? player.field_70165_t + x : x, rY ? player.field_70163_u + y : y, rZ ? player.field_70161_v + z : z, rYaw ? player.field_70177_z + yaw : yaw, rPitch ? player.field_70125_A + pitch : pitch);
   }
}
