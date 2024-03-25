package com.pixelmonmod.pixelmon.quests.quest;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Arguments implements Iterable {
   private final ArrayList arguments = new ArrayList();

   private Arguments(Argument... arguments) {
      this.arguments.addAll(Arrays.asList(arguments));
   }

   public static Arguments create(Argument... arguments) {
      return new Arguments(arguments);
   }

   public static Arguments empty() {
      return new Arguments(new Argument[0]);
   }

   public ArrayList getArguments() {
      return this.arguments;
   }

   public boolean isAllSet(QuestProgress progress) {
      Iterator var2 = this.iterator();

      Argument argument;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         argument = (Argument)var2.next();
      } while(argument.isPresent(progress));

      return false;
   }

   public Argument get(int index) {
      return (Argument)this.arguments.get(index);
   }

   public Object value(int index, QuestProgress progress) {
      return this.get(index).value(progress);
   }

   public int quantity(int index, QuestProgress progress) {
      Argument argument = this.get(index);
      return argument.isPresent(progress) ? (Integer)argument.value(progress) : 1;
   }

   public int size() {
      return this.arguments.size();
   }

   public int usableSize() {
      int count = 0;
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Argument argument = (Argument)var2.next();
         if (!argument.isEmpty()) {
            ++count;
         }
      }

      return count;
   }

   public Iterator iterator() {
      return this.arguments.iterator();
   }

   public void forEach(Consumer action) {
      this.arguments.forEach(action);
   }

   public Spliterator spliterator() {
      return this.arguments.spliterator();
   }
}
