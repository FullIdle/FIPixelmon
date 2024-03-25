package com.pixelmonmod.pixelmon.quests.objectives.objectives.entity;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.comm.QuestMarker;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.objectives.IObjective;
import com.pixelmonmod.pixelmon.quests.objectives.Objective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.player.ItemObjective;
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
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

public class NPCObjective implements IObjective {
   private final String identifier;
   private final Type type;

   public NPCObjective(String identifier, Type type) {
      this.identifier = identifier;
      this.type = type;
   }

   public String identifier() {
      return this.identifier;
   }

   public QuestElement getStructure() {
      if (this.type == NPCObjective.Type.TALK) {
         return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("entity_uuid", false, false, ArgumentType.TEXT, new String[0])});
      } else if (this.type == NPCObjective.Type.RESPOND) {
         return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("entity_uuid", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("dialogue_option", false, false, ArgumentType.WHOLE_NUMBER, new String[]{"0", "1", "2", "3", "4", "5", "6", "7"})});
      } else {
         return this.type != NPCObjective.Type.GIVE && this.type != NPCObjective.Type.SHOW ? new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[0]) : new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("entity_uuid", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("success", false, false, ArgumentType.SUCCESS, new String[0]), new QuestElementArgument("item", false, false, ArgumentType.ITEM, new String[0]), new QuestElementArgument("count", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("damage_metadata", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("display_name", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("nbt", true, false, ArgumentType.TEXT, new String[0])});
      }
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      if (this.type != NPCObjective.Type.GIVE && this.type != NPCObjective.Type.SHOW) {
         return this.type == NPCObjective.Type.RESPOND ? Arguments.create(Argument.of(this.type), Argument.from(args.get(0), UUIDHelper::questUUID), Argument.from(args.get(1), Integer::parseInt, 1)) : Arguments.create(Argument.of(this.type), Argument.from(args.get(0), UUIDHelper::questUUID));
      } else {
         return Arguments.create(Argument.of(this.type), Argument.from(args.get(0), UUIDHelper::questUUID), Argument.from(args.get(1), (s) -> {
            return s.equalsIgnoreCase("success") || s.equalsIgnoreCase("true");
         }, true), Argument.from(args.get(2), Item::func_111206_d, (Object)null), Argument.from(args.get(3), Integer::parseInt, -1), Argument.from(args.get(4), Integer::parseInt, -1), Argument.from(args.get(5), (s) -> {
            return s;
         }, ""), Argument.from(args.get(6), (s) -> {
            try {
               return JsonToNBT.func_180713_a(s);
            } catch (Exception var2) {
               return new NBTTagCompound();
            }
         }, new NBTTagCompound()));
      }
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      Entity npc = (Entity)context.get(0);
      Type type = (Type)arguments.value(0, progress);
      UUID uuid = (UUID)arguments.value(1, progress);
      if (uuid != null && npc.getPersistentID().equals(uuid)) {
         int i;
         switch (type) {
            case GIVE:
            case SHOW:
               InventoryPlayer inventory = data.getPlayer().field_71071_by;
               boolean success = (Boolean)arguments.value(2, progress);
               Item item = (Item)arguments.value(3, progress);
               int count = (Integer)arguments.value(4, progress);
               int damage = (Integer)arguments.value(5, progress);
               String name = (String)arguments.value(6, progress);
               NBTTagCompound nbt = (NBTTagCompound)arguments.value(7, progress);

               for(i = 0; i < inventory.func_70302_i_(); ++i) {
                  ItemStack stack = inventory.func_70301_a(i);
                  if (!stack.func_190926_b() && ItemObjective.compare(stack, item, count, damage, name, nbt)) {
                     if (type == NPCObjective.Type.GIVE) {
                        stack.func_190918_g(count);
                     }

                     return success;
                  }
               }

               return !success;
            case RESPOND:
               i = (Integer)context.get(1);
               int b = (Integer)arguments.value(2, progress);
               if (i == b) {
                  return true;
               }
            default:
               return false;
            case TALK:
               return true;
         }
      } else {
         return false;
      }
   }

   public ArrayList mark(Stage stage, QuestProgress progress, Objective objective, int objectiveIndex, Arguments arguments, Context context) {
      ArrayList markers = new ArrayList();
      if (!progress.isObjectiveComplete(objectiveIndex)) {
         UUID uuid = (UUID)arguments.value(1, progress);
         if (uuid != null) {
            markers.add(new QuestMarker(uuid, 0, progress.getQuest().getColor(), progress.getMarkerType()));
         }
      }

      return markers;
   }

   public static enum Type {
      TALK,
      GIVE,
      SHOW,
      RESPOND;
   }
}
