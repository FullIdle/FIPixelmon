package com.pixelmonmod.pixelmon.advancements;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.advancements.triggers.BallCaptureTrigger;
import com.pixelmonmod.pixelmon.api.advancements.triggers.CaptureTypeTrigger;
import com.pixelmonmod.pixelmon.api.advancements.triggers.LegendaryCaptureTrigger;
import com.pixelmonmod.pixelmon.api.advancements.triggers.PokedexTrigger;
import com.pixelmonmod.pixelmon.api.advancements.triggers.SpecTrigger;
import com.pixelmonmod.pixelmon.api.advancements.triggers.StarterTrigger;
import com.pixelmonmod.pixelmon.api.advancements.triggers.WildBattleVictoryTrigger;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStats;
import java.util.Map;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;

public class PixelmonAdvancements {
   public static final CaptureTypeTrigger CAPTURE_TYPE_TRIGGER = (CaptureTypeTrigger)CriteriaTriggers.func_192118_a(new CaptureTypeTrigger());
   public static final PokedexTrigger POKEDEX_TRIGGER = (PokedexTrigger)CriteriaTriggers.func_192118_a(new PokedexTrigger());
   public static final StarterTrigger STARTER_TRIGGER = (StarterTrigger)CriteriaTriggers.func_192118_a(new StarterTrigger());
   public static final BallCaptureTrigger BALL_CAPTURE_TRIGGER = (BallCaptureTrigger)CriteriaTriggers.func_192118_a(new BallCaptureTrigger());
   public static final LegendaryCaptureTrigger LEGENDARY_CAPTURE_TRIGGER = (LegendaryCaptureTrigger)CriteriaTriggers.func_192118_a(new LegendaryCaptureTrigger());
   public static final SpecTrigger SPEC_TRIGGER = (SpecTrigger)CriteriaTriggers.func_192118_a(new SpecTrigger());
   public static final WildBattleVictoryTrigger WILD_BATTLE_VICTORY_TRIGGER = (WildBattleVictoryTrigger)CriteriaTriggers.func_192118_a(new WildBattleVictoryTrigger());

   public PixelmonAdvancements() {
      Pixelmon.LOGGER.info("Loading Advancements");
   }

   public static void throwCaptureTriggers(EntityPlayerMP player, EnumPokeballs ball, Pokemon pokemon) {
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
      PlayerStats stats = party.stats;
      Map caughtTypeCount = stats.getCaughtTypeCount();
      CAPTURE_TYPE_TRIGGER.trigger(player, pokemon, caughtTypeCount);
      if (!party.pokedex.hasCaught(pokemon.getSpecies().getNationalPokedexInteger())) {
         POKEDEX_TRIGGER.trigger(player);
      }

      BALL_CAPTURE_TRIGGER.trigger(player, ball);
      LEGENDARY_CAPTURE_TRIGGER.trigger(player, pokemon.getSpecies());
      SPEC_TRIGGER.trigger(player, pokemon);
   }

   public static void throwBattleTriggers(EntityPlayerMP player) {
      PlayerStats stats = Pixelmon.storageManager.getParty(player).stats;
      WILD_BATTLE_VICTORY_TRIGGER.trigger(player, stats.getCurrentKills());
   }
}
