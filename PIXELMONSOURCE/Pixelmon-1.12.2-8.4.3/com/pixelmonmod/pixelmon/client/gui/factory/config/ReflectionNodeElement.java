package com.pixelmonmod.pixelmon.client.gui.factory.config;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.Node;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ReflectionNodeElement implements IConfigElement {
   private final Field field;
   Object defaultValue;
   String name;
   String humanName;
   String comment;
   Number lowerBound = Integer.MIN_VALUE;
   Number upperBound = Integer.MAX_VALUE;
   boolean useSlider = false;
   boolean requiresRestart = false;
   Consumer reloadFunction = (o) -> {
   };

   public ReflectionNodeElement(Field field, Object defaultValue) {
      this.field = field;
      this.defaultValue = defaultValue;
      this.field.setAccessible(true);
      this.readAnnotation(field);
   }

   public void readAnnotation(Field field) {
      Node node = (Node)field.getAnnotation(Node.class);
      String name = node.nameOverride().isEmpty() ? field.getName() : node.nameOverride();
      this.name = name;
      this.humanName = I18n.func_74838_a("pixelmon.config." + name);
      this.comment = I18n.func_74838_a("pixelmon.config." + name + ".comment");
      this.useSlider = node.useSlider();
      this.requiresRestart = node.requiresRestart();
      if (this.getType() == ConfigGuiType.INTEGER) {
         this.lowerBound = (int)node.minValue();
         this.upperBound = (int)node.maxValue();
      } else {
         this.lowerBound = node.minValue();
         this.upperBound = node.maxValue();
      }

   }

   public void setReloadFunction(Consumer reloadFunction) {
      this.reloadFunction = reloadFunction;
   }

   public boolean isProperty() {
      return true;
   }

   public Class getConfigEntryClass() {
      return this.useSlider ? GuiConfigEntries.NumberSliderEntry.class : null;
   }

   public Class getArrayEntryClass() {
      return null;
   }

   public String getName() {
      return this.humanName;
   }

   public String getQualifiedName() {
      return null;
   }

   public String getLanguageKey() {
      return "";
   }

   public String getComment() {
      return this.comment != null && !this.comment.isEmpty() ? this.comment : "No comment?";
   }

   public List getChildElements() {
      return null;
   }

   public ConfigGuiType getType() {
      return this.getType(this.defaultValue);
   }

   public ConfigGuiType getType(Object value) {
      if (value instanceof Boolean) {
         return ConfigGuiType.BOOLEAN;
      } else if (value instanceof Integer) {
         return ConfigGuiType.INTEGER;
      } else if (value instanceof String) {
         return ConfigGuiType.STRING;
      } else if (!(value instanceof Double) && !(value instanceof Float)) {
         if (value instanceof List) {
            List list = (List)value;
            return !list.isEmpty() ? this.getType(list.get(0)) : ConfigGuiType.STRING;
         } else {
            Pixelmon.LOGGER.error("TODO getType " + value.getClass());
            return ConfigGuiType.STRING;
         }
      } else {
         return ConfigGuiType.DOUBLE;
      }
   }

   public boolean isList() {
      return this.defaultValue instanceof List;
   }

   public boolean isListLengthFixed() {
      return false;
   }

   public int getMaxListLength() {
      return -1;
   }

   public boolean isDefault() {
      return true;
   }

   public Object getDefault() {
      return this.defaultValue;
   }

   public Object[] getDefaults() {
      return this.defaultValue instanceof List ? ((List)this.defaultValue).toArray() : null;
   }

   public void setToDefault() {
      this.set(this.getDefault());
   }

   public boolean requiresWorldRestart() {
      return false;
   }

   public boolean showInGui() {
      return true;
   }

   public boolean requiresMcRestart() {
      return this.requiresRestart;
   }

   public Object get() {
      try {
         return this.field.getType().isEnum() ? ((Enum)this.field.get((Object)null)).ordinal() : this.field.get((Object)null);
      } catch (IllegalAccessException var2) {
         var2.printStackTrace();
         return this.defaultValue;
      }
   }

   public Object[] getList() {
      List list = (List)this.get();
      return list.toArray();
   }

   public void set(Object value) {
      try {
         if (this.field.getType().isEnum()) {
            this.field.set((Object)null, this.field.getType().getEnumConstants()[(Integer)value]);
         } else if (this.field.getType() == Float.TYPE && value instanceof Double) {
            this.field.setFloat((Object)null, ((Double)value).floatValue());
         } else {
            this.field.set((Object)null, value);
         }

         this.reloadFunction.accept(value);
         PixelmonConfig.saveConfig();
      } catch (IllegalAccessException var3) {
         var3.printStackTrace();
      }

   }

   public void set(Object[] aVal) {
      try {
         if (aVal.length >= 1) {
            Object type = aVal[0];
            if (type instanceof String) {
               this.set((Object)Lists.newArrayList(Arrays.copyOf(aVal, aVal.length, String[].class)));
            } else if (type instanceof Integer) {
               this.set((Object)Lists.newArrayList(Arrays.copyOf(aVal, aVal.length, Integer[].class)));
            } else if (type instanceof Boolean) {
               this.set((Object)Lists.newArrayList(Arrays.copyOf(aVal, aVal.length, Boolean[].class)));
            } else if (type instanceof Float) {
               this.set((Object)Lists.newArrayList(Arrays.copyOf(aVal, aVal.length, Float[].class)));
            } else if (type instanceof Double) {
               this.set((Object)Lists.newArrayList(Arrays.copyOf(aVal, aVal.length, Double[].class)));
            }
         } else {
            this.set((Object[])null);
         }

         this.reloadFunction.accept(aVal);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      PixelmonConfig.saveConfig();
   }

   public String[] getValidValues() {
      return new String[0];
   }

   public String[] getValidValuesDisplay() {
      return new String[0];
   }

   public Object getMinValue() {
      return this.lowerBound;
   }

   public Object getMaxValue() {
      return this.upperBound;
   }

   public Pattern getValidationPattern() {
      return null;
   }
}
