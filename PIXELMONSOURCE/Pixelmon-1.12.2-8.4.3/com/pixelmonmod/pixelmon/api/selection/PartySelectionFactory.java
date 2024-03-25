package com.pixelmonmod.pixelmon.api.selection;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PartySelectionFactory {
   private static final Map OPEN_SELECTIONS = Maps.newConcurrentMap();

   public static void beginSelection(EntityPlayerMP player, BiConsumer consumer, List options) {
      OPEN_SELECTIONS.put(player.func_110124_au(), new SelectionData(player.func_110124_au(), consumer, options));
   }

   public static SelectionData getSelectionConsumer(EntityPlayerMP player) {
      return (SelectionData)OPEN_SELECTIONS.get(player.func_110124_au());
   }

   public static void removeSelection(EntityPlayerMP player) {
      OPEN_SELECTIONS.remove(player.func_110124_au());
   }

   public static boolean inSelection(EntityPlayerMP player) {
      return OPEN_SELECTIONS.containsKey(player.func_110124_au());
   }

   public static class SelectionData {
      private final UUID player;
      private final BiConsumer consumer;
      private final List pokemon;

      public SelectionData(UUID player, BiConsumer consumer, List pokemon) {
         this.player = player;
         this.consumer = consumer;
         this.pokemon = pokemon;
      }

      public UUID getPlayer() {
         return this.player;
      }

      public BiConsumer getConsumer() {
         return this.consumer;
      }

      public List getPokemon() {
         return this.pokemon;
      }
   }
}
