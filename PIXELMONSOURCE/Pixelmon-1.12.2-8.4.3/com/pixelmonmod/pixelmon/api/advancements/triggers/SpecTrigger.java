package com.pixelmonmod.pixelmon.api.advancements.triggers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class SpecTrigger implements ICriterionTrigger {
   private static final ResourceLocation ID = new ResourceLocation("pixelmon:spec_trigger");
   private final Map listeners = Maps.newHashMap();

   public ResourceLocation func_192163_a() {
      return ID;
   }

   public void func_192165_a(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners spectrigger$Listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (spectrigger$Listeners == null) {
         spectrigger$Listeners = new Listeners(playerAdvancementsIn);
         this.listeners.put(playerAdvancementsIn, spectrigger$Listeners);
      }

      spectrigger$Listeners.add(listener);
   }

   public void func_192164_b(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners spectrigger$Listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (spectrigger$Listeners != null) {
         spectrigger$Listeners.remove(listener);
         if (spectrigger$Listeners.isEmpty()) {
            this.listeners.remove(playerAdvancementsIn);
         }
      }

   }

   public void func_192167_a(PlayerAdvancements playerAdvancementsIn) {
      this.listeners.remove(playerAdvancementsIn);
   }

   public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
      String specJson = json.has("spec") ? JsonUtils.func_151200_h(json, "spec") : "";
      PokemonSpec spec = PokemonSpec.from(specJson.split(" "));
      return new Instance(this.func_192163_a(), spec, specJson);
   }

   public void trigger(EntityPlayerMP player, Pokemon pokemon) {
      Listeners spectrigger$Listeners = (Listeners)this.listeners.get(player.func_192039_O());
      if (spectrigger$Listeners != null) {
         spectrigger$Listeners.trigger(pokemon);
      }

   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements playerAdvancementsIn) {
         this.playerAdvancements = playerAdvancementsIn;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener listener) {
         this.listeners.add(listener);
      }

      public void remove(ICriterionTrigger.Listener listener) {
         this.listeners.remove(listener);
      }

      public void trigger(Pokemon pokemon) {
         List list = null;

         Iterator var3;
         ICriterionTrigger.Listener listener;
         for(var3 = this.listeners.iterator(); var3.hasNext(); list.add(listener)) {
            listener = (ICriterionTrigger.Listener)var3.next();
            if (list == null) {
               list = Lists.newArrayList();
            }
         }

         if (list != null) {
            var3 = list.iterator();

            while(var3.hasNext()) {
               listener = (ICriterionTrigger.Listener)var3.next();
               if (((Instance)listener.func_192158_a()).test(pokemon)) {
                  listener.func_192159_a(this.playerAdvancements);
               }
            }
         }

      }
   }

   public static class Instance extends AbstractCriterionInstance {
      PokemonSpec spec;
      String name;

      public Instance(ResourceLocation criterionIn, PokemonSpec spec, String name) {
         super(criterionIn);
         this.spec = spec;
         this.name = name;
      }

      public boolean test(Pokemon pokemon) {
         return pokemon != null ? this.spec.matches(pokemon) : false;
      }
   }
}
