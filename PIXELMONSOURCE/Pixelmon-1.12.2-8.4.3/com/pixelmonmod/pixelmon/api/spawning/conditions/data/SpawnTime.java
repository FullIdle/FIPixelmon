package com.pixelmonmod.pixelmon.api.spawning.conditions.data;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class SpawnTime {
   private String timeZone = null;
   private int dayOfWeek = -1;
   private int hourOfDay = -1;
   private int minuteOfHour = -1;
   private transient TimeZone cachedTimeZone = null;

   public SpawnTime() {
   }

   public SpawnTime(String timeZone, int dayOfWeek, int hourOfDay, int minuteOfHour, TimeZone cachedTimeZone) {
      this.timeZone = timeZone;
      this.dayOfWeek = dayOfWeek;
      this.hourOfDay = hourOfDay;
      this.minuteOfHour = minuteOfHour;
      this.cachedTimeZone = cachedTimeZone;
   }

   public boolean matches() {
      Calendar calendar = new GregorianCalendar(this.getCachedTimeZone());
      if (this.dayOfWeek != -1 && calendar.get(7) != this.dayOfWeek) {
         return false;
      } else if (this.hourOfDay != -1 && calendar.get(11) != this.hourOfDay) {
         return false;
      } else {
         return this.minuteOfHour == -1 || calendar.get(12) == this.minuteOfHour;
      }
   }

   private TimeZone getCachedTimeZone() {
      if (this.cachedTimeZone == null) {
         if (this.timeZone != null) {
            this.cachedTimeZone = TimeZone.getTimeZone(this.timeZone);
         } else {
            this.cachedTimeZone = TimeZone.getDefault();
         }
      }

      return this.cachedTimeZone;
   }
}
