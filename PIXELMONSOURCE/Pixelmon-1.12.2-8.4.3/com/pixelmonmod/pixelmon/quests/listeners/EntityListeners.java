package com.pixelmonmod.pixelmon.quests.listeners;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.EggHatchEvent;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.api.events.LostToWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.NPCChatEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonKnockoutEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EntityListeners {
   @SubscribeEvent
   public void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) throws InvalidQuestArgsException {
      if (event.getSide() == Side.SERVER && event.getHand() == EnumHand.MAIN_HAND && !(event.getEntity() instanceof FakePlayer)) {
         EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
         QuestData questData = Pixelmon.storageManager.getParty(player).getQuestData(true);
         questData.receiveInternal("DIALOGUE", true, event.getTarget(), null);
         questData.receive("ENTITY_INTERACT", event.getTarget());
      }

   }

   @SubscribeEvent
   public void onNPCChat(NPCChatEvent event) throws InvalidQuestArgsException {
      if (event.player instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)event.player;
         Pixelmon.storageManager.getParty(player).getQuestData(true).receiveInternal("DIALOGUE", true, event.npc, event);
      }

   }

   @SubscribeEvent
   public void onPixelmonSpawn(SpawnEvent event) throws InvalidQuestArgsException {
      if (event.action.spawnLocation.cause instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)event.action.spawnLocation.cause;
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         QuestData data = pps.getQuestData(false);
         data.receive("NPC_SPAWN_INSERTER", event.action.getOrCreateEntity());
      }

   }

   @SubscribeEvent
   public void onEvolve(EvolveEvent.PostEvolve event) throws InvalidQuestArgsException {
      EntityPlayerMP player = event.player;
      PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
      pps.getQuestData(true).receive("POKEMON_EVOLVE_PRE", event.preEvo.getPokemonData());
      pps.getQuestData(true).receive("POKEMON_EVOLVE_POST", event.pokemon.getPokemonData());
   }

   @SubscribeEvent
   public void onEggHatch(EggHatchEvent.Post event) throws InvalidQuestArgsException {
      EntityPlayerMP player = event.getPokemon().getOwnerPlayer();
      PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
      pps.getQuestData(true).receive("POKEMON_HATCH", event.getPokemon());
   }

   @SubscribeEvent
   public void onWildCapture(CaptureEvent.SuccessfulCapture event) throws InvalidQuestArgsException {
      EntityPlayerMP player = event.player;
      PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
      pps.getQuestData(true).receive("POKEMON_CAPTURE", event.getPokemon().getPokemonData());
   }

   @SubscribeEvent
   public void onKnockout(PixelmonKnockoutEvent event) throws InvalidQuestArgsException {
      EntityPlayerMP player = event.source.getPlayerOwner();
      if (player != null) {
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         QuestData data = pps.getQuestData(true);
         Pokemon pokemon = event.pokemon.getInnerLink();
         data.receive("POKEMON_DEFEAT", pokemon);
         if (event.pokemon.getTrainerOwner() != null) {
            data.receive("POKEMON_DEFEAT_NPC", pokemon);
         } else if (event.pokemon.getPlayerOwner() != null) {
            data.receive("POKEMON_DEFEAT_PLAYER", pokemon);
         }
      }

   }

   @SubscribeEvent
   public void onDefeatWild(BeatWildPixelmonEvent event) throws InvalidQuestArgsException {
      PlayerPartyStorage pps = Pixelmon.storageManager.getParty(event.player);
      QuestData data = pps.getQuestData(true);
      PixelmonWrapper[] var4 = event.wpp.allPokemon;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         PixelmonWrapper pw = var4[var6];
         data.receive("POKEMON_DEFEAT_WILD", pw.pokemon);
      }

   }

   @SubscribeEvent
   public void onLoseToWild(LostToWildPixelmonEvent event) throws InvalidQuestArgsException {
      EntityPlayerMP player = event.player;
      if (player != null) {
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         Iterator var4 = event.wpp.getActiveUnfaintedPokemon().iterator();

         while(var4.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var4.next();
            pps.getQuestData(true).receive("POKEMON_DEFEATED_BY", pw.getInnerLink());
         }
      }

   }
}
