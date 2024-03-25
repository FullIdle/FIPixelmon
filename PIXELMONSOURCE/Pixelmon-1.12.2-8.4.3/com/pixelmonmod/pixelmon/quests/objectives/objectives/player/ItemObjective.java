package com.pixelmonmod.pixelmon.quests.objectives.objectives.player;

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
import com.pixelmonmod.pixelmon.util.helpers.TextHelper;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class ItemObjective implements IObjective {
   private final String identifier;
   private final boolean itemList;
   private final boolean seekMatch;

   public ItemObjective(String identifier) {
      this(identifier, false, true);
   }

   public ItemObjective(String identifier, boolean itemList, boolean seekMatch) {
      this.identifier = identifier;
      this.itemList = itemList;
      this.seekMatch = seekMatch;
   }

   public int quantity(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments) {
      return arguments.quantity(1, progress);
   }

   public String identifier() {
      return this.identifier;
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("item", false, false, ArgumentType.ITEM, new String[0]), new QuestElementArgument("count", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("damage_metadata", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("display_name", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("nbt", true, false, ArgumentType.TEXT, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), Item::func_111206_d, (Object)null), Argument.from(args.get(1), Integer::parseInt, -1), Argument.from(args.get(2), Integer::parseInt, -1), Argument.from(args.get(3), TextHelper::format, ""), Argument.from(args.get(4), (s) -> {
         try {
            return JsonToNBT.func_180713_a(s);
         } catch (Exception var2) {
            return new NBTTagCompound();
         }
      }, new NBTTagCompound()));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      if (this.itemList) {
         Item item = (Item)arguments.value(0, progress);
         Integer count = (Integer)arguments.value(1, progress);
         Integer damage = (Integer)arguments.value(2, progress);
         String name = (String)arguments.value(3, progress);
         NBTTagCompound nbt = (NBTTagCompound)arguments.value(4, progress);
         ArrayList items = (ArrayList)context.get(0);
         Iterator var13 = items.iterator();

         ItemStack stack;
         do {
            if (!var13.hasNext()) {
               return !this.seekMatch;
            }

            stack = (ItemStack)var13.next();
         } while(stack.func_190926_b() || !compare(stack, item, count, damage, name, nbt));

         return this.seekMatch;
      } else {
         return compare((ItemStack)context.get(0), (Item)arguments.value(0, progress), 1, (Integer)arguments.value(2, progress), (String)arguments.value(3, progress), (NBTTagCompound)arguments.value(4, progress));
      }
   }

   public static boolean compare(ItemStack stack, Item item, int count, int damage, String name, NBTTagCompound nbt) {
      if (item != null && stack.func_77973_b() != item) {
         return false;
      } else if (count > 0 && count < stack.func_190916_E()) {
         return false;
      } else if (damage >= 0 && damage != stack.func_77952_i()) {
         return false;
      } else if (!name.isEmpty() && !stack.func_82833_r().equalsIgnoreCase(name)) {
         return false;
      } else {
         if (!nbt.func_82582_d()) {
            if (!stack.func_77942_o()) {
               return false;
            }

            NBTTagCompound stackNbt = stack.func_77978_p();
            Iterator var7 = nbt.func_150296_c().iterator();

            while(var7.hasNext()) {
               String key = (String)var7.next();
               NBTBase base = nbt.func_74781_a(key);
               if (!stackNbt.func_150297_b(key, base.func_74732_a())) {
                  return false;
               }

               NBTBase stackBase = stackNbt.func_74781_a(key);
               if (!stackBase.equals(base)) {
                  return false;
               }
            }
         }

         return true;
      }
   }
}
