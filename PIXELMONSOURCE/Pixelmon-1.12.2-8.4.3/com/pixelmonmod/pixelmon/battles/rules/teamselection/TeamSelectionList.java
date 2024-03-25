package com.pixelmonmod.pixelmon.battles.rules.teamselection;

import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayerMP;

public final class TeamSelectionList {
   private static int idCounter;
   private static final Map teamSelectMap = new HashMap();

   private TeamSelectionList() {
   }

   public static void addTeamSelection(BattleRules rules, boolean showRules, PartyStorage... storages) {
      int newID = idCounter++;
      TeamSelection selection = new TeamSelection(newID, rules, showRules, storages);
      teamSelectMap.put(newID, selection);
      selection.initializeClient();
   }

   public static void removeSelection(EntityPlayerMP player) {
      Iterator var1 = teamSelectMap.values().iterator();

      TeamSelection selection;
      do {
         if (!var1.hasNext()) {
            return;
         }

         selection = (TeamSelection)var1.next();
      } while(!selection.hasPlayer(player));

      teamSelectMap.remove(selection.id);
   }

   static void removeSelection(int id) {
      teamSelectMap.remove(id);
   }

   public static TeamSelection getTeamSelection(int id) {
      return (TeamSelection)teamSelectMap.get(id);
   }
}
