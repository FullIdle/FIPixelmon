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
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandAction implements IAction {
   public String identifier() {
      return "COMMAND";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("command", false, false, ArgumentType.SPACED_TEXT, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      StringBuilder builder = new StringBuilder();

      for(int i = 0; i < args.size(); ++i) {
         if (builder.length() > 0) {
            builder.append(" ");
         }

         builder.append(args.get(i));
      }

      return Arguments.create(Argument.from(builder.toString(), (s) -> {
         return s;
      }));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      String command = (String)arguments.value(0, progress);
      command = command.replace("@p", data.getPlayer().func_70005_c_());
      if (command.contains("?")) {
         Iterator var7;
         Map.Entry entry;
         for(var7 = progress.getDataLongMap().entrySet().iterator(); var7.hasNext(); command = command.replace("?" + (String)entry.getKey() + "?", Long.toString((Long)entry.getValue()))) {
            entry = (Map.Entry)var7.next();
         }

         for(var7 = progress.getDataStringMap().entrySet().iterator(); var7.hasNext(); command = command.replace("?" + (String)entry.getKey() + "?", I18n.func_135052_a((String)entry.getValue(), new Object[0]))) {
            entry = (Map.Entry)var7.next();
         }
      }

      MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
      server.func_71187_D().func_71556_a(server, command);
   }
}
