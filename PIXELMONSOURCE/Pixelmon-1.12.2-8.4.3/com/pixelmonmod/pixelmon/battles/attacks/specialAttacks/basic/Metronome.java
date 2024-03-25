package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.List;

public class Metronome extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.effect.wagfinger", user.getNickname());

      Attack randomAttack;
      int attackID;
      do {
         do {
            randomAttack = new Attack((AttackBase)CollectionHelper.getRandomElement((List)AttackBase.ATTACKS));
            attackID = randomAttack.getMove().getAttackId();
         } while(randomAttack.isAttack("After You", "Apple Acid", "Assist", "Astral Barrage", "Aura Wheel", "Baneful Bunker", "Beak Blast", "Behemoth Bash", "Behemoth Blade", "Belch", "Bestow", "Body Press", "Branch Poke", "Breaking Swipe", "Celebrate", "Chatter", "Clangorous Soul", "Copycat", "Counter", "Covet", "Crafty Shield", "Decorate", "Destiny Bond", "Detect", "Diamond Storm", "Double Iron Bash", "Dragon Ascent", "Dragon Energy", "Drum Beating", "Dynamax Cannon", "Endure", "Eternabeam", "False Surrender", "Feint", "Fiery Wrath", "Fleur Cannon", "Focus Punch", "Follow Me", "Freeze Shock", "Freezing Glare", "Glacial Lance", "Grav Apple", "Helping Hand", "Hold Hands", "Hyperspace Fury", "Hyperspace Hole", "Ice Burn", "Instruct", "Jungle Healing", "King's Shield", "Life Dew", "Light of Ruin", "Mat Block", "Me First", "Meteor Assault", "Metronome", "Mimic", "Mind Blown", "Mirror Coat", "Mirror Move", "Moongeist Beam", "Nature Power", "Nature's Madness", "Obstruct", "Origin Pulse", "Overdrive", "Photon Geyser", "Plasma Fists", "Precipice Blades", "Protect", "Pyro Ball", "Quash", "Quick Guard", "Rage Powder", "Relic Song", "Secret Sword", "Shell Trap", "Sketch", "Sleep Talk", "Snap Trap", "Snarl", "Snatch", "Snore", "Spectral Thief", "Spiky Shield", "Spotlight", "Steam Eruption", "Steel Beam", "Strange Steam", "Struggle", "Sunsteel Strike", "Surging Strikes", "Switcheroo", "Techno Blast", "Thousand Arrows", "Thousand Waves", "Thief", "Thunder Cage", "Thunderous Kick", "Transform", "Trick", "V-Create", "Wide Guard", "Breakneck Blitz", "All-Out Pummeling", "Supersonic Skystrike", "Acid Downpour", "Tectonic Rage", "Continental Crush", "Savage Spin-Out", "Never-Ending Nightmare", "Corkscrew Crash", "Inferno Overdrive", "Hydro Vortex", "Bloom Doom", "Gigavolt Havoc", "Shattered Psyche", "Subzero Slammer", "Devastating Drake", "Black Hole Eclipse", "Twinkle Tackle"));
      } while(randomAttack.isZ || randomAttack.isMax || attackID == -1 || attackID >= 10000);

      user.useTempAttack(randomAttack);
      return AttackResult.ignore;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.raiseWeight(20.0F);
   }
}
