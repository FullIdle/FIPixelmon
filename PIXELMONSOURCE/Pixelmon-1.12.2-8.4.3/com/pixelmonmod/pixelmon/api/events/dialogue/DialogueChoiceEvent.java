package com.pixelmonmod.pixelmon.api.events.dialogue;

import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.DialogueNextAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DialogueChoiceEvent extends Event {
   public final EntityPlayerMP player;
   public final Choice choice;
   private DialogueNextAction.DialogueGuiAction action;
   private ArrayList newDialogues = new ArrayList();

   public DialogueChoiceEvent(EntityPlayerMP player, Choice choice) {
      this.player = player;
      this.choice = choice;
      this.action = DialogueNextAction.DialogueGuiAction.CONTINUE;
   }

   public DialogueNextAction.DialogueGuiAction getAction() {
      return this.action;
   }

   public void setAction(DialogueNextAction.DialogueGuiAction action) {
      if (action != null) {
         this.action = action;
      }

   }

   public DialogueChoiceEvent addDialogue(Dialogue newDialogue) {
      if (newDialogue != null) {
         this.newDialogues.add(newDialogue);
      }

      return this;
   }

   public void setNewDialogues(ArrayList newDialogues) {
      if (newDialogues != null) {
         this.newDialogues = newDialogues;
      }

   }

   public ArrayList getNewDialogues() {
      return this.newDialogues;
   }

   public void reply(Dialogue... dialogues) {
      this.reply(false, dialogues);
   }

   public void reply(boolean insert, Dialogue... dialogues) {
      this.action = insert ? DialogueNextAction.DialogueGuiAction.INSERT_DIALOGUES : DialogueNextAction.DialogueGuiAction.NEW_DIALOGUES;
      HashMap choiceMap = new HashMap();
      Dialogue[] var4 = dialogues;
      int var5 = dialogues.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Dialogue dialogue = var4[var6];
         Iterator var8 = dialogue.choices.iterator();

         while(var8.hasNext()) {
            Choice choice = (Choice)var8.next();
            choiceMap.put(choice.choiceID, choice.handle);
         }

         this.addDialogue(dialogue);
      }

      Choice.handleMap.put(this.player.func_110124_au(), choiceMap);
   }

   public void reply(String name, String... texts) {
      String[] var3 = texts;
      int var4 = texts.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String text = var3[var5];
         this.addDialogue(Dialogue.builder().setName(name).setText(text).build());
      }

   }
}
