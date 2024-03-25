package com.pixelmonmod.pixelmon.quests.actions.actions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
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
import net.minecraft.entity.player.EntityPlayerMP;

public class KeyItemAction implements IAction {
   private final boolean isTake;

   public KeyItemAction(boolean isTake) {
      this.isTake = isTake;
   }

   public String identifier() {
      return this.isTake ? "TAKE_KEY_ITEM" : "KEY_ITEM";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("key_item", false, false, ArgumentType.TEXT, new String[]{"MegaRing", "DynamaxBand", "OvalCharm", "ShinyCharm", "ExpCharm", "CatchingCharm", "MarkCharm"})});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), KeyItem::getKeyItem));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      KeyItem item = (KeyItem)arguments.value(0, progress);
      if (item != null) {
         item.action(data.getPlayer(), this.isTake);
      }

   }

   static enum KeyItem {
      MegaRing,
      DynamaxBand,
      OvalCharm,
      ShinyCharm,
      ExpCharm,
      CatchingCharm,
      MarkCharm;

      public static KeyItem getKeyItem(String name) {
         KeyItem[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            KeyItem item = var1[var3];
            if (item.name().equalsIgnoreCase(name)) {
               return item;
            }
         }

         return null;
      }

      public void action(EntityPlayerMP player, boolean isTake) {
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         switch (this) {
            case MegaRing:
               if (isTake) {
                  pps.lockMega();
               } else {
                  pps.unlockMega();
               }
               break;
            case DynamaxBand:
               if (isTake) {
                  pps.lockDynamax();
               } else {
                  pps.unlockDynamax();
               }
               break;
            case OvalCharm:
               if (isTake) {
                  pps.setOvalCharm(EnumFeatureState.Disabled);
               } else {
                  pps.setOvalCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.OvalCharm);
               }
               break;
            case ShinyCharm:
               if (isTake) {
                  pps.setShinyCharm(EnumFeatureState.Disabled);
               } else {
                  pps.setShinyCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.ShinyCharm);
               }
               break;
            case ExpCharm:
               if (isTake) {
                  pps.setExpCharm(EnumFeatureState.Disabled);
               } else {
                  pps.setExpCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.ExpCharm);
               }
               break;
            case CatchingCharm:
               if (isTake) {
                  pps.setCatchingCharm(EnumFeatureState.Disabled);
               } else {
                  pps.setCatchingCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.CatchingCharm);
               }
               break;
            case MarkCharm:
               if (isTake) {
                  pps.setMarkCharm(EnumFeatureState.Disabled);
               } else {
                  pps.setMarkCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.MarkCharm);
               }
         }

      }
   }
}
