package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import io.netty.buffer.ByteBuf;

public class DuelLogAttachCardParameters extends DuelLogParameters {
   private final PokemonCardState pokemon;
   private final CommonCardState attachment;

   public DuelLogAttachCardParameters(PokemonCardState pokemon, CommonCardState attachment) {
      this.pokemon = pokemon;
      this.attachment = attachment;
   }

   public DuelLogAttachCardParameters(ByteBuf buf) {
      this.pokemon = new PokemonCardState(ByteBufTCG.readCard(buf), 0);
      this.attachment = new CommonCardState(ByteBufTCG.readCard(buf));
   }

   public void write(ByteBuf buf, GamePhase gamePhase, int itemPlayerIndex, int receiverIndex, boolean isMyTurn) {
      ByteBufTCG.writeCard(buf, this.pokemon.getData());
      ByteBufTCG.writeCard(buf, this.attachment.getData());
   }

   public PokemonCardState getPokemon() {
      return this.pokemon;
   }

   public CommonCardState getAttachment() {
      return this.attachment;
   }
}
