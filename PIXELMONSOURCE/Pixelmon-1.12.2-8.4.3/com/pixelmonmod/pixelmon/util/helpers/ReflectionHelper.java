package com.pixelmonmod.pixelmon.util.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

public class ReflectionHelper {
   public static Object getPrivateValue(Class clazz, Object instance, String fieldName, String obfName) {
      try {
         Field field = findField(clazz, fieldName, obfName);
         return field.get(instance);
      } catch (Exception var5) {
         throw new net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException(var5);
      }
   }

   public static Object getPrivateValue(Class clazz, Object instance, String fieldName) {
      try {
         Field field = findField(clazz, fieldName, fieldName);
         return field.get(instance);
      } catch (Exception var4) {
         throw new net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException(var4);
      }
   }

   public static void setPrivateValue(Class clazz, Object instance, Object value, String fieldName, String obfName) {
      try {
         Field field = findField(clazz, fieldName, obfName);
         field.set(instance, value);
      } catch (Exception var6) {
         throw new net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException(var6);
      }
   }

   public static void setPrivateValue(Class clazz, Object instance, Object value, String fieldName) {
      try {
         Field field = findField(clazz, fieldName, fieldName);
         field.setAccessible(true);
         field.set(instance, value);
      } catch (Exception var5) {
         throw new net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException(var5);
      }
   }

   public static Field findField(Class clazz, String fieldName, String obfName) {
      try {
         Field field = clazz.getDeclaredField(FMLLaunchHandler.isDeobfuscatedEnvironment() ? fieldName : obfName);
         field.setAccessible(true);
         return field;
      } catch (Exception var4) {
         throw new net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindFieldException(var4);
      }
   }

   public static Method findMethod(Class clazz, String name, String obfName, Class... parameters) {
      try {
         Method method = clazz.getDeclaredMethod(FMLLaunchHandler.isDeobfuscatedEnvironment() ? name : obfName, parameters);
         method.setAccessible(true);
         return method;
      } catch (Exception var5) {
         throw new net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException(var5);
      }
   }
}
