package com.pixelmonmod.tcg.duel.log.parameters;

import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.api.registries.AttackCardRegistry;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DuelLogAttackParameters extends DuelLogParameters {
   private final AttackCard cardAttack;
   private final PokemonCardState attacker;
   private final PokemonCardState attacking;
   private final int damage;
   private final List conditions;

   public DuelLogAttackParameters(AttackCard cardAttack, PokemonCardState attacker, PokemonCardState attacking, int damage, List conditions) {
      this.cardAttack = cardAttack;
      this.attacker = attacker;
      this.attacking = attacking;
      this.damage = damage;
      this.conditions = conditions;
   }

   public DuelLogAttackParameters(ByteBuf buf) {
      this.cardAttack = AttackCardRegistry.get(ByteBufUtils.readUTF8String(buf), buf.readInt());
      this.attacker = new PokemonCardState(ByteBufTCG.readCard(buf), 0);
      this.attacking = new PokemonCardState(ByteBufTCG.readCard(buf), 0);
      this.damage = buf.readInt();
      if (buf.readBoolean()) {
         int count = buf.readInt();
         this.conditions = new ArrayList();

         for(int i = 0; i < count; ++i) {
            CardCondition cardCondition = CardCondition.values()[buf.readInt()];
            Integer modifier = null;
            if (buf.readBoolean()) {
               modifier = buf.readInt();
            }

            this.conditions.add(new ImmutablePair(cardCondition, modifier));
         }
      } else {
         this.conditions = null;
      }

   }

   public void write(ByteBuf buf, GamePhase gamePhase, int itemPlayerIndex, int receiverIndex, boolean isMyTurn) {
      ByteBufUtils.writeUTF8String(buf, this.cardAttack.getCardCode());
      buf.writeInt(this.cardAttack.getAttackNumber());
      ByteBufTCG.writeCard(buf, this.attacker.getData());
      ByteBufTCG.writeCard(buf, this.attacking.getData());
      buf.writeInt(this.damage);
      buf.writeBoolean(this.conditions != null);
      if (this.conditions != null) {
         buf.writeInt(this.conditions.size());
         Iterator var6 = this.conditions.iterator();

         while(var6.hasNext()) {
            Pair pair = (Pair)var6.next();
            buf.writeInt(((CardCondition)pair.getLeft()).ordinal());
            buf.writeBoolean(pair.getRight() != null);
            if (pair.getRight() != null) {
               buf.writeInt((Integer)pair.getRight());
            }
         }
      }

   }

   public AttackCard getAttack() {
      return this.cardAttack;
   }

   public PokemonCardState getAttacker() {
      return this.attacker;
   }

   public PokemonCardState getAttacking() {
      return this.attacking;
   }

   public int getDamage() {
      return this.damage;
   }

   public List getConditions() {
      return this.conditions;
   }
}
