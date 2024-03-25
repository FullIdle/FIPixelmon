package com.pixelmonmod.pixelmon.quests.actions.actions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ServerCosmetics;
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
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;

public class ServerCosmeticAction implements IAction {
   private final boolean isTake;

   public ServerCosmeticAction(boolean isTake) {
      this.isTake = isTake;
   }

   public String identifier() {
      return this.isTake ? "TAKE_SERVER_COSMETIC" : "SERVER_COSMETIC";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("server_cosmetic", false, false, ArgumentType.TEXT, new String[]{"drowned_robe", "espeon_scarf", "flareon_scarf", "glaceon_scarf", "jolteon_scarf", "leafeon_scarf", "sylveon_scarf", "umbreon_scarf", "vaporeon_scarf"})});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), ServerCosmetics::get));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      ServerCosmetics cosmetics = (ServerCosmetics)arguments.value(0, progress);
      if (cosmetics != null) {
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(data.getPlayer());
         if (this.isTake) {
            pps.revokeServerCosmetics(cosmetics);
         } else {
            pps.grantServerCosmetics(cosmetics);
         }
      }

   }
}
