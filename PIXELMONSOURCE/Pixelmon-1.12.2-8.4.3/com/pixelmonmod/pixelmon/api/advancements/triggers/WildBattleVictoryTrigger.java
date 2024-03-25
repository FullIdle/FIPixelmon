package com.pixelmonmod.pixelmon.api.advancements.triggers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
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

public class WildBattleVictoryTrigger implements ICriterionTrigger {
   private static final ResourceLocation ID = new ResourceLocation("pixelmon:wild_battle_victory_trigger");
   private final Map listeners = Maps.newHashMap();

   public ResourceLocation func_192163_a() {
      return ID;
   }

   public void func_192165_a(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners wildbattlevictorytrigger$Listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (wildbattlevictorytrigger$Listeners == null) {
         wildbattlevictorytrigger$Listeners = new Listeners(playerAdvancementsIn);
         this.listeners.put(playerAdvancementsIn, wildbattlevictorytrigger$Listeners);
      }

      wildbattlevictorytrigger$Listeners.add(listener);
   }

   public void func_192164_b(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners BallCaptureTrigger$listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (BallCaptureTrigger$listeners != null) {
         BallCaptureTrigger$listeners.remove(listener);
         if (BallCaptureTrigger$listeners.isEmpty()) {
            this.listeners.remove(playerAdvancementsIn);
         }
      }

   }

   public void func_192167_a(PlayerAdvancements playerAdvancementsIn) {
      this.listeners.remove(playerAdvancementsIn);
   }

   public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
      String victoryString = json.has("wild_kills") ? JsonUtils.func_151200_h(json, "wild_kills") : "";
      int victories = Integer.parseInt(victoryString);
      return new Instance(this.func_192163_a(), victories);
   }

   public void trigger(EntityPlayerMP player, int victories) {
      Listeners wildbattlevictorytrigger$Listeners = (Listeners)this.listeners.get(player.func_192039_O());
      if (wildbattlevictorytrigger$Listeners != null) {
         wildbattlevictorytrigger$Listeners.trigger(victories);
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

      public void trigger(int victories) {
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
               if (((Instance)listener.func_192158_a()).test(victories)) {
                  listener.func_192159_a(this.playerAdvancements);
               }
            }
         }

      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final int victories;

      public Instance(ResourceLocation criterionIn, int victories) {
         super(criterionIn);
         this.victories = victories;
      }

      public boolean test(int victories) {
         return this.victories <= victories;
      }
   }
}
