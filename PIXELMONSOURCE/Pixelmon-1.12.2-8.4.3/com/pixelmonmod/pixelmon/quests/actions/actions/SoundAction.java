package com.pixelmonmod.pixelmon.quests.actions.actions;

import com.pixelmonmod.pixelmon.RandomHelper;
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
import com.pixelmonmod.pixelmon.quests.util.PosAlignment;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class SoundAction implements IAction {
   public String identifier() {
      return "SOUND";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("x", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("y", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("z", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("alignment", false, false, ArgumentType.TEXT, new String[]{"Relative", "Rotational", "Random", "Absolute"}), new QuestElementArgument("sound", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("sound_category", false, false, ArgumentType.TEXT, new String[]{"master", "music", "record", "weather", "block", "hostile", "neutral", "player", "ambient", "voice"}), new QuestElementArgument("volume", false, false, ArgumentType.DECIMAL_NUMBER, new String[0]), new QuestElementArgument("pitch", false, false, ArgumentType.DECIMAL_NUMBER, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), Double::parseDouble, 0.0), Argument.from(args.get(1), Double::parseDouble, 0.0), Argument.from(args.get(2), Double::parseDouble, 0.0), Argument.from(args.get(3), PosAlignment::getForName, PosAlignment.Relative), Argument.from(args.get(4), (s) -> {
         return (SoundEvent)SoundEvent.field_187505_a.func_82594_a(new ResourceLocation(s));
      }), Argument.from(args.get(5), SoundCategory::func_187950_a, SoundCategory.MASTER), Argument.from(args.get(6), Float::parseFloat, 1.0F), Argument.from(args.get(7), Float::parseFloat, 1.0F));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      EntityPlayerMP player = data.getPlayer();
      Pos pos = new Pos((Double)arguments.value(0, progress), (Double)arguments.value(1, progress), (Double)arguments.value(2, progress), (PosAlignment)arguments.value(3, progress));
      player.func_71121_q().func_184133_a((EntityPlayer)null, pos.get(player), (SoundEvent)arguments.value(4, progress), (SoundCategory)arguments.value(5, progress), (Float)arguments.value(6, progress), (Float)arguments.value(7, progress));
   }

   private static class Pos {
      private double x;
      private double y;
      private double z;
      private final boolean relativeXYZ;
      private final boolean relativeYPR;

      public Pos(double x, double y, double z, PosAlignment alignment) {
         this.x = x;
         this.y = y;
         this.z = z;
         if (alignment == PosAlignment.Relative) {
            this.relativeXYZ = true;
            this.relativeYPR = false;
         } else if (alignment == PosAlignment.Rotational) {
            this.relativeXYZ = false;
            this.relativeYPR = true;
         } else if (alignment == PosAlignment.Random) {
            this.relativeXYZ = true;
            this.relativeYPR = false;
            this.x = RandomHelper.rand.nextDouble() * this.x * 2.0 - this.x;
            this.y = RandomHelper.rand.nextDouble() * this.y * 2.0 - this.y;
            this.z = RandomHelper.rand.nextDouble() * this.z * 2.0 - this.z;
         } else {
            this.relativeXYZ = false;
            this.relativeYPR = false;
         }

      }

      public BlockPos get(EntityPlayerMP player) {
         if (this.relativeXYZ) {
            return new BlockPos(player.field_70165_t + this.x, player.field_70163_u + this.y, player.field_70161_v + this.z);
         } else if (this.relativeYPR) {
            double theta = (double)player.field_70177_z * Math.PI / 180.0;
            return new BlockPos(player.field_70165_t + this.x * Math.cos(theta) + this.z * Math.sin(theta), player.field_70163_u + this.y, player.field_70161_v + this.x * Math.sin(theta) + this.z * Math.cos(theta));
         } else {
            return new BlockPos(this.x, this.y, this.z);
         }
      }
   }
}
