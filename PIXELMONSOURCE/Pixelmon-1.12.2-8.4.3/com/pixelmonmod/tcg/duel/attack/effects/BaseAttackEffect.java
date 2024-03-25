package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.dto.CustomGUI;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.List;

public class BaseAttackEffect {
   private final String[] codes;
   private boolean optional = false;

   public BaseAttackEffect(String... codes) {
      this.codes = codes;
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      return true;
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
   }

   public void applyAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server, int finalDamage) {
   }

   public List flipCoin(List currentResult, PokemonCardState pokemon, GameServerState server) {
      return currentResult;
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      return null;
   }

   public CustomGUI getCustomGUI(PokemonCardState pokemon, GameServerState server) {
      return null;
   }

   public CardSelectorState getOpponentSelectorState(GameServerState server) {
      return null;
   }

   public boolean chooseOppAttack() {
      return false;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public int modifyDamage(int damage, PokemonCardState pokemon, GameServerState server) {
      return damage;
   }

   public void modifyTurn(PokemonCardState pokemon, GameServerState server) {
   }

   public void applyDelayAfterDamage(PokemonCardState pokemon, GameServerState server) {
   }

   public String[] getCodes() {
      return this.codes;
   }

   public BaseAttackEffect setRequiredCoinSide(CoinSide coinSide) {
      return this;
   }

   public BaseAttackEffect setOptional(boolean optional) {
      this.optional = optional;
      return this;
   }

   public BaseAttackEffect parse(String... args) {
      return this;
   }
}
