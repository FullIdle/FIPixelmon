package com.pixelmonmod.pixelmon.quests.objectives.objectives.entity;

import com.google.common.collect.Lists;
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
import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileEntityVicinityObjective implements IObjective {
   public String identifier() {
      return "TILEENTITY_VICINITY";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("tile_entity", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("range", false, false, ArgumentType.WHOLE_NUMBER, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), Integer::parseInt));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      String type = (String)arguments.value(0, progress);
      int distance = (Integer)arguments.value(1, progress);
      WorldServer world = data.getPlayer().func_71121_q();
      Iterator iterator = this.getTileEntitiesWithinAABB(world, data.getPlayer().func_174813_aQ().func_72321_a((double)distance, (double)distance, (double)distance)).iterator();

      TileEntity te;
      do {
         if (!iterator.hasNext()) {
            return false;
         }

         te = (TileEntity)iterator.next();
      } while(!(data.getPlayer().func_70011_f((double)te.func_174877_v().func_177958_n(), (double)te.func_174877_v().func_177956_o(), (double)te.func_174877_v().func_177952_p()) <= (double)distance) || !te.getClass().getSimpleName().equalsIgnoreCase(type));

      return true;
   }

   private List getTileEntitiesWithinAABB(World world, AxisAlignedBB aabb) {
      int j2 = MathHelper.func_76128_c((aabb.field_72340_a - World.MAX_ENTITY_RADIUS) / 16.0);
      int k2 = MathHelper.func_76143_f((aabb.field_72336_d + World.MAX_ENTITY_RADIUS) / 16.0);
      int l2 = MathHelper.func_76128_c((aabb.field_72339_c - World.MAX_ENTITY_RADIUS) / 16.0);
      int i3 = MathHelper.func_76143_f((aabb.field_72334_f + World.MAX_ENTITY_RADIUS) / 16.0);
      List list = Lists.newArrayList();

      for(int j3 = j2; j3 < k2; ++j3) {
         for(int k3 = l2; k3 < i3; ++k3) {
            if (world.func_72863_F().func_186026_b(j3, k3) != null) {
               list.addAll(world.func_72964_e(j3, k3).func_177434_r().values());
            }
         }
      }

      return list;
   }
}
