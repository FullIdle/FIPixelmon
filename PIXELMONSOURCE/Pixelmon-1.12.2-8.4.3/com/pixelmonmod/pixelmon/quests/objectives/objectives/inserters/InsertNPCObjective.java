package com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.quests.NPCInserterEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
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
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class InsertNPCObjective implements IObjective {
   private final Mode mode;

   public InsertNPCObjective(Mode mode) {
      this.mode = mode;
   }

   public String identifier() {
      return this.mode == InsertNPCObjective.Mode.SPAWN ? "NPC_SPAWN_INSERTER" : "NPC_TIMED_INSERTER";
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return this.mode == InsertNPCObjective.Mode.SPAWN ? Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), Double::parseDouble)) : Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), Double::parseDouble), Argument.from(args.get(2), Integer::parseInt), Argument.from(args.get(3), Long::parseLong), Argument.from(args.get(4), Long::parseLong, -1L), Argument.from(args.get(5), Long::parseLong, -1L), Argument.from(args.get(6), Long::parseLong, -1L), Argument.from(args.get(7), Long::parseLong, -1L), Argument.from(args.get(8), Long::parseLong, -1L), Argument.from(args.get(9), Long::parseLong, -1L), Argument.from(args.get(10), Long::parseLong, -1L));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      EntityPlayerMP player = data.getPlayer();
      if (player != null && player.func_130014_f_() != null && player.func_130014_f_().field_72996_f != null) {
         String key = (String)arguments.value(0, progress);
         double chance = (Double)arguments.value(1, progress);
         if (this.mode == InsertNPCObjective.Mode.TIMED) {
            int range = (Integer)arguments.value(2, progress);
            boolean carryOn = false;
            long time = player.func_130014_f_().func_72820_D() % 24000L;

            for(int i = 3; i < arguments.size(); ++i) {
               Long testTime = (Long)arguments.value(i, progress);
               if (testTime != null && testTime != -1L && time == testTime) {
                  carryOn = true;
                  break;
               }
            }

            if (!carryOn) {
               return false;
            }

            Iterator var17 = player.func_130014_f_().field_72996_f.iterator();

            while(var17.hasNext()) {
               Entity entity = (Entity)var17.next();
               if (entity.func_70032_d(player) < (float)range && this.attemptInsertion(data, entity, progress, key, chance, player)) {
                  return false;
               }
            }
         } else {
            this.attemptInsertion(data, (Entity)context.get(0), progress, key, chance, player);
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean attemptInsertion(QuestData dataIn, Entity entityIn, QuestProgress progressIn, String key, double chance, EntityPlayerMP playerIn) {
      String testStr = progressIn.getDataString(key);
      if (testStr != null && !testStr.isEmpty()) {
         MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
         Entity testEnt = server.func_175576_a(UUID.fromString(testStr));
         if (testEnt != null && testEnt.func_70032_d(playerIn) < (float)PixelmonConfig.questMaxRange) {
            return false;
         }
      }

      if (PixelmonConfig.questRandomNPCs && RandomHelper.rand.nextDouble() < chance) {
         String value = entityIn.getPersistentID().toString();
         Iterator var14 = dataIn.getProgress().iterator();

         while(var14.hasNext()) {
            QuestProgress progress = (QuestProgress)var14.next();
            if (!progress.isComplete() && !progress.isFailed()) {
               String potentialUUID = progress.getDataString(key);
               if (potentialUUID != null && potentialUUID.equalsIgnoreCase(value)) {
                  return false;
               }
            }
         }

         if (entityIn instanceof NPCQuestGiver && !Pixelmon.EVENT_BUS.post(new NPCInserterEvent(playerIn, progressIn, (NPCQuestGiver)entityIn))) {
            progressIn.setData(key, value);
            progressIn.sendTo(playerIn);
            return true;
         }
      }

      return false;
   }

   public QuestElement getStructure() {
      return this.mode == InsertNPCObjective.Mode.SPAWN ? new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("chance", false, false, ArgumentType.DECIMAL_NUMBER, new String[0])}) : new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("chance", false, false, ArgumentType.DECIMAL_NUMBER, new String[0]), new QuestElementArgument("range", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("world_time", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("world_time_optional", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("world_time_optional", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("world_time_optional", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("world_time_optional", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("world_time_optional", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("world_time_optional", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("world_time_optional", true, false, ArgumentType.WHOLE_NUMBER, new String[0])});
   }

   public static enum Mode {
      SPAWN,
      TIMED;
   }
}
