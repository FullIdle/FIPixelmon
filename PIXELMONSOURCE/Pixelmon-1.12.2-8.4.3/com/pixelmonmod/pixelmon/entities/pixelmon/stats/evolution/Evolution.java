package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.EvoCondition;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.HeldItemCondition;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.InteractEvolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.LevelingEvolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.TickingEvolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.TradeEvolution;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public abstract class Evolution {
   public static HashMap evolutionTypes = new HashMap();
   public static final Gson EVOLUTION_GSON;
   public PokemonSpec to;
   public ArrayList conditions;
   public ArrayList moves;
   public String evoType;

   public Evolution(String evoType) {
      this.conditions = new ArrayList();
      this.moves = new ArrayList();
      this.evoType = evoType;
   }

   public Evolution(String evoType, PokemonSpec to, EvoCondition... conditions) {
      this(evoType);
      this.to = to;
      this.conditions.addAll(Arrays.asList(conditions));
   }

   protected boolean canEvolve(EntityPixelmon pokemon) {
      if (pokemon.func_110143_aJ() < 1.0F) {
         return false;
      } else {
         if (this.conditions != null) {
            Iterator var2 = this.conditions.iterator();

            while(var2.hasNext()) {
               EvoCondition condition = (EvoCondition)var2.next();
               if (!condition.passes(pokemon)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean doEvolution(EntityPixelmon pixelmon) {
      if (!Pixelmon.EVENT_BUS.post(new EvolveEvent.PreEvolve((EntityPlayerMP)pixelmon.func_70902_q(), pixelmon, this))) {
         EnumSpecies species = EnumSpecies.getFromNameAnyCaseNoTranslate(this.to.name);
         if (species != null) {
            IEnumForm form = pixelmon.getFormEnum();
            if (form instanceof EnumSpecial && species.getPossibleForms(false).contains(form)) {
               pixelmon.startEvolution(this, form.getForm());
               return true;
            }
         }

         pixelmon.startEvolution(this, this.to != null && this.to.form != null ? this.to.form : -1);
         return true;
      } else {
         return false;
      }
   }

   public void finishedEvolving(EntityPixelmon pokemon) {
      if (this.consumesHeldItem()) {
         pokemon.getPokemonData().setHeldItem(ItemStack.field_190927_a);
         pokemon.update(new EnumUpdateType[]{EnumUpdateType.HeldItem});
      }

   }

   public boolean consumesHeldItem() {
      if (this.conditions == null) {
         return false;
      } else {
         Iterator var1 = this.conditions.iterator();

         EvoCondition condition;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            condition = (EvoCondition)var1.next();
         } while(!(condition instanceof HeldItemCondition));

         return true;
      }
   }

   public List getConditionsOfType(Class clazz) {
      if (this.conditions == null) {
         return Collections.emptyList();
      } else {
         List list = new ArrayList();
         Iterator var3 = this.conditions.iterator();

         while(var3.hasNext()) {
            EvoCondition condition = (EvoCondition)var3.next();
            if (clazz.isInstance(condition)) {
               list.add(clazz.cast(condition));
            }
         }

         return list;
      }
   }

   static {
      EVOLUTION_GSON = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(Evolution.class, new EvolutionTypeAdapter()).registerTypeAdapter(EvoCondition.class, new EvoConditionTypeAdapter()).registerTypeAdapter(PokemonSpec.class, PokemonSpec.SPEC_ADAPTER).create();
      evolutionTypes.put("leveling", LevelingEvolution.class);
      evolutionTypes.put("trade", TradeEvolution.class);
      evolutionTypes.put("interact", InteractEvolution.class);
      evolutionTypes.put("ticking", TickingEvolution.class);
   }
}
