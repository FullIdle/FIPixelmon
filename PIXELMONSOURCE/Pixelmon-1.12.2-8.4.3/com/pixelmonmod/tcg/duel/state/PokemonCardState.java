package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.api.card.CardModifier;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import com.pixelmonmod.tcg.api.registries.AbilityCardRegistry;
import com.pixelmonmod.tcg.duel.attack.effects.BaseAttackEffect;
import com.pixelmonmod.tcg.duel.trainer.BaseTrainerEffect;
import com.pixelmonmod.tcg.duel.trainer.ClefairyDoll;
import com.pixelmonmod.tcg.duel.trainer.MysteriousFossil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PokemonCardState extends CommonCardState {
   private List attachments = new ArrayList();
   private int previousPokemonId;
   private PokemonCardStatus status;
   private PokemonAttackStatus[] attacksStatus;
   private Energy weakness;
   private int weaknessValue;
   private CardModifier weaknessCardModifier;
   private Energy resistance;
   private int resistanceValue;
   private CardModifier resistanceCardModifier;
   private boolean canRetreat;
   private int retreatCost;
   private AbilityCard cardAbility;
   private AbilityCard hiddenCardAbility;
   private boolean abilitiesDisabled;
   protected int turn = -1;

   public PokemonCardState(BaseTrainerEffect effect, TrainerCardState trainer, int turn) {
      super(trainer.getData());
      this.turn = turn;
      if (effect instanceof ClefairyDoll || effect instanceof MysteriousFossil) {
         this.previousPokemonId = trainer.getData().getPrevEvoID();
         this.attacksStatus = new PokemonAttackStatus[0];
         this.status = new PokemonCardStatus();
         this.weakness = null;
         this.resistance = null;
         this.retreatCost = 0;
         this.canRetreat = false;
      }

   }

   public PokemonCardState(ImmutableCard card, int turn) {
      super(card);
      this.turn = turn;
      this.transform(card, (PokemonCardStatus)null);
   }

   public void transform(ImmutableCard card, PokemonCardStatus stats) {
      super.transform(card, this.status);
      this.previousPokemonId = card.getPrevEvoID();
      if (stats != null) {
         this.status = stats;
      } else {
         this.status = new PokemonCardStatus();
      }

      this.attacksStatus = new PokemonAttackStatus[card.getAttacks().length];

      for(int i = 0; i < this.attacksStatus.length; ++i) {
         this.attacksStatus[i] = new PokemonAttackStatus(card.getAttacks()[i]);
      }

      this.cardAbility = card.getAbility();
      this.weakness = card.getEnergyWeakness();
      this.weaknessCardModifier = card.getWeaknessModifier();
      this.weaknessValue = card.getWeaknessValue();
      this.resistance = card.getEnergyResistance();
      this.resistanceCardModifier = card.getResistanceModifier();
      this.resistanceValue = card.getResistanceValue();
      this.retreatCost = card.getRetreatCost();
      this.canRetreat = true;
   }

   public PokemonCardState(ByteBuf buf) {
      super(buf);
      int attachmentSize = buf.readInt();

      int i;
      for(i = 0; i < attachmentSize; ++i) {
         this.attachments.add(new CommonCardState(buf));
      }

      this.previousPokemonId = buf.readInt();
      this.status = new PokemonCardStatus(buf);
      if (this.data.getID() != ImmutableCard.FACE_DOWN_ID) {
         this.attacksStatus = new PokemonAttackStatus[buf.readInt()];

         for(i = 0; i < this.attacksStatus.length; ++i) {
            this.attacksStatus[i] = new PokemonAttackStatus(buf);
         }

         String abilityID = ByteBufUtils.readUTF8String(buf);
         if (!abilityID.equals("")) {
            this.cardAbility = AbilityCardRegistry.get(abilityID);
         }

         String hiddenAbilityID = ByteBufUtils.readUTF8String(buf);
         if (!hiddenAbilityID.equals("")) {
            this.hiddenCardAbility = AbilityCardRegistry.get(hiddenAbilityID);
         }

         int weaknessIndex = buf.readInt();
         if (weaknessIndex != -1) {
            this.weakness = Energy.values()[weaknessIndex];
            this.weaknessCardModifier = CardModifier.values()[buf.readInt()];
            this.weaknessValue = buf.readInt();
         }

         int resistanceIndex = buf.readInt();
         if (resistanceIndex != -1) {
            this.resistance = Energy.values()[resistanceIndex];
            this.resistanceCardModifier = CardModifier.values()[buf.readInt()];
            this.resistanceValue = buf.readInt();
         }

         this.retreatCost = buf.readInt();
      }

      this.abilitiesDisabled = buf.readBoolean();
      this.canRetreat = buf.readBoolean();
      this.turn = buf.readInt();
   }

   protected void write(ByteBuf buf, boolean faceUp, boolean writeParams) {
      super.write(buf, faceUp, writeParams);
      buf.writeInt(this.attachments.size());
      Iterator var4 = this.attachments.iterator();

      while(var4.hasNext()) {
         CommonCardState card = (CommonCardState)var4.next();
         if (card instanceof PokemonCardState) {
            ((PokemonCardState)card).writeAsCommonCardState(buf, faceUp, writeParams);
         } else {
            card.write(buf, faceUp, writeParams);
         }
      }

      buf.writeInt(this.previousPokemonId);
      this.status.write(buf);
      if (faceUp) {
         buf.writeInt(this.attacksStatus.length);
         PokemonAttackStatus[] var8 = this.attacksStatus;
         int var9 = var8.length;

         for(int var6 = 0; var6 < var9; ++var6) {
            PokemonAttackStatus attackStatus = var8[var6];
            attackStatus.write(buf);
         }

         ByteBufUtils.writeUTF8String(buf, this.cardAbility == null ? "" : this.cardAbility.getID());
         ByteBufUtils.writeUTF8String(buf, this.hiddenCardAbility == null ? "" : this.hiddenCardAbility.getID());
         if (this.weakness == null) {
            buf.writeInt(-1);
         } else {
            buf.writeInt(this.weakness.ordinal());
            buf.writeInt(this.weaknessCardModifier.ordinal());
            buf.writeInt(this.weaknessValue);
         }

         if (this.resistance == null) {
            buf.writeInt(-1);
         } else {
            buf.writeInt(this.resistance.ordinal());
            buf.writeInt(this.resistanceCardModifier.ordinal());
            buf.writeInt(this.resistanceValue);
         }

         buf.writeInt(this.retreatCost);
      }

      buf.writeBoolean(this.abilitiesDisabled);
      buf.writeBoolean(this.canRetreat);
      buf.writeInt(this.turn);
   }

   public void handleEndTurn(PokemonCardState attachTo, PlayerServerState player, GameServerState server) {
      super.handleEndTurn(attachTo, player, server);
      if (this.cardAbility != null && this.cardAbility.getEffect() != null) {
         this.cardAbility.getEffect().onEndTurn(this, server);
      }

      if (this.hiddenCardAbility != null && this.hiddenCardAbility.getEffect() != null) {
         this.hiddenCardAbility.getEffect().onEndTurn(this, server);
      }

      PokemonAttackStatus[] var4 = this.attacksStatus;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         PokemonAttackStatus status = var4[var6];
         status.setMissed(false);
         if (server.getPlayer(server.getCurrentTurn()) == player) {
            status.setTemporaryEffect((BaseAttackEffect)null, (String)null);
         }
      }

   }

   void writeAsCommonCardState(ByteBuf buf, boolean faceUp, boolean writeParams) {
      super.write(buf, faceUp, writeParams);
   }

   public void addDamage(PokemonCardState attacker, int damage, GameServerState server) {
      if (!this.status.isDamageImmune()) {
         if (attacker != null && this.cardAbility != null && this.cardAbility.getEffect() != null) {
            damage = this.cardAbility.getEffect().onDamage(this, attacker, server, damage);
         }

         if (attacker != null && this.hiddenCardAbility != null && this.hiddenCardAbility.getEffect() != null) {
            damage = this.hiddenCardAbility.getEffect().onDamage(this, attacker, server, damage);
         }

         this.status.setDamage(this.status.getDamage() + damage);
      }
   }

   public void addCondition(PokemonCardState attacker, CardCondition cardCondition, Integer modifier, GameServerState server) {
      if (!this.status.isConditionImmune()) {
         if (attacker == null || this.cardAbility == null || this.cardAbility.getEffect() == null || this.cardAbility.getEffect().onCondition(this, attacker, cardCondition, server)) {
            if (attacker == null || this.hiddenCardAbility == null || this.hiddenCardAbility.getEffect() == null || this.hiddenCardAbility.getEffect().onCondition(this, attacker, cardCondition, server)) {
               this.status.addCondition(cardCondition, modifier);
            }
         }
      }
   }

   public void addDamageFromCondition(int d) {
      this.status.setDamage(this.status.getDamage() + d);
   }

   public void bindPreviousPokemonSprite(Minecraft mc) {
      if (this.previousPokemonId > 0) {
         GuiHelper.bindPokemonSprite(EnumSpecies.getFromDex(this.previousPokemonId), 0, Gender.None, "", false, 0, mc);
      }

   }

   public List getAttachments() {
      return this.attachments;
   }

   public int getPreviousPokemonID() {
      return this.previousPokemonId;
   }

   public void setPreviousPokemonID(int previousPokemonId) {
      this.previousPokemonId = previousPokemonId;
   }

   public PokemonCardStatus getStatus() {
      return this.status;
   }

   public void setStatus(PokemonCardStatus s) {
      this.status = s;
   }

   public PokemonAttackStatus[] getAttacksStatus() {
      return this.attacksStatus;
   }

   public AbilityCard getAbility() {
      return this.cardAbility;
   }

   public void setAbility(AbilityCard cardAbility) {
      this.cardAbility = cardAbility;
   }

   public boolean abilitiesDisabled() {
      return this.abilitiesDisabled;
   }

   public void disableAbilities(boolean disable) {
      this.abilitiesDisabled = disable;
   }

   public AbilityCard getHiddenAbility() {
      return this.hiddenCardAbility;
   }

   public void setHiddenAbility(AbilityCard hiddenCardAbility) {
      this.hiddenCardAbility = hiddenCardAbility;
   }

   public Energy getWeakness() {
      return this.weakness;
   }

   public void setWeakness(Energy weakness) {
      this.weakness = weakness;
   }

   public CardModifier getWeaknessModifier() {
      return this.weaknessCardModifier;
   }

   public void setWeaknessModifier(CardModifier weaknessCardModifier) {
      this.weaknessCardModifier = weaknessCardModifier;
   }

   public int getWeaknessValue() {
      return this.weaknessValue;
   }

   public void setWeaknessValue(int weaknessValue) {
      this.weaknessValue = weaknessValue;
   }

   public Energy getResistance() {
      return this.resistance;
   }

   public void setResistance(Energy resistance) {
      this.resistance = resistance;
   }

   public CardModifier getResistanceModifier() {
      return this.resistanceCardModifier;
   }

   public void setResistanceModifier(CardModifier resistanceCardModifier) {
      this.resistanceCardModifier = resistanceCardModifier;
   }

   public int getResistanceValue() {
      return this.resistanceValue;
   }

   public void setResistanceValue(int resistanceValue) {
      this.resistanceValue = resistanceValue;
   }

   public int getRetreatCost() {
      return this.retreatCost;
   }

   public void setRetreatCost(int retreatCost) {
      this.retreatCost = retreatCost;
   }

   public int getTurn() {
      return this.turn;
   }

   public void setCanRetreat(boolean canRetreat) {
      this.canRetreat = canRetreat;
   }

   public boolean canRetreat() {
      return this.canRetreat;
   }
}
