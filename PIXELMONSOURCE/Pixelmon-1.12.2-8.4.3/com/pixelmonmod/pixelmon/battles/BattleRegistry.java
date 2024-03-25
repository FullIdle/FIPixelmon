package com.pixelmonmod.pixelmon.battles;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.Spectator;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class BattleRegistry {
   private static int battleIndex = 0;
   private static final Map battleIDMap = new HashMap();
   private static final Map battleNameMap = new HashMap();
   private static final List battleList = Collections.synchronizedList(new ArrayList());

   public static BattleControllerBase startBattle(BattleParticipant[] team1, BattleParticipant[] team2, BattleRules rules) {
      BattleControllerBase battle = new BattleControllerBase(team1, team2, rules);
      Iterator var4 = battle.participants.iterator();

      BattleParticipant bp2;
      while(var4.hasNext()) {
         bp2 = (BattleParticipant)var4.next();
         if (bp2 instanceof PlayerParticipant && getBattle(((PlayerParticipant)bp2).player) != null) {
            return null;
         }

         PartyStorage party = bp2.getStorage();
         if (party != null && party.findOne((pokemon) -> {
            return pokemon.getPixelmonIfExists() != null && pokemon.getPixelmonIfExists().isEvolving();
         }) != null) {
            return null;
         }
      }

      if (battle.participants.size() == 2) {
         BattleParticipant bp1 = (BattleParticipant)battle.participants.get(0);
         bp2 = (BattleParticipant)battle.participants.get(1);
         if (bp1 instanceof PlayerParticipant && bp2 instanceof TrainerParticipant || bp2 instanceof PlayerParticipant && bp1 instanceof TrainerParticipant) {
            NPCEvent.StartBattle npcStartBattleEvent = new NPCEvent.StartBattle(battle.participants);
            Pixelmon.EVENT_BUS.post(npcStartBattleEvent);
            if (npcStartBattleEvent.isCanceled()) {
               return null;
            }
         }
      }

      BattleStartedEvent battleStartedEvent = new BattleStartedEvent(battle, team1, team2);
      Pixelmon.EVENT_BUS.post(battleStartedEvent);
      if (battleStartedEvent.isCanceled()) {
         return null;
      } else {
         registerBattle(battle);
         Iterator var14 = battle.getPlayers().iterator();

         while(var14.hasNext()) {
            BattleParticipant p = (BattleParticipant)var14.next();
            EntityPlayerMP player = (EntityPlayerMP)p.getEntity();
            Iterator var8 = battle.participants.iterator();

            while(var8.hasNext()) {
               BattleParticipant p2 = (BattleParticipant)var8.next();
               if (p2 != p) {
                  PixelmonWrapper pix = p2.allPokemon[0];
                  PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.func_110124_au());
                  if (!Pixelmon.EVENT_BUS.post(new PokedexEvent(player.func_110124_au(), pix.pokemon, EnumPokedexRegisterStatus.seen, "pokedexKey"))) {
                     storage.pokedex.set(pix.pokemon, EnumPokedexRegisterStatus.seen);
                     storage.pokedex.update();
                     if (player != null) {
                        storage.pokedex.update();
                     }
                  }
               }
            }
         }

         return battle;
      }
   }

   public static BattleControllerBase startBattle(BattleParticipant[] team1, BattleParticipant[] team2, EnumBattleType type) {
      return startBattle(team1, team2, new BattleRules(type));
   }

   public static BattleControllerBase startBattle(BattleParticipant team1, BattleParticipant team2) {
      return startBattle(new BattleParticipant[]{team1}, new BattleParticipant[]{team2}, new BattleRules(EnumBattleType.Single));
   }

   public static void registerBattle(BattleControllerBase bc) {
      bc.battleIndex = battleIndex++;
      battleIDMap.put(bc.battleIndex, bc);
      synchronized(battleList) {
         battleList.add(bc);
      }

      Iterator var1 = bc.getPlayers().iterator();

      while(var1.hasNext()) {
         PlayerParticipant p = (PlayerParticipant)var1.next();
         battleNameMap.put(p.player.func_110124_au(), bc);
      }

   }

   public static BattleControllerBase getBattle(int index) {
      return (BattleControllerBase)battleIDMap.get(index);
   }

   public static BattleControllerBase getBattle(EntityPlayer player) {
      return player == null ? null : (BattleControllerBase)battleNameMap.get(player.func_110124_au());
   }

   public static BattleControllerBase getBattleExcludeSpectate(EntityPlayer player) {
      BattleControllerBase bc = getBattle(player);
      if (bc == null) {
         return null;
      } else {
         return bc.hasSpectator(player) ? null : bc;
      }
   }

   public static BattleControllerBase getSpectatedBattle(EntityPlayer player) {
      return (BattleControllerBase)battleNameMap.get(player.func_110124_au());
   }

   public static void registerSpectator(Spectator spectator, BattleControllerBase bc) {
      battleNameMap.put(spectator.getEntity().func_110124_au(), bc);
   }

   public static void unregisterSpectator(Spectator spectator) {
      battleNameMap.remove(spectator.getEntity().func_110124_au());
   }

   public static boolean removeSpectator(EntityPlayerMP player) {
      BattleControllerBase battle = (BattleControllerBase)battleNameMap.get(player.func_110124_au());
      return battle != null ? battle.removeSpectator(player) : false;
   }

   public static void deRegisterBattle(BattleControllerBase bc) {
      if (bc != null) {
         battleIDMap.remove(bc.battleIndex);
         synchronized(battleList) {
            battleList.remove(bc);
         }

         Iterator var1 = bc.getPlayers().iterator();

         while(var1.hasNext()) {
            PlayerParticipant p = (PlayerParticipant)var1.next();
            battleNameMap.remove(p.player.func_110124_au());
         }

         var1 = bc.spectators.iterator();

         while(var1.hasNext()) {
            Spectator s = (Spectator)var1.next();
            battleNameMap.remove(s.getEntity().func_110124_au());
         }
      }

   }

   public static void updateBattles() {
      ArrayList list = new ArrayList(battleList);
      Iterator var1 = list.iterator();

      while(var1.hasNext()) {
         BattleControllerBase base = (BattleControllerBase)var1.next();
         base.update();
      }

   }
}
