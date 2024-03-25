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
import com.pixelmonmod.pixelmon.util.helpers.TextHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class GiveItemAction implements IAction {
   public String identifier() {
      return "ITEM_GIVE";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("item", false, false, ArgumentType.ITEM, new String[0]), new QuestElementArgument("count", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("damage_metadata", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("display_name", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("lore", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("nbt", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("has_effect", true, false, ArgumentType.BOOLEAN, new String[0]), new QuestElementArgument("flags", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("hide_tooltip", true, false, ArgumentType.BOOLEAN, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), Item::func_111206_d), Argument.from(args.get(1), Integer::parseInt, 1), Argument.from(args.get(2), Integer::parseInt, -1), Argument.from(args.get(3), TextHelper::format, (Object)null), Argument.from(args.get(4), (s) -> {
         return TextHelper.format(s.replace("_", " ")).split("\n");
      }, (Object)null), Argument.from(args.get(5), (s) -> {
         try {
            return JsonToNBT.func_180713_a(s);
         } catch (Exception var2) {
            return null;
         }
      }, (Object)null), Argument.from(args.get(6), Boolean::parseBoolean, false), Argument.from(args.get(7), Integer::parseInt, -1), Argument.from(args.get(8), Boolean::parseBoolean, false));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      Item item = (Item)arguments.value(0, progress);
      int count = (Integer)arguments.value(1, progress);
      int damage = (Integer)arguments.value(2, progress);
      String name = (String)arguments.value(3, progress);
      String[] lore = (String[])arguments.value(4, progress);
      NBTTagCompound nbt = (NBTTagCompound)arguments.value(5, progress);
      boolean hasEffect = (Boolean)arguments.value(6, progress);
      int flags = (Integer)arguments.value(7, progress);
      boolean hideTooltip = (Boolean)arguments.value(8, progress);
      ItemStack stack = new ItemStack(item, count);
      if (damage >= 0) {
         stack.func_77964_b(damage);
      }

      if (name != null) {
         stack.func_151001_c(name);
      }

      if (lore != null) {
         if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
         }

         NBTTagList tags = new NBTTagList();
         String[] var17 = lore;
         int var18 = lore.length;

         for(int var19 = 0; var19 < var18; ++var19) {
            String line = var17[var19];
            tags.func_74742_a(new NBTTagString(line));
         }

         stack.func_190925_c("display").func_74782_a("Lore", tags);
      }

      if (nbt != null) {
         if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
         }

         stack.func_77978_p().func_179237_a(nbt);
      }

      if (hideTooltip) {
         if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
         }

         stack.func_77978_p().func_74778_a("tooltip", "");
      }

      if (flags >= 0) {
         if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
         }

         stack.func_77978_p().func_74768_a("HideFlags", flags);
      }

      if (hasEffect) {
         if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
         }

         stack.func_77978_p().func_74757_a("HasEffect", true);
      }

      EntityPlayerMP player = data.getPlayer();
      if (!player.func_191521_c(stack)) {
         player.func_71019_a(stack, true);
      }

   }
}
