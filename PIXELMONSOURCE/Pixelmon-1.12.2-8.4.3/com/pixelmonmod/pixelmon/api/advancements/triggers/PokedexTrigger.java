package com.pixelmonmod.pixelmon.api.advancements.triggers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
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

public class PokedexTrigger implements ICriterionTrigger {
   private static final ResourceLocation ID = new ResourceLocation("pixelmon:pokedex_trigger");
   private final Map listeners = Maps.newHashMap();

   public ResourceLocation func_192163_a() {
      return ID;
   }

   public void func_192165_a(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners pokedextrigger$listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (pokedextrigger$listeners == null) {
         pokedextrigger$listeners = new Listeners(playerAdvancementsIn);
         this.listeners.put(playerAdvancementsIn, pokedextrigger$listeners);
      }

      pokedextrigger$listeners.add(listener);
   }

   public void func_192164_b(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners pokedextrigger$listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (pokedextrigger$listeners != null) {
         pokedextrigger$listeners.remove(listener);
         if (pokedextrigger$listeners.isEmpty()) {
            this.listeners.remove(playerAdvancementsIn);
         }
      }

   }

   public void func_192167_a(PlayerAdvancements playerAdvancementsIn) {
      this.listeners.remove(playerAdvancementsIn);
   }

   public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
      float percent = json.has("percent") ? JsonUtils.func_151217_k(json, "percent") : 0.0F;
      return new Instance(percent);
   }

   public void trigger(EntityPlayerMP player) {
      Listeners pokedextrigger$listeners = (Listeners)this.listeners.get(player.func_192039_O());
      if (pokedextrigger$listeners != null) {
         pokedextrigger$listeners.trigger(player);
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

      public void trigger(EntityPlayerMP player) {
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
               if (((Instance)listener.func_192158_a()).test(player)) {
                  listener.func_192159_a(this.playerAdvancements);
               }
            }
         }

      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final float percent;

      public Instance(float percent) {
         super(PokedexTrigger.ID);
         this.percent = percent;
      }

      public boolean test(EntityPlayerMP player) {
         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
         float caught = storage.pokedex.countCaught() > 0 ? (float)(storage.pokedex.countCaught() + 1) : 0.0F;
         float totalDex = (float)EnumSpecies.values().length;
         return caught / totalDex * 100.0F >= this.percent;
      }
   }
}
