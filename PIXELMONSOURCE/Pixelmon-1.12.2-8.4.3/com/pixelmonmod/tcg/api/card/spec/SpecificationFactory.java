package com.pixelmonmod.tcg.api.card.spec;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.nbt.NBTTagCompound;

public class SpecificationFactory {
   private static final Map REGISTERED_REQUIREMENTS = Maps.newHashMap();

   public static void register(Class clazz, Requirement requirement) {
      SpecTypeData specTypeData = (SpecTypeData)REGISTERED_REQUIREMENTS.computeIfAbsent(clazz, (___) -> {
         return new SpecTypeData(clazz);
      });
      specTypeData.getRequirements().add(requirement);
      specTypeData.getRequirements().sort(Comparator.comparing((o) -> {
         return ((Requirement)o).getPriority();
      }).reversed());
      Iterator var3 = requirement.getAliases().iterator();

      while(var3.hasNext()) {
         String alias = (String)var3.next();
         specTypeData.getRequirementNames().add(alias.trim());
      }

   }

   public static List requirements(Class specification, String spec) {
      if (!REGISTERED_REQUIREMENTS.containsKey(specification)) {
         return Collections.emptyList();
      } else {
         List requirements = Lists.newArrayList();
         SpecTypeData specTypeData = (SpecTypeData)REGISTERED_REQUIREMENTS.get(specification);
         Iterator var4 = specTypeData.getRequirements().iterator();

         while(var4.hasNext()) {
            Requirement registeredRequirement = (Requirement)var4.next();
            if (registeredRequirement.fits(spec)) {
               requirements.addAll(registeredRequirement.create(spec));
               if (!registeredRequirement.shouldContinue()) {
                  break;
               }
            }
         }

         return requirements;
      }
   }

   public static Specification create(Class clazz, String... specs) {
      StringBuilder builder = new StringBuilder();
      String[] var3 = specs;
      int var4 = specs.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String spec = var3[var5];
         if (builder.length() > 0) {
            builder.append(" ");
         }

         builder.append(spec);
      }

      SpecTypeData specTypeData = (SpecTypeData)REGISTERED_REQUIREMENTS.get(clazz);
      if (specTypeData == null) {
         return null;
      } else {
         try {
            return (Specification)specTypeData.getConstructor().newInstance(builder.toString(), requirements(clazz, builder.toString()));
         } catch (IllegalAccessException | InvocationTargetException | InstantiationException var7) {
            Pixelmon.LOGGER.error("Couldn't construct " + clazz.getSimpleName() + " specification");
            var7.printStackTrace();
            return null;
         }
      }
   }

   public static Specification create(Class clazz, Object... args) {
      String spec = (String)Arrays.stream(args).map(Object::toString).collect(Collectors.joining(" "));
      SpecTypeData specTypeData = (SpecTypeData)REGISTERED_REQUIREMENTS.get(clazz);
      if (specTypeData == null) {
         return null;
      } else {
         try {
            return (Specification)specTypeData.getConstructor().newInstance(spec, requirements(clazz, spec));
         } catch (IllegalAccessException | InvocationTargetException | InstantiationException var5) {
            Pixelmon.LOGGER.error("Couldn't construct " + clazz.getSimpleName() + " specification");
            var5.printStackTrace();
            return null;
         }
      }
   }

   public static Specification empty() {
      return EmptySpecification.INSTANCE;
   }

   public static String[] getRequirementNames(Class clazz) {
      SpecTypeData specTypeData = (SpecTypeData)REGISTERED_REQUIREMENTS.get(clazz);
      return specTypeData == null ? new String[0] : (String[])specTypeData.getRequirementNames().toArray(new String[0]);
   }

   public static Specification fromNbt(Class clazz, NBTTagCompound nbt) {
      return !nbt.func_74764_b("spec") ? create(clazz) : create(clazz, nbt.func_74779_i("spec"));
   }

   private static class SpecTypeData {
      private Constructor constructor;
      private List requirements;
      private List requirementNames;

      private SpecTypeData(Class clazz) {
         this.requirements = Lists.newArrayList();
         this.requirementNames = Lists.newArrayList();

         try {
            this.constructor = clazz.getConstructor(String.class, List.class);
         } catch (NoSuchMethodException var3) {
            Pixelmon.LOGGER.error("Couldn't construct " + clazz.getSimpleName() + " specification");
            var3.printStackTrace();
         }

      }

      public Constructor getConstructor() {
         return this.constructor;
      }

      public List getRequirements() {
         return this.requirements;
      }

      public List getRequirementNames() {
         return this.requirementNames;
      }

      // $FF: synthetic method
      SpecTypeData(Class x0, Object x1) {
         this(x0);
      }
   }
}
