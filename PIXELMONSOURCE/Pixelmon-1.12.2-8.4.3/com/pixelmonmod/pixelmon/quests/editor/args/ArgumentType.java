package com.pixelmonmod.pixelmon.quests.editor.args;

import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.Iterator;
import net.minecraft.item.Item;
import org.apache.commons.lang3.math.NumberUtils;

public enum ArgumentType {
   WHOLE_NUMBER,
   DECIMAL_NUMBER,
   TEXT,
   SPACED_TEXT,
   ENTITY_UUID,
   BOOLEAN,
   SUCCESS,
   ITEM,
   TYPE,
   DATE;

   public boolean isValidInput(String input) {
      switch (this) {
         case WHOLE_NUMBER:
            if (input.startsWith("-")) {
               input = input.substring(1);
            }

            return NumberUtils.isDigits(input);
         case DECIMAL_NUMBER:
            return NumberUtils.isParsable(input);
         case TEXT:
         case SPACED_TEXT:
            return !input.isEmpty();
         case ENTITY_UUID:
            String[] components = input.split("-");
            return components.length == 5;
         case BOOLEAN:
            return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false");
         case SUCCESS:
            return input.equalsIgnoreCase("success") || input.equalsIgnoreCase("failure");
         case ITEM:
            return Item.func_111206_d(input) != null;
         case TYPE:
            Iterator var3 = EnumType.getAllTypes().iterator();

            EnumType type;
            do {
               if (!var3.hasNext()) {
                  return false;
               }

               type = (EnumType)var3.next();
            } while(!type.func_176610_l().equalsIgnoreCase(input));

            return true;
         case DATE:
            return input.split("/").length == 2;
         default:
            return false;
      }
   }
}
