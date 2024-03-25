package com.pixelmonmod.pixelmon.api.spawning.calculators;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public interface ICheckSpawns {
   static ICheckSpawns getDefault() {
      return (spawner, sender, arguments) -> {
         sender.func_145747_a(new TextComponentString("Not implemented."));
      };
   }

   default String getPermissionNode() {
      return "pixelmon.checkspawns.normal";
   }

   void checkSpawns(AbstractSpawner var1, ICommandSender var2, List var3);

   default List generateMessages(Map percentages, List arguments) {
      List translationMessages = new ArrayList();
      List messages = new ArrayList();
      Predicate caredAbout = null;
      Iterator var6 = arguments.iterator();

      while(var6.hasNext()) {
         String argument = (String)var6.next();
         Iterator var8 = percentages.keySet().iterator();

         while(var8.hasNext()) {
            String key = (String)var8.next();
            if (key.equalsIgnoreCase(argument)) {
               String needed;
               if (EnumSpecies.hasPokemonAnyCase(argument)) {
                  needed = I18n.func_74838_a("pixelmon." + EnumSpecies.getFromNameAnyCase(argument).name.toLowerCase() + ".name");
               } else {
                  needed = I18n.func_74838_a(argument);
               }

               if (caredAbout == null) {
                  caredAbout = (s) -> {
                     return s.equalsIgnoreCase(needed);
                  };
               } else {
                  caredAbout = (s) -> {
                     return caredAbout.test(s) || s.equalsIgnoreCase(needed);
                  };
               }
            }
         }
      }

      if (caredAbout == null) {
         caredAbout = (s) -> {
            return true;
         };
      }

      double greenThreshold = 10.0;
      double yellowThreshold = 2.0;
      double redThreshold = 0.5;
      double lightPurpleThreshold = 0.001;
      ArrayList sortedEntries = new ArrayList(percentages.entrySet());
      sortedEntries.sort(Entry.comparingByValue());
      Iterator var15 = sortedEntries.iterator();

      while(var15.hasNext()) {
         Map.Entry entry = (Map.Entry)var15.next();
         if (caredAbout.test(entry.getKey())) {
            TextFormatting colour = TextFormatting.DARK_PURPLE;
            if ((Double)entry.getValue() > 10.0) {
               colour = TextFormatting.GREEN;
            } else if ((Double)entry.getValue() > 2.0) {
               colour = TextFormatting.YELLOW;
            } else if ((Double)entry.getValue() > 0.5) {
               colour = TextFormatting.RED;
            } else if ((Double)entry.getValue() > 0.001) {
               colour = TextFormatting.LIGHT_PURPLE;
            }

            messages.add(TextFormatting.GOLD + (String)entry.getKey() + TextFormatting.GRAY + ": " + colour + entry.getValue() + "%");
         }
      }

      while(!messages.isEmpty()) {
         String message = (String)messages.remove(0);

         for(int onThisLine = 1; !messages.isEmpty() && onThisLine++ < 3; message = message + TextFormatting.GOLD + ", " + (String)messages.remove(0)) {
         }

         translationMessages.add(new TextComponentString(message));
      }

      return translationMessages;
   }

   default TextComponentTranslation translate(TextFormatting colour, String text, Object... args) {
      TextComponentTranslation translation = new TextComponentTranslation(text, args);
      translation.func_150256_b().func_150238_a(colour);
      return translation;
   }
}
