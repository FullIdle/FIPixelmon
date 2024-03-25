package com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue;

import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.client.gui.custom.dialogue.GuiDialogue;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SetDialogueData implements IMessage {
   private boolean openGui;
   private int numDialogues;
   private List dialogues;
   private UUID quest;

   public SetDialogueData() {
      this.openGui = false;
      this.numDialogues = 0;
      this.dialogues = new ArrayList();
      this.quest = null;
   }

   public SetDialogueData(List dialogues) {
      this.openGui = false;
      this.numDialogues = 0;
      this.dialogues = new ArrayList();
      this.quest = null;
      if (dialogues != null) {
         this.numDialogues = dialogues.size();
      }

      this.dialogues = dialogues;
   }

   public SetDialogueData(List dialogues, boolean openGui) {
      this(dialogues);
      this.openGui = openGui;
   }

   public SetDialogueData(List dialogues, boolean openGui, UUID quest) {
      this(dialogues, openGui);
      this.quest = quest;
   }

   public void fromBytes(ByteBuf buffer) {
      this.openGui = buffer.readBoolean();
      if (buffer.readBoolean()) {
         this.quest = UUIDHelper.readUUID(buffer);
      }

      this.numDialogues = buffer.readInt();

      for(int i = 0; i < this.numDialogues; ++i) {
         this.dialogues.add(new Dialogue(buffer, this.quest));
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBoolean(this.openGui);
      buffer.writeBoolean(this.quest != null);
      if (this.quest != null) {
         UUIDHelper.writeUUID(this.quest, buffer);
      }

      buffer.writeInt(this.numDialogues);
      Iterator var2 = this.dialogues.iterator();

      while(var2.hasNext()) {
         Dialogue dialogue = (Dialogue)var2.next();
         dialogue.writeToBytes(buffer);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SetDialogueData message, MessageContext ctx) {
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(SetDialogueData message) {
         GuiDialogue.setDialogues(message.dialogues, message.quest);
         if (message.openGui) {
            Minecraft.func_71410_x().func_147108_a(new GuiDialogue());
         }

      }
   }
}
