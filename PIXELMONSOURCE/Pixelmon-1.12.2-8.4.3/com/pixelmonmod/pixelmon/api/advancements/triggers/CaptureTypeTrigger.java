package com.pixelmonmod.pixelmon.api.advancements.triggers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumType;
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

public class CaptureTypeTrigger implements ICriterionTrigger {
   private static final ResourceLocation ID = new ResourceLocation("pixelmon:capture_type_trigger");
   private final Map listeners = Maps.newHashMap();

   public ResourceLocation func_192163_a() {
      return ID;
   }

   public void func_192165_a(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners capturetypetrigger$Listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (capturetypetrigger$Listeners == null) {
         capturetypetrigger$Listeners = new Listeners(playerAdvancementsIn);
         this.listeners.put(playerAdvancementsIn, capturetypetrigger$Listeners);
      }

      capturetypetrigger$Listeners.add(listener);
   }

   public void func_192164_b(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener listener) {
      Listeners capturetypetrigger$Listeners = (Listeners)this.listeners.get(playerAdvancementsIn);
      if (capturetypetrigger$Listeners != null) {
         capturetypetrigger$Listeners.remove(listener);
         if (capturetypetrigger$Listeners.isEmpty()) {
            this.listeners.remove(playerAdvancementsIn);
         }
      }

   }

   public void func_192167_a(PlayerAdvancements playerAdvancementsIn) {
      this.listeners.remove(playerAdvancementsIn);
   }

   public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
      int count = json.has("count") ? JsonUtils.func_151203_m(json, "count") : 0;
      EnumType type;
      if (json.has("type")) {
         String value = JsonUtils.func_151200_h(json, "type");
         type = EnumType.parseType(value);
      } else {
         type = EnumType.Mystery;
      }

      return new Instance(count, type);
   }

   public void trigger(EntityPlayerMP player, Pokemon pokemon, Map caughtTypeCount) {
      Listeners capturetypetrigger$Listeners = (Listeners)this.listeners.get(player.func_192039_O());
      if (capturetypetrigger$Listeners != null) {
         capturetypetrigger$Listeners.trigger(pokemon, caughtTypeCount);
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

      public void trigger(Pokemon pokemon, Map caughtTypeCount) {
         List list = null;

         Iterator var4;
         ICriterionTrigger.Listener listener;
         for(var4 = this.listeners.iterator(); var4.hasNext(); list.add(listener)) {
            listener = (ICriterionTrigger.Listener)var4.next();
            if (list == null) {
               list = Lists.newArrayList();
            }
         }

         if (list != null) {
            var4 = list.iterator();

            while(var4.hasNext()) {
               listener = (ICriterionTrigger.Listener)var4.next();
               if (((Instance)listener.func_192158_a()).test(pokemon, caughtTypeCount)) {
                  listener.func_192159_a(this.playerAdvancements);
               }
            }
         }

      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final int count;
      private final EnumType type;

      public Instance(int count, EnumType type) {
         super(CaptureTypeTrigger.ID);
         this.count = count;
         this.type = type;
      }

      public boolean test(Pokemon pokemon, Map caughtTypeCount) {
         if (this.type == EnumType.Mystery) {
            return true;
         } else {
            int count = (Integer)caughtTypeCount.get(this.type.func_176610_l());
            if (pokemon.getBaseStats().types.contains(this.type)) {
               return this.count <= count;
            } else {
               return false;
            }
         }
      }
   }
}
