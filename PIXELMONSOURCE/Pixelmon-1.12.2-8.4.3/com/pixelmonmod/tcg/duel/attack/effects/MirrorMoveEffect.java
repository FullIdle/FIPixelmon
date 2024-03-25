package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.duel.log.DuelLogItem;
import com.pixelmonmod.tcg.duel.log.DuelLogType;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogAttackParameters;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

public class MirrorMoveEffect extends BaseAttackEffect {
   private static final String CODE = "MIRRORMOVE";

   public MirrorMoveEffect() {
      super("MIRRORMOVE");
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      List logs = (List)server.getLog().getItems().stream().filter((item) -> {
         return item.getTurn() == server.getTurnCount() - 1 && item.getType() == DuelLogType.ATTACK;
      }).collect(Collectors.toList());
      if (!logs.isEmpty()) {
         DuelLogAttackParameters params = ((DuelLogItem)logs.get(0)).getAttackParameters();
         if (params.getAttacking() == card) {
            PlayerServerState opp = server.getPlayer(server.getNextTurn());
            opp.getActiveCard().addDamage(card, params.getDamage(), server);
            Iterator var8 = params.getConditions().iterator();

            while(var8.hasNext()) {
               Pair pair = (Pair)var8.next();
               opp.getActiveCard().addCondition(card, (CardCondition)pair.getLeft(), (Integer)pair.getRight(), server);
            }
         }
      }

   }
}
