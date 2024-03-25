package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.api.world.WeatherType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class WeatherCondition extends EvoCondition {
   public WeatherType weather;

   public WeatherCondition() {
      super("weather");
      this.weather = WeatherType.RAIN;
   }

   public WeatherCondition(WeatherType weather) {
      this();
      this.weather = weather;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return pixelmon.func_70902_q() != null ? this.weather.isCurrent(pixelmon.func_70902_q().func_130014_f_()) : this.weather.isCurrent(pixelmon.func_130014_f_());
   }
}
