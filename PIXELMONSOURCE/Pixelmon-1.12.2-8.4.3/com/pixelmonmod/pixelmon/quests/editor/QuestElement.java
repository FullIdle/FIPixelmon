package com.pixelmonmod.pixelmon.quests.editor;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiTextField;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuestElement {
   private QuestElementType type;
   private String name;
   private String identifier;
   private QuestElementArgument[] args;
   private transient String extra;
   private final ArrayList fields;

   public QuestElement(QuestElementType type, String identifier, boolean unused) {
      this(type, identifier, new QuestElementArgument("Arg 1", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("Arg 2", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("Arg 3", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("Arg 4", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("Arg 5", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("Arg 6", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("Arg 7", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("Arg 8", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("Arg 9", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("Arg 10", true, false, ArgumentType.TEXT, new String[0]));
   }

   public QuestElement(QuestElementType type, String identifier, QuestElementArgument... args) {
      this(type, identifier, "quest.element." + identifier.toLowerCase() + ".name", args);
   }

   public QuestElement(QuestElementType type, String identifier, String name, QuestElementArgument... args) {
      this.extra = "";
      this.fields = new ArrayList();
      this.type = type;
      this.name = name;
      this.identifier = identifier;
      this.args = args;
   }

   public QuestElement(ByteBuf buf) {
      this.extra = "";
      this.fields = new ArrayList();
      this.readFromByteBuf(buf);
   }

   @SideOnly(Side.CLIENT)
   public void initialize(Minecraft mc, int x, int y, int w, int h) {
      int left = x + w / 2 + 6;
      int top = y + 22;
      int width = 121;
      int height = 9;
      this.fields.clear();
      int i = 0;
      QuestElementArgument[] var11 = this.args;
      int var12 = var11.length;

      for(int var13 = 0; var13 < var12; ++var13) {
         QuestElementArgument arg = var11[var13];
         mc.field_71466_p.func_78264_a(true);
         GuiTextField field = new GuiTextField(mc.field_71466_p, left, top + (height + 8) * i, width, height, -6908266, -7895161);
         field.setMaxStringLength(Integer.MAX_VALUE);
         field.setText(arg.getCurrentValue());
         this.updateValidity(field, arg);
         this.fields.add(field);
         mc.field_71466_p.func_78264_a(false);
         ++i;
      }

   }

   @SideOnly(Side.CLIENT)
   public void draw(Minecraft mc, int x, int y, int w, int h) {
      if (this.fields.size() != this.args.length) {
         this.initialize(mc, x, y, w, h);
      }

      int index = 0;

      for(Iterator var7 = this.fields.iterator(); var7.hasNext(); ++index) {
         GuiTextField field = (GuiTextField)var7.next();
         QuestElementArgument arg = this.args[index];
         mc.field_71466_p.func_78264_a(true);
         field.drawTextBox();
         mc.field_71466_p.func_78276_b(TextFormatting.BOLD + arg.getName(), field.xPosition, field.yPosition - 8, 0);
         mc.field_71466_p.func_78264_a(false);
         if (arg.getOptions().length > 0) {
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b(770, 771);
            GlStateManager.func_179131_c(0.0F, 0.6F, 1.0F, 1.0F);
            GuiHelper.drawImage(GuiResources.refresh, (double)(field.xPosition + field.width - field.height), (double)field.yPosition, (double)field.height, (double)field.height, 2.0F);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void onClick(int mouseX, int mouseY, int mouseButton) {
      int index = 0;

      for(Iterator var5 = this.fields.iterator(); var5.hasNext(); ++index) {
         GuiTextField field = (GuiTextField)var5.next();
         QuestElementArgument arg = this.args[index];
         field.field_146211_a.func_78264_a(true);
         boolean skip = false;
         if (arg.getOptions().length > 0) {
            int x = field.xPosition + field.width - field.height;
            int y = field.yPosition;
            if (mouseX > x && mouseX < x + field.height && mouseY > y && mouseY < y + field.height) {
               skip = true;
               boolean set = false;
               String value = arg.getTrueCurrentValue();

               for(int i = 0; i < arg.getOptions().length; ++i) {
                  if (arg.getOptions()[i].equalsIgnoreCase(value)) {
                     if (i + 1 == arg.getOptions().length) {
                        arg.setCurrentValue("");
                     } else {
                        arg.setCurrentValue(arg.getOptions()[i + 1]);
                     }

                     set = true;
                  }
               }

               if (!set) {
                  arg.setCurrentValue(arg.getOptions()[0]);
               }

               field.setText(arg.getCurrentValue());
               this.updateValidity(field, arg);
            }
         }

         if (!skip) {
            field.mouseClicked(mouseX, mouseY, mouseButton);
         }

         field.field_146211_a.func_78264_a(false);
      }

   }

   @SideOnly(Side.CLIENT)
   public void onType(char c, int i) {
      int index = 0;

      for(Iterator var4 = this.fields.iterator(); var4.hasNext(); ++index) {
         GuiTextField field = (GuiTextField)var4.next();
         field.field_146211_a.func_78264_a(true);
         if (field.isFocused() && field.textboxKeyTyped(c, i)) {
            QuestElementArgument arg = this.args[index];
            arg.setCurrentValue(field.getText());
            this.updateValidity(field, arg);
         }

         field.field_146211_a.func_78264_a(false);
      }

   }

   @SideOnly(Side.CLIENT)
   public void updateValidity(GuiTextField field, QuestElementArgument arg) {
      boolean valid = arg.isValid();
      field.setTextColor(valid ? 16777215 : 14889216);
      field.setBorderColour(valid ? -7895161 : -1888000);
   }

   public QuestElement copy() {
      QuestElementArgument[] args = new QuestElementArgument[this.args.length];

      for(int i = 0; i < args.length; ++i) {
         args[i] = this.args[i].copy();
      }

      return new QuestElement(this.type, this.identifier, args);
   }

   public void duplicate(QuestElement element) {
      this.type = element.type;
      this.name = element.name;
      this.identifier = element.identifier;
      this.args = new QuestElementArgument[element.args.length];

      for(int i = 0; i < this.args.length; ++i) {
         this.args[i] = element.args[i].copy();
      }

   }

   public QuestElement create(Quest quest, String args) {
      QuestElement qe = this.copy();
      String[] split = args.split(" ");
      int start = 1;
      if (qe.type == QuestElementType.ACTION) {
         qe.setExtra(split[0]);
         ++start;
      }

      for(int i = start; i < split.length; ++i) {
         int argIndex = i - start;
         if (argIndex < qe.args.length) {
            String value = split[i];
            if (qe.args[argIndex].isString()) {
               String str = quest.getUnlocalizedString(value);
               boolean translatable = str != null && I18n.func_94522_b(str);
               if (!translatable && (str == null || value.isEmpty() || value.equalsIgnoreCase("-"))) {
                  value = "";
               } else if (!translatable) {
                  value = str;
               }
            }

            qe.args[argIndex].setCurrentValue(value);
         }
      }

      return qe;
   }

   public void setExtra(String extra) {
      this.extra = extra;
   }

   public String getExtra() {
      return this.extra;
   }

   public QuestElementType getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public QuestElementArgument[] getArgs() {
      return this.args;
   }

   public String build(Quest quest, Stage stage, int i) {
      StringBuilder builder;
      int j;
      QuestElementArgument[] var6;
      int var7;
      int var8;
      QuestElementArgument arg;
      String key;
      if (this.type == QuestElementType.OBJECTIVE) {
         builder = new StringBuilder(this.identifier);
         j = 0;
         var6 = this.args;
         var7 = var6.length;

         for(var8 = 0; var8 < var7; ++var8) {
            arg = var6[var8];
            if (arg.isString()) {
               key = "s" + stage.getStage() + "-o" + i + "-p" + j++;
               if (arg.isEmpty()) {
                  builder.append(" -");
                  quest.removeUnlocalizedString(key);
               } else if (!I18n.func_94522_b(quest.getLangKey(arg.getTrueCurrentValue())) && !I18n.func_94522_b(arg.getTrueCurrentValue())) {
                  builder.append(" ").append(key);
                  quest.putUnlocalizedString(key, arg.getCurrentValue());
               } else {
                  builder.append(" ").append(arg.getTrueCurrentValue());
               }
            } else {
               builder.append(" ").append(arg.getTrueCurrentValue());
            }
         }

         return builder.toString();
      } else if (this.type != QuestElementType.ACTION) {
         return "";
      } else {
         builder = (new StringBuilder(this.extra)).append(" ").append(this.identifier);
         j = 0;
         var6 = this.args;
         var7 = var6.length;

         for(var8 = 0; var8 < var7; ++var8) {
            arg = var6[var8];
            if (arg.isString()) {
               key = "s" + stage.getStage() + "-a" + i + "-p" + j++;
               if (arg.isEmpty()) {
                  builder.append(" -");
                  quest.removeUnlocalizedString(key);
               } else if (!I18n.func_94522_b(quest.getLangKey(arg.getTrueCurrentValue())) && !I18n.func_94522_b(arg.getTrueCurrentValue())) {
                  builder.append(" ").append(key);
                  quest.putUnlocalizedString(key, arg.getCurrentValue());
               } else {
                  builder.append(" ").append(arg.getTrueCurrentValue());
               }
            } else {
               builder.append(" ").append(arg.getTrueCurrentValue());
            }
         }

         return builder.toString();
      }
   }

   public void readFromByteBuf(ByteBuf buf) {
      this.type = QuestElementType.values()[buf.readByte()];
      this.name = ByteBufUtils.readUTF8String(buf);
      this.identifier = ByteBufUtils.readUTF8String(buf);
      this.args = new QuestElementArgument[buf.readShort()];

      for(int i = 0; i < this.args.length; ++i) {
         this.args[i] = new QuestElementArgument(buf);
      }

   }

   public void writeToByteBuf(ByteBuf buf) {
      buf.writeByte(this.type.ordinal());
      ByteBufUtils.writeUTF8String(buf, this.name);
      ByteBufUtils.writeUTF8String(buf, this.identifier);
      buf.writeShort(this.args.length);
      QuestElementArgument[] var2 = this.args;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         QuestElementArgument arg = var2[var4];
         arg.writeToByteBuf(buf);
      }

   }
}
