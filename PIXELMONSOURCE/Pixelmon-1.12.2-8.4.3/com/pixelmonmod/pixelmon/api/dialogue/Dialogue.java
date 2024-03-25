package com.pixelmonmod.pixelmon.api.dialogue;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.SetDialogueData;
import com.pixelmonmod.pixelmon.quests.client.QuestDataClient;
import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class Dialogue {
   public final String name;
   private final String text;
   private final String localizedText;
   public final ArrayList choices;
   public QuestProgressClient quest;

   /** @deprecated */
   @Deprecated
   public Dialogue(ByteBuf buffer) {
      this(buffer, false);
   }

   /** @deprecated */
   @Deprecated
   public Dialogue(ByteBuf buffer, boolean localized) {
      this.quest = null;
      String name = ByteBufUtils.readUTF8String(buffer);
      this.name = localized ? I18n.func_135052_a(name, new Object[0]) : name;
      if (!buffer.readBoolean() && !localized) {
         this.text = ByteBufUtils.readUTF8String(buffer);
         this.localizedText = null;
      } else {
         this.text = null;
         this.localizedText = ByteBufUtils.readUTF8String(buffer);
      }

      ArrayList choices = new ArrayList();
      byte choiceCount = buffer.readByte();

      while(choiceCount > 0) {
         --choiceCount;
         choices.add(new Choice(buffer, localized));
      }

      this.choices = choices;
   }

   public Dialogue(ByteBuf buffer, UUID quest) {
      this.quest = null;
      if (quest == null) {
         this.name = ByteBufUtils.readUTF8String(buffer);
         if (buffer.readBoolean()) {
            this.localizedText = ByteBufUtils.readUTF8String(buffer);
            this.text = null;
         } else {
            this.localizedText = null;
            this.text = ByteBufUtils.readUTF8String(buffer);
         }
      } else {
         this.quest = QuestDataClient.getInstance().get(quest);
         this.name = this.quest.format(ByteBufUtils.readUTF8String(buffer));
         buffer.readBoolean();
         this.text = this.quest.format(ByteBufUtils.readUTF8String(buffer));
         this.localizedText = null;
      }

      ArrayList choices = new ArrayList();
      byte choiceCount = buffer.readByte();

      while(choiceCount > 0) {
         --choiceCount;
         choices.add(new Choice(buffer, true));
      }

      this.choices = choices;
   }

   public Dialogue(String name, String text, String localizedText, ArrayList choices) {
      this.quest = null;
      this.name = name;
      this.text = text;
      this.localizedText = localizedText;
      if (choices == null) {
         this.choices = new ArrayList();
      } else {
         this.choices = choices;
      }

   }

   /** @deprecated */
   @Deprecated
   public void writeToBytes(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.name);
      buffer.writeBoolean(this.text == null);
      if (this.text == null) {
         ByteBufUtils.writeUTF8String(buffer, this.localizedText);
      } else {
         ByteBufUtils.writeUTF8String(buffer, this.text);
      }

      buffer.writeByte(this.choices.size());
      Iterator var2 = this.choices.iterator();

      while(var2.hasNext()) {
         Choice choice = (Choice)var2.next();
         choice.toBytes(buffer);
      }

   }

   public String getText() {
      return this.text == null ? (I18n.func_188566_a(this.localizedText) ? I18n.func_135052_a(this.localizedText, new Object[0]) : this.localizedText) : this.text;
   }

   public void open(EntityPlayerMP... players) {
      this.open((UUID)null, players);
   }

   public void open(UUID quest, EntityPlayerMP... players) {
      EntityPlayerMP[] var3 = players;
      int var4 = players.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EntityPlayerMP player = var3[var5];
         setPlayerDialogueData(quest, player, Lists.newArrayList(new Dialogue[]{this}), true);
      }

   }

   public static void setPlayerDialogueData(EntityPlayerMP player, List dialogues, boolean openGui) {
      setPlayerDialogueData((UUID)null, player, dialogues, openGui);
   }

   public static void setPlayerDialogueData(UUID quest, EntityPlayerMP player, List dialogues, boolean openGui) {
      HashMap choiceMap = new HashMap();
      Iterator var5 = dialogues.iterator();

      while(var5.hasNext()) {
         Dialogue dialogue = (Dialogue)var5.next();
         Iterator var7 = dialogue.choices.iterator();

         while(var7.hasNext()) {
            Choice choice = (Choice)var7.next();
            choiceMap.put(choice.choiceID, choice.handle);
         }
      }

      Choice.handleMap.put(player.func_110124_au(), choiceMap);
      Pixelmon.network.sendTo(new SetDialogueData(dialogues, openGui, quest), player);
   }

   public static DialogueBuilder builder() {
      return new DialogueBuilder();
   }

   public static class DialogueBuilder {
      private String name = "";
      private String text = null;
      private String localizedText = null;
      private ArrayList choices = new ArrayList();

      public Dialogue build() {
         return new Dialogue(this.name, this.text, this.localizedText, this.choices);
      }

      public DialogueBuilder setName(String name) {
         this.name = name;
         return this;
      }

      public DialogueBuilder setText(String text) {
         this.text = text;
         return this;
      }

      /** @deprecated */
      @Deprecated
      public DialogueBuilder setLocalizedText(String localizedText) {
         if (this.text != null) {
            throw new IllegalStateException("You must use setText()");
         } else {
            this.localizedText = localizedText;
            return this;
         }
      }

      public DialogueBuilder addChoice(Choice choice) {
         this.choices.add(choice);
         return this;
      }

      public DialogueBuilder setChoices(ArrayList choices) {
         this.choices = choices;
         return this;
      }

      public DialogueBuilder injectHandler(Consumer handle) {
         ArrayList newChoices = new ArrayList();
         Iterator var3 = this.choices.iterator();

         while(var3.hasNext()) {
            Choice choice = (Choice)var3.next();
            newChoices.add(Choice.builder().setText(choice.text).setHandle(handle).build(choice.choiceID));
         }

         this.setChoices(newChoices);
         return this;
      }

      public void open(EntityPlayerMP... players) {
         this.build().open(players);
      }
   }
}
