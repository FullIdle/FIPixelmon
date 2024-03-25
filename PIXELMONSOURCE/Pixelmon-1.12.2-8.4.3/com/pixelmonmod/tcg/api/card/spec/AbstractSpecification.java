package com.pixelmonmod.tcg.api.card.spec;

import com.google.common.collect.Lists;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractSpecification implements Specification {
   protected final Class dataType;
   protected final Class minecraftType;
   protected final String originalSpec;
   protected final List requirements = Lists.newArrayList();

   public AbstractSpecification(Class dataType, Class minecraftType, String originalSpec, List requirements) {
      this.dataType = dataType;
      this.minecraftType = minecraftType;
      this.originalSpec = originalSpec;
      this.requirements.addAll(requirements);
      this.requirements.sort(Comparator.comparing((o) -> {
         return ((Requirement)o).getPriority();
      }).reversed());
   }

   public boolean matches(Object o) {
      if (o == null) {
         return false;
      } else {
         Class clazz = o.getClass();
         BiPredicate predicate = null;
         if (clazz.isAssignableFrom(this.dataType)) {
            predicate = (abPokemonRequirement, o1) -> {
               return abPokemonRequirement.isDataMatch(o1);
            };
         } else if (clazz.isAssignableFrom(this.minecraftType)) {
            predicate = (abPokemonRequirement, o1) -> {
               return abPokemonRequirement.isMinecraftMatch(o1);
            };
         }

         Iterator var4 = this.requirements.iterator();

         Requirement requirement;
         do {
            if (!var4.hasNext()) {
               return false;
            }

            requirement = (Requirement)var4.next();
         } while(predicate.test(requirement, o));

         return false;
      }
   }

   public void apply(Object o) {
      if (o != null) {
         Class clazz = o.getClass();
         BiConsumer consumer = null;
         if (clazz.isAssignableFrom(this.dataType)) {
            consumer = (abPokemonRequirement, o1) -> {
               abPokemonRequirement.applyData(o1);
            };
         } else if (clazz.isAssignableFrom(this.minecraftType)) {
            consumer = (abPokemonRequirement, o1) -> {
               abPokemonRequirement.applyMinecraft(o1);
            };
         }

         Iterator var4 = this.requirements.iterator();

         while(var4.hasNext()) {
            Requirement requirement = (Requirement)var4.next();
            consumer.accept(requirement, o);
         }

      }
   }

   public Optional getValue(Class clazz) {
      Iterator var2 = this.requirements.iterator();

      Requirement requirement;
      do {
         if (!var2.hasNext()) {
            return Optional.empty();
         }

         requirement = (Requirement)var2.next();
      } while(!Objects.equals(requirement.getClass(), clazz));

      return Optional.ofNullable(requirement.getValue());
   }

   public NBTTagCompound write(NBTTagCompound nbt) {
      if (this.originalSpec == null) {
         return nbt;
      } else {
         if (nbt == null) {
            nbt = new NBTTagCompound();
         }

         nbt.func_74778_a("PokemonSpecification", this.originalSpec);
         return nbt;
      }
   }

   public String toString() {
      return this.originalSpec;
   }
}
