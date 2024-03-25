package com.pixelmonmod.pixelmon.api.advancements.triggers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class StarterTrigger implements ICriterionTrigger {
   private static final ResourceLocation ID = new ResourceLocation("pixelmon:starter_trigger");
   private final Map listeners = Maps.newHashMap();

   public ResourceLocation func_192163_a() {
      return ID;
   }

   public void func_192165_a(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners startertrigger$listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (startertrigger$listeners == null) {
         startertrigger$listeners = new Listeners(playerAdvancementsIn);
         this.listeners.put(playerAdvancementsIn, startertrigger$listeners);
      }

      startertrigger$listeners.add(listener);
   }

   public void func_192164_b(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners startertrigger$listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (startertrigger$listeners != null) {
         startertrigger$listeners.remove(listener);
         if (startertrigger$listeners.isEmpty()) {
            this.listeners.remove(playerAdvancementsIn);
         }
      }

   }

   public void func_192167_a(PlayerAdvancements playerAdvancementsIn) {
      this.listeners.remove(playerAdvancementsIn);
   }

   public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
      return new Instance();
   }

   public void trigger(EntityPlayerMP player) {
      Listeners startertrigger$listeners = (Listeners)this.listeners.get(player.func_192039_O());
      if (startertrigger$listeners != null) {
         startertrigger$listeners.trigger(player);
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
      public Instance() {
         super(StarterTrigger.ID);
      }

      public boolean test(EntityPlayerMP player) {
         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
         return storage.starterPicked;
      }
   }
}
