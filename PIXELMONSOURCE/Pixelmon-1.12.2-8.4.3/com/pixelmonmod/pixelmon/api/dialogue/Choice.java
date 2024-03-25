package com.pixelmonmod.pixelmon.api.dialogue;

import com.pixelmonmod.pixelmon.quests.client.QuestProgressClient;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.function.Consumer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class Choice {
   public static final HashMap handleMap = new HashMap();
   private static int nextID = 0;
   public final int choiceID;
   public final String text;
   public final Consumer handle;

   public Choice(String text, Consumer handle) {
      this(text, handle, nextID++);
   }

   public Choice(String text, Consumer handle, int choiceID) {
      this.choiceID = choiceID;
      this.text = text;
      this.handle = handle;
   }

   public Choice(ByteBuf buffer) {
      this(buffer, false);
   }

   public Choice(ByteBuf buffer, boolean localized) {
      this.choiceID = buffer.readInt();
      String text = ByteBufUtils.readUTF8String(buffer);
      this.text = localized ? I18n.func_135052_a(text, new Object[0]) : text;
      this.handle = null;
   }

   public Choice(ByteBuf buffer, QuestProgressClient quest) {
      this.choiceID = buffer.readInt();
      String text = ByteBufUtils.readUTF8String(buffer);
      this.text = quest.format(text);
      this.handle = null;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.choiceID);
      ByteBufUtils.writeUTF8String(buffer, this.text);
   }

   public static ChoiceBuilder builder() {
      return new ChoiceBuilder();
   }

   public static class ChoiceBuilder {
      private String text;
      private Consumer handle;

      public ChoiceBuilder setText(String text) {
         this.text = text;
         return this;
      }

      public ChoiceBuilder setHandle(Consumer handle) {
         this.handle = handle;
         return this;
      }

      public Choice build() {
         return new Choice(this.text, this.handle);
      }

      public Choice build(int choiceID) {
         return new Choice(this.text, this.handle, choiceID);
      }
   }
}
