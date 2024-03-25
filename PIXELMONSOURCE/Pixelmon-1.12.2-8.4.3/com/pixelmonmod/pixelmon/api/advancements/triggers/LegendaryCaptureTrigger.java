package com.pixelmonmod.pixelmon.api.advancements.triggers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
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

public class LegendaryCaptureTrigger implements ICriterionTrigger {
   private static final ResourceLocation ID = new ResourceLocation("pixelmon:legendary_capture_trigger");
   private final Map listeners = Maps.newHashMap();

   public ResourceLocation func_192163_a() {
      return ID;
   }

   public void func_192165_a(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners legendarycapturetrigger$Listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (legendarycapturetrigger$Listeners == null) {
         legendarycapturetrigger$Listeners = new Listeners(playerAdvancementsIn);
         this.listeners.put(playerAdvancementsIn, legendarycapturetrigger$Listeners);
      }

      legendarycapturetrigger$Listeners.add(listener);
   }

   public void func_192164_b(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners legendarycapturetrigger$Listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (legendarycapturetrigger$Listeners != null) {
         legendarycapturetrigger$Listeners.remove(listener);
         if (legendarycapturetrigger$Listeners.isEmpty()) {
            this.listeners.remove(playerAdvancementsIn);
         }
      }

   }

   public void func_192167_a(PlayerAdvancements playerAdvancementsIn) {
      this.listeners.remove(playerAdvancementsIn);
   }

   public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
      String pokemon = json.has("pokemon") ? JsonUtils.func_151200_h(json, "pokemon") : "";
      if (EnumSpecies.getFromNameAnyCase(pokemon) == null && !pokemon.equalsIgnoreCase("legendary")) {
         Pixelmon.LOGGER.error("Invalid Pokémon name for " + this.func_192163_a().toString());
      }

      EnumSpecies pixelmon = EnumSpecies.getFromNameAnyCase(pokemon);
      return new Instance(this.func_192163_a(), pixelmon, pokemon);
   }

   public void trigger(EntityPlayerMP player, EnumSpecies pokemon) {
      Listeners legendarycapturetrigger$Listeners = (Listeners)this.listeners.get(player.func_192039_O());
      if (legendarycapturetrigger$Listeners != null) {
         legendarycapturetrigger$Listeners.trigger(pokemon);
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

      public void trigger(EnumSpecies pokemon) {
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
      EnumSpecies pokemon;
      String name;

      public Instance(ResourceLocation criterionIn, EnumSpecies pokemon, String name) {
         super(criterionIn);
         this.pokemon = pokemon;
         this.name = name;
      }

      public boolean test(EnumSpecies pokemon) {
         if (this.pokemon == null && this.name.equalsIgnoreCase("legendary") && pokemon.isLegendary()) {
            return true;
         } else {
            return this.pokemon == pokemon;
         }
      }
   }
}
