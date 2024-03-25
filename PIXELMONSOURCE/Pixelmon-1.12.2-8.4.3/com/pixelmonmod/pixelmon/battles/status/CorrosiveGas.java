package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.StickyHold;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.ItemMemory;
import com.pixelmonmod.pixelmon.items.heldItems.ItemPlate;
import com.pixelmonmod.pixelmon.items.heldItems.ItemRustedShield;
import com.pixelmonmod.pixelmon.items.heldItems.ItemRustedSword;
import java.util.ArrayList;
import java.util.Iterator;

public class CorrosiveGas extends StatusBase {
   public CorrosiveGas() {
      super(StatusType.CorrosiveGas);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.addStatus(new CorrosiveGas(), target)) {
         user.bc.sendToAll("pixelmon.status.corrosivegas", target.getNickname());
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public boolean isImmune(PixelmonWrapper pokemon) {
      return pokemon.getBattleAbility() instanceof StickyHold || pokemon.getHeldItem() instanceof ItemMemory && pokemon.getSpecies() == EnumSpecies.Silvally || pokemon.getHeldItem() instanceof ItemRustedSword && pokemon.getSpecies() == EnumSpecies.Zacian || pokemon.getHeldItem() instanceof ItemRustedShield && pokemon.getSpecies() == EnumSpecies.Zamazenta || pokemon.getHeldItem() == PixelmonItemsHeld.blueOrb && pokemon.getSpecies() == EnumSpecies.Kyogre || pokemon.getHeldItem() == PixelmonItemsHeld.redOrb && pokemon.getSpecies() == EnumSpecies.Groudon || pokemon.getHeldItem() instanceof ItemMemory && pokemon.getSpecies() == EnumSpecies.Silvally || pokemon.getHeldItem() instanceof ItemPlate && pokemon.getSpecies() == EnumSpecies.Arceus || (pokemon.getHeldItem() == PixelmonItemsHeld.burnDrive || pokemon.getHeldItem() == PixelmonItemsHeld.chillDrive || pokemon.getHeldItem() == PixelmonItemsHeld.shockDrive || pokemon.getHeldItem() == PixelmonItemsHeld.douseDrive) && pokemon.getSpecies() == EnumSpecies.Genesect || pokemon.getHeldItem() == PixelmonItemsHeld.griseous_orb && pokemon.getSpecies() == EnumSpecies.Giratina;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         ItemHeld heldItem = target.getUsableHeldItem();
         if (heldItem != null && !heldItem.hasNegativeEffect()) {
            userChoice.raiseWeight(15.0F);
         }
      }

   }
}
