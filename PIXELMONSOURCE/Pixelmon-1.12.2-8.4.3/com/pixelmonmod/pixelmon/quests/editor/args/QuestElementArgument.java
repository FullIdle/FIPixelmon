package com.pixelmonmod.pixelmon.quests.editor.args;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class QuestElementArgument {
   private String name;
   private boolean optional;
   private boolean string;
   private ArgumentType type;
   private String[] options;
   private String currentValue = "-";

   public QuestElementArgument(String name, boolean optional, boolean string, ArgumentType type, String... options) {
      this.name = name;
      this.optional = optional;
      this.string = string;
      this.type = type;
      this.options = options;
      if (this.options.length == 0) {
         if (type == ArgumentType.BOOLEAN) {
            this.options = new String[]{"true", "false"};
         } else if (type == ArgumentType.SUCCESS) {
            this.options = new String[]{"success", "failure"};
         }
      }

   }

   public QuestElementArgument(ByteBuf buf) {
      this.readFromByteBuf(buf);
   }

   public QuestElementArgument copy() {
      String[] options = new String[this.options.length];
      System.arraycopy(this.options, 0, options, 0, options.length);
      return new QuestElementArgument(this.name, this.optional, this.string, this.type, options);
   }

   public String getName() {
      String langKey = "quest.argument." + this.name;
      return I18n.func_188566_a(langKey) ? I18n.func_135052_a(langKey, new Object[0]) : this.name;
   }

   public String getCurrentValue() {
      return this.currentValue.isEmpty() ? "-" : (this.type == ArgumentType.TEXT ? this.currentValue.replace("_", " ") : this.currentValue);
   }

   public String getTrueCurrentValue() {
      return this.currentValue;
   }

   public boolean isString() {
      return this.string;
   }

   public void setCurrentValue(String value) {
      this.currentValue = value.isEmpty() ? "-" : value;
      if (this.type == ArgumentType.TEXT) {
         this.currentValue = this.currentValue.replace(" ", "_");
      }

   }

   public boolean isEmpty() {
      return this.currentValue.isEmpty() || this.currentValue.equalsIgnoreCase("-");
   }

   public boolean isValid() {
      if (!this.isEmpty() || !this.optional && !this.string) {
         if (this.options.length > 0) {
            String[] var1 = this.options;
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               String option = var1[var3];
               if (option.equalsIgnoreCase(this.currentValue)) {
                  return true;
               }
            }

            return false;
         } else {
            return this.type.isValidInput(this.currentValue);
         }
      } else {
         return true;
      }
   }

   public String[] getOptions() {
      return this.options;
   }

   public void readFromByteBuf(ByteBuf buf) {
      this.name = ByteBufUtils.readUTF8String(buf);
      this.optional = buf.readBoolean();
      this.string = buf.readBoolean();
      this.type = ArgumentType.values()[buf.readByte()];
      this.options = new String[buf.readShort()];

      for(int i = 0; i < this.options.length; ++i) {
         this.options[i] = ByteBufUtils.readUTF8String(buf);
      }

      this.currentValue = ByteBufUtils.readUTF8String(buf);
   }

   public void writeToByteBuf(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.name);
      buf.writeBoolean(this.optional);
      buf.writeBoolean(this.string);
      buf.writeByte(this.type.ordinal());
      buf.writeShort(this.options.length);
      String[] var2 = this.options;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String option = var2[var4];
         ByteBufUtils.writeUTF8String(buf, option);
      }

      ByteBufUtils.writeUTF8String(buf, this.currentValue);
   }
}
