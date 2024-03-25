package com.pixelmonmod.pixelmon.quests.objectives.objectives.entity;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
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
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class EntityVicinityObjective implements IObjective {
   public String identifier() {
      return "ENTITY_VICINITY";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("entity_uuid_class", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("range", false, false, ArgumentType.WHOLE_NUMBER, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), Integer::parseInt));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      String uuidOrType = (String)arguments.value(0, progress);
      int distance = (Integer)arguments.value(1, progress);
      WorldServer world = data.getPlayer().func_71121_q();
      if (!UUIDHelper.isUUID(uuidOrType)) {
         BlockPos pos = data.getPlayer().func_180425_c();
         List entities = world.func_175674_a(data.getPlayer(), new AxisAlignedBB(pos.func_177982_a(distance, distance, distance), pos.func_177982_a(-distance, -distance, -distance)), (e) -> {
            return e != null && e.getClass().getSimpleName().equalsIgnoreCase(uuidOrType);
         });
         return !entities.isEmpty();
      } else {
         UUID uuid = UUID.fromString(uuidOrType);
         Entity entity = world.func_175733_a(uuid);
         return entity != null && entity.func_70032_d(data.getPlayer()) <= (float)distance;
      }
   }
}
