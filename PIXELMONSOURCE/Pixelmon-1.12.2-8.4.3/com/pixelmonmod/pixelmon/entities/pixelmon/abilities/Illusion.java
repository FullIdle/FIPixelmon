package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;

public class Illusion extends AbilityBase {
   public EnumSpecies disguisedPokemon = null;
   public String disguisedNickname = null;
   public String disguisedTexture = null;
   public Gender disguisedGender = null;
   public int disguisedForm = -1;
   public boolean disguisedShiny = false;

   public void beforeSwitch(PixelmonWrapper newPokemon) {
      if (!newPokemon.bc.simulateMode) {
         BattleParticipant participant = newPokemon.getParticipant();
         if (!(participant instanceof WildPixelmonParticipant)) {
            PixelmonWrapper disguised = null;

            for(int i = participant.allPokemon.length - 1; i >= 0; --i) {
               PixelmonWrapper pw = participant.allPokemon[i];
               if (!pw.isFainted()) {
                  if (pw.getPokemonUUID().equals(newPokemon.getPokemonUUID())) {
                     return;
                  }

                  disguised = pw;
                  this.disguisedTexture = pw.getRealTextureNoCheck();
                  if (this.disguisedTexture.equals(newPokemon.getRealTextureNoCheck())) {
                     this.disguisedTexture = null;
                     return;
                  }

                  this.disguisedPokemon = pw.getSpecies();
                  this.disguisedNickname = pw.getNickname();
                  this.disguisedGender = pw.getGender();
                  this.disguisedForm = pw.getForm();
                  this.disguisedShiny = pw.getInnerLink().isShiny();
                  break;
               }
            }

            if (disguised != null) {
               newPokemon.entity.transformServer(disguised.getSpecies(), this.disguisedForm, this.disguisedTexture);
               if (newPokemon.getPlayerOwner() != null) {
                  this.updateOwner(newPokemon);
               }

            }
         }
      }
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (newPokemon.entity.transformedTexture == null && this.disguisedPokemon != null) {
         newPokemon.entity.transformServer(this.disguisedPokemon, this.disguisedForm, this.disguisedTexture);
      }

   }

   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      this.fade(target);
   }

   public void onAbilityLost(PixelmonWrapper pokemon) {
      this.fade(pokemon);
   }

   private void fade(PixelmonWrapper target) {
      if (this.disguisedPokemon != null && !target.bc.simulateMode) {
         this.disguisedPokemon = null;
         this.disguisedNickname = null;
         this.disguisedTexture = null;
         this.disguisedGender = null;
         target.bc.sendToAll("pixelmon.abilities.illusion", target.getNickname());
         target.entity.cancelTransform();
         target.bc.participants.stream().filter((participant) -> {
            return participant instanceof PlayerParticipant;
         }).forEach((participant) -> {
            ((PlayerParticipant)participant).updateOpponentPokemon();
         });
         if (target.getPlayerOwner() != null) {
            this.updateOwner(target);
         }
      }

   }

   private void updateOwner(PixelmonWrapper pokemon) {
   }

   public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
      EntityPlayerMP player = oldPokemon.getPlayerOwner();
      if (!oldPokemon.bc.simulateMode && player != null) {
      }

   }

   public void applyEndOfBattleEffect(PixelmonWrapper pokemon) {
      EntityPlayerMP player = pokemon.getPlayerOwner();
      if (player != null) {
      }

   }

   public boolean needNewInstance() {
      return true;
   }

   public boolean canBeIgnored() {
      return false;
   }
}
