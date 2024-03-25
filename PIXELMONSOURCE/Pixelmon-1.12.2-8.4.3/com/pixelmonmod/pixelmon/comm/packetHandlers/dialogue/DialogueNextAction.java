package com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue;

import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.client.gui.custom.dialogue.GuiDialogue;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DialogueNextAction implements IMessage {
   public DialogueGuiAction action;
   private int numDialogues = 0;
   public ArrayList newDialogues = null;

   public DialogueNextAction() {
   }

   public DialogueNextAction(DialogueGuiAction action, ArrayList newDialogues) {
      this.action = action;
      this.newDialogues = newDialogues;
      this.numDialogues = newDialogues == null ? 0 : newDialogues.size();
   }

   public void fromBytes(ByteBuf buffer) {
      this.action = DialogueNextAction.DialogueGuiAction.values()[buffer.readInt()];
      if (this.action == DialogueNextAction.DialogueGuiAction.NEW_DIALOGUES || this.action == DialogueNextAction.DialogueGuiAction.INSERT_DIALOGUES) {
         this.newDialogues = new ArrayList();
         this.numDialogues = buffer.readInt();

         while(this.numDialogues-- > 0) {
            this.newDialogues.add(new Dialogue(buffer));
         }
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.action.ordinal());
      buffer.writeInt(this.numDialogues);
      if (this.numDialogues > 0) {
         Iterator var2 = this.newDialogues.iterator();

         while(var2.hasNext()) {
            Dialogue dialogue = (Dialogue)var2.next();
            dialogue.writeToBytes(buffer);
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(DialogueNextAction message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            GuiScreen openScreen = Minecraft.func_71410_x().field_71462_r;
            if (openScreen instanceof GuiDialogue) {
               GuiDialogue dialogueScreen = (GuiDialogue)openScreen;
               if (message.action == DialogueNextAction.DialogueGuiAction.CLOSE) {
                  dialogueScreen.close();
               } else if (!GuiDialogue.isQuest()) {
                  GuiDialogue.removeImmediateDialogue();
                  if (message.action != DialogueNextAction.DialogueGuiAction.CONTINUE) {
                     ArrayList dialogues = new ArrayList(message.newDialogues);
                     if (message.action == DialogueNextAction.DialogueGuiAction.NEW_DIALOGUES) {
                        GuiDialogue.setDialogues(dialogues);
                     } else if (message.action == DialogueNextAction.DialogueGuiAction.INSERT_DIALOGUES) {
                        GuiDialogue.insertDialogues(dialogues);
                     }
                  }

                  dialogueScreen.next();
               }
            }

         });
         return null;
      }
   }

   public static enum DialogueGuiAction {
      CLOSE,
      CONTINUE,
      NEW_DIALOGUES,
      INSERT_DIALOGUES;
   }
}
