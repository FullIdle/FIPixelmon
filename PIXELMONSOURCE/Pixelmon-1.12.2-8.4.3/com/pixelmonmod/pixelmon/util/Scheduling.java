package com.pixelmonmod.pixelmon.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class Scheduling {
   private static ArrayList tasks = new ArrayList();
   private static boolean iterating = false;
   private static ArrayList queue = new ArrayList();

   public static void tick() {
      iterating = true;
      Iterator it = tasks.iterator();

      while(it.hasNext()) {
         ScheduledTask t = (ScheduledTask)it.next();
         if (--t.ticks <= 0) {
            t.task.accept(t);
            if (t.repeats) {
               t.ticks = t.originalTicks;
            } else {
               it.remove();
            }
         }
      }

      iterating = false;
      if (!queue.isEmpty()) {
         tasks.addAll(queue);
         queue.clear();
      }

   }

   public static void schedule(int ticks, Runnable task, boolean repeats) {
      schedule(ticks, (t) -> {
         task.run();
      }, repeats);
   }

   public static void schedule(int ticks, Consumer task, boolean repeats) {
      ScheduledTask t = new ScheduledTask();
      t.ticks = ticks;
      t.task = task;
      t.repeats = repeats;
      t.originalTicks = ticks;
      if (iterating) {
         queue.add(t);
      } else {
         tasks.add(t);
      }

   }

   public static class ScheduledTask {
      public int originalTicks;
      public int ticks;
      public Consumer task;
      public boolean repeats = false;
   }
}
