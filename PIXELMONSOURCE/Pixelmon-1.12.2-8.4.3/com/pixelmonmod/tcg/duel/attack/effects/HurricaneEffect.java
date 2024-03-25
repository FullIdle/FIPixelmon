package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.Iterator;
import java.util.List;

public class HurricaneEffect extends BaseAttackEffect {
   private static final String CODE = "HURRICANE";

   public HurricaneEffect() {
      super("HURRICANE");
   }

   public void applyAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server, int finalDamage) {
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      PokemonCardState active = opp.getActiveCard();
      if (active.getStatus().getDamage() < active.getHP()) {
         opp.setActiveCard((PokemonCardState)null);
         opp.getHand().add(active.getData());
         Iterator var8 = active.getAttachments().iterator();

         while(var8.hasNext()) {
            CommonCardState attachment = (CommonCardState)var8.next();
            opp.getHand().add(attachment.getData());
         }
      }

   }
}
