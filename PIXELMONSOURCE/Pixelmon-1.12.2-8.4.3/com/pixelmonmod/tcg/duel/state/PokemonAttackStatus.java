package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.api.registries.AttackCardRegistry;
import com.pixelmonmod.tcg.duel.attack.effects.BaseAttackEffect;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.commons.lang3.StringUtils;

public class PokemonAttackStatus {
   public static final int INFINITE_TURN_COUNT = 1000000;
   private AttackCard cardAttack;
   private boolean isDisabled;
   private int disabledTurnCountLeft;
   private int damageBonus;
   private int damageBonusCountLeft;
   private BaseAttackEffect temporaryEffect;
   private String temporaryEffectText;
   private int damage;
   private boolean isMissed;

   public PokemonAttackStatus(AttackCard cardAttack) {
      this.cardAttack = cardAttack;
      this.resetDamage();
   }

   public PokemonAttackStatus(ByteBuf buf) {
      this.cardAttack = AttackCardRegistry.get(ByteBufUtils.readUTF8String(buf), buf.readInt());
      this.isDisabled = buf.readBoolean();
      this.disabledTurnCountLeft = buf.readInt();
      this.damageBonus = buf.readInt();
      this.damageBonusCountLeft = buf.readInt();
      if (buf.readBoolean()) {
         this.temporaryEffectText = ByteBufUtils.readUTF8String(buf);
      }

   }

   public void write(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.cardAttack.getCardCode());
      buf.writeInt(this.cardAttack.getAttackNumber());
      buf.writeBoolean(this.isDisabled);
      buf.writeInt(this.disabledTurnCountLeft);
      buf.writeInt(this.damageBonus);
      buf.writeInt(this.damageBonusCountLeft);
      buf.writeBoolean(this.temporaryEffectText != null);
      if (this.temporaryEffectText != null) {
         ByteBufUtils.writeUTF8String(buf, this.temporaryEffectText);
      }

   }

   public AttackCard getData() {
      return this.cardAttack;
   }

   public boolean isDisabled() {
      return this.isDisabled;
   }

   public void disable(int turnCount) {
      if (this.isDisabled) {
         if (this.disabledTurnCountLeft != 1000000 && this.disabledTurnCountLeft < turnCount) {
            this.disabledTurnCountLeft = turnCount;
         }
      } else {
         this.isDisabled = true;
         this.disabledTurnCountLeft = turnCount;
      }

   }

   public void enable() {
      this.isDisabled = false;
      this.disabledTurnCountLeft = 0;
   }

   public int getDisabledTurnCountLeft() {
      return this.disabledTurnCountLeft;
   }

   public void reduceTurnCount() {
      if (this.disabledTurnCountLeft > 0) {
         --this.disabledTurnCountLeft;
      }

      if (this.disabledTurnCountLeft == 0 && this.isDisabled) {
         this.isDisabled = false;
      }

      if (this.damageBonusCountLeft > 0) {
         --this.damageBonusCountLeft;
      }

      if (this.damageBonusCountLeft == 0) {
         this.damageBonus = 0;
      }

      this.isMissed = false;
   }

   public int getDamage() {
      return this.damage + this.damageBonus;
   }

   public void setDamage(int damage) {
      this.damage = Math.max(0, damage);
   }

   public void resetDamage() {
      this.damage = this.cardAttack.getDamage();
   }

   public int getDamageBonus() {
      return this.damageBonus;
   }

   public int getDamageBonusCountLeft() {
      return this.damageBonusCountLeft;
   }

   public void setDamageBonus(int damageBonus, int damageBonusCountLeft) {
      this.damageBonus = damageBonus;
      this.damageBonusCountLeft = damageBonusCountLeft;
   }

   public BaseAttackEffect getTemporaryEffect() {
      return this.temporaryEffect;
   }

   public String getTemporaryEffectText() {
      return this.temporaryEffectText;
   }

   public void setTemporaryEffect(BaseAttackEffect temporaryEffect, String effectText) {
      this.temporaryEffect = temporaryEffect;
      this.temporaryEffectText = effectText;
   }

   public boolean isMissed() {
      return this.isMissed;
   }

   public void setMissed(boolean missed) {
      this.isMissed = missed;
   }

   public boolean hasDescription() {
      return StringUtils.isNotBlank(this.cardAttack.getText()) || StringUtils.isNotBlank(this.temporaryEffectText);
   }

   public String getLocalizedDescription() {
      String text = "";
      if (StringUtils.isNotBlank(this.cardAttack.getText())) {
         text = LanguageMapTCG.translateKey(this.cardAttack.getText().toLowerCase());
      }

      if (StringUtils.isNotBlank(this.temporaryEffectText)) {
         text = (LanguageMapTCG.translateKey(this.temporaryEffectText.toLowerCase()) + " " + text).trim();
      }

      return text;
   }
}
