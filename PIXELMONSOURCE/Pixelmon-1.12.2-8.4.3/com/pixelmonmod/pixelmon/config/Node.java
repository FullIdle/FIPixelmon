package com.pixelmonmod.pixelmon.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Node {
   PixelmonConfig.Category category();

   String nameOverride() default "";

   double minValue() default -2.147483648E9;

   double maxValue() default 2.147483647E9;

   Class type() default Void.class;

   boolean useSlider() default false;

   boolean requiresRestart() default false;
}
