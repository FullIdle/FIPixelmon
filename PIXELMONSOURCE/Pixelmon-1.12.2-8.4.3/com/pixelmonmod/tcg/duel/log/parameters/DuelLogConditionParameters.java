package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import io.netty.buffer.ByteBuf;

public class DuelLogConditionParameters extends DuelLogParameters {
   private final CardCondition cardCondition;
   private final PokemonCardState affected;
   private final int damage;
   private final boolean isHealed;

   public DuelLogConditionParameters(CardCondition cardCondition, PokemonCardState affected, int damage, boolean isHealed) {
      this.cardCondition = cardCondition;
      this.affected = affected;
      this.damage = damage;
      this.isHealed = isHealed;
   }

   public DuelLogConditionParameters(ByteBuf buf) {
      this.cardCondition = CardCondition.values()[buf.readInt()];
      this.affected = new PokemonCardState(ByteBufTCG.readCard(buf), 0);
      this.damage = buf.readInt();
      this.isHealed = buf.readBoolean();
   }

   public void write(ByteBuf buf, GamePhase gamePhase, int itemPlayerIndex, int receiverIndex, boolean isMyTurn) {
      buf.writeInt(this.cardCondition.ordinal());
      ByteBufTCG.writeCard(buf, this.affected.getData());
      buf.writeInt(this.damage);
      buf.writeBoolean(this.isHealed);
   }
}
